import {Injectable, InjectionToken} from '@angular/core';
import {TokenStorage} from "../../../core/auth/token-storage.service";
import {isFunction, isNullOrUndefined} from "util";
import {IWebsocketService} from "./websecket";

declare const Stomp : any;
declare const SockJS : any;


// export const WebsocketServiceProvider = new InjectionToken(
//     "WebsocketServiceProvider",
//     { providedIn: "root", factory: () => new WebsocketService() }
// );

@Injectable({
    providedIn: 'root'
})
export class WebsocketService implements IWebsocketService{


    private socket :any;

    private readonly WHEN_CONNECTED_CALLBACK_WAIT_INTERVAL = 1000;

    private notificationSubscribeList: Map<String,((data :any) => void)>  = new Map();
    private chatNotificationSubscribeList: Map<String,((data :any) => void)>  = new Map();


    constructor(private tokenService : TokenStorage) {
    }

    public connect() {
        //this.socket = Stomp.over(new SockJS('http://localhost:8030/message/ws'));
        this.socket = Stomp.over(new SockJS('http://test.isisbilisim.com.tr:8030/message/ws'));
        this.socket.debug = null;
        this.socket.connect({"Authorization": "Bearer " + this.tokenService.getAccessTokenSync()},
            this.onOpen.bind(this), this.onClose.bind(this));
    }

    disconnect(cb :() =>void) {
        this.socket.disconnect(() =>{
            this.whenDisconnected.bind(this)
            if(isFunction(cb)){
                cb();
            }},
            {"Authorization": "Bearer " + this.tokenService.getAccessTokenSync()});
    }

    onOpen () {
        this.socket.subscribe('/topic/user.notification.' + this.tokenService.getUserData().id,
            this.onNotificationMessage.bind(this));

        this.socket.subscribe('/topic/user.chat.notification',
            this.onChatNotificationMessage.bind(this));
    };


    subscribeNotification(key,func : (data :any) => void){
        this.notificationSubscribeList.set(key ,func);
    }

    subscribeChatNotification(key,func : (data :any) => void){
        this.chatNotificationSubscribeList.set(key ,func);
    }

    unsubscribeNotification(key){
        this.notificationSubscribeList.delete(key);
    }

    unsubscribeChatNotification(key : string){
        this.chatNotificationSubscribeList.delete(key);
    }


    private onNotificationMessage(data) {
        this.notificationSubscribeList.forEach((l :any) => {
            if(!isNullOrUndefined(l) && isFunction(l)){
                l(data);
            }
        });
    };


    private onChatNotificationMessage(data) {
        this.chatNotificationSubscribeList.forEach((l :any) => {
            if(!isNullOrUndefined(l) && isFunction(l)){
                l(data);
            }
        });
    };

    onClose() {
        //       alert('You have disconnected, hit "OK" to reload.');
 //       window.location.reload();
    };

    isConnected() {
        return (this.socket && this.socket.connected);
    };


    whenDisconnected(_do){

    }

    whenConnected (_do) {
        setTimeout(
            () =>  {
                if (this.isConnected()) {
                    if (_do !== null) { _do(); }
                    return;
                } else {
                    this.whenConnected(_do);
                }

            }, this.WHEN_CONNECTED_CALLBACK_WAIT_INTERVAL);
    };

    get(){
        return this.socket;
    }



}
