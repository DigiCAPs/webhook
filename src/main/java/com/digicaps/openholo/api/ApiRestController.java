package com.digicaps.openholo.api;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.jdom2.JDOMException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digicaps.openholo.jninka.JninkaResult;
import com.digicaps.openholo.jninka.JninkaWrapper;
import com.digicaps.openholo.repository.JninkaFilesRepository;
import com.digicaps.openholo.repository.PusherRepository;
import com.digicaps.openholo.webhook.GithubWrapper;
import com.digicaps.openholo.webhook.Pusher;

@RestController
public class ApiRestController {

    @Autowired
    PusherRepository pusherRepository;

    @Autowired
    JninkaFilesRepository jninkaRepo;

    // Secret Key: df673c0a8cc4530fc6ccac5e2b27d16ac0d0d22b9aaf5e01a88696ef16cd23ab
    // Secret Key = sha256("openholo webhook digicap")

    private Logger logger = Logger.getLogger(ApiRestController.class);
    
    @Value("${github.clone.directory}")
    String githubDirectory;
    
    @Value("${jninka.jar.path}")
    String jninkaLibraryPath;

    
    @RequestMapping(value = {"/"})
    String helloOpenHolo() {
       logger.info("[GET] / " + githubDirectory);

       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       return "Hello OpenHolo Backend. Timestamp is " + timestamp; 
    }

    @RequestMapping(value = {"/hook/github"}, method = RequestMethod.POST)
    String notificationFromGithub(@RequestBody String body) {
        logger.info("[POST] /hook/github | Notification. " + githubDirectory);

        Pusher pusher = new Pusher();

        makeGitDirectory();

        GithubWrapper githubWebHook = new GithubWrapper();
        String url;
        try {
            url = githubWebHook.getCloneUrlFormJson(body);
            pusher.email = githubWebHook.getEmailOfPuhserFromJson(body);
            pusher.name = githubWebHook.getNameOfPuhserFromJson(body);
        } catch (JSONException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            return e.getMessage();
        }
        
        // --------------------------------------------------------------
        // url = "https://github.com/Openhologram/OpenHologram";
        // --------------------------------------------------------------

        pusher.id = (System.currentTimeMillis() / 1000L);

        pusher.resultPath = makeGitTempDirectory(pusher.id);
        if (pusher.resultPath.length() == 0) {
            logger.info("github clone path is 0");
            return "github clone path is 0";
        }
        
        GitCommand gc = new GitCommand(pusher, url, pusher.resultPath);
        gc.start();
        
        return "response /hook/github";
    }

    // ------------------------------------------------------------------------
    // Util Func
    // ------------------------------------------------------------------------

    /**
     * 
     */
    boolean makeGitDirectory() {
        File directoryPath = new File(githubDirectory);
        if (directoryPath.exists()) {
            return true;
        }
        
        return directoryPath.mkdirs(); 
    }
    
    /**
     * 
     * @param id
     * @return
     */
    String makeGitTempDirectory(long id) {
        String path = githubDirectory + "/" + id; 
        File tempPath = new File(path);

        if (!tempPath.mkdirs()) {
           return ""; 
        }
        
        return path;
    }
    
    class GitCommand extends Thread {

        Pusher pusher;
        String urlClone;
        File localClonePath;
        String resultPath;
        
        public GitCommand(Pusher pusher, String url, String clonePath) {
            this.pusher = pusher;
            this.urlClone= url;
            this.localClonePath = new File(clonePath);
            this.resultPath = new String();
        }
        
        @Override
        public void run() {
            CloneCommand cc = new CloneCommand()
                    .setURI(urlClone)
                    .setDirectory(localClonePath)
                    .setProgressMonitor(new TextProgressMonitor());
            try {
                cc.call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                return;
            } 

            JninkaWrapper jninka = new JninkaWrapper();
            try {
                resultPath = jninka.findLicense(localClonePath.getPath(), jninkaLibraryPath);
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                return;
            }

            if (resultPath.length() != 0) {
                logger.info("Success. Result File Path: " + resultPath);

                // update result.xml path 
                pusher.resultPath = resultPath;
                
                JninkaResult result = null;

                // Parsing XML
                try {
                    result = jninka.toParser(resultPath);
                } catch (JDOMException | IOException e) {
                    e.printStackTrace();
                    logger.info(e.getMessage());
                }
                
                // DB에 저장 
                DBWrapper db = new DBWrapper(pusher, result);
                db.insert(pusherRepository, jninkaRepo);
            }
        }
    }
}
