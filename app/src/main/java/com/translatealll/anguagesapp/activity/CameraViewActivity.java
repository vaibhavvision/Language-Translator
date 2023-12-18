package com.translatealll.anguagesapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.canhub.cropper.CropImage;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Preview;
import com.translatealll.anguagesapp.R;
import com.translatealll.anguagesapp.databinding.ActivityCameraViewBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class CameraViewActivity extends AppCompatActivity {
    public ActivityCameraViewBinding binding;
    private long captureTime;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityCameraViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }

        binding.camera.setLifecycleOwner(this);
        binding.camera.addCameraListener(new Listener());
        setClick();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    private void setClick() {
        binding.capturePictureSnapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capturePictureSnapshot();
            }
        });
        binding.imgChangeFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCurrentFlash();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onBackClick() {
        finish();
    }

    public void changeCurrentFlash() {
        if (binding.camera.getFlash() == Flash.OFF) {
            binding.camera.setFlash(Flash.TORCH);
            binding.imgChangeFlash.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.flash_on));
            return;
        }
        binding.camera.setFlash(Flash.OFF);
        binding.imgChangeFlash.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.flash_off));
    }


    public void capturePictureSnapshot() {
        if (!binding.camera.isTakingPicture() && binding.camera.getPreview() == Preview.GL_SURFACE) {
            binding.watermark.setVisibility(View.VISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.watermark.setVisibility(View.GONE);

                }
            }, 200L);
            captureTime = System.currentTimeMillis();
            binding.camera.takePicture();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    public File saveToInternalStoragePrivate(Context context, Bitmap bitmap) {


        String uuid = UUID.randomUUID().toString();

        String substring = uuid.substring(0, 5);

        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!externalStoragePublicDirectory.exists()) {
            externalStoragePublicDirectory.mkdir();
        }
        File file = new File(externalStoragePublicDirectory, substring + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return file;
    }

    private class Listener extends CameraListener {
        public Listener() {
        }

        @Override
        public void onCameraOpened(CameraOptions options) {

        }

        @Override
        public void onCameraError(CameraException exception) {

            super.onCameraError(exception);
        }

        @Override
        public void onPictureTaken(PictureResult result) {

            super.onPictureTaken(result);
            if (binding.camera.isTakingVideo()) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            if (captureTime == 0) {
                captureTime = currentTimeMillis - ((long) 300);
            }
            try {
                CameraViewActivity cameraViewActivity = CameraViewActivity.this;
                result.toBitmap(1000, 1000, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        MainActivity.CameraPic = saveToInternalStoragePrivate(CameraViewActivity.this, bitmap).getAbsolutePath();
                        finish();
                    }
                });
            } catch (UnsupportedOperationException unused) {
            }
            captureTime = 0L;
        }


        @Override
        public void onExposureCorrectionChanged(float f, float[] bounds, PointF[] pointFArr) {

            super.onExposureCorrectionChanged(f, bounds, pointFArr);
        }

        @Override
        public void onZoomChanged(float f, float[] bounds, PointF[] pointFArr) {

            super.onZoomChanged(f, bounds, pointFArr);
        }
    }


}
