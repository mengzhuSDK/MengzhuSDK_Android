package com.mzmedia.presentation.dto;


import tv.mengzhu.core.module.model.dto.BaseDto;

public class LiveConfigDto extends BaseDto {
    private int bitrate;
    private boolean cbbeauty;
    private boolean cblater;
    private boolean cbAudio;
    private String live_tk;
    private String ticketId;
    private int fps;
    private int time;
    private int allBanChat = 1; //全体禁言0开启1关闭

    public int getAllBanChat() {
        return allBanChat;
    }

    public void setAllBanChat(int allBanChat) {
        this.allBanChat = allBanChat;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public boolean isCbbeauty() {
        return cbbeauty;
    }

    public void setCbbeauty(boolean cbbeauty) {
        this.cbbeauty = cbbeauty;
    }

    public boolean isCblater() {
        return cblater;
    }

    public void setCblater(boolean cblater) {
        this.cblater = cblater;
    }

    public boolean isCbAudio() {
        return cbAudio;
    }

    public void setCbAudio(boolean cbAudio) {
        this.cbAudio = cbAudio;
    }

    public String getLive_tk() {
        return live_tk;
    }

    public void setLive_tk(String live_tk) {
        this.live_tk = live_tk;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
