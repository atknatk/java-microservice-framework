import {from, Observable, of, Subject} from 'rxjs';
import {catchError, map} from 'rxjs/operators';
import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {AuthService} from 'ngx-auth';
import {TokenStorage} from './token-storage.service';
import {UtilsService} from '../services/utils.service';
import {AccessData} from './access-data';
import {Credential} from './credential';
import {WebsocketService} from "../../content/pages/services/websocket.service";
import swal from "sweetalert";

declare const $:any;

@Injectable()
export class AuthenticationService implements AuthService {
	API_URL = 'api';
    API_USER_INFO = '/uaa/user/me';
	API_ENDPOINT_LOGIN = '/uaa/oauth/token';
	API_ENDPOINT_REFRESH = '/uaa/oauth/token';
	API_ENDPOINT_REGISTER = '/uaa/account/register/captcha';
    API_ENDPOINT_OAUTH2_CLIENTS = '/uaa/account/oauth2/client';
	API_ENDPOINT_RESETPASSWORD = '/uaa/user/resetPassword';
    interruptedUrl: string = '/';

	public onCredentialUpdated$: Subject<AccessData>;

	constructor(
		private http: HttpClient,
		private tokenStorage: TokenStorage,
		private util: UtilsService,
        private websocketService : WebsocketService

	) {
		this.onCredentialUpdated$ = new Subject();
	}

	/**
	 * Check, if user already authorized.
	 * @description Should return Observable with true or false values
	 * @returns {Observable<boolean>}
	 * @memberOf AuthService
	 */
	public isAuthorized(): Observable<boolean> {
		return this.tokenStorage.getAccessToken().pipe(map(token => !!token));
	}

	/**
	 * Get access token
	 * @description Should return access token in Observable from e.g. localStorage
	 * @returns {Observable<string>}
	 */
	public getAccessToken(): Observable<string> {
		return this.tokenStorage.getAccessToken();
	}

	/**
	 * Get user roles
	 * @returns {Observable<any>}
	 */
	public getUserRoles(): Observable<any> {
		return this.tokenStorage.getUserRoles();
	}

	/**
	 * Function, that should perform refresh token verifyTokenRequest
	 * @description Should be successfully completed so interceptor
	 * can execute pending requests or retry original one
	 * @returns {Observable<any>}
	 */
	public refreshToken(): Observable<AccessData> | void | any {
        // const httpOptions = {
        //     headers: new HttpHeaders({
        //         'Content-Type':  'application/x-www-form-urlencoded',
        //         'Authorization': 'Basic Y2xpZW50YXBwOnBhc3N3b3Jk'
        //     })};
		// return this.tokenStorage.getRefreshToken().pipe(
		// 	switchMap((refreshToken: string) => {
        //
        //
        //         // return Observable.create<any>(promise => {
        //         //
        //         //     this.http.post<AccessData>(this.API_URL + this.API_ENDPOINT_LOGIN,
        //         //         `grant_type=refresh_token&refresh_token=${refreshToken}`,httpOptions)
        //         //         .subscribe(tokenRes => {
        //         //             this.tokenStorage.setAccessToken(tokenRes.access_token);
        //         //             promise.next();
        //         //             promise.complete();
        //         //         }, e2 => {
        //         //             this.handleError('login', e2.error)
        //         //             promise.next();
        //         //             promise.complete();
        //         //         });
        //         // });
		// 	}),
		// 	tap(this.saveAccessData.bind(this)),
		// 	catchError(err => {
		// 		this.logout();
		// 		return throwError(err);
		// 	})
        //);
        return null;
	}

	/**
	 * Function, checks response of failed request to determine,
	 * whether token be refreshed or not.
	 * @description Essentialy checks status
	 * @param {Response} response
	 * @returns {boolean}
	 */
	public refreshShouldHappen(response: HttpErrorResponse): boolean {
		return response.status === 401;
	}

	/**
	 * Verify that outgoing request is refresh-token,
	 * so interceptor won't intercept this request
	 * @param {string} url
	 * @returns {boolean}
	 */
	public verifyTokenRequest(url: string): boolean {
		return url.endsWith(this.API_ENDPOINT_REFRESH);
	}

    public requestRole(role: string){
        const swalData : any = {
            title: `Bu işlemi yapabilmek için yetkiniz bulunmamaktadır`,
            text: 'Yetkiliden yetki ister misiniz?',
            icon: 'warning',
            buttons: true,
            dangerMode: true,
        };
        swal(swalData).then((willDelete) => {
            if (willDelete) {
                this.http.post(this.API_URL+"/uaa/authority/request",{name : role}).subscribe((res : any) => {
                    if(res.success){
                        swal('Yetki isteme işlemi başarılı.', {
                            icon: 'success',
                        });
                    }else{
                        swal("Yetki isteme işleminde bir hata oluştu.", {
                            icon: "danger",
                        });
                    }
                })
            } else {
                swal("İşleminiz iptal edilmiştir.!");
            }
        });
    }
	/**
	 * Submit login request
	 * @param {Credential} credential
	 * @returns {Observable<any>}
	 */
	public login(credential: Credential): Observable<any> {
		// Expecting response from API
		// {"id":1,"username":"admin","password":"demo","email":"admin@demo.com","accessToken":"access-token-0.022563452858263444","refreshToken":"access-token-0.9348573301432961","roles":["ADMIN"],"pic":"./assets/app/media/img/users/user4.jpg","fullname":"Mark Andre"}
        this.tokenStorage.clear();
		const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type':  'application/x-www-form-urlencoded',
				'Authorization': 'Basic Y2xpZW50YXBwOnBhc3N3b3Jk'
            })};

       return Observable.create(promise => {

           this.http.post<AccessData>(this.API_URL + this.API_ENDPOINT_LOGIN,
               `username=${credential.email}&password=${credential.password}&grant_type=password&scope=webclient&domain=isis`,
               httpOptions)
               .subscribe(tokenRes => {
			       this.tokenStorage
                       .setAccessToken(tokenRes.access_token);

                   this.http.get(this.API_URL + this.API_USER_INFO).subscribe((res: any) => {
                   	if(res.success == false){

                   		return
					}
					const user : any= res.data;
                   	const roles = [];
                       user.roles.forEach(l => {
                           l.authorities.forEach(z => {
                               roles.push(z.name);
                           })
                       })

                       delete user.roles
                       const data: AccessData = {
                           access_token: tokenRes.access_token,
                           refresh_token: tokenRes.refresh_token,
                           roles: roles,
                           userData : user
                       };
                       this.saveAccessData(data);
                       promise.next(data);
                       promise.complete();
                   }, e2 => {
                       this.handleError('login', [])
                   });


               }, e2 => {
                   this.handleError('login', e2.error)
               });
       });

       /* return this.http.post(this.API_URL + this.API_ENDPOINT_LOGIN,
            `username=${credential.email}&password=${credential.password}&grant_type=password&scope=mobileclient`,
            httpOptions).pipe(
			map((result: any) => {
				if (result instanceof Array) {
					return result.pop();
				}


				return result;
			}),
			tap(this.saveAccessData.bind(this)),
			catchError(this.handleError('login', []))
		);*/
	}

	/**
	 * Handle Http operation that failed.
	 * Let the app continue.
	 * @param operation - name of the operation that failed
	 * @param result - optional value to return as the observable result
	 */
	private handleError<T>(operation = 'operation', result?: any) {
     if(result.status == -1 || result.status == undefined){
         $.toast({
             heading: 'Hata',
             text: 'Beklenmeyen bir hata oluştu',
             position: 'top-right',
             loaderBg: '#fff',
             icon: 'error',
             hideAfter: 3500,
             stack: 6
         })
	 } else{
            $.toast({
                heading: result.title,
                text: result.message,
                position: 'top-right',
                loaderBg: '#fff',
                icon: 'error',
                hideAfter: 3500,
                stack: 6
            })

		}
		return (error: any): Observable<any> => {
			// TODO: send the error to remote logging infrastructure
			console.error(error); // log to console instead

			// Let the app keep running by returning an empty result.
			return from(result);
		};
	}

	/**
	 * Logout
	 */
	public logout(refresh?: boolean): void {
		if(this.websocketService.isConnected()){
            this.websocketService.disconnect(() => {
                this.tokenStorage.clear();
                if (refresh) {
                    location.reload(true);
                }
            });
		}else{
            this.tokenStorage.clear();
            if (refresh) {
                location.reload(true);
            }
		}

	}

	/**
	 * Save access data in the storage
	 * @private
	 * @param {AccessData} data
	 */
	private saveAccessData(accessData: AccessData) {
		if (typeof accessData !== 'undefined') {
			this.tokenStorage
				.setAccessToken(accessData.access_token)
				.setRefreshToken(accessData.refresh_token)
                .setUserData(accessData.userData)
				.setUserRoles(accessData.roles);
			this.onCredentialUpdated$.next(accessData);
		}
	}

	/**
	 * Submit registration request
	 * @param {Credential} credential
	 * @returns {Observable<any>}
	 */
	public register(value : {
                        credential: Credential,
                        capthcaKey? : string
					}): Observable<any> {
		const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type':  'application/json',
                'Authorization': 'Basic Y2xpZW50YXBwOnBhc3N3b3Jk'
            })};
		return this.http.post(this.API_URL + this.API_ENDPOINT_REGISTER+`?g-recaptcha-response=${value.capthcaKey}`,
            value.credential, httpOptions)
			.pipe(catchError(val => of(this.handleError('register', val.error)))
		);
	}

	/**
	 * Submit forgot password request
	 * @param {Credential} credential
	 * @returns {Observable<any>}
	 */
	public requestPassword(credential: Credential): Observable<any> {
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type':  'application/json',
                'Authorization': 'Basic Y2xpZW50YXBwOnBhc3N3b3Jk'
            })};

		return this.http.post(this.API_URL + this.API_ENDPOINT_RESETPASSWORD + '?' + this.util.urlParam(credential), httpOptions)
			.pipe(catchError(this.handleError('forgot-password', []))
		);
	}

    public getInterruptedUrl(): string {
        return this.interruptedUrl;
    }

    public setInterruptedUrl(url: string): void {
        this.interruptedUrl = url;
    }


    /**
     * Submit forgot password request
     * @param {Credential} credential
     * @returns {Observable<any>}
     */
    public getOAuth2Clients(): Observable<any> {
        const httpOptions = {
            headers: new HttpHeaders({
                'Content-Type':  'application/json',
                'Authorization': 'Basic Y2xpZW50YXBwOnBhc3N3b3Jk'
            })};

        return this.http.get(this.API_URL + this.API_ENDPOINT_OAUTH2_CLIENTS, httpOptions);
    }



}
