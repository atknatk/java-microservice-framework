package com.esys.framework.message.service.impl;

import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.service.impl.BaseService;
import com.esys.framework.message.dto.*;
import com.esys.framework.message.entity.ChatChannel;
import com.esys.framework.message.entity.ChatChannelSubscribtion;
import com.esys.framework.message.entity.ChatMessage;
import com.esys.framework.message.entity.ChatUserStatus;
import com.esys.framework.message.enums.NotificationType;
import com.esys.framework.message.enums.UserStatus;
import com.esys.framework.message.exceptions.IsSameUserException;
import com.esys.framework.message.repository.ChatChannelRepository;
import com.esys.framework.message.repository.ChatChannelSubscribtionReporsitory;
import com.esys.framework.message.repository.ChatMessageRepository;
import com.esys.framework.message.repository.ChatUserStatusRepository;
import com.esys.framework.message.service.IChatService;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Service
@Slf4j
@EnableAsync
public class ChatService extends BaseService<ChatChannelDto, ChatChannel> implements IChatService {


    @Autowired
    private transient ChatMessageRepository chatMessageRepository;

    @Autowired
    private transient ChatUserStatusRepository userStatusRepository;

    @Autowired
    private transient MessageSource messageSource;

    @Autowired
    private transient SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private transient ChatChannelSubscribtionReporsitory channelSubscribtionReporsitory;



    private final ModelMapper mapper;
    private transient ChatChannelRepository chatChannelRepository;

    private final int MAX_PAGABLE_CHAT_MESSAGES = 100;


    @Autowired
    public ChatService(ModelMapper mapper,ChatChannelRepository chatChannelRepository) {
        super(mapper, log, ChatChannelDto.class, ChatChannel.class, chatChannelRepository);
        this.mapper = mapper;
        this.chatChannelRepository = chatChannelRepository;
    }


    public ModelResult<List<FriendsDto>> retrieveFriendsList() {
        User own = new User(SecurityUtils.getCurrentBasicUser().getId());
        List<FriendsDto> res = userStatusRepository.findFriendsListFor(own.getId());
        List<ChatChannel> chatChannelList =  chatChannelRepository.findAllOwnChatChannelObj(own);
        List<MessageCountDto> unReadMessageCount= null;
        if(!chatChannelList.isEmpty())
            unReadMessageCount = chatMessageRepository.getUnReadMessages(chatChannelList,own);

        List<MessageCountDto> finalUnReadMessageCount = unReadMessageCount;
        res.stream().forEach(r -> {
            if(r.getStatus() == null) r.setStatus(UserStatus.OFFLINE);
            chatChannelList.stream().forEach(chatChannel -> {
                long id = r.getId();
                if(chatChannel.getUsers().stream().anyMatch(u -> u.getId().equals(r.getId()))){
                    r.setChannel(chatChannel.getChannelName());
                }
            });

            if(finalUnReadMessageCount != null){
                finalUnReadMessageCount.stream().forEach(messageCount -> {
                    if(messageCount.getChannelName().equals(r.getChannel())){
                        r.setUnReadMessageCount(messageCount.getCount());
                    }
                });
            }
        });
        res.stream().filter(distinctByKey(FriendsDto::getEmail));
        ModelResult.ModelResultBuilder<List<FriendsDto>> builder = new ModelResult.ModelResultBuilder(messageSource,LocaleContextHolder.getLocale());
        builder.setData(res);
        return builder.build();
    }


    public ChatChannelDto establishChatSession(long userId){
        BasicUserDto curr = SecurityUtils.getCurrentBasicUser();

        if (curr.getId() == userId) {
            throw new IsSameUserException();
        }

        Collection<User> users = Arrays.asList(new User(userId),new User(curr.getId()));

        List<String> channels =  chatChannelRepository.findAllOwnChatChannel(new User(curr.getId()));
        Optional<ChatChannel> chatChannel = Optional.empty();
        if(!channels.isEmpty()){
            chatChannel =  chatChannelRepository.findChatChannelByOtherUser(channels, new User(userId));
        }



        if(chatChannel.isPresent()){
            ChatChannel _chat = chatChannel.get();
            _chat.setChatMessages(new ArrayList<>());
            return mapper.map(_chat,ChatChannelDto.class);
        }

        return newChatSession(users);
    }

    private ChatChannelDto newChatSession(Collection<User> users){
        ChatChannel channel = new ChatChannel();
        channel.setUsers(users);
        channel.setChannelName(UUID.randomUUID().toString());
        chatChannelRepository.save(channel);
        return mapper.map(channel,ChatChannelDto.class);
    }

    @HystrixCommand(fallbackMethod = "emptyChannel")
    public List<ChatChannelDto> getExistingChannel() {
        List<ChatChannel> list = chatChannelRepository.findAllByUsers(SecurityUtils.getCurrentUser());
        return mapper.map(list,new TypeToken<List<ChatChannelDto>>() {}.getType());

    }

    public ChatMessageDto submitMessage(String channelName, ChatMessageDto chatMessageDto){
        Optional<ChatChannel> chatChannel = chatChannelRepository.findByChannelName(channelName);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatChannel(chatChannel.get());
        chatMessage.setOwnerUser(new User(chatMessageDto.getOwnerUser().getId()));
        chatMessage.setMessageType(chatMessageDto.getMessageType());
        chatMessage.setContents(chatMessageDto.getContents());
        chatMessage.setSendTime(LocalDateTime.now());



        ChatMessage message =  chatMessageRepository.save(chatMessage);

        Optional<ChatMessage> dbMessage =  chatMessageRepository.findById(message.getId());
        ChatMessageDto resultMessage= null;
        if(dbMessage.isPresent()){
            resultMessage = mapper.map(dbMessage.get(),ChatMessageDto.class);
            resultMessage.setClientId(chatMessageDto.getClientId());
            if(chatChannel.isPresent()){
                notifyMessage(chatChannel.get(), resultMessage);
            }
        }

        return resultMessage;

    }


    @Transactional
    @Override
    public List<ChatMessageDto> getExistingChatMessages(String channelName) {
        List<ChatMessage> chatMessages =
                chatMessageRepository.getExistingChatMessages(channelName, new PageRequest(0, MAX_PAGABLE_CHAT_MESSAGES));
        Optional<ChatChannel> chatChannel = chatChannelRepository.findByChannelName(channelName);
        if(chatChannel.isPresent()){
            updateMessageRead(chatChannel.get());
            Collection<User> users = chatChannel.get().getUsers();
            NotificationDto notification = new NotificationDto();
            notification.setType(NotificationType.CHAT_READ);
            // notification.setContents();
            BasicUserDto currentUser = SecurityUtils.getCurrentBasicUser();
            users.stream().forEach(l -> {
                if(l.getId() != currentUser.getId()){
                notification.setFromUserId(currentUser.getId());
                simpMessagingTemplate.convertAndSend("/topic/user.notification."+ l.getId() , notification);
            }
        });


        }

        List<ChatMessage> messagesByLatest = Lists.reverse(chatMessages);
        Type listType = new TypeToken<List<ChatMessageDto>>() {}.getType();
        List<ChatMessageDto> res= mapper.map(messagesByLatest, listType);
        return res;
    }

    @Override
    @Transactional
    public void updateMessageRead(ChatChannel chatChannel){
        chatMessageRepository.updateMessageRead(chatChannel,new User(SecurityUtils.getCurrentBasicUser().getId()));
    }

    @Override
    public void notifyStatus(ChatUserStatusDto userStatus) {
        NotificationDto<ChatUserStatusDto> userStatusNotification = new NotificationDto<>();
        userStatusNotification.setData(userStatus);
        userStatusNotification.setFromUserId(userStatus.getUser().getId());
        userStatusNotification.setType(NotificationType.STATUS_CHANGE);
        simpMessagingTemplate.convertAndSend("/topic/user.chat.notification", userStatusNotification);
    }


    public List<String> emptyChannel() {
        return new ArrayList<>();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    @Async("taskExecutor")
    public CompletableFuture notifyMessage(ChatChannel chatChannel, ChatMessageDto message){
        for(User user : chatChannel.getUsers()){
            if(!user.getId().equals(SecurityUtils.getCurrentBasicUser().getId())){
                if(userStatusRepository.existsByUserAndUserStatus(user,UserStatus.ONLINE)){
                    NotificationDto notification = new NotificationDto();
                    if(isSubscribe(chatChannel)){
                        notification.setType(NotificationType.CHAT_READ);
                    }else{
                        notification.setType(NotificationType.NEW_MESSAGE);
                    }
                    notification.setFromUserId(SecurityUtils.getCurrentBasicUser().getId());
                    simpMessagingTemplate.convertAndSend("/topic/user.notification."+ user.getId() , notification);
                }else{
                    // send email
                }
                //message.setReadTime(LocalDateTime.now());
                //chatMessageRepository.save(message);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture setOnline(UserDto userDto, String connectionId) {
        try {
            ChatUserStatus userStatus = new ChatUserStatus();
            userStatus.setUserStatus(UserStatus.ONLINE);
            userStatus.setConnectionId(connectionId);
            userStatus.setUser(mapper.map(userDto, User.class));
            userStatusRepository.save(userStatus);
            ChatUserStatusDto dto = mapper.map(userStatus, ChatUserStatusDto.class);
            notifyStatus(dto);
        } catch (Exception e) {
            log.error("User set ChatStatus error ", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture setOffline(String connectionId) {
        try {
            List<ChatUserStatus> userStatus = userStatusRepository.findByConnectionId(connectionId);
            if (userStatus.size() > 0) {
                userStatus.stream().forEach(chatUserStatus -> {
                    chatUserStatus.setUserStatus(UserStatus.OFFLINE);
                    ChatUserStatusDto dto = mapper.map(chatUserStatus, ChatUserStatusDto.class);
                    notifyStatus(dto);
                });
                userStatusRepository.deleteAll(userStatus);

            }
            channelSubscribtionReporsitory.deleteAllByUser(new User(SecurityUtils.getCurrentBasicUser()));
        } catch (Exception e) {
            log.error("User set ChatStatus error ", e);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public ChatMessageDto readMessage(ChatMessageDto message) {
        Optional<ChatMessage> chatMessage =  chatMessageRepository.findById(message.getId());
        if(chatMessage.isPresent()){
            chatMessage.get().setReadTime(LocalDateTime.now());
            chatMessageRepository.save(chatMessage.get());
            message.setReadTime(chatMessage.get().getReadTime());
            NotificationDto notification = new NotificationDto();
            notification.setType(NotificationType.CHAT_READ);
            notification.setFromUserId(SecurityUtils.getCurrentBasicUser().getId());
            notification.setData(message);
            simpMessagingTemplate.convertAndSend("/topic/user.notification."+ message.getOwnerUser().getId() , notification);
        }
        return message;
    }

    @Override
    public Optional<ChatChannel> findByName(String channelName) {
        return chatChannelRepository.findByChannelName(channelName);
    }

    @Override
    protected boolean exist(ChatChannelDto dto) {
        return chatChannelRepository.findByChannelName(dto.getChannelName()).isPresent();
    }

    private boolean isSubscribe(ChatChannel channelName){
        final Optional<User> first = channelName.getUsers().stream().filter(l -> !l.getId().equals(SecurityUtils.getCurrentBasicUser().getId())).findFirst();
        if(first.isPresent()){
            final List<ChatChannelSubscribtion> allByChannelNameAndUser = channelSubscribtionReporsitory.findAllByChannelNameAndUser(channelName, first.get());
            return !allByChannelNameAndUser.isEmpty();
        }
        return false;
    }

}
