import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { User } from "../models/user";
import { Observable, tap } from "rxjs";
import { SignUpRequest } from "../models/signUpRequest";
import { SignUpResponse } from "../models/signUpResponse";
import { LoginResponse } from "../models/loginResponse";
import { LoginRequest } from "../models/loginRequest";

@Injectable( { providedIn: "root" } )
export class AuthenticationService {
    public currentUser: User | null = null;

    constructor( private http: HttpClient ) {
        let currentUser: string = <string>localStorage.getItem( "currentUser" );
        if( currentUser ) {
            this.currentUser = JSON.parse( currentUser );
        }
    }

    public login( loginRequest: LoginRequest ): Observable<LoginResponse> {
        return this.http
                   .post<LoginResponse>( `http://localhost:8080/api/auth/login`, loginRequest )
                   .pipe( tap( ( user: User ) => {
                       localStorage.setItem( "currentUser", JSON.stringify( user ) );
                       return this.currentUser = user;
                   } ) );
    }

    public register( signUpRequest: SignUpRequest ): Observable<SignUpResponse> {
        return this.http
                   .post<SignUpResponse>( `http://localhost:8080/api/auth/signup`, signUpRequest )
                   .pipe();
    }

    public logout(): void {
        localStorage.removeItem( "currentUser" );
        this.currentUser = null;
    }
}
