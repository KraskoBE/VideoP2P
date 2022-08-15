import {Component} from "@angular/core";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {AuthenticationService} from "./services/authentication.service";

@Component( {
    selector: "root",
    templateUrl: "./app.component.html",
    styleUrls: [ "./app.component.scss" ]
} )
export class AppComponent {
    public isSideNavOpened: boolean = true;
    public windowTitle: string = "";

    constructor( public authenticationService: AuthenticationService, private route: ActivatedRoute, private router: Router ) {

        router.events.subscribe( event => {
            if ( event instanceof NavigationEnd ) {
                this.windowTitle = route.root.firstChild?.snapshot.data["title"];
            }
        } );
    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate( [ "/login" ] );
    }
}
