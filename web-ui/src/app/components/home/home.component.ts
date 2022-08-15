import {Component} from "@angular/core";
import {AuthenticationService} from "../../services/authentication.service";

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
export class HomeComponent {

    displayedColumns: string[] = [ "roomId", "duration", "lastJoined", "actions" ];
    recentRooms: Room[] = [];
    availableRooms: Room[] = [];

    constructor( public authenticationService: AuthenticationService ) {
        [ ...Array( 20 ) ].map( () =>
            this.recentRooms.push( this.generateRoom() ) );

        [ ...Array( 10 ) ].map( () =>
            this.availableRooms.push( this.generateRoom() ) );

        this.recentRooms.sort( ( a: Room, b: Room ) => b.lastJoined.getTime() - a.lastJoined.getTime() );
        this.availableRooms.sort( ( a: Room, b: Room ) => b.lastJoined.getTime() - a.lastJoined.getTime() );

    }

    private generateRoom(): Room {
        return {
            roomId: Math.floor( Math.random() * 89999999 + 10000000 ),
            duration: Math.floor( Math.random() * 5 + 5 ),
            lastJoined: new Date( new Date( 2022, 0, 1 ).getTime() + Math.random() * (new Date().getTime() - new Date( 2022, 0, 1 ).getTime()) )
        };
    }

}
