import {RouterModule, Routes} from "@angular/router";
import {AuthGuard} from "./providers/auth.guard";
import {LoginComponent} from "./components/login/login.component";
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";


const routes: Routes = [
    { path: "", component: HomeComponent, canActivate: [ AuthGuard ] },
    { path: "login", component: LoginComponent },
    { path: "register", component: RegisterComponent },

    { path: "**", redirectTo: "" }
];

export const appRoutingModule = RouterModule.forRoot( routes );
