package com.turing.unifyid_androidtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private static final int    interval = 500;
    private static final int    maxPictures = 10;
    private static String       keyAlias = "UnifyIDKey";
    private static String       androidKeyStoreName = "AndroidKeyStore";

    private static String       imageName = "snapshot_";
    private CameraView  cameraView = null;
    private Handler     handler;
    private int         currentPicture;
    private KeyStore    keyStore;
    private SecretKey   secretKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraView = findViewById(R.id.camera);

        // Set Camera

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onCameraOpened(CameraOptions options) {
                super.onCameraOpened(options);
            }

            @Override
            public void onCameraClosed() {
                super.onCameraClosed();
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                super.onCameraError(exception);
            }

            class SaveTask extends AsyncTask<byte[], Integer, Void> {
                @Override
                protected Void doInBackground(byte[]... bytes) {

                    FileOutputStream outStream;
                    try {
                        // encrypt jpeg
                        HashMap<String, byte[]> encryped =  encryptBytes(bytes[0]);

                        outStream = openFileOutput(String.format("%s_%d.bmp", imageName, currentPicture), Context.MODE_PRIVATE);
                        outStream.write(encryped.get("encrypted"));
                        outStream.close();
                        Log.d(TAG, "saved: "+ currentPicture);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }

                    return null;
                }
            }
            @SuppressLint("DefaultLocale")
            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                new SaveTask().execute(jpeg);

            }
        });

        // Init cipher
        try {
            initKeyStore();
            secretKey = getSecretKey(keyAlias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        // Generate capture timer
        currentPicture = 0;
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (cameraView != null)
            cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null)
            cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null)
            cameraView.destroy();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // take snapshots
            if (currentPicture < maxPictures)
            {
                cameraView.captureSnapshot();
                handler.postDelayed(this, interval);
                currentPicture ++;
            }
        }
    };

    private void initKeyStore() throws KeyStoreException, CertificateException,
            NoSuchAlgorithmException, IOException {
        keyStore = KeyStore.getInstance(androidKeyStoreName);
        keyStore.load(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, androidKeyStoreName);

        keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }

    private HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes)
    {
        HashMap<String, byte[]> map = new HashMap<>();

        try
        {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainTextBytes);
            map.put("encrypted", encrypted);
        }
        catch(Exception e)
        {
        }

        return map;
    }
}