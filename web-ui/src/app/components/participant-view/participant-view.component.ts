import { Component, Input } from "@angular/core";

@Component( {
    selector: "participant-view",
    templateUrl: "./participant-view.component.html",
    styleUrls: [ "./participant-view.component.scss" ]
} )
export class ParticipantViewComponent {
    @Input()
    stream: MediaStream;

    @Input()
    muted: boolean = true;

    @Input()
    showName: boolean = true;

    @Input()
    participantName: string;

}
