import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { SignUpRequest } from "../models/signUpRequest";
import { SignUpResponse } from "../models/signUpResponse";
import { LoginResponse } from "../models/loginResponse";
import { LoginRequest } from "../models/loginRequest";

@Injectable( { providedIn: "root" } )
export class AuthenticationService {
    public currentUser: LoginResponse | null = null;

    constructor( private http: HttpClient ) {
        let currentUser: string = <string>localStorage.getItem( "currentUser" );
        if ( currentUser ) {
            this.currentUser = JSON.parse( currentUser );
        }
    }

    public isCurrentUserAdmin(): boolean {
        return !!this.currentUser?.roles.find( role => role === "ROLE_ADMIN" );
    }

    public login( loginRequest: LoginRequest ): Observable<LoginResponse> {
        return this.http
            .post<LoginResponse>( "/api/auth/login", loginRequest )
            .pipe( tap( ( user: LoginResponse ) => {
                localStorage.setItem( "currentUser", JSON.stringify( user ) );
                return this.currentUser = user;
            } ) );
    }

    public register( signUpRequest: SignUpRequest ): Observable<SignUpResponse> {
        return this.http
            .post<SignUpResponse>( "/api/auth/signup", signUpRequest )
            .pipe();
    }

    public logout(): void {
        localStorage.removeItem( "currentUser" );
        this.currentUser = null;
    }
}
