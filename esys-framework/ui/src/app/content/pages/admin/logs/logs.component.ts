import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import * as moment from 'moment';
import {TranslateService} from "@ngx-translate/core";

declare const $:any;

@Component({
    templateUrl: './logs.component.html',
})
export class LogsComponent implements OnInit{

    modal;traceModal;table;traceTable;
    data : any = {};
    exception : string= '';

    constructor(private tokenService : TokenStorage,
                private cd : ChangeDetectorRef,
                private translate: TranslateService) {
    }

    ngOnInit(): void {
        moment.locale(this.translate.currentLang);
        this.modal = $('#log-detail-modal').modal('hide');
        this.traceModal = $('#log-ex-modal').modal('hide');

        this.table = $('#log-table').DataTable({
            'ajax': {
                type: 'GET',
                url: '/api/base/logevent/paging',
                headers: {
                    'Authorization': 'Bearer ' + this.tokenService.getAccessTokenSync()
                }
            },
            order: [[ 2, "desc" ]],
            serverSide: true,
            processing: true,
            fixedHeader: true,
            pageLength: 25,
            //  dom: 'Bfrtip',
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ],
            columns: [
                {
                    data: 'id',
                    orderable : false,
                    render : function (data,type,row) {
                        return `<button class="btn btn-circle log-detail"><i class="fa fa-search" aria-label="Bul"></i></button>`
                    }
                },
                {
                    data: 'success',
                    title: '',
                    render: function (data, type, row) {
                        return data == true ? '<i class="fa fa-check-circle font-success"></i>' :
                            '<i class="fa fa-times-circle font-error"></i>';
                    },
                },
                {
                    data: 'createdDate',
                    title: 'Zaman',
                    render: function(data, type, row) {
                        if(!data) return "";
                        return moment(data,"YYYY-MM-DDTHH:mm:ss.SSSSZ").format("LLL");
                    }
                },
                {
                    data: 'module',
                    title: 'Modül'
                }, {
                    data: 'createdBy',
                    title: 'Kullanıcı'
                }, {
                    data: 'service',
                    title: 'Servis',
                    render: function (data, type, row) {
                        if(data && data.indexOf('.') > -1){
                        return data.substring(data.lastIndexOf('.')+1,data.length);
                        }
                        return data

                    },
                }, {
                    data: 'method',
                    title: 'İşlem',
                }, {
                    data: 'executionTime',
                    title: 'Süre',
                    render: function (data, type, row) {
                        return data + ' ms'

                    },
                }, {
                    data: 'ip',
                    title: 'Ip adresi',
                }, {
                    data: 'client',
                    title: 'İstemci',
                }, {
                    data: 'browser',
                    title: 'Tarayıcı',
                }],
            // scrollY: 400,
            // scroller: {
            //     loadingIndicator: true
            // },
            // stateSave: true
        });
        this.table.buttons().container().appendTo($('.col-sm-6:eq(0)', this.table.table().container()));
        this.initAction();
    }

    traceInit(){
        this.traceTable = $('#trace-table').DataTable({
            'ajax': {
                type: 'GET',
                url: '/api/base/logexception/paging',
                headers: {
                    'Authorization': 'Bearer ' + this.tokenService.getAccessTokenSync()
                }
            },
            order: [[ 2, "desc" ]],
            serverSide: true,
            processing: true,
            fixedHeader: true,
            pageLength: 25,
            //  dom: 'Bfrtip',
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ],
            columns: [
                {
                    data: 'id',
                    orderable : false,
                    render : function (data,type,row) {
                        return `<button class="btn btn-circle trace-detail"><i class="fa fa-search" aria-label="Detay"></i></button>`
                    }
                },
                {
                    data: 'success',
                    title: '',
                    render: function (data, type, row) {
                        return data == true ? '<i class="fa fa-check-circle font-success"></i>' :
                            '<i class="fa fa-times-circle font-error"></i>';
                    },
                },
                {
                    data: 'createdDate',
                    title: 'Zaman',
                    render: function(data, type, row) {
                        if(!data) return "";
                        return moment(data,"YYYY-MM-DDTHH:mm:ss.SSSSZ").format("LLL");
                    }
                },
                {
                    data: 'module',
                    title: 'Modül'
                }, {
                    data: 'createdBy',
                    title: 'Kullanıcı'
                }, {
                    data: 'message',
                    title: 'Mesaj',
                }, {
                    data: 'ip',
                    title: 'Ip adresi',
                }, {
                    data: 'client',
                    title: 'İstemci',
                }, {
                    data: 'browser',
                    title: 'Tarayıcı',
                }],
            // scrollY: 400,
            // scroller: {
            //     loadingIndicator: true
            // },
            // stateSave: true
        });
        this.traceTable.buttons().container().appendTo($('.col-sm-6:eq(0)', this.traceTable.table().container()));
        this.initTraceAction();
    }


    initAction(){
        const that = this;
        $('#log-table tbody').on( 'click', 'button', function () {
            var data = that.table.row( $(this).parents('tr') ).data();

            if($(this).hasClass('log-detail')){
                that.detail(data);
            }
        } );
    }

    initTraceAction(){
        const that = this;
        $('#trace-table tbody').on( 'click', 'button', function () {
            var data = that.traceTable.row( $(this).parents('tr') ).data();
            if($(this).hasClass('trace-detail')){
                that.traceDetail(data);
            }
        } );
    }


    detail(data){
        this.data = data;
        this.openModal();
        this.cd.detectChanges();
    }

    traceDetail(data){
        this.exception = data.exception;
        this.traceModal.modal('show');
        this.cd.detectChanges();
    }

    openModal(){
        this.modal.modal('show');
    }

    closeModal(){
        this.modal.modal('hide');
    }

    closeTraceModal(){
        this.traceModal.modal('hide');
    }
    getDate(date){
        moment("YYYY-MM-DDTHH:mm:ss.SSSSZ",date).format("LLL");
    }

    getDate2(date){
        moment("YYYY-MM-DDTHH:mm:ss.SSSSZ",date).fromNow();
    }


}
