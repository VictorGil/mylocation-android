package net.devaction.mylocation.services;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author Víctor Gil
 *
 * since October 2018
 */
public class SslSocketFactoryCreator {

    public static SSLSocketFactory create(byte[] keyStorebytes, String keyStorePassword){
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            ByteArrayInputStream keyStoreInputStream = new ByteArrayInputStream(keyStorebytes);
            keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
            keyStoreInputStream.close();
            Log.d(SslSocketFactoryCreator.class.getSimpleName(), "Loaded key store file");

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            String trustManagerFactoryDefaultAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            Log.d("SslSocketFactoryCreator", "TrustManagerFactory default algorithm: " +
                    trustManagerFactoryDefaultAlgorithm); //it is PKIX

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(trustManagerFactoryDefaultAlgorithm);
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            Log.e("SslSocketFactoryCreator", ex.toString(), ex);
        }
        return null;
    }
}

