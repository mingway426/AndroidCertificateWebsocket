package com.example.mingwaytestproject;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.CustomSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    String TAG = "Mingway";
    private Button button2;
    private WebSocketServer server;

    String STOREPASSWORD = "xxxx";
    String KEYPASSWORD = "xxxx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2 = findViewById(R.id.button2);
        init();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server.start();
            }
        });
    }

    private void init() {
        server = new WebSocketServer(NetInfoUtils.getInetAddress(this)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                Log.d(TAG, "onOpen: ");
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                Log.d(TAG, "onClose: ");
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                Log.d(TAG, "onMessage: ");
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                Log.d(TAG, "onError: " + ex.getMessage()



                );
            }

            @Override
            public void onStart() {
                Log.d(TAG, "onStart: ");
            }
        };


        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("BKS");
            InputStream inputStream = getResources().openRawResource(R.raw.client);
            ks.load(inputStream, STOREPASSWORD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(ks, KEYPASSWORD.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLEngine engine = sslContext.createSSLEngine();
            List<String> ciphers = new ArrayList<String>(Arrays.asList(engine.getEnabledCipherSuites()));
            ciphers.remove("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
            List<String> protocols = new ArrayList<String>(Arrays.asList(engine.getEnabledProtocols()));
            protocols.remove("SSLv3");
            CustomSSLWebSocketServerFactory factory = new CustomSSLWebSocketServerFactory(sslContext,
                    protocols.toArray(new String[]{}), ciphers.toArray(new String[]{}));

            server.setWebSocketFactory(factory);
            server.start();
        } catch (KeyStoreException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
