import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticationService} from "../../../../core/auth/authentication.service";
import {DashboardService} from "./dashboard.service";
import {_} from 'underscore';
import * as moment from 'moment';
import {TranslateService} from "@ngx-translate/core";
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import {WebsocketService} from "../../services/websocket.service";
import {isNullOrUndefined, isObject, isString} from "util";
import * as uuid from 'uuid';

declare const $:any
declare const Chartist:any

@Component({
    selector: 'm-dashboard',
    templateUrl: './dashboard.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit,AfterViewInit,OnDestroy {

    public config: any;
    private chatWebSocket: any;
    private channelName: string;
    private JSObject: Object = Object;
    friends = [];
    currentChatUserId = 0;
    message = '';
    messageSubscription = null;
    messageList = {};
    showChat = false;
    private user :any;
    private messageLoading = false;
    private today;
    constructor(private authService: AuthenticationService,
                private websocketService: WebsocketService,
                private storage:TokenStorage,
                private service:DashboardService,
                private cdr: ChangeDetectorRef,
                private translate: TranslateService) {
        this.user = storage.getUserData();
    }

    ngOnInit(): void {
        moment.locale(this.translate.currentLang);
        this.today = moment(new Date(),"YYYY-MM-DDTHH:mm:ss.SSSSZ").startOf('day').format("LL");
    }

    get todayMessageList() : any[] {
        this.today = moment(new Date(),"YYYY-MM-DDTHH:mm:ss.SSSSZ").startOf('day').format("LL");
        if(!this.messageList.hasOwnProperty(this.today)){
            this.messageList[this.today] = [];
        }
        return this.messageList[this.today];
    }


    ngAfterViewInit(): void {
        this.service.getFriendList().subscribe(res => {
            if(res.success){
                this.friends = res.data;
                this.detectChanges();
            }
        });

        this.websocketService.subscribeNotification("DashboardNotification",this.notification.bind(this));

        this.websocketService.subscribeChatNotification("DashboardChatNotification",this.chatNotification.bind(this));

        $(".panel-body ").slimScroll({
            height: "100%",
            position: "right",
            size: "0px",
            color: "#dcdcdc"
        })

        /*   $.toast({
               heading: 'Dış Ticaret Portalına Hoşgeldiniz.',
               text: 'Bizimle irtibata geçiniz.',
               position: 'top-right',
               loaderBg: '#fff',
               icon: 'warning',
               hideAfter: 3500,
               stack: 6
           })*/


        //ct-visits

        // counter
        /*$(".counter").counterUp({
            delay: 100,
            time: 1200
        });*/



    }

    startChat(id:number){
        if(this.currentChatUserId == id) return;
        this.currentChatUserId = id;
        if(this.messageSubscription != null){
            this.unSubscribeChannel(this.messageSubscription.id)
        }

        this.showChatPanel();
        this.messageLoading = true;
        this.service.establishChatSession(this.currentChatUserId).subscribe(res => {
            console.log(res);
            if(res.success){
                this.chatWebSocket = this.websocketService.get();
                this.channelName = res.data.channelName;
                this.messageSubscription = this.chatWebSocket.subscribe('/topic/channel.' + this.channelName, this.onMessage.bind(this),
                    {"Authorization": "Bearer " + this.storage.getAccessTokenSync()});
                this.service.getExistingChatMessages(this.channelName).subscribe(res2 => {
                    this.messageLoading = false;

                    if(res2.success){
                        this.messageList =      _.groupBy(res2.data, (result) => {
                            const i = moment(result.sendTime,"YYYY-MM-DDTHH:mm:ss.SSSSZ").startOf('day');
                            return i.format('LL');
                        });
                        this.cdr.detectChanges();
                        this.scrollTo();
                    }
                })
            }
        })
    }

    notification(data){
        if(!isNullOrUndefined(data)){
            const notification = JSON.parse(data.body);
            if(notification.type == "CHAT_READ" && notification.fromUserId  == this.currentChatUserId){
                this.setAllRead(notification.data);
            }else{
                this.friends.forEach(friend => {
                    if(friend.id == notification.fromUserId){
                        if(isNaN(friend.unReadMessageCount)){
                            friend.unReadMessageCount = 0;
                        }
                        friend.unReadMessageCount++;
                    }
                });
                this.detectChanges();
            }
        }
    }

    chatNotification(data){
        if(!isNullOrUndefined(data)){
            const chatNotification = JSON.parse(data.body);
            if(chatNotification.type == "STATUS_CHANGE"){
                this.friends.forEach(friend => {
                    if(friend.id == chatNotification.fromUserId){
                        friend.status = chatNotification.data.userStatus;
                    }
                });
                this.detectChanges();
            }
        }
    }

    sendChatMessage () {
        const data = {
            ownerUser: {
                id : this.user.id,
                firstName : this.user.firstName,
                lastName : this.user.lastName,
            },
            messageType: "TEXT",
            clientId : uuid.v4(),
            contents: this.message
        };
        this.onMessage(data);
        this.chatWebSocket.send("/app/channel." + this.channelName, {Authorization : this.storage.getAccessTokenSync()}, JSON.stringify(data));
        this.message= '';
    };


    scrollTo(){
        const itemContainer= $(".panel-body ");
        var scrollTo_int = itemContainer.prop('scrollHeight') + 'px';
        setTimeout(() => {
            itemContainer.slimScroll({
                scrollTo : scrollTo_int,
                height: "100%",
                start: 'bottom'
            })
        },10);
    }



    showChatPanel(){
        this.showChat = true;
        const itemContainer= $(".panel-body ");
        var scrollTo_int = itemContainer.prop('scrollHeight') + 'px';
        setTimeout(() => {
            itemContainer.slimScroll({
                height: "100%",
                scrollTo : scrollTo_int,
                position: "right",
                color: "#dcdcdc",
                start: 'bottom'
            })
        },10);


    }

    onMessage(data){
        let message;
        if(isObject(data) && isString(data.body)){
            message = JSON.parse(data.body);
        }else{
            message = data;
        }

        const arr = this.todayMessageList;
        if(message.ownerUser.id == this.user.id && message.id !== undefined ){
            for (let i = arr.length-1; i >= 0; --i){
                if(arr[i].clientId == message.clientId){
                    arr[i] = message
                }
            }
        }else{
            arr.push(message);
        }
        this.detectChanges();
        this.scrollTo();
        this.sendReadMessage(message);

    }


    ngOnDestroy() {
        if(this.messageSubscription != null){
            this.unSubscribeChannel(this.messageSubscription.id)
        }

        this.websocketService.unsubscribeNotification("DashboardNotification");
        this.websocketService.unsubscribeChatNotification("DashboardChatNotification");

    }

    getTime(time :string){
        return moment(time,"YYYY-MM-DDTHH:mm:ss.SSSSZ").format('LTS');
    }


    setAllRead(data){
        for (var key in this.messageList) {
            if(this.messageList.hasOwnProperty(key)){
                const now = new Date();
                this.messageList[key].forEach(l => {
                    if(data){
                        if(l.id == data.id){
                            l.readTime = data.readTime;
                        }
                    }else{
                        l.readTime = now;
                    }

                });
            }
        }
        this.detectChanges();
    }

    sendReadMessage(message){
        if(this.storage.getUserData().id != message.ownerUser.id){
            this.chatWebSocket.send('/app/user.chat.read.' + this.storage.getUserData().id , {Authorization : this.storage.getAccessTokenSync()}, JSON.stringify(message));
        }

    }

    detectChanges() {
        setTimeout(() => {
            this.cdr.detectChanges();
        }, 250);
    }

    unSubscribeChannel(id){
        delete this.chatWebSocket.subscriptions[id];
        this.chatWebSocket._transmit("UNSUBSCRIBE",{
            id: id,
            channelName : this.channelName,
            Authorization: "Bearer " + this.storage.getAccessTokenSync()
        })
    }

}
