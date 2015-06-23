package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ImageUploadHandler extends AsyncTask {
    private File photoFile;

    public ImageUploadHandler(File photoFile) {
        this.photoFile = photoFile;
    }


    @Override
    protected Object doInBackground(Object[] params) {

        try {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead;
            int totalAmountBytesToUpload;
            int bufferSize;

            byte[] buffer;

            int maxBufferSize = 1024 * 8;

            trustAllCerts();

            URL url = new URL(ServerConstantsUtils.getActiveServer() + "images");

        HttpsURLConnection    connection = (HttpsURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(maxBufferSize);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Authorization", ActiveUser.getInstance().getAuthentificationHash());
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", photoFile.getName());
            connection.setRequestProperty("Content-Length", String.valueOf(photoFile.length()));
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + photoFile.getName() + "\"" + lineEnd);
            dataOutputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
            dataOutputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            dataOutputStream.writeBytes(lineEnd);

            FileInputStream fileInputStream = new FileInputStream(photoFile);

            BitmapFactory.decodeStream(fileInputStream).compress(Bitmap.CompressFormat.JPEG, 80, dataOutputStream);

            totalAmountBytesToUpload = fileInputStream.available();

            bufferSize = Math.min(totalAmountBytesToUpload, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                totalAmountBytesToUpload = fileInputStream.available();
                bufferSize = Math.min(totalAmountBytesToUpload, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                final int restBytes = totalAmountBytesToUpload;
                final int uploadedBytes = totalAmountBytesToUpload - restBytes;

                publishProgress(uploadedBytes, totalAmountBytesToUpload);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                sb.append(output);
                Log.i("uploadFile", sb.toString());
                //TODO do something with the id
            }

            Log.i("uploadFile", sb.toString());

            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (Exception e) {
            Log.e("bla", "Exception : " + e.getMessage(), e);
        }

        return null;
    }

    private void trustAllCerts() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);

        int uploadedBytes = (int) values[0];
        int totalAmountBytesToUpload = (int) values[1];
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
