import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable( { providedIn: "root" } )
export class FaceRecognitionService {

    constructor( private http: HttpClient ) {
    }

    public verify( imageString: string ): Observable<string> {
        return this.http.post<string>( "/api/face/verify", imageString );
    }

    public analyze( imageString: string ): Observable<string> {
        return this.http.post<string>( "/api/face/analyze", imageString );
    }


}
