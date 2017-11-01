package com.digicaps.openholo.api;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JninkaFilesEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    Long pusher_id;
    String fileName;
    String matchName;
    String originalLine;

    public JninkaFilesEntity() {
    }
    
    public JninkaFilesEntity(Long pusherId, String fileName, String matchName, String originalLine) {
        this.pusher_id = pusherId;
        this.fileName = fileName;
        this.matchName = matchName;
        this.originalLine = originalLine;
    }

}
