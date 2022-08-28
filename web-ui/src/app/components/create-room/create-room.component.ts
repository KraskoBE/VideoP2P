import { Component } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { RoomService } from "src/app/services/room.service";
import { RoomDto } from "src/app/models/roomDto";

@Component( {
    selector: "create-room",
    templateUrl: "./create-room.component.html",
    styleUrls: [ "./create-room.component.scss" ]
} )
export class CreateRoomComponent {

    roomCreationForm: FormGroup;
    public userRooms: RoomDto[] = [];

    constructor( private formBuilder: FormBuilder, private roomService: RoomService ) {
        this.roomCreationForm = this.formBuilder.group( { roomName: [ "", [ Validators.required, Validators.minLength( 6 ) ] ] } );
        this.roomService.getUserRooms().subscribe( ( rooms: RoomDto[] ) => this.userRooms = rooms );
    }

    public createRoom(): void {
        if( this.roomCreationForm.invalid ) {
            return;
        }

        this.roomService.createRoom( this.roomCreationForm.get( "roomName" )?.value ).subscribe( {
            next: result => console.log( result ),
            error: err => console.log( err )
        } );
    }
}
