package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.NewImageReceivedFromServer;
import neeedo.imimaprx.htw.de.neeedo.fragments.OfferImage;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.UploadImageResult;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.utils.ImageUtils;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ImageUploadHandler extends AsyncTask<Void, Integer, UploadImageResult> {
    private final Activity activity;
    private File photoFile;
    private String imageFileNameOnServer;

    private EventService eventService = EventService.getInstance();

    private int totalAmountBytesToUpload;
    private ProgressDialog progressDialog;

    public ImageUploadHandler(File photoFile, Activity activity) {
        this.photoFile = photoFile;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.camera_uploading_progress));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    @Override
    protected UploadImageResult doInBackground(Void... params) {
        try {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";

            int bytesRead;

            int bufferSize;

            byte[] buffer;

            int maxBufferSize = 1024 * 8;

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

            FileInputStream byteArrayInputStream = getFileInputStream();

            totalAmountBytesToUpload = byteArrayInputStream.available();

            bufferSize = Math.min(totalAmountBytesToUpload, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = byteArrayInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);

                bufferSize = Math.min(totalAmountBytesToUpload, maxBufferSize);
                bytesRead = byteArrayInputStream.read(buffer, 0, bufferSize);

                int restBytesToUpload = byteArrayInputStream.available();

                publishProgress(restBytesToUpload);
            }

            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String responseString;
            while ((responseString = br.readLine()) != null) {
                sb.append(responseString);
            }

            JSONObject responseObject = new JSONObject(sb.toString());

            imageFileNameOnServer = responseObject.getString("image");

            byteArrayInputStream.close();
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Exception e) {
            return new UploadImageResult(RestResult.ReturnType.FAILED);
        }

        return new UploadImageResult(RestResult.ReturnType.SUCCESS);
    }

    private FileInputStream getFileInputStream() throws FileNotFoundException {
        FileInputStream fileInputStream = null;
        try {
            Bitmap rotatedBitmap = ImageUtils.rotateBitmap(photoFile);
            Bitmap scaledBitmap = ImageUtils.resize(rotatedBitmap, 1024, 1024);

            File file = ImageUtils.getNewOutputImageFile();
            FileOutputStream fOut = new FileOutputStream(file);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
            fOut.flush();
            fOut.close();

            fileInputStream = new FileInputStream(file);
        } catch (Exception e) {
            System.out.println(e);
        }

        return fileInputStream;
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
    protected void onPostExecute(UploadImageResult result) {
        super.onPostExecute(result);

        if (result.getResult() == RestResult.ReturnType.SUCCESS) {
            progressDialog.dismiss();
            eventService.post(new NewImageReceivedFromServer(new OfferImage(imageFileNameOnServer)));
        } else {
            progressDialog.dismiss();
            new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.image_upload_failed_title))
                    .setMessage(activity.getString(R.string.image_upload_failed_message))
                    .setCancelable(false)
                    .setPositiveButton(activity.getString(R.string.image_upload_failed_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    }).create().show();
        }
    }
}
