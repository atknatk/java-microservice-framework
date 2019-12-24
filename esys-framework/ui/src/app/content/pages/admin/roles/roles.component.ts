import {ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import {RoleService} from "./role.service";
import {TreeNode} from "primeng/api";
import {isArray} from "util";
import swal from 'sweetalert';
import * as moment from "moment";
import {TranslateService} from "@ngx-translate/core";
import {AuthenticationService} from "../../../../core/auth/authentication.service";
import {NotificationService} from "../../../../core/services/notification.service";

declare const $:any;

@Component({
    templateUrl: './roles.component.html',
    providers: [RoleService],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class RolesComponent implements OnInit{

    @Input() newName: any = {};
    editedData: any;
    @Input() dataTable :any;
    @Input() name;
    modal;

    authorities: TreeNode[] = [];
    selectedAuthorities: TreeNode[]= [];

    constructor(private tokenService : TokenStorage,
                private roleService : RoleService,
                private authService: AuthenticationService,
                private notification: NotificationService,
                private translate : TranslateService,
                private cd: ChangeDetectorRef) {
    }

    ngOnInit(): void {
        moment.locale(this.translate.currentLang);
        const that= this;
        this.dataTable = $('#role-datatable').DataTable(that.getDatatableOptions());
        this.initAction()
        this.modal = $('#role-modal2').modal('hide');
    }

    private getDatatableOptions(){
        const that= this;
        return  {
            serverSide: true,
            processing: true,
            fixedHeader: true,
            pageLength: 25,
            order: [[ 2, "asc" ]],
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ],
            'ajax': {
                type: 'GET',
                url: '/api/uaa/role/paging',
                headers: {
                    'Authorization': 'Bearer ' + this.tokenService.getAccessTokenSync()
                },
                error: (jqXHR, textStatus, errorThrown)  => {
                    if(jqXHR.status == 403){
                    this.authService.requestRole("authority.role.view");
                    }else {
                        this.notification.err("Hata","Beklenmeyen bir hata oluştu");
                    }
                }
            },
/*            columns: [
                {data: 'id'},
                {data: 'name'},
                {data: 'createdDate'}
                ],*/
            "columnDefs": [
                {
                    targets: 0,
                    title: "İşlemler",
                    orderable: !1,
                    searchable: !1,
                    width : "80px",
                    render: function(data, type, row) {
                        let ret = `<button type="button" class="edit-role btn btn-sm btn-info">
                                          <i class="fa fa-edit"></i>`;
                        if(!row.predefined){
                            ret += `<button type="button" 
                                            style="margin-left: 5px;"
                                            class="remove-role btn btn-sm btn-danger">
                                          <i class="fa fa-trash"></i>
                                          </button>`;
                        }
                        return ret;
                    }

                },
                {
                    targets: 1,
                    title: "Role Adı",
                    data : "name",
                    render: function (data, type, row) {
                        let col = data + '&nbsp;&nbsp;'
                        if(row.predefined){
                            col += `<span class="label label-info">Sabit</span>&nbsp;&nbsp;`
                        }
                        if(row.default){
                            col += `<span class="label label-primary">Varsayılan</span>`
                        }
                        return col
                    }
                },
                {
                    targets: 2,
                    title: "Eklenme Tarihi",
                    data : "createdDate",
                    render: function(data, type, row) {
                        if(!data) return "";
                        return moment(data,"YYYY-MM-DDTHH:mm:ss.SSSSZ").format("LLL");
                    }
                }
            ]
        };
    }

    initAction(){
        const that = this;
        $('#role-datatable tbody').on( 'click', 'button', function () {
            var data = that.dataTable.row( $(this).parents('tr') ).data();

            if($(this).hasClass('remove-role')){
                that.remove(data);
            }else if($(this).hasClass('edit-role')){
                that.edit(data);
            }
        } );
    }


    edit (data){
        this.editedData = data;
        this.openModal();
    }

    remove (data){
        const swalData : any = {
            title: `${data.name} rolünü silmek istiyor musunuz ?`,
            text: 'Silinen bir rol tekrar geri alınamaz',
            icon: 'warning',
            buttons: true,
            dangerMode: true,
        };
        swal(swalData).then((willDelete) => {
            if (willDelete) {
                this.roleService.remove(data.id).subscribe(res => {
                    if(res.success){
                        swal("Silme işlemi başarılı", {
                            icon: "success",
                        });
                        this.dataTable.ajax.reload();
                    }else{
                        swal("Silme işleminde bir hata oluştu", {
                            icon: "danger",
                        });
                    }
                })
            } else {
                swal("Veriniz güvende!");
            }
        });
    }


    openModal(){
        this.modal.modal('show')
        if(this.editedData){
            this.newName.name = this.editedData.name;
            this.newName.default = this.editedData.default;
            this.newName.id = this.editedData.id;
        }

        this.roleService.getAllAuthorities().subscribe(all => {
            if(all.success){
                this.authorities = [];
                this.selectedAuthorities = [];
                all.data.forEach( l => {
                    const dataNode = {
                        label : this.translate.instant(l.name),
                        data : l.id,
                        styleClass : 'col-xl-12'
                    };
                    this.authorities.push(dataNode);
                    if(this.editedData && isArray(this.editedData.authorities)){
                        if(this.editedData.authorities.filter(k => k.id == l.id).length > 0){
                            this.selectedAuthorities.push(dataNode)
                        }
                    }
                });
            }
            this.cd.detectChanges();
        })
    }



    saveRole(){
        console.log(this.newName)
        console.log(this.selectedAuthorities)

        if(this.newName.name == '' || this.newName.name == null || this.newName.name === undefined){
            alert("Role adınını girmelisiniz");
            return;
        }

        if(!(this.selectedAuthorities && this.selectedAuthorities.length > 0)){
            alert("En az bir tane yetki seçmelisiniz");
            return;
        }

        this.newName.authorities = [];
        this.selectedAuthorities.forEach((l:TreeNode) => {
            this.newName.authorities.push({
                id : l.data,
                name : l.label
            })
        });

        if(this.newName.id && this.newName.id > 0){
            this.roleService.update(this.newName).subscribe(res => {
                if(res.success){
                this.closeModal();
                    this.dataTable.ajax.reload();
                }else{
                    alert('hata')
                }
            })
        }else{
            this.roleService.save(this.newName).subscribe(res => {
                if(res.success){
                    this.closeModal();
                    this.dataTable.ajax.reload();
                }else{
                    alert('hata')
                }
            })
        }
    }


    closeModal(){
        this.modal.modal('hide')
    }

    newRoleRecord() {
     this.editedData = undefined;
     this.openModal();
    }

}
