package com.p2p.express.lane.example;

/**
 * Created by hliang on 7/2/15.
 */
public class GeoLocation {
    private String id;
    private Double positon_x;
    private Double position_y;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPositon_x() {
        return positon_x;
    }

    public void setPositon_x(Double positon_x) {
        this.positon_x = positon_x;
    }

    public Double getPosition_y() {
        return position_y;
    }

    public void setPosition_y(Double position_y) {
        this.position_y = position_y;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
