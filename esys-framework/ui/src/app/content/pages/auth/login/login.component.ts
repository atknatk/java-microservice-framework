import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    Input,
    OnDestroy,
    OnInit,
    Output,
    ViewChild
} from '@angular/core';
import {AuthenticationService} from '../../../../core/auth/authentication.service';
import {Router} from '@angular/router';
import {Subject} from 'rxjs';
import {AuthNoticeService} from '../../../../core/auth/auth-notice.service';
import {NgForm} from '@angular/forms';
import {Credential} from "../../../../core/auth/credential";
import {TranslateService} from "@ngx-translate/core";

declare const $:any;

@Component({
	selector: 'esys-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent implements OnInit, OnDestroy {
	public model: Credential  = { email: 'test@test.com', password: 'test' };
	@Output() actionChange = new Subject<string>();

	@Input() action: string;

	@ViewChild('f') f: NgForm;

    errors: any = [];
    clients: any = [];


	constructor(
		private authService: AuthenticationService,
		private router: Router,
		public authNoticeService: AuthNoticeService,
		private cdr: ChangeDetectorRef,
        private translate: TranslateService
	) {}

	get currentLang(){
		return this.translate.currentLang;
	}

	submit() {

			this.authService.login(this.model).subscribe(response => {

				if (typeof response !== 'undefined') {
					this.router.navigate([this.authService.getInterruptedUrl()]);
				} else {
					console.log(response)
                    $.toast({
                        heading: 'Dış Ticaret Portalına Hoşgeldiniz.',
                        text: 'Bizimle irtibata geçiniz.',
                        position: 'top-right',
                        loaderBg: '#fff',
                        icon: 'warning',
                        hideAfter: 3500,
                        stack: 6
                    })
				//	this.authNoticeService.setNotice(this.translate.instant('AUTH.VALIDATION.INVALID_LOGIN'), 'error');
				}
				//this.spinner.active = false;
				//this.cdr.detectChanges();
			},error1 => {
					console.log(this.errors)
			});
		}


    validate(f: NgForm) {

        return true;
    }


    ngOnInit(): void {
		this.getOAuth2Clients();
	}

	ngOnDestroy(): void {
		this.authNoticeService.setNotice(null);
	}


    register(event: Event) {
        this.action = 'register';
        this.actionChange.next(this.action);
    }



    private getOAuth2Clients(){
		this.authService.getOAuth2Clients().subscribe(res => {
			if(res.success){
				for(let client in  res.data){
					this.clients.push({
						label : client,
						url : res.data[client]
					})
				}
			}
		});
	}

    changeLang(lang){
		this.translate.use(lang).subscribe(res => {
            this.cdr.detectChanges();
		});

	}

}
