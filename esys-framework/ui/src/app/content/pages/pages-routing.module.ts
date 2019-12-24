import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PagesComponent} from './pages.component';
import {AuthPermissonServiceGuard} from "../../core/services/auth.service";
import {SocketGuard} from "../../core/services/socket-guard.service";

const routes: Routes = [
	{
		path: '',
		component: PagesComponent,
        canActivate: [AuthPermissonServiceGuard, SocketGuard],
        //canLoad: [NgxPermissionsGuard],
        data: {
            	permissions: {
                    only: ['authority.dashboard.view'],
                    redirectTo: '/login'
            	}
		},
		children: [
			{
				path: '',
				loadChildren: './components/dashboard/dashboard.module#DashboardModule',
			},
            {
                path: 'product',
                loadChildren: './components/product/product.module#ProductModule',
            },
            {
                path: 'admin',
                loadChildren: './admin/admin.module#AdminModule',

            }
		]
	},
	{
		path: 'login',
        canActivate: [AuthPermissonServiceGuard, SocketGuard],
        loadChildren: './auth/auth.module#AuthModule',
		data: {
			permissions: {
				except: 'ALL'
			}
		},
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class PagesRoutingModule {



}
