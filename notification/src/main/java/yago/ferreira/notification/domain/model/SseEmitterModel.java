package yago.ferreira.notification.domain.model;

public class SseEmitterModel {
    private Long id;
    private Object data;
    private String event;

    public SseEmitterModel() {
    }

    public SseEmitterModel(Long id, Object data, String event) {
        this.id = id;
        this.data = data;
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
