package com.mzmedia.presentation.presenter.stop;


import tv.mengzhu.core.module.model.dto.BaseDto;


/**
 * @author xiefei
 * @description
 */

public class EndBroadcastInfoDto extends BaseDto {

    private String ticket_id;
    private String uv;
    private String duration;

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String cover;// 频道封面
    private String name;

}
