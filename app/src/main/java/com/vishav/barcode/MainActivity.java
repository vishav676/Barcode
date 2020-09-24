package com.vishav.barcode;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import android.hardware.camera2.*;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "My Tag";
    private static final String ErrorTAG = "Error";
    boolean isDetected = false;
    TextView ticketNum,ticketType;
    Switch flash;
    ImageAnalysis imageAnalysis;
    CardView cardView;
    TextView tvName, tvType, tvNo,tv_lastCheck, errorNum, issue, errorDetail;
    PreviewView cameraPreview;
    Camera camera;
    Calendar calendar;
    CardView error_cardView;
    ProcessCameraProvider cameraProvider;
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;
    ArrayList<Ticket> cardNo = new ArrayList<>();
    dbHelper db = new dbHelper(this);
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("NewApi")
    HashMap<String, String> result = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraPreview = (PreviewView) findViewById(R.id.CameraViewid);
        cardView = findViewById(R.id.barcode_result);
        error_cardView = findViewById(R.id.barcode_error);
        tvName = cardView.findViewById(R.id.tvname);
        tvNo = cardView.findViewById(R.id.tvType);
        ticketNum = findViewById(R.id.ticketNumber);
        ticketType = findViewById(R.id.ticketType);
        tvType = cardView.findViewById(R.id.number);
        flash = findViewById(R.id.toggle_flash);
        errorNum = findViewById(R.id.errorNum);
        issue = findViewById(R.id.issueTv);
        tv_lastCheck = findViewById(R.id.last_check);
        errorDetail = findViewById(R.id.tvErrorDetail);
        calendar = Calendar.getInstance();
        makelist();
        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        setupCamera();
                        boolean hasFlash = getApplicationContext().getPackageManager().
                                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        flash.setOnCheckedChangeListener((compoundButton, b) -> {
                            if(hasFlash)
                                camera.getCameraControl().enableTorch(b);
                            else
                                Toast.makeText(getApplicationContext(),"Flash Not Available",
                                        Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

    }

    public void makelist(){
        for(int i =0; i<10; i++)
        {
            Ticket ticket = new Ticket("ELB1","Vishav","2 more",
                    "Employee",2,"none");
            db.insertTicket(ticket);
            cardNo.add(new Ticket("ELB"+i,
                    "Vishav",
                    "Boat Party",
                    "guest 2",
                    1,
                    "Employee"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_history){
            Intent intent = new Intent(this, history.class);
            intent.putExtra("history_list", result);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupCamera()
    {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getApplicationContext());
        cameraProviderFuture.addListener(()->{
            try {
                cameraProvider = cameraProviderFuture.get();
                options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                        .build();
                detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
                bindPreview(cameraProvider);


            } catch (ExecutionException | InterruptedException e){
                Log.e(ErrorTAG, "setupCamera: ", e);
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

         imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        configImageAnaylsis();

        preview.setSurfaceProvider(cameraPreview.createSurfaceProvider());

        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this
                ,cameraSelector,preview,imageAnalysis);
    }

    private void configImageAnaylsis(){
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), image -> {
            @SuppressLint("UnsafeExperimentalUsageError") Image media = image.getImage();
            if(media!=null) {
                Image.Plane[] plane = media.getPlanes();
                if (plane.length >= 3) {
                    for (Image.Plane plane1 : plane) {
                        plane1.getBuffer().rewind();
                    }
                    int rotation = degreeToFirebaseRotation(image.getImageInfo()
                            .getRotationDegrees());
                    FirebaseVisionImage fromMediaImage = FirebaseVisionImage
                            .fromMediaImage(media, rotation);
                    processImage(fromMediaImage);
                }
            }

            image.close();
        });
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


    private String trackHistory()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm:ss a",
                Locale.ENGLISH);
        return currentDate.format(date);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size()>0)
        {
            isDetected = true;
            for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes)
            {
                int value_type = barcode.getValueType();
                if (value_type == FirebaseVisionBarcode.TYPE_TEXT) {
                    imageAnalysis.clearAnalyzer();
                    if(db.searchTicket(barcode.getRawValue())
                            && !result.containsKey(barcode.getRawValue())){
                        String time = trackHistory();
                        result.put(barcode.getRawValue(), time);
                        cardView.setVisibility(View.VISIBLE);
                        delay(cardView);
                        ticketNum.setText(barcode.getRawValue());
                        tvName.setText(barcode.getRawValue());
                        tv_lastCheck.setText(time);
                        delay();
                    }
                    else
                    {
                        error_cardView.setVisibility(View.VISIBLE);
                        if (!db.searchTicket(barcode.getRawValue()))
                        {
                            issue.setText("Ticket Number not in the list");
                            errorNum.setText(barcode.getRawValue());
                        }
                        else if(result.containsKey(barcode.getRawValue())){
                            issue.setText("Already used");
                            errorDetail.setText(result.get(barcode.getRawValue()));
                            errorNum.setText(barcode.getRawValue());
                        }
                        delay(error_cardView);
                        delay();

                    }

                }
            }
            isDetected = false;
        }
    }
    private void delay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    configImageAnaylsis();
            }
        },5000);
    }


    private void delay(CardView cardView){
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    cardView.setVisibility(View.INVISIBLE);
                });
            }
        },3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}