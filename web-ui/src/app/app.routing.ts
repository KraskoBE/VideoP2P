import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./providers/auth.guard";
import { LoginComponent } from "./components/login/login.component";
import { HomeComponent } from "./components/home/home.component";
import { RegisterComponent } from "./components/register/register.component";
import { JoinRoomComponent } from "src/app/components/join-room/join-room.component";
import { MyRoomsComponent } from "src/app/components/my-rooms/my-rooms.component";
import { UserProfileComponent } from "src/app/components/user-profile/user-profile.component";

const routes: Routes = [
    {
        path: "",
        component: HomeComponent,
        canActivate: [ AuthGuard ],
        data: { title: "Home" }
    },
    {
        path: "login",
        component: LoginComponent
    },
    {
        path: "room",
        component: JoinRoomComponent,
        canActivate: [ AuthGuard ],
        data: { title: "Join Room" }
    },
    {
        path: "my-rooms",
        component: MyRoomsComponent,
        canActivate: [ AuthGuard ],
        data: { title: "My Rooms" }
    },
    {
        path: "profile",
        component: UserProfileComponent,
        canActivate: [ AuthGuard ],
        data: { title: "My Profile" }
    },
    {
        path: "register",
        component: RegisterComponent
    },

    {
        path: "**",
        redirectTo: ""
    }
];

export const appRoutingModule = RouterModule.forRoot( routes, { useHash: true } );
