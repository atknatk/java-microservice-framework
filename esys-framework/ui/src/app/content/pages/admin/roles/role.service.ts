import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs/index";
import {TokenStorage} from "../../../../core/auth/token-storage.service";

@Injectable()
export class RoleService{

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


    getAllRoles() : Observable<any> {
       return this.http.get('api/uaa/role',this.getHeader());
    }

    getAllAuthorities() : Observable<any> {
        return this.http.get('api/uaa/authority',this.getHeader());
    }

    save(role :any) : Observable<any> {
        return this.http.post('api/uaa/role',role,this.getHeader());
    }

    remove(roleId : number) : Observable<any> {
        return this.http.delete('api/uaa/role/'+roleId,this.getHeader());
    }

    update(role : any) : Observable<any> {
        return this.http.put('api/uaa/role/'+role.id,role,this.getHeader());
    }

    getAllAuthoritiesByRole(id) : Observable<any> {
        return this.http.get(`api/uaa/role/${id}/authority`,this.getHeader());
    }

}
