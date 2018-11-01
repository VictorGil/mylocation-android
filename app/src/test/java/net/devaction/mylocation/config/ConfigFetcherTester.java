package net.devaction.mylocation.config;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Víctor Gil
 *
 * since November 2018
 */
public class ConfigFetcherTester {
    private static final Logger log = LogManager.getLogger(ConfigFetcherTester.class);

    public static void main(String args){
        new ConfigFetcherTester().run();
    }

    @Test
    public void runTest(){
        run();
    }

    private void run(){
        log.info("Starting test");
        File fileFromClasspath = getFileFromClasspath("config.json");
        log.debug("File path: " + fileFromClasspath.getAbsolutePath());

        InputStream configInputStream = null;
        ConfigData configData;
        try{
            configInputStream = new FileInputStream(fileFromClasspath);
            configData = ConfigFetcher.readConfigData(configInputStream, null);
        } catch(FileNotFoundException ex){
            log.error("Exception when opening the InputStream: " + ex, ex);
            throw new RuntimeException(ex);
        } finally{
            try{
                if (configInputStream != null)
                    configInputStream.close();
            } catch(IOException ex){
                log.error("Exception when closing the InputStream: " + ex, ex);
            }
        }

        log.info("Config data loaded from JSON file: " + configData);
    }

    private File getFileFromClasspath(String fileName) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }
}
