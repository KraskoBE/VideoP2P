import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { RoomDto } from "src/app/models/roomDto";

@Injectable( { providedIn: "root" } )
export class RoomService {

    constructor( private http: HttpClient ) {
    }

    public createRoom( roomName: string ): Observable<RoomDto> {
        const params = new HttpParams().append( "roomName", roomName );

        return this.http.post<RoomDto>( "/api/room", null, { params: params } );
    }

    public getAllRooms(): Observable<RoomDto[]> {
        return this.http.get<RoomDto[]>( "/api/room" );
    }
    public getUserRooms(): Observable<RoomDto[]> {
        return this.http.get<RoomDto[]>( "/api/room/my" );
    }

}
