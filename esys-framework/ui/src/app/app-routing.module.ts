import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthPermissonServiceGuard} from "./core/services/auth.service";
import {SocketGuard} from "./core/services/socket-guard.service";

const routes: Routes = [
	{
		path: '',
        loadChildren: 'app/content/pages/pages.module#PagesModule'
    },
	{
		path: '**',
		redirectTo: '404',
		pathMatch: 'full'
	}
];

@NgModule({
	imports: [
		RouterModule.forRoot(routes,{
            useHash : true
		})
	],
	exports: [RouterModule],
	providers : [AuthPermissonServiceGuard,SocketGuard],
	declarations :[]
})
export class AppRoutingModule {}
