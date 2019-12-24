import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthComponent} from './auth.component';
import {LoginComponent} from './login/login.component';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {RegisterComponent} from './register/register.component';
import {ForgotPasswordComponent} from './forgot-password/forgot-password.component';
import {NgxCaptchaModule} from "ngx-captcha";
import {CoreModule} from "../../../core/core.module";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
        NgxCaptchaModule,
		CoreModule,
        RouterModule.forChild([
            {
                path: '',
                component: AuthComponent
            }
        ]),
        TranslateModule
	],
	providers: [

	],
	declarations: [
		AuthComponent,
		LoginComponent,
		RegisterComponent,
		ForgotPasswordComponent
	]
})
export class AuthModule {}
