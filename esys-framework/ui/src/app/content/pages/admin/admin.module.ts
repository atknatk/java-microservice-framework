import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from "@angular/router";
import {LanguagesComponent} from "./languages/languages.component";
import {LogsComponent} from "./logs/logs.component";
import {OrganizationsComponent} from "./organizations/organizations.component";
import {RolesComponent} from "./roles/roles.component";
import {SettingsComponent} from "./settings/settings.component";
import {UsergroupsComponent} from "./usergroups/usergroups.component";
import {UsersComponent} from "./users/users.component";
import {TreeModule} from "primeng/tree";
import {AuthPermissonServiceGuard} from "../../../core/services/auth.service";
import {CoreModule} from "../../../core/core.module";


const routes: Routes = [
    {
        path: '',
        //component: ECommerceComponent,
        children: [
            {
                path: '',
                redirectTo: 'organizations',
                pathMatch: 'full'
            },
            {
                path: 'organizations',
                component: OrganizationsComponent,
                canActivate: [AuthPermissonServiceGuard],
                data: {
                    permissions: {
                        only: ["authority.organization.edit","authority.organization.delete","authority.organization.new"],
                        redirectTo: '/'
                    }
                },
            },
            {
                path: 'roles',
                component: RolesComponent,
                canActivate: [AuthPermissonServiceGuard],
                data: {
                    permissions: {
                        only: ["authority.role.edit","authority.role.delete","authority.role.new"],
                        redirectTo: '/'
                    }
                },
            },
            {
                path: 'users',
                component: UsersComponent,
                canActivate: [AuthPermissonServiceGuard],
                data: {
                    permissions: {
                        only: ["authority.user.edit","authority.user.delete","authority.user.new"],
                        redirectTo: '/'
                    }
                },
            },
            {
                path: 'usergroups',
                component: UsergroupsComponent,
                canActivate: [AuthPermissonServiceGuard],
                data: {
                    permissions: {
                        only: ["authority.usergroup.edit","authority.usergroup.delete","authority.usergroup.new"],
                        redirectTo: '/'
                    }
                },
            },
            {
                path: 'logs',
                component: LogsComponent,
                canActivate: [AuthPermissonServiceGuard],
                data: {
                    permissions: {
                        only: ["authority.log.view","authority.log.ownview"],
                        redirectTo: '/'
                    }
                }
            },
            {
                path: 'settings',
                component: SettingsComponent
            },
            {
                path: 'languages',
                component: LanguagesComponent
            }
        ]
    }
];


@NgModule({
    declarations: [
        LanguagesComponent,
        LogsComponent,
        OrganizationsComponent,
        RolesComponent,
        SettingsComponent,
        UsergroupsComponent,
        UsersComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        CoreModule,
        RouterModule.forChild(routes),
        TreeModule
    ],
    providers: [    ]
})
export class AdminModule {
}
