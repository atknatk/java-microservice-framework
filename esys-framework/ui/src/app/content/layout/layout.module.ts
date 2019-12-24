import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './header/header.component';

import { CoreModule } from '../../core/core.module';

import { FormsModule } from '@angular/forms';
import {AsideLeftComponent} from "./aside/left/aside-left.component";
import {FooterComponent} from "./footer/footer.component";



@NgModule({
	declarations: [
		HeaderComponent,
        AsideLeftComponent,
        FooterComponent
	],
	exports: [
		HeaderComponent,
        AsideLeftComponent,
        FooterComponent

	],
	imports: [
		CommonModule,
		RouterModule,
		CoreModule,
		FormsModule
	]
})
export class LayoutModule {}
