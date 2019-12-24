import {
	Component,
	OnInit,
	Input,
	HostBinding,
	OnDestroy
} from '@angular/core';


@Component({
	selector: 'm-auth',
	templateUrl: './auth.component.html',
	styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit, OnDestroy {
	@HostBinding('id') id = 'm_login';
	@HostBinding('class')

	@Input() action = 'login';

	constructor() {}

	ngOnInit(): void {

	}

	ngOnDestroy(): void {

	}

	register() {
		this.action = 'register';
	}
}
