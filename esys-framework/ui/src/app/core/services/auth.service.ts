import {ActivatedRouteSnapshot, CanActivate, Route, Router, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs";
import {PermissionsRouterData} from "../models/permissions-router-data.model";
import {isArray, isFunction} from "util";
import {TokenStorage} from "../auth/token-storage.service";
import {Injectable} from "@angular/core";
import {AuthenticationService} from "../auth/authentication.service";

@Injectable()
export class AuthPermissonServiceGuard implements CanActivate {

    constructor(private tokenStorage : TokenStorage,
                private authService : AuthenticationService,
                private router : Router){

    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
        return this.hasPermissions(route, state);
    }


    private hasPermissions(route: ActivatedRouteSnapshot | Route, state?: RouterStateSnapshot) {
        const purePermissions = !!route && route.data ? route.data['permissions'] as PermissionsRouterData : {};
        let permissions: PermissionsRouterData = this.transformPermission(purePermissions, route, state);

        // if (this.isParameterAvailable(permissions.except)) {
        //     return this.passingExceptPermissionsValidation(permissions, route, state);
        // }

        if (this.isParameterAvailable(permissions.only)) {
            return this.checkOnlyPermissions(permissions, route, state);
        }

        return true;
    }

    private transformPermission(purePermissions: PermissionsRouterData, route: any, state: any): any {
        let permissions = {
            ...purePermissions
        };

        if (isFunction(permissions.except)) {
            permissions.except = (permissions.except as Function)(route, state);
        }

        if (isFunction(permissions.only)) {
            permissions.only = (permissions.only as Function)(route, state);
        }

        permissions.except = permissions.except;
        permissions.only = permissions.only;

        return permissions;
    }

    private isParameterAvailable(permission: any) {
        return !!(permission) && permission.length > 0;
    }

    private checkOnlyPermissions(purePermissions: any, route: ActivatedRouteSnapshot | Route, state?: RouterStateSnapshot) {
        const roles = this.tokenStorage.getUserRolesSync();
        let int = [];
        if(isArray(roles)){
            int = purePermissions.only.filter(value => -1 !== roles.indexOf(value));
        }
        if(int.length == 0){
            if(purePermissions.redirectTo){
                if("authority.dashboard.view" != purePermissions.only[0])
                    this.authService.requestRole(purePermissions.only[0]);
                this.router.navigate([purePermissions.redirectTo])
            }
                return false;
        }
        return true;
    }

}
