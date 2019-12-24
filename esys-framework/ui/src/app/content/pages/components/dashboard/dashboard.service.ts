import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";

declare const $:any

@Injectable()
export class DashboardService{

    constructor(private http: HttpClient){}
    getFriendList() : Observable<any> {
        return this.http.get('api/message/chat/friends');
    }

    establishChatSession(id:number): Observable<any>{
        return this.http.put('api/message/chat/channel/' + id,null);
    }

    getExistingChatMessages(channelName :string): Observable<any>{
        return this.http.get('api/message/chat/channel/' + channelName+'/message');
    }


}
