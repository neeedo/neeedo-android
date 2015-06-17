package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BlaHandler extends AsyncTask{
    private final File photoFile;

    public BlaHandler(File photoFile) {
        this.photoFile = photoFile;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead;
        int bytesAvailable;
        int bufferSize;

        byte[] buffer;

        int maxBufferSize = 1024 * 2 * 2 * 2;

        try {

            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
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

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            FileInputStream fileInputStream = new FileInputStream(photoFile);

            URL url =   new URL("https://api.neeedo.com/images");
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setChunkedStreamingMode(maxBufferSize);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Authorization", "Basic dGVzdEB0ZXN0MTIzLmRlOnRlc3Q=");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            connection.setRequestProperty("uploaded_file", photoFile.getName());

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + photoFile.getName() + "\"" + lineEnd);

            dataOutputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            final int hundredPercent = bytesAvailable;
//            progressDialog.setMax(hundredPercent);

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

//                System.out.println();

                final int restBytes = bytesAvailable;
                final int uploadedBytes = hundredPercent - restBytes;

//                progressDialog.setProgress((int) uploadedBytes);
//                if (restBytes <= 0) {
//                    progressDialog.setMessage(activity.getString(R.string.camera_uploading_done));
//                }

//                Log.d("bla", "uploadedBytes  " + uploadedBytes);
//                Log.d("bla", "#################################");
//                Log.d("bla", "bufferSize     " + bufferSize);
//                Log.d("bla", "bytesRead      " + bytesRead);
//                Log.d("bla", "bytesAvailable " + bytesAvailable);
//                Log.d("bla", "hundredPercent " + hundredPercent);
//                Log.d("bla", "uploadedBytes  " + uploadedBytes);
//                Log.d("bla", "#################################");
//                Log.d("bla", "-");
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

//            connection.get

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            BufferedReader   br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder  sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
                Log.i("uploadFile", sb.toString());
            }
//            if (serverResponseCode == 200) {
//                progressDialog.dismiss();
//            }

            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (Exception e) {
//            progressDialog.dismiss();

            Log.e("bla", "Exception : " + e.getMessage(), e);
        }

        return null;
    }
}
