import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from "../../../core/auth/authentication.service";
import {TokenStorage} from "../../../core/auth/token-storage.service";
import {TranslateService} from "@ngx-translate/core";
import {WebsocketService} from "../../pages/services/websocket.service";
import {isNullOrUndefined} from "util";
import {NotificationService} from "../../../core/services/notification.service";


@Component({
    selector: 'esys-header',
    templateUrl: './header.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class HeaderComponent implements OnInit, AfterViewInit,OnDestroy {

    userData :any= {};


    constructor(private router: Router,
                private authService : AuthenticationService,
                private websocketService: WebsocketService,
                private _nf: NotificationService,
                private cdr: ChangeDetectorRef,
                private tokenStore: TokenStorage,
                private translate: TranslateService) {
        this.userData =  tokenStore.getUserData()
    }

    get currentLang(){
        return this.translate.currentLang;
    }


    ngOnInit(): void {
        this.websocketService.subscribeNotification("HeaderNotification",this.notification.bind(this));
    }

    ngAfterViewInit(): void {

    }

    logout(){
        this.authService.logout(true);
    }

    changeLang(lang){
        this.translate.use(lang).subscribe(res => {
            this.cdr.detectChanges();
        });

    }

    notification(data){
        if(!isNullOrUndefined(data)){
            const notification = JSON.parse(data.body);
            if(notification.type == "NEW_MESSAGE"){
                this._nf.info("Yeni Mesajınız var","Chat");
            }else{

            }
        }
    }

    ngOnDestroy(): void {
        this.websocketService.unsubscribeNotification("HeaderNotification");
    }

}
