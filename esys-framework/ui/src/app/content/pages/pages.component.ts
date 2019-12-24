import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';

declare const $:any;

@Component({
	selector: 'pages',
	templateUrl: './pages.component.html',
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class PagesComponent  implements OnInit{

    selfLayout : any = '';

	constructor() {
	}

    ngOnInit(): void {

    }


}
