import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {AuthenticationService} from "./services/authentication.service";

@Component( {
    selector: "root",
    templateUrl: "./app.component.html",
    styleUrls: [ "./app.component.css" ]
} )
export class AppComponent {

    constructor( public authenticationService: AuthenticationService, private router: Router ) {
    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate( [ "/login" ] );
    }
}
