import {AfterViewInit, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {TreeNode} from "primeng/api";
import {OrganizationsService} from "./organizations.service";
import * as moment from "moment";
import {TokenStorage} from "../../../../core/auth/token-storage.service";
import {NotificationService} from "../../../../core/services/notification.service";
import {AuthenticationService} from "../../../../core/auth/authentication.service";

enum OrganizationType {
    MainGroup = 0,
    Group = 1,
    Company = 2,
    BranchOffice = 3,
    Employee = 4,
    Customer= 5,
    Suplier= 6
}



declare const $:any;

@Component({
    templateUrl: './organizations.component.html',
    providers: [OrganizationsService],
    changeDetection: ChangeDetectionStrategy.Default
})
export class OrganizationsComponent implements OnInit,AfterViewInit{

    treeData: TreeNode[];
    userTableData: any[];
    selectedFile: TreeNode;
    loading: boolean;
    orgType  = OrganizationType;
    userListDatatable;
    private modal;
    constructor(private readonly service : OrganizationsService,
                private readonly tokenService : TokenStorage,
                private readonly _nf : NotificationService,
                private readonly authService : AuthenticationService,
                private cd: ChangeDetectorRef){

    }

    get selectedUsers(){
        if(this.userListDatatable){
        return this.userListDatatable.rows('.selected').data();
        }
        return [];
    }


    ngOnInit(): void {
        this.loading = true;
        this.modal = $('#userlist-modal').modal('hide');
        this.initTable();
            this.service.getAllMainGroup().toPromise().then(res => {
               this.treeData = this.map(res, this.orgType.MainGroup)
                this.loading = false;
                this.cd.detectChanges();
        })

    }


    nodeExpand(event) {
        if(event.node) {
            if(event.node.data.type == this.orgType.MainGroup){
                this.service.getGroup(event.node.data.entity.id).subscribe(res => {
                    event.node.children =  this.map(res,OrganizationType.Group)
                    this.cd.detectChanges();
                });
            }else if(event.node.data.type == this.orgType.Group){
                this.service.getCompany(event.node.data.entity.id).subscribe(res => {
                    event.node.children =  this.map(res,OrganizationType.Company)
                    this.cd.detectChanges();
                });
            }else if(event.node.data.type == this.orgType.Company){
                this.service.getBranchOffice(event.node.data.entity.id).subscribe(res => {
                    event.node.children =  this.map(res,OrganizationType.BranchOffice)
                    this.cd.detectChanges();
                });
            }else if(event.node.data.type == this.orgType.BranchOffice){
                this.service.getEmployee(event.node.data.entity.id).subscribe(res => {
                    event.node.children =  this.map(res,OrganizationType.Employee)
                    this.cd.detectChanges();
                });
            }else if(event.node.data.type == this.orgType.Employee){
                this.service.getCustomer(event.node.data.entity.id).subscribe(res => {
                    event.node.children =  this.map(res,OrganizationType.Customer)
                    this.cd.detectChanges();
                });
            }

        }
    }

    ngAfterViewInit(): void {

    }


    private map(res,type : OrganizationType){
        if(res.success){
            return res.data.map(l => {
                return {
                    label : l.name,
                    data : {
                        type: type,
                        entity : l
                    },
                    expandedIcon: "fa fa-folder-open",
                    collapsedIcon: "fa fa-folder",
                    leaf: type == OrganizationType.Customer
                }
            });
        }
        return []
    }

    private initTable(){
        this.userListDatatable = $('#userlist-table').DataTable({
            'ajax': {
                type: 'GET',
                url: '/api/uaa/user/paging/basic',
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
            "columnDefs": [
                {
                    targets: 0,
                    title: "Ad",
                    data : "firstName"
                },
                {
                    targets: 1,
                    title: "Soyad",
                    data : "lastName",
                }
            ],
            order: [
                [1, 'asc']
            ]
        });

        const that = this;
        $('#userlist-table tbody').on( 'click', 'tr', function () {
            $(this).toggleClass('selected');
            that.cd.detectChanges();
        } );

        // $('#button').click( function () {
        //     alert( table.rows('.selected').data().length +' row(s) selected' );
        // } );

    }

    saveUsers(){
        const arr =  this.userListDatatable.rows('.selected').data();
        if(arr.length > 0){

            if(this.selectedFile.data.type == OrganizationType.MainGroup){
                this.service.saveMainGroupUser(this.selectedFile.data.entity.id,arr.toArray()).subscribe(_=> {
                    this.selectedEvent({node: this.selectedFile});
                    this.closeModal();
                });
            }else if(this.selectedFile.data.type == OrganizationType.Group){
                this.service.saveGroupUser(this.selectedFile.data.entity.id,arr.toArray()).subscribe(_=> {
                    this.selectedEvent({node: this.selectedFile});
                    this.closeModal();
                });
            }else if(this.selectedFile.data.type == OrganizationType.BranchOffice){
                this.service.saveBranchOfficeUser(this.selectedFile.data.entity.id,arr.toArray()).subscribe(_=> {
                    this.selectedEvent({node: this.selectedFile});
                    this.closeModal();
                });
            }else if(this.selectedFile.data.type == OrganizationType.Company){
                this.service.saveCompanyUser(this.selectedFile.data.entity.id,arr.toArray()).subscribe(_=> {
                    this.selectedEvent({node: this.selectedFile});
                    this.closeModal();
                });
            }else if(this.selectedFile.data.type == OrganizationType.Employee){
                this.service.saveEmployeeUser(this.selectedFile.data.entity.id,arr.toArray()).subscribe(_=> {
                    this.selectedEvent({node: this.selectedFile});
                    this.closeModal();
                });
            }else if(this.selectedFile.data.type == OrganizationType.Customer){
                this.service.saveCustomerUser(this.selectedFile.data.entity.id,arr.toArray()).subscribe(_=> {
                    this.selectedEvent({node: this.selectedFile});
                    this.closeModal();
                });
            }


        }

    }

    openModal(){
        this.modal.modal('show');
    }

    closeModal(){
        this.modal.modal('hide')
    }

    selectedEvent(data){
        if(data.node.data.type == OrganizationType.MainGroup){
            this.service.getMainGroupUser(data.node.data.entity.id).subscribe(res => {
                this.userTableData = res;
                this.detectChanges();
            });
        }else if(data.node.data.type == OrganizationType.Group){
            this.service.getGroupUser(data.node.data.entity.id).subscribe(res => {
                this.userTableData = res;
                this.detectChanges();
            });
        }else if(data.node.data.type == OrganizationType.BranchOffice){
            this.service.getBranchofficeUser(data.node.data.entity.id).subscribe(res => {
                this.userTableData = res;
                this.detectChanges();
            });
        }else if(data.node.data.type == OrganizationType.Company){
            this.service.getCompanyUser(data.node.data.entity.id).subscribe(res => {
                this.userTableData = res;
                this.detectChanges();
            });
        }else if(data.node.data.type == OrganizationType.Employee){
            this.service.getEmployeeUser(data.node.data.entity.id).subscribe(res => {
                this.userTableData = res;
                this.detectChanges();
            });
        }else if(data.node.data.type == OrganizationType.Customer){
            this.service.getCustomerUser(data.node.data.entity.id).subscribe(res => {
                this.userTableData = res;
                this.detectChanges();
            });
        }

    }

    removeUser(user){
        if(this.selectedFile.data.type == OrganizationType.MainGroup){
            this.service.removeMainGroupUser(this.selectedFile.data.entity.id,user).subscribe(_=> {
                this.selectedEvent({node: this.selectedFile});
                this.closeModal();
            });
        }else if(this.selectedFile.data.type == OrganizationType.Group){
            this.service.removeGroupUser(this.selectedFile.data.entity.id,user).subscribe(_=> {
                this.selectedEvent({node: this.selectedFile});
                this.closeModal();
            });
        }else if(this.selectedFile.data.type == OrganizationType.BranchOffice){
            this.service.removeBranchOfficeUser(this.selectedFile.data.entity.id,user).subscribe(_=> {
                this.selectedEvent({node: this.selectedFile});
                this.closeModal();
            });
        }else if(this.selectedFile.data.type == OrganizationType.Company){
            this.service.removeCompanyUser(this.selectedFile.data.entity.id,user).subscribe(_=> {
                this.selectedEvent({node: this.selectedFile});
                this.closeModal();
            });
        }else if(this.selectedFile.data.type == OrganizationType.Employee){
            this.service.removeEmployeeUser(this.selectedFile.data.entity.id,user).subscribe(_=> {
                this.selectedEvent({node: this.selectedFile});
                this.closeModal();
            });
        }else if(this.selectedFile.data.type == OrganizationType.Customer){
            this.service.removeCustomerUser(this.selectedFile.data.entity.id,user).subscribe(_=> {
                this.selectedEvent({node: this.selectedFile});
                this.closeModal();
            });
        }
    }

    detectChanges(){
        setTimeout(() => {
            this.cd.detectChanges();
        },250);
    }


}
