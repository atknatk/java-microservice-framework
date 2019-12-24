import {AfterViewInit, ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';


@Component({
    selector: 'esys-footer',
    templateUrl: './footer.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class FooterComponent implements OnInit, AfterViewInit {


    constructor(
        private router: Router,
    ) {

    }

    ngOnInit(): void {
    }

    ngAfterViewInit(): void {

    }
}
