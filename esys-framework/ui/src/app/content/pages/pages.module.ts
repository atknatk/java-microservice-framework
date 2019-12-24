import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PagesRoutingModule} from './pages-routing.module';
import {PagesComponent} from './pages.component';
import {CoreModule} from '../../core/core.module';
import {FormsModule} from '@angular/forms';
import {LayoutModule} from "../layout/layout.module";
import {NotificationService} from "../../core/services/notification.service";

@NgModule({
	declarations: [
		PagesComponent,
	],
	imports: [
		CommonModule,
		FormsModule,
		PagesRoutingModule,
		CoreModule,
        LayoutModule
	],
	providers: [NotificationService]
})
export class PagesModule {
}
