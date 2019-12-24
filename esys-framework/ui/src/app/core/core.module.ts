import {NgModule} from '@angular/core';
import {KeysPipe} from "./pipes/keys.pipe";
import {NgxPermissionsDirective} from "./directive/permissions.directive";
import {WindowRef} from "./services/windowref.service";

@NgModule({
    imports: [],
    declarations: [KeysPipe,NgxPermissionsDirective],
    exports: [KeysPipe,NgxPermissionsDirective],
    providers: [WindowRef]
})
export class CoreModule {}
