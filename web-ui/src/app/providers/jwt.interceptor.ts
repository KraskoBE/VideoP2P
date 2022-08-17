import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { AuthenticationService } from "../services/authentication.service";
import { Observable } from "rxjs";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor( private authenticationService: AuthenticationService ) {
    }

    intercept( request: HttpRequest<any>, next: HttpHandler ): Observable<HttpEvent<any>> {
        if( this.authenticationService.currentUser && this.authenticationService.currentUser.token ) {
            request = request.clone( { setHeaders: { Authorization: `Bearer ${ this.authenticationService.currentUser.token }` } } );
        }

        return next.handle( request );
    }
}
