import {AfterViewInit, Component, ElementRef, Input, OnDestroy, ViewChild} from "@angular/core";
import {from, Observable, Subject, takeUntil} from "rxjs";
import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import * as SimplePeer from "simple-peer";
import {Instance} from "simple-peer";
import {AuthenticationService} from "../../services/authentication.service";
import {FaceRecognitionService} from "../../services/face-recognition.service";

@Component( {
    selector: "camera-view",
    templateUrl: "./camera-view.component.html",
    styleUrls: [ "./camera-view.component.scss" ]
} )
export class CameraViewComponent implements AfterViewInit, OnDestroy {

    @ViewChild( "video" ) videoRef!: ElementRef;
    @ViewChild( "canvas" ) canvasRef!: ElementRef;

    @Input() roomId: string;

    public localStream: MediaStream;
    public videos: { id: string, stream: MediaStream }[] = [];
    private ngUnsubscribe: Subject<void> = new Subject<void>();
    private socketConnection: WebSocketSubject<any>;
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

    constructor( private authenticationService: AuthenticationService, private faceRecognitionService: FaceRecognitionService ) {
    }

    get video(): HTMLVideoElement {
        return this.videoRef.nativeElement;
    }

    get canvas(): HTMLCanvasElement {
        return this.canvasRef.nativeElement;
    }

    public ngAfterViewInit(): void {
        this.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    public joinVideo(): void {
        let loc = window.location, new_uri;
        if ( loc.protocol === "https:" ) {
            new_uri = "wss:";
        } else {
            new_uri = "ws:";
        }
        new_uri += loc.host;
        new_uri += loc.pathname;

        this.socketConnection = webSocket( {
            url: `${new_uri}socket?authToken=${this.authenticationService.currentUser?.token}&roomId=${this.roomId}`,
            openObserver: {
                next: ( event ) => {
                    console.log( event );
                }
            },
            closeObserver: {
                next( closeEvent ) {
                    console.log( closeEvent );
                }
            }
        } );

        this.socketConnection.subscribe( {
            next: msg => this.handleIncomingMessage( msg ),
            error: err => console.log( err )
        } );
    }

    public ngOnDestroy(): void {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
        this.localStream.getVideoTracks().forEach( track => track.stop() );
        this.socketConnection?.complete();
    }

    verifyImage(): void {
        this.canvas.getContext( "2d" )?.drawImage( this.video, 0, 0, 1920, 1080 );

        let base64Image = this.canvas.toDataURL( "image/jpeg" );
        this.faceRecognitionService.verify( base64Image ).subscribe( result => console.log( result ) );
    }

    private handleIncomingMessage( msg: any ): void {
        switch ( msg.key ) {
            case "initReceive":
                this.addPeer( msg.value, false );
                this.socketConnection.next( {
                    key: "initSend",
                    value: msg.value
                } );
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
            this.socketConnection.next( {
                key: "signal",
                value: {
                    signal: data,
                    socketId: socketId
                }
            } );
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
