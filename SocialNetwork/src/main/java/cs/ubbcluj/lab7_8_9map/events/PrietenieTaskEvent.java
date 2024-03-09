package cs.ubbcluj.lab7_8_9map.events;

import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;

public class PrietenieTaskEvent implements Event {
    private PrietenieEvent event;
    private DTOPrietenie data, oldData;

    public PrietenieTaskEvent(PrietenieEvent event, DTOPrietenie data) {
        this.event = event;
        this.data = data;
    }

    public PrietenieTaskEvent(PrietenieEvent event, DTOPrietenie data, DTOPrietenie oldData) {
        this.event = event;
        this.data = data;
        this.oldData = oldData;
    }

    public PrietenieEvent getEvent() {
        return event;
    }

    public DTOPrietenie getData() {
        return data;
    }

    public DTOPrietenie getOldData() {
        return oldData;
    }
}
