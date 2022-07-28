import {Component} from "@angular/core";
import {AuthenticationService} from "../../services/authentication.service";

@Component( {
    selector: "home",
    templateUrl: "./home.component.html",
    styleUrls: [ "./home.component.css" ]
} )
export class HomeComponent {

    constructor( public authenticationService: AuthenticationService ) {
    }

    testAuthenticatedRequest(): void {
        this.authenticationService.testRequest().subscribe( ( response: any ) => console.log( response ) );
    }
}
