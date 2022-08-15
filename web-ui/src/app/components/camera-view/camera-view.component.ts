import {AfterViewInit, Component, OnDestroy} from "@angular/core";
import {from, Observable, Subject, takeUntil} from "rxjs";
import {webSocket} from "rxjs/webSocket";
import * as SimplePeer from "simple-peer";
import {Instance} from "simple-peer";
import {AuthenticationService} from "../../services/authentication.service";

@Component( {
    selector: "camera-view",
    templateUrl: "./camera-view.component.html",
    styleUrls: [ "./camera-view.component.scss" ]
} )
export class CameraViewComponent implements AfterViewInit, OnDestroy {
    public localStream: MediaStream;
    public videos: { id: string, stream: MediaStream }[] = [];
    private ngUnsubscribe: Subject<void> = new Subject<void>();
    private socketConnection: Subject<any> = webSocket( "ws://localhost:8080/socket" );
    private peers: Map<string, Instance> = new Map<string, Instance>();
    private configuration: RTCConfiguration = {
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


    constructor( private authenticationService: AuthenticationService ) {
    }

    public ngAfterViewInit(): void {
        this.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    public joinVideo(): void {
        document.cookie = `auth_token=${this.authenticationService.currentUser?.token}`;
        this.socketConnection.subscribe( {
            next: msg => this.handleIncomingMessage( msg ),
            error: err => console.log( err )
        } );
    }

    public ngOnDestroy(): void {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
        this.localStream.getVideoTracks().forEach( track => track.stop() );
    }

    private handleIncomingMessage( msg: any ): void {
        switch ( msg.key ) {
            case "initReceive":
                this.addPeer( msg.value, false );
                this.socketConnection.next( { key: "initSend", value: msg.value } );
                break;
            case "initSend":
                this.addPeer( msg.value, true );
                break;
            case "stopReceive":
                this.removePeer( msg.value );
                break;
            case "signal":
                this.peers.get( msg.value.socketId )?.signal( msg.value.signal );
                break;
        }
    }

    private removePeer( socketId: string ): void {
        this.videos = this.videos.filter( peer => peer.id !== socketId );
        this.peers.delete( socketId );
    }

    private addPeer( socketId: string, initiator: boolean ): void {
        this.peers.set( socketId, new SimplePeer( {
            initiator: initiator,
            stream: this.localStream,
            config: this.configuration
        } ) );

        this.peers.get( socketId )?.on( "signal", data => {
            this.socketConnection.next( { key: "signal", value: { signal: data, socketId: socketId } } );
        } );

        this.peers.get( socketId )?.on( "stream", stream => {
            this.videos.push( { id: socketId, stream: stream } );
        } );
    }

    private requestLocalUserMedia(): Observable<MediaStream> {
        return from( navigator.mediaDevices.getUserMedia( {
            audio: true,
            video: { width: 1920, height: 1080 }
        } ) ).pipe( takeUntil( this.ngUnsubscribe ) );
    }
}
