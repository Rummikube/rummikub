package Controller;

import java.io.Serializable;

// 직렬화 객체
class SerializeObject implements Serializable {
    private Object eventObject; // 실제 객체를 포장하는 필드
    private String objectType; // 객체의 유형을 나타내는 필드

    private int index; // 객체의 인덱스 번호

    public SerializeObject(Object eventObject, String objectType, int index) {
        this.eventObject = eventObject;
        this.objectType = objectType;
        this.index = index;
    }

    public Object getEventObject() {
        return eventObject;
    }
    public String getObjectType() {
        return objectType;
    }

    public int getIndex(){ return index; }
}