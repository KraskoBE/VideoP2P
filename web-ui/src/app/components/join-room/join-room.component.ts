import { Component } from "@angular/core";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";

@Component( {
    selector: "join-room",
    templateUrl: "./join-room.component.html",
    styleUrls: [ "./join-room.component.scss" ]
} )
export class JoinRoomComponent {

    roomJoinForm: FormGroup;
    roomId: string;

    constructor( private formBuilder: FormBuilder, private route: ActivatedRoute, private router: Router ) {
        this.roomJoinForm = this.formBuilder.group( { roomId: [ "", [ Validators.required, Validators.minLength( 36 ) ] ] } );
        this.route.queryParams.subscribe( params => {
            if( params[ "roomId" ] ) {
                this.roomJoinForm.get( "roomId" )?.setValue( params[ "roomId" ] );
                this.joinRoom();
            }
        } );
    }

    public joinRoom(): void {
        if( this.roomJoinForm.invalid ) {
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
}
