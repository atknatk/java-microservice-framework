import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardComponent } from './dashboard.component';
import { RouterModule } from '@angular/router';
import {DashboardService} from "./dashboard.service";
import {TranslateModule} from "@ngx-translate/core";
import {FormsModule} from "@angular/forms";

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		RouterModule.forChild([
			{
				path: '',
				component: DashboardComponent
			}
		]),
        TranslateModule
	],
	providers: [DashboardService],
	declarations: [DashboardComponent]
})
export class DashboardModule {}
