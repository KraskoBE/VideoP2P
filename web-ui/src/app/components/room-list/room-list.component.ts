import { Component, Input, ViewChild, AfterViewInit, OnChanges, SimpleChanges } from "@angular/core";
import { RoomDto } from "src/app/models/roomDto";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";

@Component( {
    selector: "room-list",
    templateUrl: "./room-list.component.html",
    styleUrls: [ "./room-list.component.scss" ]
} )
export class RoomListComponent implements OnChanges, AfterViewInit {

    @ViewChild( MatPaginator ) paginator: MatPaginator;

    @Input() rooms: RoomDto[] = [];
    @Input() pageSize: number = 5;
    @Input() pageSizeOptions: number[] = [ 5, 10, 15 ];

    dataSource: MatTableDataSource<RoomDto> = new MatTableDataSource<RoomDto>();
    displayedColumns: string[] = [ "name", "id", "createdBy", "createdOn", "actions" ];

    public ngOnChanges( changes: SimpleChanges ): void {
        if( changes[ "rooms" ] && changes[ "rooms" ].currentValue ) {
            this.dataSource.data = changes[ "rooms" ].currentValue;
        }
    }

    public ngAfterViewInit(): void {
        this.dataSource.paginator = this.paginator;
    }

}
