import { Injectable } from "@angular/core";
import { Observable, from } from "rxjs";

@Injectable( { providedIn: "root" } )
export class CameraService {

    constructor() {

    }

    public requestLocalUserMedia(): Observable<MediaStream> {
        return from( navigator.mediaDevices.getUserMedia( { audio: true, video: { width: 960, height: 540 } } ) );
    }

}
