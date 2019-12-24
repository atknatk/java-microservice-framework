import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {catchError, map} from "rxjs/operators";
import {NotificationService} from "../../../../core/services/notification.service";

declare const $:any;

@Injectable()
export class ProductService{

    constructor(private http: HttpClient,
                private notification: NotificationService){}

    downloadXls(): Observable<any> {
        return this.http.get('api/base/product/export/xls?datatablesColumns=name,price').pipe(catchError(err => {
            console.log(err);
            return Observable.throw(err || 'Server error')
        }))
    }

    downloadCsv(): Observable<any> {
        return this.http.get('api/base/product/export/csv?datatablesColumns=name,price').pipe(catchError(err => {
            console.log(err);
            return Observable.throw(err || 'Server error')
        }))
    }

    getProductList() : Observable<any> {
        return this.http.get('api/base/product').pipe(map((res :any) => {
            if(res.success){
                return res.data;
            }
            this.notification.showResult(res);
            return [];
        }));
    }

    getVersionList() : Observable<any> {
        return this.http.get('api/base/product/version').pipe(map((res :any) => {
            if(res.success){
                return res.data;
            }
            this.notification.showResult(res);
            return [];
        }));
    }

    getSumPrices() : Observable<any> {
        return this.http.get('api/base/product/sum');
    }

    revert(versionId: any,password) {
        return this.http.put('api/base/product/revert/'+versionId,{password});
    }

    sendMail(mail : string, versionId?: any) {
        if(versionId){
            return this.http.get(`api/base/product/send?datatablesColumns=name,piece,price,total&mail=${mail}&version=${versionId}`);
        }else {
            return this.http.get(`api/base/product/send?datatablesColumns=name,piece,price,total&mail=${mail}`);
        }

    }
}
