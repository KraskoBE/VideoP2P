import { Component, AfterViewInit, OnDestroy } from "@angular/core";
import { AuthenticationService } from "../../services/authentication.service";
import { CameraService } from "src/app/services/camera.service";
import { RoomDto } from "src/app/models/roomDto";
import { RoomService } from "src/app/services/room.service";

export interface Room {
    roomId: number;
    duration: number;
    lastJoined: Date;
}

@Component( {
    selector: "home",
    templateUrl: "./home.component.html",
    styleUrls: [ "./home.component.scss" ]
} )
export class HomeComponent implements AfterViewInit, OnDestroy {
    public displayedColumns: string[] = [ "roomId", "duration", "lastJoined", "actions" ];
    userRooms: RoomDto[] = [];
    public availableRooms: Room[] = [];
    public localStream: MediaStream;

    constructor( public authenticationService: AuthenticationService,
                 private cameraService: CameraService,
                 private roomService: RoomService ) {
        [ ...Array( 5 ) ].map( () =>
            this.availableRooms.push( this.generateRoom() ) );
        this.availableRooms.sort( ( a: Room, b: Room ) => b.lastJoined.getTime() - a.lastJoined.getTime() );

        this.updateRoomList();
    }

    public ngAfterViewInit(): void {
        this.cameraService.requestLocalUserMedia().subscribe( {
            next: ( mediaStream: MediaStream ) => this.localStream = mediaStream,
            error: ( error ) => console.log( error )
        } );
    }

    private updateRoomList(): void {
        this.roomService.getUserRooms().subscribe( ( rooms: RoomDto[] ) => this.userRooms = rooms );
    }

    private generateRoom(): Room {
        return {
            roomId: Math.floor( Math.random() * 89999999 + 10000000 ),
            duration: Math.floor( Math.random() * 5 + 5 ),
            lastJoined: new Date( new Date( 2022, 0, 1 ).getTime() + Math.random() * ( new Date().getTime() - new Date(
                2022,
                0,
                1 ).getTime() ) )
        };
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
