import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {from, Observable, Subject, takeUntil} from "rxjs";
import {UserService} from "../../services/user.service";

@Component( {
    selector: "user-profile",
    templateUrl: "./user-profile.component.html",
    styleUrls: [ "./user-profile.component.scss" ]
} )
export class UserProfileComponent implements OnInit, AfterViewInit, OnDestroy {

    @ViewChild( "video" ) videoRef!: ElementRef;
    @ViewChild( "canvas" ) canvasRef!: ElementRef;

    public localStream: MediaStream;
    public currentUserPicture: string = "https://dummyimage.com/960x540/f/0.png&text=No+Image";
    private ngUnsubscribe: Subject<void> = new Subject<void>();


    constructor( private userService: UserService ) {
    }

    get video(): HTMLVideoElement {
        return this.videoRef.nativeElement;
    }

    get canvas(): HTMLCanvasElement {
        return this.canvasRef.nativeElement;
    }

    ngOnInit(): void {
        this.userService.getCurrentUser().subscribe( user => this.currentUserPicture = user.pictureString );
    }

    public ngAfterViewInit(): void {
        this.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    public ngOnDestroy(): void {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
        this.localStream.getVideoTracks().forEach( track => track.stop() );
    }

    public captureImage(): void {
        this.canvas.getContext( "2d" )?.drawImage( this.video, 0, 0, 1920, 1080 );

        let base64Image = this.canvas.toDataURL( "image/jpeg" );
        this.userService.updatePicture( base64Image ).subscribe( user => this.currentUserPicture = user.pictureString );
    }

    private requestLocalUserMedia(): Observable<MediaStream> {
        return from( navigator.mediaDevices.getUserMedia( {
            audio: true,
            video: { width: 1920, height: 1080 }
        } ) ).pipe( takeUntil( this.ngUnsubscribe ) );
    }
}
