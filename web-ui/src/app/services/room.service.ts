import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { RoomDto } from "src/app/models/roomDto";

@Injectable( { providedIn: "root" } )
export class RoomService {

    public static configuration: RTCConfiguration = {
        "iceServers": [
            {
                urls: "stun:openrelay.metered.ca:80"
            },
            {
                urls: "turn:openrelay.metered.ca:80",
                username: "openrelayproject",
                credential: "openrelayproject"
            },
            {
                urls: "turn:openrelay.metered.ca:443",
                username: "openrelayproject",
                credential: "openrelayproject"
            },
            {
                urls: "turn:openrelay.metered.ca:443?transport=tcp",
                username: "openrelayproject",
                credential: "openrelayproject"
            }
        ]
    };

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

    public getRoomById( roomId: string ): Observable<RoomDto> {
        return this.http.get<RoomDto>( `/api/room/${ encodeURIComponent( roomId ) }` );
    }
}
