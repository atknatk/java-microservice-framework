import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";


declare const $:any;

@Injectable()
export class OrganizationsService{



    constructor(private http : HttpClient){

    }

    getAllMainGroup() : Observable<any>{
         return this.http.get('/api/organization/maingroup');
    }

    getGroup(idMainGroup : number) : Observable<any>{
        return this.http.get('/api/organization/maingroup/' + idMainGroup+ '/group');
    }

    getCompany(idGroup : number) : Observable<any>{
        return this.http.get('/api/organization/group/' + idGroup+ '/company');
    }

    getBranchOffice(idCompany : number) : Observable<any>{
        return this.http.get('/api/organization/company/' + idCompany+ '/branchoffice');
    }

    getEmployee(idBranchOffice : number) : Observable<any>{
        return this.http.get('/api/organization/branchoffice/' + idBranchOffice+ '/employee');
    }

    getCustomer(idEmployee : number) : Observable<any>{
        return this.http.get('/api/organization/employee/' + idEmployee+ '/customer');
    }


    getMainGroupUser(id :number) : Observable<any>{
        return this.http.get('/api/organization/maingroup/' + id+ '/user');
    }

    getGroupUser(id :number) : Observable<any>{
        return this.http.get('/api/organization/group/' + id+ '/user');
    }
    getCompanyUser(id :number) : Observable<any>{
        return this.http.get('/api/organization/company/' + id+ '/user');
    }
    getBranchofficeUser(id :number) : Observable<any>{
        return this.http.get('/api/organization/branchoffice/' + id+ '/user');
    }
    getEmployeeUser(id :number) : Observable<any>{
        return this.http.get('/api/organization/employee/' + id+ '/user');
    }
    getCustomerUser(id :number) : Observable<any>{
        return this.http.get('/api/organization/customer/' + id+ '/user');
    }


    saveMainGroupUser(id :number,user:any[]) : Observable<any>{
        return this.http.post('/api/organization/maingroup/' + id+ '/user',user);
    }

    saveGroupUser(id :number,user:any[]) : Observable<any>{
        return this.http.post('/api/organization/group/' + id+ '/user',user);
    }

    saveCompanyUser(id :number,user:any[]) : Observable<any>{
        return this.http.post('/api/organization/company/' + id+ '/user',user);
    }

    saveBranchOfficeUser(id :number,user:any[]) : Observable<any>{
        return this.http.post('/api/organization/branchoffice/' + id+ '/user',user);
    }

    saveEmployeeUser(id :number,user:any[]) : Observable<any>{
        return this.http.post('/api/organization/employee/' + id+ '/user',user);
    }

    saveCustomerUser(id :number,user:any[]) : Observable<any>{
        return this.http.post('/api/organization/customer/' + id+ '/user',user);
    }





    removeMainGroupUser(id: any, user: any) {
        return this.http.delete('/api/organization/maingroup/' + id+ '/user/'+ user.id);
    }

    removeGroupUser(id: any, user: any) {
        return this.http.delete('/api/organization/group/' + id+ '/user/'+ user.id);
    }

    removeCompanyUser(id: any, user: any) {
        return this.http.delete('/api/organization/company/' + id+ '/user/'+ user.id);
    }

    removeBranchOfficeUser(id: any, user: any) {
        return this.http.delete('/api/organization/branchoffice/' + id+ '/user/'+ user.id);
    }

    removeEmployeeUser(id: any, user: any) {
        return this.http.delete('/api/organization/employee/' + id+ '/user/'+ user.id);
    }

    removeCustomerUser(id: any, user: any) {
        return this.http.delete('/api/organization/customer/' + id+ '/user/'+ user.id);
    }

}
