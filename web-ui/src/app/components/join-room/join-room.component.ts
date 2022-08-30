import { Component } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { NotificationService } from "src/app/services/notification.service";

@Component( {
    selector: "join-room",
    templateUrl: "./join-room.component.html",
    styleUrls: [ "./join-room.component.scss" ]
} )
export class JoinRoomComponent {

    roomJoinForm: FormGroup;
    roomId: string;

    constructor( private formBuilder: FormBuilder, private notificationService: NotificationService ) {
        this.roomJoinForm = this.formBuilder.group( { roomId: [ "", [ Validators.required, Validators.minLength( 36 ) ] ] } );
    }

    public joinRoom(): void {
        if( this.roomJoinForm.invalid ) {
            return;
        }
        this.roomId = this.roomJoinForm.get( "roomId" )?.value;
    }

    public leaveRoom(): void {
        this.roomId = "";
    }
}
