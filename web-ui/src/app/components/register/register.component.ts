import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {first} from "rxjs/operators";
import {SignUpResponse} from "../../models/signUpResponse";

@Component( {
    selector: "register",
    templateUrl: "./register.component.html",
    styleUrls: [ "./register.component.css" ]
} )
export class RegisterComponent implements OnInit {
    public registerFormGroup: FormGroup;

    constructor( private formBuilder: FormBuilder,
                 private route: ActivatedRoute,
                 private router: Router,
                 private snackBar: MatSnackBar,
                 private authenticationService: AuthenticationService ) {
        if ( this.authenticationService.currentUser ) {
            this.router.navigate( [ "/" ] );
        }
    }

    public ngOnInit() {
        this.registerFormGroup = this.formBuilder.group( {
            username: [ "", Validators.required ],
            password: [ "", [ Validators.required, Validators.min( 6 ) ] ],
            email: [ "", [ Validators.required, Validators.email ] ],
            firstName: [ "", Validators.required ],
            lastName: [ "", Validators.required ]
        } );
    }

    public register(): void {
        this.registerFormGroup.markAsDirty();

        if ( this.registerFormGroup.invalid ) {
            return;
        }

        this.authenticationService.register( this.registerFormGroup.value )
            .pipe( first() )
            .subscribe( {
                next: ( user: SignUpResponse ) => {
                    this.snackBar.open( `${user.username} Successfully registered! You can now log in!` );
                    this.router.navigate( [ "/login" ] );
                },
                error: err => this.snackBar.open( err.error )
            } );

    }

}
