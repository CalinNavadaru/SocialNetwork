package cs.ubbcluj.lab7_8_9map.events;

import cs.ubbcluj.lab7_8_9map.domain.dto.DTOMessage;

public class MessageTaskEvent implements Event{

    private MessageEvent event;
    private DTOMessage data, oldData;

    public MessageTaskEvent(MessageEvent event, DTOMessage data) {
        this.event = event;
        this.data = data;
    }

    public MessageTaskEvent(MessageEvent event, DTOMessage data, DTOMessage oldData) {
        this.event = event;
        this.data = data;
        this.oldData = oldData;
    }

    public MessageEvent getEvent() {
        return event;
    }

    public DTOMessage getData() {
        return data;
    }

    public DTOMessage getOldData() {
        return oldData;
    }
}
