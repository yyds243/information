package com.example.entity;


/**
 * 
*/
public class Carousel {
    /** ID */
    private Integer id;
    /** 赛事ID */
    private Integer eventId;
    private String eventName;
    /** 图片 */
    private String img;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}