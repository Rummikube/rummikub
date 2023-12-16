package Controller;

import java.io.Serializable;

// 직렬화 객체
class SerializeObject implements Serializable {
    private Object eventObject; // 실제 객체를 포장하는 필드
    private String objectType; // 객체의 유형을 나타내는 필드

    public SerializeObject(Object eventObject, String objectType) {
        this.eventObject = eventObject;
        this.objectType = objectType;
    }

    public Object getEventObject() {
        return eventObject;
    }
    public String getObjectType() {
        return objectType;
    }
}