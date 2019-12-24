import {
    ChangeDetectorRef,
    Directive,
    EventEmitter,
    Input,
    OnInit,
    Output,
    TemplateRef,
    ViewContainerRef
} from "@angular/core";
import {TokenStorage} from "../auth/token-storage.service";
import {isBoolean} from "util";

@Directive({
    selector: '[permissionsOnly],[permissionsExcept]'
})
export class NgxPermissionsDirective implements OnInit{

    @Input() permissionsOnly: string[];
    @Input() permissionsOnlyThen: TemplateRef<any>;
    @Input() permissionsOnlyElse: TemplateRef<any>;


    @Input() permissionsThen: TemplateRef<any>;
    @Input() permissionsElse: TemplateRef<any>;


    @Output() permissionsAuthorized = new EventEmitter();
    @Output() permissionsUnauthorized = new EventEmitter();

    private currentAuthorizedState: boolean;

    constructor(
        private tokenService: TokenStorage,
        private viewContainer: ViewContainerRef,
        private changeDetector: ChangeDetectorRef,
        private templateRef: TemplateRef<any>
    ) {
    }

    ngOnInit(): void {
        this.viewContainer.clear();
        this.validateExceptOnlyPermissions();
    }


    private validateExceptOnlyPermissions(){
        const roles = this.tokenService.getUserRolesSync()

             //    if (this.permissionsOnly && ) {
             // //       return;
             //    }


        this.handleAuthorisedPermission(this.checkOnlyPermissions([...this.permissionsOnly],roles));

    }



    private handleAuthorisedPermission(isShow:boolean): void {
        if (isBoolean(this.currentAuthorizedState) && this.currentAuthorizedState) return;

        this.currentAuthorizedState = true;
        this.permissionsAuthorized.emit();

          this.applyStrategy(isShow);

    }


    private showTemplateBlockInView(template: TemplateRef<any>): void {
        this.viewContainer.clear();
        if (!template) {
            return;
        }

        this.viewContainer.createEmbeddedView(template);
        this.changeDetector.markForCheck();
    }

    private getAuthorisedTemplates(): TemplateRef<any> {
        return this.permissionsOnlyThen
            || this.permissionsThen
            || this.templateRef;
    }





    private applyStrategy(isShow: boolean) {
        if (isShow) {
            this.showTemplateBlockInView(this.templateRef);
            return;
        }
            this.viewContainer.clear();
    }

    private checkOnlyPermissions(purePermissions: any, roles) {
        return purePermissions.filter(value => -1 !== roles.indexOf(value)).length > 0;
    }

}
