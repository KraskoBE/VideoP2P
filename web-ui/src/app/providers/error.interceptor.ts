import { Injectable } from "@angular/core";
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { AuthenticationService } from "../services/authentication.service";
import { catchError, Observable, throwError } from "rxjs";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor( private authenticationService: AuthenticationService, private snackBar: MatSnackBar ) {
    }

    intercept( request: HttpRequest<any>, next: HttpHandler ): Observable<HttpEvent<any>> {
        return next.handle( request ).pipe( catchError( err => {
            this.snackBar.open( `ERROR ${ err.status }: ${ err.error }` );

            return throwError( err );
        } ) );
    }
}
