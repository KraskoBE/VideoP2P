import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";
import { interval, Subject, takeUntil } from "rxjs";
import { webSocket, WebSocketSubject } from "rxjs/webSocket";
import * as SimplePeer from "simple-peer";
import { Instance } from "simple-peer";
import { AuthenticationService } from "../../services/authentication.service";
import { RoomService } from "../../services/room.service";
import { RoomDto } from "../../models/roomDto";
import { UserService } from "../../services/user.service";
import { User } from "../../models/user";
import { NotificationService } from "../../services/notification.service";
import { FaceRecognitionService } from "../../services/face-recognition.service";

@Component( {
    selector: "room-participants",
    templateUrl: "./room-participants.component.html",
    styleUrls: [ "./room-participants.component.scss" ]
} )
export class RoomParticipantsComponent implements OnInit, OnDestroy {

    @Input()
    roomId: string;

    @Input()
    localStream: MediaStream;

    @Output()
    connectionChange: EventEmitter<boolean> = new EventEmitter<boolean>();

    @ViewChild( "video" ) videoRef!: ElementRef;
    @ViewChild( "canvas" ) canvasRef!: ElementRef;

    public videos: { id: string, name: string, alert: boolean, stream: MediaStream }[] = [];
    public roomDto: RoomDto;

    private ngUnsubscribe: Subject<void> = new Subject<void>();
    private socketConnection: WebSocketSubject<any>;
    private peers: Map<string, Instance> = new Map<string, Instance>();

    constructor( private authenticationService: AuthenticationService,
                 private roomService: RoomService,
                 private userService: UserService,
                 private notificationService: NotificationService,
                 private faceRecognitionService: FaceRecognitionService ) {
    }

    get video(): HTMLVideoElement {
        return this.videoRef.nativeElement;
    }

    get canvas(): HTMLCanvasElement {
        return this.canvasRef.nativeElement;
    }

    ngOnInit(): void {
        this.roomService.getRoomById( this.roomId ).pipe( takeUntil( this.ngUnsubscribe ) ).subscribe( ( room: RoomDto ) => {
            this.roomDto = room;
            this.joinVideo();
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

        new_uri = "ws:localhost"; // comment this for prod

        this.socketConnection = webSocket( {
            url: `${ new_uri }:8080/socket?authToken=${ this.authenticationService.currentUser?.token }&roomId=${ this.roomId }`,
            openObserver: {
                next: ( event ) => {
                    this.notificationService.show( `Joined room: ${ this.roomId }` );
                    this.connectionChange.emit( true );
                    if ( this.roomDto.createdBy != this.authenticationService.currentUser?.username ) {
                        interval( 9000 ).pipe( takeUntil( this.ngUnsubscribe ) ).subscribe( () => this.sendImageForVerification() );
                    }
                }
            },
            closeObserver: {
                next: ( event ) => {
                    this.notificationService.show( `Left room: ${ this.roomId }` );
                    this.connectionChange.emit( false );
                }
            }

        } );

        this.socketConnection.subscribe( {
            next: msg => this.handleIncomingMessage( msg ),
            error: err => console.log( err )
        } );
    }

    ngOnDestroy(): void {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
        this.socketConnection?.complete();
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
            case "alert":
                this.handleAlertMessage( msg.value );
                break;
        }
    }

    private addPeer( socketId: string, initiator: boolean ): void {
        this.peers.set( socketId, new SimplePeer( {
            initiator: initiator,
            stream: this.localStream,
            config: RoomService.configuration
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
            this.userService.getUsernameFromRoom( socketId, this.roomId )
                .pipe( takeUntil( this.ngUnsubscribe ) )
                .subscribe( ( user: User ) => this.videos.push( { id: socketId, name: user.username, alert: false, stream: stream } ) );

        } );
    }

    private removePeer( socketId: string ): void {
        this.videos = this.videos.filter( peer => peer.id !== socketId );
        this.peers.delete( socketId );
    }

    private sendImageForVerification(): void {
        this.canvas.getContext( "2d" )?.drawImage( this.video, 0, 0, 1920, 1080 );
        const base64Image = this.canvas.toDataURL( "image/jpeg" );

        this.faceRecognitionService.verify( base64Image, this.roomDto.id ).subscribe();
    }

    private handleAlertMessage( message: string ) {
        const userVideo = this.videos.find( video => video.name === message );
        if ( userVideo ) {
            console.log( "found" );
            userVideo.alert = true;
            setTimeout( () => {
                userVideo.alert = false;
            }, 7000 );
        }
    }
}
