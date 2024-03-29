import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";

import { AppComponent } from "./app.component";
import { LoginComponent } from "./components/login/login.component";
import { appRoutingModule } from "./app.routing";
import { ReactiveFormsModule } from "@angular/forms";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { HomeComponent } from "./components/home/home.component";
import { RegisterComponent } from "./components/register/register.component";
import { MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule } from "@angular/material/snack-bar";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatCardModule } from "@angular/material/card";
import { MatDividerModule } from "@angular/material/divider";
import { JwtInterceptor } from "./providers/jwt.interceptor";
import { ErrorInterceptor } from "./providers/error.interceptor";
import { MatSidenavModule } from "@angular/material/sidenav";
import { MatListModule } from "@angular/material/list";
import { MatIconModule } from "@angular/material/icon";
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatTooltipModule } from "@angular/material/tooltip";
import { MatTableModule } from "@angular/material/table";
import { JoinRoomComponent } from "./components/join-room/join-room.component";
import { MyRoomsComponent } from "./components/my-rooms/my-rooms.component";
import { UserProfileComponent } from "./components/user-profile/user-profile.component";
import { RoomListComponent } from "./components/room-list/room-list.component";
import { MatPaginatorModule } from "@angular/material/paginator";
import { ParticipantViewComponent } from "./components/participant-view/participant-view.component";
import { RoomParticipantsComponent } from "./components/room-participants/room-participants.component";
import { MatCheckboxModule } from "@angular/material/checkbox";

@NgModule( {
    declarations: [
        AppComponent,
        LoginComponent,
        HomeComponent,
        RegisterComponent,
        JoinRoomComponent,
        MyRoomsComponent,
        UserProfileComponent,
        RoomListComponent,
        ParticipantViewComponent,
        RoomParticipantsComponent
    ],
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        appRoutingModule,
        MatInputModule,
        MatButtonModule,
        MatSnackBarModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatDividerModule,
        MatSidenavModule,
        MatListModule,
        MatIconModule,
        MatToolbarModule,
        MatTooltipModule,
        MatTableModule,
        MatPaginatorModule,
        MatCheckboxModule
    ],
    providers: [
        {
            provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
            useValue: {
                duration: 2500,
                verticalPosition: "top"
            }
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: JwtInterceptor,
            multi: true
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ErrorInterceptor,
            multi: true
        }
    ],
    bootstrap: [ AppComponent ]
} )
export class AppModule {
}
