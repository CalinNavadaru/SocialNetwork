package cs.ubbcluj.lab7_8_9map.events;

import cs.ubbcluj.lab7_8_9map.domain.dto.DTOCererePrietenie;

public class CerereTaskEvent implements Event {
    private final CerereEvent event;
    private final DTOCererePrietenie data;
    private DTOCererePrietenie oldData;

    public CerereTaskEvent(CerereEvent event, DTOCererePrietenie data) {
        this.event = event;
        this.data = data;
    }

    public CerereTaskEvent(CerereEvent event, DTOCererePrietenie data, DTOCererePrietenie oldData) {
        this.event = event;
        this.data = data;
        this.oldData = oldData;
    }

    public CerereEvent getEvent() {
        return event;
    }

    public DTOCererePrietenie getData() {
        return data;
    }

    public DTOCererePrietenie getOldData() {
        return oldData;
    }
}
