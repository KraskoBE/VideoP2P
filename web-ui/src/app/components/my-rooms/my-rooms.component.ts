import { Component } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { RoomDto } from "src/app/models/roomDto";
import { RoomService } from "src/app/services/room.service";
import { NotificationService } from "src/app/services/notification.service";

@Component( {
    selector: "my-rooms",
    templateUrl: "./my-rooms.component.html",
    styleUrls: [ "./my-rooms.component.scss" ]
} )
export class MyRoomsComponent {

    roomCreationForm: FormGroup;
    userRooms: RoomDto[] = [];

    constructor( private formBuilder: FormBuilder, private roomService: RoomService, private notificationService: NotificationService ) {
        this.roomCreationForm = this.formBuilder.group( {
            roomName: [ "", [ Validators.required, Validators.minLength( 6 ) ] ],
            publicRoom: [ false ]
        } );
        this.updateRoomList();
    }

    public createRoom(): void {
        if ( this.roomCreationForm.invalid ) {
            return;
        }

        this.roomService.createRoom( this.roomCreationForm.get( "roomName" )?.value, this.roomCreationForm.get( "publicRoom" )?.value )
            .subscribe( ( room: RoomDto ) => {
                this.notificationService.show( `Room ${ room.name } created`, "CLOSE" );
                this.updateRoomList();
            } );
    }

    private updateRoomList(): void {
        this.roomService.getUserRooms().subscribe( ( rooms: RoomDto[] ) => this.userRooms = rooms );
    }

}
