import {BrowserModule} from '@angular/platform-browser';
import {isDevMode, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {AppRoutingModule} from "./app-routing.module";
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CoreModule} from "./core/core.module";
import {AuthenticationModule} from "./core/auth/authentication.module";
import {UtilsService} from "./core/services/utils.service";
import {LayoutModule} from "./content/layout/layout.module";
import {TokenInterceptor} from "./core/auth/token.interceptor";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {TranslateLoader, TranslateModule, TranslateService} from "@ngx-translate/core";
import {WebsocketService} from "./content/pages/services/websocket.service";
import {FileSelectDirective, FileUploadModule} from "ng2-file-upload";
import {NotificationService} from "./core/services/notification.service";

export function HttpLoaderFactory(http: HttpClient) {
    if(isDevMode()){
        return new TranslateHttpLoader(http,"/api/base/i18n/","");
    }else{
        return new TranslateHttpLoader(http,"/ti/api/base/i18n/","");
    }
}

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        CoreModule,
        LayoutModule,
        AuthenticationModule,
        FileUploadModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        })

    ],
    providers: [
        UtilsService,
        WebsocketService,
        NotificationService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true
        }],
    bootstrap: [AppComponent],
})
export class AppModule {

    constructor(translate: TranslateService,http: HttpClient){
        translate.setDefaultLang('tr');
        translate.use('tr');
    }

}
