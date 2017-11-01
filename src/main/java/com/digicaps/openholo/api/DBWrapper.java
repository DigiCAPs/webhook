package com.digicaps.openholo.api;

import com.digicaps.openholo.jninka.JninkaAttribute;
import com.digicaps.openholo.jninka.JninkaFiles;
import com.digicaps.openholo.jninka.JninkaResult;
import com.digicaps.openholo.repository.JninkaFilesRepository;
import com.digicaps.openholo.repository.PusherRepository;
import com.digicaps.openholo.webhook.Pusher;

public class DBWrapper {

    Pusher pusher; 

    JninkaResult result;
    
    public DBWrapper(Pusher pusher, JninkaResult result) {
        this.pusher = pusher;
        this.result = result;
    }
    
    public void insert(PusherRepository pusherRepo, JninkaFilesRepository jninkaRepo) {
        PusherEntity p = new PusherEntity();
        p.setPusherId(pusher.id);
        p.setEmail(pusher.email);
        p.setName(pusher.name);
        p.setResultPath(pusher.resultPath);
        p.setScanTime(result.scanTime);

        pusherRepo.save(p); 
        pusherRepo.flush();
        
        for (JninkaFiles files: result.files) {
            for (JninkaAttribute attribute : files.attributes) {
                JninkaFilesEntity j = new JninkaFilesEntity();
                j.pusher_id = pusher.id;
                j.fileName = files.fileName; 
                j.matchName = attribute.matchName;
                j.originalLine = attribute.originalLine;

                jninkaRepo.save(j);
            }
        }
    }
}
