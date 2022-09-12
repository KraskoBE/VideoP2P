import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable( { providedIn: "root" } )
export class FaceRecognitionService {

    constructor( private http: HttpClient ) {
    }

    public verify( imageString: string, roomId: string ): Observable<string> {
        return this.http.post<string>( `/api/face/verify/${ roomId }`, imageString );
    }

    public analyze( imageString: string ): Observable<string> {
        return this.http.post<string>( "/api/face/analyze", imageString );
    }


}
