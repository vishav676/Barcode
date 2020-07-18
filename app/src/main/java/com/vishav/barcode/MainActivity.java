package com.vishav.barcode;



import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "My Tag";
    boolean isDetected = false;
    TextView ticketNo;
    Switch flash;
    CardView cardView;
    TextView tvName, tvType, tvNo;
    PreviewView cameraPreview;
    Camera camera;
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("NewApi")
    Set<String> result = new ArraySet<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ticketNo = (TextView)findViewById(R.id.ticket_num);
        cameraPreview = (PreviewView) findViewById(R.id.CameraViewid);
        cardView = findViewById(R.id.barcode_result);
        tvName = cardView.findViewById(R.id.tvname);
        tvNo = cardView.findViewById(R.id.tvType);
        tvType = cardView.findViewById(R.id.number);
        flash = findViewById(R.id.toggle_flash);
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        setupCamera();
                        boolean hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(hasFlash)
                                    camera.getCameraControl().enableTorch(b);
                                else
                                    Toast.makeText(getApplicationContext(),"Flash Not Available",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupCamera()
    {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getApplicationContext());
        cameraProviderFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                        .build();
                detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e){

            }
        }, ContextCompat.getMainExecutor(getApplicationContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void bindPreview(ProcessCameraProvider cameraProvider)
    {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector  = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
            @SuppressLint("UnsafeExperimentalUsageError") Image media = image.getImage();
            if(media!=null) {
                Image.Plane[] plane = media.getPlanes();
                if (plane.length >= 3) {
                    for (Image.Plane plane1 : plane) {
                        plane1.getBuffer().rewind();
                    }
                    int rotation = degreeToFirebaseRotation(image.getImageInfo().getRotationDegrees());
                    FirebaseVisionImage fromMediaImage = FirebaseVisionImage.fromMediaImage(media, rotation);
                    processImage(fromMediaImage);
                }
            }
            image.close();
        });

        preview.setSurfaceProvider(cameraPreview.createSurfaceProvider());
        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this,cameraSelector,preview,imageAnalysis);
    }

    private void processImage(FirebaseVisionImage visionImageFromFrame) {
        if(!isDetected)
        {
            detector.detectInImage(visionImageFromFrame)
                    .addOnSuccessListener(this::processResult)
                    .addOnFailureListener(e -> {
                    });
        }
    }

    private int degreeToFirebaseRotation(int degrees)
    {
        switch (degrees)
        {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size()>0)
        {
            isDetected = true;
            for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes)
            {
                int value_type = barcode.getValueType();
                if (value_type == FirebaseVisionBarcode.TYPE_TEXT) {
                    ticketNo.setText(barcode.getRawValue());
                    result.add(barcode.getRawValue());
                    cardView.setVisibility(View.VISIBLE);
                    Timer time = new Timer();
                    time.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cardView.setVisibility(View.INVISIBLE);
                                    result.add(barcode.getRawValue());
                                    Log.d(TAG, String.valueOf(result));
                                }
                            });
                        }
                    },3000);
                    tvName.setText(barcode.getRawValue());
                }
            }
            isDetected = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}