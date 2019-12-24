import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import {TranslateModule} from "@ngx-translate/core";
import {FormsModule} from "@angular/forms";
import {ProductComponent} from "./product.component";
import {ProductService} from "./product.service";
import {FileUploadModule} from "ng2-file-upload";

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		RouterModule.forChild([
			{
				path: '',
				component: ProductComponent
			}
		]),
        TranslateModule,
        FileUploadModule
	],
	providers: [ProductService],
	declarations: [ProductComponent]
})
export class ProductModule {}
