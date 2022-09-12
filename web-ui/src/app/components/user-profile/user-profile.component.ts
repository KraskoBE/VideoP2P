import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { Subject } from "rxjs";
import { UserService } from "../../services/user.service";
import { CameraService } from "../../services/camera.service";

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


    constructor( private userService: UserService, private cameraService: CameraService ) {
    }

    get video(): HTMLVideoElement {
        return this.videoRef.nativeElement;
    }

    get canvas(): HTMLCanvasElement {
        return this.canvasRef.nativeElement;
    }

    ngOnInit(): void {
        this.userService.getCurrentUser().subscribe( user => this.currentUserPicture = user.pictureString || this.currentUserPicture );
    }

    public ngAfterViewInit(): void {
        this.cameraService.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    public ngOnDestroy(): void {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
        this.localStream.getTracks().forEach( track => track.stop() );
    }

    public captureImage(): void {
        this.canvas.getContext( "2d" )?.drawImage( this.video, 0, 0, 1920, 1080 );

        let base64Image = this.canvas.toDataURL( "image/jpeg" );
        this.userService.updatePicture( base64Image ).subscribe( user => this.currentUserPicture = user.pictureString );
    }
}
