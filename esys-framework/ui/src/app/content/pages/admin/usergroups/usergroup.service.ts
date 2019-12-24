import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {TokenStorage} from "../../../../core/auth/token-storage.service";

@Injectable()
export class UsergroupService{


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


    getAll() : Observable<any> {
        return this.http.get('api/uaa/usergroup',this.getHeader());
    }

    save(role :any) : Observable<any> {
        return this.http.post('api/uaa/usergroup',role,this.getHeader());
    }

    remove(roleId : number) : Observable<any> {
        return this.http.delete('api/uaa/usergroup/'+roleId,this.getHeader());
    }

    update(role : any) : Observable<any> {
        return this.http.put('api/uaa/usergroup/'+role.id,role,this.getHeader());
    }

    assign(id : any,list : any[]) : Observable<any> {
        return this.http.put('api/uaa/usergroup/'+id + '/user',list,this.getHeader());
    }

    getAllUsers(id?) : Observable<any> {
        if(id=== undefined){
            return this.http.get(`api/uaa/user`,this.getHeader());
        }else{
            return this.http.get(`api/uaa/usergroup/${id}/user`,this.getHeader());
        }

    }




}
