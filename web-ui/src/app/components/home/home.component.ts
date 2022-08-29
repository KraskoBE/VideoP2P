import { Component, AfterViewInit, OnDestroy } from "@angular/core";
import { AuthenticationService } from "../../services/authentication.service";
import { CameraService } from "src/app/services/camera.service";
import { RoomDto } from "src/app/models/roomDto";
import { RoomService } from "src/app/services/room.service";

@Component( {
    selector: "home",
    templateUrl: "./home.component.html",
    styleUrls: [ "./home.component.scss" ]
} )
export class HomeComponent implements AfterViewInit, OnDestroy {
    userRooms: RoomDto[] = [];
    availableRooms: RoomDto[] = [];
    localStream: MediaStream;

    constructor( public authenticationService: AuthenticationService,
                 private cameraService: CameraService,
                 private roomService: RoomService ) {
        this.updateUserRoomList();
        this.updateAvailableRoomList();
    }

    public ngAfterViewInit(): void {
        this.cameraService.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    private updateUserRoomList(): void {
        this.roomService.getUserRooms().subscribe( ( rooms: RoomDto[] ) => this.userRooms = rooms );
    }

    private updateAvailableRoomList(): void {
        this.roomService.getAllRooms().subscribe( ( rooms: RoomDto[] ) => this.availableRooms = rooms );
    }

    public toggleCameraPreview(): void {
        if( this.localStream?.active ) {
            this.localStream.getTracks().forEach( track => track.stop() );
            this.localStream = new MediaStream();
        } else {
            this.cameraService.requestLocalUserMedia().subscribe( ( mediaStream: MediaStream ) => this.localStream = mediaStream );
        }
    }

    public isLocalStreamActive(): boolean {
        return this.localStream?.active;
    }

    public ngOnDestroy(): void {
        this.localStream?.getVideoTracks().forEach( track => track.stop() );
    }
}
