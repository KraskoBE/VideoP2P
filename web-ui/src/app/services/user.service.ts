import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../models/user";

@Injectable( { providedIn: "root" } )
export class UserService {

    constructor( private http: HttpClient ) {
    }

    public getCurrentUser(): Observable<User> {
        return this.http.get<User>( "/api/user/current" );
    }

    public updatePicture( pictureString: string ): Observable<User> {
        return this.http.put<User>( "/api/user/picture", pictureString );
    }

    public getUsernameFromRoom( sessionId: string, roomId: string ): Observable<User> {
        return this.http.get<User>( `/api/user/user-from-room/${ sessionId }/${ roomId }` );
    }
}
