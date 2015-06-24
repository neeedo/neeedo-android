package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

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

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.SuperFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ImageUploadHandler extends AsyncTask<Void, Integer, Void> {
    private final NewOfferFragment fragment;
    private File photoFile;
    private String imageFileName;

    private int totalAmountBytesToUpload;
    private ProgressDialog progressDialog;

    public ImageUploadHandler(File photoFile, NewOfferFragment fragment) {
        this.photoFile = photoFile;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(fragment.getActivity());
        progressDialog.setMessage(fragment.getString(R.string.camera_uploading_progress));
        progressDialog.setMessage("asd");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead;

            int bufferSize;

            byte[] buffer;

            int maxBufferSize = 1024 * 8;

            trustAllCerts();

            URL url = new URL(ServerConstantsUtils.getActiveServer() + "images");

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

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

//            BitmapFactory.decodeStream(fileInputStream).compress(Bitmap.CompressFormat.JPEG, 80, dataOutputStream);

            totalAmountBytesToUpload = fileInputStream.available();

            bufferSize = Math.min(totalAmountBytesToUpload, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);

                bufferSize = Math.min(totalAmountBytesToUpload, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                int restBytesToUpload = fileInputStream.available();

                publishProgress(restBytesToUpload);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder sb = new StringBuilder();

            String responseString;
            while ((responseString = br.readLine()) != null) {
                sb.append(responseString);
            }

            JSONObject responseObject
                    = new JSONObject(sb.toString());

            imageFileName = responseObject.getString("image");

            fileInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();

        } catch (Exception e) {
            Toast.makeText(fragment.getActivity(), "something went wrong!", Toast.LENGTH_SHORT).show();
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
    protected void onProgressUpdate(Integer... stillLeftToUpload) {
        super.onProgressUpdate(stillLeftToUpload);

        int alreadyUploaded = totalAmountBytesToUpload - stillLeftToUpload[0];

        int onePercent = totalAmountBytesToUpload / 100;
        int percentUploaded = alreadyUploaded / onePercent;

        progressDialog.setProgress(percentUploaded);
    }

    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);
        progressDialog.dismiss();

        fragment.setNewImage( imageFileName );
    }
}
