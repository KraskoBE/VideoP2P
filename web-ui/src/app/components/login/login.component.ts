import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {first} from "rxjs/operators";
import {AuthenticationService} from "../../services/authentication.service";
import {User} from "../../models/user";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LoginRequest} from "../../models/loginRequest";


@Component( {
    selector: "login",
    templateUrl: "./login.component.html",
    styleUrls: [ "./login.component.scss" ]
} )
export class LoginComponent implements OnInit {
    public loginFormGroup: FormGroup;
    private returnUrl: string;

    constructor( private formBuilder: FormBuilder,
                 private route: ActivatedRoute,
                 private router: Router,
                 private snackBar: MatSnackBar,
                 private authenticationService: AuthenticationService ) {
        if ( this.authenticationService.currentUser ) {
            this.router.navigate( [ "/" ] );
        }
    }

    ngOnInit(): void {
        this.loginFormGroup = this.formBuilder.group( { username: [ "", Validators.required ], password: [ "", Validators.required ] } );
        this.returnUrl = this.route.snapshot.queryParams["returnUrl"] || "/";
    }

    login(): void {
        this.loginFormGroup.markAsDirty();

        if ( this.loginFormGroup.invalid ) {
            return;
        }

        const loginRequest: LoginRequest = {
            username: this.loginFormGroup.get( "username" )?.value,
            password: this.loginFormGroup.get( "password" )?.value
        };

        this.authenticationService.login( loginRequest )
            .pipe( first() )
            .subscribe( {
                next: ( user: User ) => {
                    this.snackBar.open( `${user.firstName} Successfully logged in!` );
                    this.router.navigate( [ this.returnUrl ] );
                },
                error: () => this.snackBar.open( "Wrong username or password!" )
            } );

    }
}
