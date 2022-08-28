import { Component, Input } from "@angular/core";
import { RoomDto } from "src/app/models/roomDto";

@Component( {
    selector: "room-list",
    templateUrl: "./room-list.component.html",
    styleUrls: [ "./room-list.component.scss" ]
} )
export class RoomListComponent {

    @Input() rooms: RoomDto[] = [];
    @Input() pageSize: number = 5;
    @Input() pageSizeOptions: number[] = [ 5, 10, 15 ];

    public displayedColumns: string[] = [ "id", "name", "createdBy", "actions" ];

    constructor() {
    }

}
