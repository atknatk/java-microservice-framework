import {
    Component, Input,
    OnInit, Output, ViewChild,
} from '@angular/core';
import {AuthenticationService} from "../../../../core/auth/authentication.service";
import {Router} from "@angular/router";
import {ReCaptcha2Component} from "ngx-captcha";
import {Subject} from "rxjs";

@Component({
	selector: 'esys-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    @Output() actionChange = new Subject<string>();
    @Input() action: string;

    public model: any = { email: '', firstName: '', id: 0, lastName: '', matchingPassword: '', password: '' };
    capthcaKey = '';
    siteKey = "6LetznUUAAAAABiHi8vU5WLTcNpMBOEoY-9G8Kk9";
    @ViewChild('captchaElem') captchaElem: ReCaptcha2Component;

    constructor(
        private authService: AuthenticationService,
        private router: Router,
    ) {}


    loginPage(event: Event) {
        event.preventDefault();
        this.action = 'login';
        this.actionChange.next(this.action);
    }

    submit() {

            this.authService.register({
                credential:this.model,
                capthcaKey: this.capthcaKey
            }).subscribe(response => {
                this.action = 'login';
            },error1 => {
                if(error1.sucess == false){

                }
            });
    }


    ngOnInit(): void {
    }

    resetCaptcha(){
        this.captchaElem.resetCaptcha()
    }
}
