import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import {RoleService} from "../roles/role.service";
import {TreeNode} from "primeng/api";
import {isArray} from "util";
import {UserService} from "./user.service";
import {NotificationService} from "../../../../core/services/notification.service";
import {TranslateService} from "@ngx-translate/core";
import * as moment from 'moment';
import {AuthenticationService} from "../../../../core/auth/authentication.service";

declare const $: any;

@Component({
    templateUrl: './users.component.html',
    providers : [RoleService,UserService],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class UsersComponent implements OnInit {

    authorities: TreeNode[] = [];
    selectedAuthorities: TreeNode[]= [];

    dataTable : any;
    private modal;
    editedData: any;
    userModel: any = {
        sendActivationEmail : true,
        enabled : true,
        isAccountNonLocked: true
    };
    constructor(private tokenService: TokenStorage,
                private userService : UserService,
                private authService : AuthenticationService,
                private cd: ChangeDetectorRef,
                private translate: TranslateService,
                private _nf: NotificationService,
                private roleService: RoleService) {
    }

    ngOnInit(): void {
        moment.locale(this.translate.currentLang);
        this.dataTable = $('#example').DataTable({
            'ajax': {
                type: 'GET',
                url: '/api/uaa/user/paging',
                headers: {
                    'Authorization': 'Bearer ' + this.tokenService.getAccessTokenSync()
                },
                error: (jqXHR, textStatus, errorThrown)  => {
                    if(jqXHR.status == 403){
                        this.authService.requestRole("authority.user.edit");
                    }else {
                        this._nf.err("Beklenmeyen bir hata oluştu");
                    }
                }
            },
            responsive: true,
            serverSide: true,
            processing: true,
            fixedHeader: true,
            pageLength: 25,
            order: [[ 7, "desc" ]],
          //  dom: 'Bfrtip',
            buttons: [
                'copyHtml5',
                'excelHtml5',
                'csvHtml5',
                'pdfHtml5'
            ],
            "columnDefs": [
                {
                    targets: 0,
                    title: "İşlemler",
                    orderable: !1,
                    searchable: !1,
                    width : "80px",
                    render: function(data, type, row) {
                        let ret = `<button type="button" class="edit-user btn btn-sm btn-info">
                                          <i class="fa fa-edit"></i>`;
                        if(!row.predefined){
                            ret += `<button type="button" 
                                            style="margin-left: 5px;"
                                            class="remove-user btn btn-sm btn-danger">
                                          <i class="fa fa-trash"></i>
                                          </button>`;
                        }
                        return ret;
                    }

                },
                {
                    targets: 1,
                    title: "Ad",
                    data : "firstName"
                },
                {
                    targets: 2,
                    title: "Soyad",
                    data : "lastName",
                },
                {
                    targets: 3,
                    title: "Roller",
                    data : "roles",
                    render: function(data, type, row) {
                        let roles = [];
                        data.forEach(l => roles.push(l.name))
                        return roles.join(', ');
                    }
                },
                {
                    targets: 4,
                    title: "E-posta adresi",
                    data : "email"
                },
                {
                    targets: 5,
                    title: "E-posta doğrulama",
                    data : "confirmEmail",
                    render: function(data, type, row) {
                        if(data == true){
                            return "Evet"
                        }else{
                            return "Hayır"
                        }
                    }
                },
                {
                    targets: 6,
                    title: "Son giriş zamanı",
                    data : "lastLoginDate",
                    orderable : !0,
                    render: function(data, type, row) {
                        if(!data) return "";
                        return moment(data,"YYYY-MM-DDTHH:mm:ss.SSSSZ").format("LLL");
                    }
                },
                {
                    targets: 7,
                    title: "Oluşturma zamanı",
                    data : "createdDate",
                    render: function(data, type, row) {
                        if(!data) return "";
                        return moment(data,"YYYY-MM-DDTHH:mm:ss.SSSSZ").format("LLL");
                    }
                }
            ]
            // scrollY: 400,
            // scroller: {
            //     loadingIndicator: true
            // },
            // stateSave: true
        });
        this.dataTable.buttons().container()
            .appendTo($('.col-sm-6:eq(0)', this.dataTable.table().container()));
        this.initAction();
        this.modal = $('#user-modal').modal('hide');
    }


    initAction(){
        const that = this;
        $('#example tbody').on( 'click', 'button', function () {
            var data = that.dataTable.row( $(this).parents('tr') ).data();

            if($(this).hasClass('remove-user')){
                that.remove(data);
            }else if($(this).hasClass('edit-user')){
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
            title: `${data.firstName} kullanıcısını silmek istiyor musunuz ?`,
            text: 'Silinen bir kullanıcı tekrar geri alınamaz',
            icon: 'warning',
            buttons: true,
            dangerMode: true,
        };
        swal(swalData).then((willDelete) => {
            if (willDelete) {
                this.userService.remove(data.id).subscribe(res => {
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
        this.modal.modal('show');
        this.authorities = [];
        this.selectedAuthorities = [];
        if(this.editedData){
            this.userModel.firstName = this.editedData.firstName;
            this.userModel.lastName = this.editedData.lastName;
            this.userModel.phone = this.editedData.phone;
            this.userModel.email = this.editedData.email;
            this.userModel.id = this.editedData.id;
            this.userModel.sendActivationEmail = this.editedData.sendActivationEmail;
            this.userModel.enabled = this.editedData.enabled;
            this.userModel.isAccountNonLocked = this.editedData.isAccountNonLocked;
            this.userModel.shouldChangePasswordOnNextLogin = this.editedData.shouldChangePasswordOnNextLogin;
        }

        if(this.userModel.id > 0){
            this.roleService.getAllRoles().subscribe(all => {
                if(all.success){
                    all.data.forEach( l => {
                        const dataNode = {
                            label : this.translate.instant(l.name),
                            data : l.id,
                            styleClass : 'col-xl-12'
                        };
                        this.authorities.push(dataNode);
                        if(isArray(this.editedData.roles)){
                            if(this.editedData.roles.filter(k => k.id == l.id).length > 0){
                                this.selectedAuthorities.push(dataNode)
                            }
                        }
                    });
                }
                this.cd.detectChanges()
            })
        }else {
            this.roleService.getAllRoles().subscribe(all => {
                if(all.success){
                    all.data.forEach( l => {
                        const dataNode = {
                            label : l.name,
                            data : l.id,
                            styleClass : 'col-xl-12'
                        };
                        this.authorities.push(dataNode);
                        if(l.default == true){
                            this.selectedAuthorities.push(dataNode)
                        }
                        /*    if(isArray(this.edit.authorities)){
                                if(this.edit.authorities.filter(k => k.id == l.id).length > 0){
                                    this.selectedAuthorities.push(dataNode)
                                }
                            }*/
                    });
                }
                this.cd.detectChanges();
            })
        }
    }


    closeModal(){
        this.modal.modal('hide')
    }

    newRecord() {
      this.editedData = undefined;
      this.openModal();
    }

    saveUser(){
        if(this.userModel.firstName == '' || this.userModel.firstName == null || this.userModel.firstName === undefined){
            this._nf.warn('Eksik Alan','Ad alanı zorunludur.');
            return;
        }

        if(this.userModel.lastName == '' || this.userModel.lastName == null || this.userModel.lastName === undefined){
            this._nf.warn('Eksik Alan','Soyad alanı zorunludur.');
            return;
        }

        if(this.userModel.email == '' || this.userModel.email == null || this.userModel.email === undefined){
            this._nf.warn('Eksik Alan','Email alanı zorunludur.');
            return;
        }


        if(this.userModel.password !== this.userModel.matchingPassword){
            this._nf.warn('Eksik Alan','Şifreler uyuşmuyor.');
            return;
        }


        if(!(this.selectedAuthorities && this.selectedAuthorities.length > 0)){
            this._nf.warn('Eksik Alan','En az bir tane yetki seçmelisiniz.');
        }

        this.userModel.roles = [];
        this.selectedAuthorities.forEach((l:TreeNode) => {
            this.userModel.roles.push({
                id : l.data,
                name : l.label
            })
        });


        if(this.userModel.id > 0){
            this.userService.update(this.userModel).subscribe(res => {
                if(res.success){
                    this.editedData = null;
                    this.closeModal();
                    this.dataTable.ajax.reload();

                }else{
                    this._nf.err('Hata','Bilinmeyen bir hata oluştu.');
                }
            })
        }else{
            if(this.userModel.password == '' || this.userModel.password == null || this.userModel.password === undefined){
                this._nf.warn('Eksik Alan','Şifre alanı zorunludur');
                return;
            }

            this.userService.save(this.userModel).subscribe(res => {
                if(res.success){
                    this.editedData = null;
                    this.closeModal()
                    this.dataTable.ajax.reload();

                }else{
                    this._nf.err('Hata','Bilinmeyen bir hata oluştu.');
                }
            })
        }

    }





}
