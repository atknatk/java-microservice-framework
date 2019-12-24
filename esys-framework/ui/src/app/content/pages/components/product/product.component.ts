import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import * as moment from 'moment';
import {TranslateService} from "@ngx-translate/core";
import {ProductService} from "./product.service";
import {WindowRef} from "../../../../core/services/windowref.service";
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import {FileUploader} from "ng2-file-upload";
import {NotificationService} from "../../../../core/services/notification.service";
import {AuthenticationService} from "../../../../core/auth/authentication.service";

declare const $: any

@Component({
    templateUrl: './product.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductComponent implements OnInit, AfterViewInit, OnDestroy {


    private today;
    private modal;
    private passwordModal;
    private mailModal;
    config: any;
    message = '';
    mail = '';
    password = '';
    nativeWindow: any;
    products: any[] = [];
    reportVersions: any[] = [];
    sumPrices :any = {};
    versionId: number = 0;
    public uploader: FileUploader = new FileUploader(
        {
            url: '/api/base/product/upload',
            itemAlias: 'file',
            authToken: 'Bearer ' + this.storage.getAccessTokenSync(),
            allowedFileType: ['xls', 'xlsx']
        });

    constructor(private service: ProductService,
                private cdr: ChangeDetectorRef,
                private winRef: WindowRef,
                private authService: AuthenticationService,
                private notification: NotificationService,
                private storage: TokenStorage,
                private translate: TranslateService) {

        this.nativeWindow = winRef.getNativeWindow();
    }

    ngOnInit(): void {
        moment.locale(this.translate.currentLang);
        this.modal = $('#settings-modal').modal('hide');
        this.passwordModal = $('#password-modal').modal('hide');
        this.mailModal = $('#mail-modal').modal('hide');

        this.loadProduct();
        this.today = moment(new Date(), "YYYY-MM-DDTHH:mm:ss.SSSSZ").startOf('day').format("LL");
        this.uploader.onAfterAddingFile = (file) => {
            file.withCredentials = false;
        };
        this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {
            // console.log('ImageUpload:uploaded:', item, status, response);
            this.closeModal();
            this.loadProduct();
        };
    }

    ngAfterViewInit(): void {

    }

    ngOnDestroy(): void {
    }

    loadProduct() {
        this.service.getProductList().subscribe(res => {
            this.products = res;
            this.cdr.detectChanges();
        });
        this.service.getSumPrices().subscribe(_ => {
            this.sumPrices = _;
            this.cdr.detectChanges();
        });
    }

    exportPdf(version?) {
        if (version && !version.current) {
            this.nativeWindow.open('api/base/product/export/pdf?datatablesColumns=name,piece,price,total&version=' + version.version
                + '&auth_token=' + this.storage.getAccessTokenSync() + "&locale=" + this.translate.currentLang);
        } else {
            this.nativeWindow.open('api/base/product/export/pdf?datatablesColumns=name,piece,price,total&auth_token=' +
                this.storage.getAccessTokenSync() + "&locale=" + this.translate.currentLang);
        }

    }

    exportXls(version?) {
        if (version) {
            this.nativeWindow.open('api/base/product/export/xls?datatablesColumns=name,piece,price,total&version=' + version
                + '&auth_token=' + this.storage.getAccessTokenSync() + "&locale=" + this.translate.currentLang);
        } else {
            this.nativeWindow.open('api/base/product/export/xls?datatablesColumns=name,piece,price,total&auth_token=' +
                this.storage.getAccessTokenSync() + "&locale=" + this.translate.currentLang);
        }

    }

    exportCsv() {
        this.nativeWindow.open('api/base/product/export/csv?datatablesColumns=name,piece,price,total&auth_token=' +
            this.storage.getAccessTokenSync() + "&locale=" + this.translate.currentLang);
    }


    openModal() {
        this.modal.modal('show');
        this.service.getVersionList().subscribe(_ => {
            this.reportVersions = _;
            this.cdr.detectChanges();
        });
    }


    openpasswordModal() {
        this.passwordModal.modal('show');
        this.password = '';
    }

    openMailModal() {
        this.mailModal.modal('show');
        this.mail = '';
    }



    closeModal() {
        this.modal.modal('hide')
    }

    closePasswordModal() {
        this.passwordModal.modal('hide')
    }

    closeMailModal() {
        this.mailModal.modal('hide')
    }


    getDate(date) {
        return moment(date, "YYYY-MM-DDTHH:mm:ss.SSSSZ").format("LLL");
    }

    revertReq() {
        this.service.revert(this.versionId, this.password).subscribe((res: any) => {
            if (res.success) {
                this.closePasswordModal();
                this.loadProduct();
            } else {
                this.notification.showResult(res);
            }
        }, e => {
            console.log(e);
        });
    }


    revert(versionId) {
        this.versionId = versionId;
        this.closeModal();
        this.openpasswordModal();
    }

    requestRole(role) {
        this.authService.requestRole(role);
    }

    sendMail(versionId?){
        this.versionId = versionId;
        this.closeModal();
        this.openMailModal();
    }

    sendMailReq(){
        this.closeMailModal();
        this.service.sendMail(this.mail).subscribe(res => {
            this.notification.showResult(res);
        })
    }

}
