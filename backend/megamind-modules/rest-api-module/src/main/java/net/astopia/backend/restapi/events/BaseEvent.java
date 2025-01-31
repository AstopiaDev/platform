package net.astopia.backend.restapi.events;

public abstract class BaseEvent implements IEvent {
    private String event;

    public String getEvent() {
        if (event == null) {
            event = this.getClass().getAnnotation(Event.class).value();
        }

        return event;
    }
}
