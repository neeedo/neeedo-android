package neeedo.imimaprx.htw.de.neeedo.service;

import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import neeedo.imimaprx.htw.de.neeedo.R;

public class S3Service {

    TransferManager transferManager;

    //singleton
    private static S3Service instance;

    public static S3Service getInstance() {
        if (S3Service.instance == null) {
            S3Service.instance = new S3Service();
        }
        return S3Service.instance;
    }

    public void initialize(Context context) {

        String accessKeyId = context.getString(R.string.s3_accessKeyId);
        String secretKey = context.getString(R.string.s3_secretKey);

        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretKey);

        transferManager = new TransferManager(credentials);
        transferManager.getAmazonS3Client().setRegion(Region.getRegion(Regions.EU_CENTRAL_1));

    }


    public TransferManager getTransferManager() {
        return transferManager;
    }
}
