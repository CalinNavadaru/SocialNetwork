package cs.ubbcluj.lab7_8_9map.events;

import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;

public class UtilizatorTaskEvent implements Event {

    private UtilizatorEvent event;
    private DTOUtilizator data, oldData;
    public UtilizatorTaskEvent(UtilizatorEvent event, DTOUtilizator data) {
        this.event = event;
        this.data = data;
    }

    public UtilizatorTaskEvent(UtilizatorEvent event, DTOUtilizator data, DTOUtilizator oldData) {
        this.event = event;
        this.data = data;
        this.oldData = oldData;
    }

    public UtilizatorEvent getEvent() {
        return event;
    }

    public DTOUtilizator getData() {
        return data;
    }

    public DTOUtilizator getOldData() {
        return oldData;
    }
}
