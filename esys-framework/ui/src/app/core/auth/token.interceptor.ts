import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {TokenStorage} from "./token-storage.service";
import {TranslateService} from "@ngx-translate/core";
import {catchError} from "rxjs/operators";
import {AuthenticationService} from "./authentication.service";
import {AccessData} from "./access-data";
import {NotificationService} from "../services/notification.service";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    isRefreshingToken: boolean = false;
    tokenSubject: BehaviorSubject<AccessData> = new BehaviorSubject<AccessData>(null);


    constructor(public tokenStorage: TokenStorage,
                private translate: TranslateService,
                private _nf: NotificationService,
                private authService : AuthenticationService) {}

    addToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
        return req.clone({ setHeaders: {
            Authorization: 'Bearer ' + token,
                "Accept-Language" : this.translate.currentLang ? this.translate.currentLang.substring(0,2) : "",
            }})
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const token = this.tokenStorage.getAccessTokenSync();

        if(token){
            return next.handle(this.addToken(request, token))
                .pipe(catchError(error => {
                    if(error instanceof HttpErrorResponse){
                        if(error.status==400){
                           return this.handle400Error(error);
                        }else if(error.status == 401){
                           return this.logoutUser();
                            // return this.handle401Error(request, next);
                        }else if(error.status == 403){
                         //   this.authService.requestRole("");

                            // return this.handle401Error(request, next);
                        }
                    }
                    return Observable.throw(error);
                }));
        }else{
            return next.handle(request);
        }


    }

    private authRequest(){

    }


    handle400Error(error) :Observable<any>{
        if (error && error.status === 400 && error.error && error.error.error === 'invalid_grant') {
            return this.logoutUser()
        }
        this._nf.showResult(error.error);

        return Observable.throw(error);
    }

    logoutUser() {
        // Route to the login page (implementation up to you)
        this.authService.logout(true);
        return Observable.throw("");
    }
}
