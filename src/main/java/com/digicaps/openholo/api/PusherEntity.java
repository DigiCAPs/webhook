package com.digicaps.openholo.api;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PusherEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long pusherId;
    private String name;
    private String email;
    private String resultPath;
    private String scanTime;
    
    public PusherEntity() {
    }
    
    public PusherEntity(Long id, String name, String email, String path, String time) {
        this.pusherId = id;
        this.name = name;
        this.email = email;
        this.resultPath = path;
        this.scanTime = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPusherId() {
        return pusherId;
    }

    public void setPusherId(Long pusherId) {
        this.pusherId = pusherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }
}
