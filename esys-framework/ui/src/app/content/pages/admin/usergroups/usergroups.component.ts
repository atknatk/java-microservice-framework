import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import {AuthenticationService} from "../../../../core/auth/authentication.service";
import {NotificationService} from "../../../../core/services/notification.service";
import {TranslateService} from "@ngx-translate/core";
import * as moment from "moment";
import swal from "sweetalert";
import {UsergroupService} from "./usergroup.service";

declare const $:any;

@Component({
    templateUrl: './usergroups.component.html',
    providers : [UsergroupService]
})
export class UsergroupsComponent implements OnInit{

    @Input() newName: any = {};
    editedData: any;
    @Input() dataTable :any;
    @Input() name;
    modal;

    users: any[] = [];
    selectedUsers: any[]= [];


    constructor(private tokenService : TokenStorage,
                private service : UsergroupService,
                private authService: AuthenticationService,
                private _nf: NotificationService,
                private translate : TranslateService,
                private cd: ChangeDetectorRef) {
    }



    ngOnInit(): void {
        moment.locale(this.translate.currentLang);
        const that= this;
        this.dataTable = $('#usergroup-datatable').DataTable(that.getDatatableOptions());
        this.initAction();
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
                url: '/api/uaa/usergroup/paging',
                headers: {
                    'Authorization': 'Bearer ' + this.tokenService.getAccessTokenSync()
                },
                error: (jqXHR, textStatus, errorThrown)  => {
                    if(jqXHR.status == 403){
                        this.authService.requestRole("authority.usergroup.view");
                    }else {
                        this._nf.err("Hata","Beklenmeyen bir hata oluştu");
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
                        let ret = `<button type="button" class="edit-usergroup btn btn-sm btn-info">
                                          <i class="fa fa-edit"></i>`;
                        if(!row.predefined){
                            ret += `<button type="button" 
                                            style="margin-left: 5px;"
                                            class="remove-usergroup btn btn-sm btn-danger">
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
        $('#usergroup-datatable tbody').on( 'click', 'button', function () {
            var data = that.dataTable.row( $(this).parents('tr') ).data();

            if($(this).hasClass('remove-usergroup')){
                that.remove(data);
            }else if($(this).hasClass('edit-usergroup')){
                that.edit(data);
            }
        } );

        if(this.users.length == 0){
            this.service.getAllUsers().subscribe(res =>  {
                this.users = res.data;
                this.cd.detectChanges();
            });
        }

    }


    remove (data){
        const swalData : any = {
            title: `${data.name} Kullanıcı Grubunu silmek istiyor musunuz ?`,
            text: 'Silinen bir Kullanıcı Grubu tekrar geri alınamaz',
            icon: 'warning',
            buttons: true,
            dangerMode: true,
        };
        swal(swalData).then((willDelete) => {
            if (willDelete) {
                this.service.remove(data.id).subscribe(res => {
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


    edit (data){
        this.editedData = data;
        this.openModal();
    }


    openModal(){
        this.modal.modal('show')
        if(this.editedData){
            this.newName.name = this.editedData.name;
            this.newName.enabled = this.editedData.enabled;
            this.newName.id = this.editedData.id;

            this.service.getAllUsers(this.editedData.id).subscribe(res => {
                if(res.success){
                    this.selectedUsers = res.data;
                    this.cd.detectChanges();
                }
            });

        }else{
            this.newName.name = null;
            this.newName.id = 0;
            this.service.getAllUsers().subscribe(res =>  {
                this.users = res.data;
                this.cd.detectChanges();
            });
            this.selectedUsers  =[];
        }

    }


    newRecord() {
        this.editedData = undefined;
        this.openModal();
    }

    setClickedRow (user){
        if(this.selectedUsers.filter(l => l.id == user.id).length > 0){
            this.selectedUsers = this.selectedUsers.filter(l => l.id != user.id);
        }else{
            this.selectedUsers.push(user);
        }
        this.cd.detectChanges();
        console.table(this.selectedUsers);
    }

    isActive(user){
        return this.selectedUsers.filter(l => l.id == user.id).length > 0;
    }


    save(){
        if(this.newName.name == '' || this.newName.name == null || this.newName.name === undefined){
            alert("Kullanıcı Grubunu adınını girmelisiniz");
            return;
        }

        if(this.newName.id > 0){
            this.service.update(this.newName).subscribe(res => {
                this.service.assign(this.newName.id, JSON.parse(JSON.stringify(this.selectedUsers))).subscribe(r2 => {
                    this._nf.showResult(res)
                    this.closeModal();
                    this.dataTable.ajax.reload();
                    this.cd.detectChanges();
                });

            })
        }else{
            this.service.save(this.newName).subscribe(res => {
                this._nf.showResult(res)
                this.closeModal();
                this.dataTable.ajax.reload();
                this.cd.detectChanges();
            })
        }



    }

    closeModal(){
        this.modal.modal('hide')
    }
}
