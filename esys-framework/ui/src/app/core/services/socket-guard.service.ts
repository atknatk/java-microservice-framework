import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs";
import {TokenStorage} from "../auth/token-storage.service";
import {WebsocketService} from "../../content/pages/services/websocket.service";

@Injectable({ providedIn: 'root' })
export class SocketGuard implements CanActivate{

    constructor(private tokenStorage : TokenStorage, private websocketService : WebsocketService){
    }


    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        if(this.tokenStorage.getAccessTokenSync()
            && state.url.indexOf('login') == -1
            && !this.websocketService.isConnected()){
            this.websocketService.connect()
        }
        return true;
    }



}