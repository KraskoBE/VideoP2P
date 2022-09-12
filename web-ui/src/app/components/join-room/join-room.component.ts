import { AfterViewInit, Component, OnDestroy } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { CameraService } from "../../services/camera.service";

@Component( {
    selector: "join-room",
    templateUrl: "./join-room.component.html",
    styleUrls: [ "./join-room.component.scss" ]
} )
export class JoinRoomComponent implements AfterViewInit, OnDestroy {

    public localStream: MediaStream;

    roomJoinForm: FormGroup;
    roomId: string;

    constructor( private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router, private cameraService: CameraService ) {
        this.roomJoinForm = this.formBuilder.group( { roomId: [ "", [ Validators.required, Validators.minLength( 36 ) ] ] } );
        this.route.queryParams.subscribe( params => {
            if ( params[ "roomId" ] ) {
                this.roomJoinForm.get( "roomId" )?.setValue( params[ "roomId" ] );
                this.joinRoom();
            }
        } );
    }

    public ngAfterViewInit(): void {
        this.cameraService.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    public joinRoom(): void {
        if ( this.roomJoinForm.invalid ) {
            return;
        }
        this.roomId = this.roomJoinForm.get( "roomId" )?.value;
        this.router.navigate(
            [],
            {
                relativeTo: this.route,
                queryParams: { roomId: this.roomId },
                queryParamsHandling: "merge"
            } );
    }

    public leaveRoom(): void {
        this.roomId = "";
    }

    public ngOnDestroy(): void {
        this.localStream.getTracks().forEach( track => track.stop() );
    }

    onConnectionChange( event: boolean ): void {
        if ( !event ) {
            this.leaveRoom();
        }
    }
}
