import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "../models/user";
import {Observable, tap} from "rxjs";
import {SignUpRequest} from "../models/signUpRequest";
import {SignUpResponse} from "../models/signUpResponse";

@Injectable( { providedIn: "root" } )
export class AuthenticationService {
    public currentUser: User | null = null;

    constructor( private http: HttpClient ) {
        let currentUser: string = <string>localStorage.getItem( "currentUser" );
        if ( currentUser ) {
            this.currentUser = JSON.parse( currentUser );
        }
    }

    public login( username: string, password: string ): Observable<User> {
        return this.http
            .post<User>( `http://localhost:8080/api/auth/login`, { username, password } )
            .pipe( tap( ( user: User ) => {
                localStorage.setItem( "currentUser", JSON.stringify( user ) );
                return this.currentUser = user;
            } ) );
    }

    public register( signUpRequest: SignUpRequest ): Observable<SignUpResponse> {
        return this.http
            .post<User>( `http://localhost:8080/api/auth/signup`, signUpRequest )
            .pipe();
    }

    public testRequest(): Observable<string> {
        return this.http
            .get( `http://localhost:8080/api/auth/test`, { responseType: "text" } )
            .pipe();
    }

    public logout(): void {
        localStorage.removeItem( "currentUser" );
        this.currentUser = null;
    }
}
