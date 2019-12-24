import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {TokenStorage} from "../../../../core/auth/token-storage.service";

@Injectable()
export class UserService{

    constructor(private readonly http : HttpClient, private tokenStorage: TokenStorage){}

    token;
    httpOptions;
    getToken() : string {
        if(this.token) return this.token;

        this.token = this.tokenStorage.getAccessTokenSync();
        return this.token;
    }

    getHeader() : any {
        if(this.httpOptions) return this.httpOptions;

        this.httpOptions = {
            headers: new HttpHeaders({
                'Content-Type':  'application/json',
                'Authorization': 'Bearer ' + this.getToken()
            })};
        return this.httpOptions;
    }



    getAllAuthorities() : Observable<any> {
        return this.http.get('api/uaa/authority',this.getHeader());
    }

    save(user :any) : Observable<any> {
        return this.http.post('api/uaa/account/register', user ,this.getHeader());
    }

    remove(roleId : number) : Observable<any> {
        return this.http.delete('api/uaa/user/'+roleId,this.getHeader());
    }

    update(role : any) : Observable<any> {
        return this.http.put('api/uaa/user/'+role.id,role,this.getHeader());
    }


}
