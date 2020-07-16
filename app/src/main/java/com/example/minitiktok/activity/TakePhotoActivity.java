package com.example.minitiktok.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.example.minitiktok.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TakePhotoActivity extends BaseActivity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private File mPictureFile;
    private Button btnPhoto, btnChange, btnZoom;
    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int rotationDegree = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;
    private static String mPhotoPath = "";
    private static final String TAG = "TakePhotoActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        mSurfaceView = findViewById(R.id.surfaceView2);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btnPhoto = findViewById(R.id.button);
        btnChange = findViewById(R.id.button2);
        btnZoom = findViewById(R.id.button3);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                startPreview(holder);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                releaseCameraAndPreview();
            }
        });
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar!=null) mActionBar.hide();
        bind();
    }

    private void bind() {
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPhoto.setText(R.string.record_saving);
                btnPhoto.setEnabled(false);
                mCamera.takePicture(null,null,mPicture);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CAMERA_TYPE == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                } else {
                    mCamera = getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
                startPreview(mSurfaceView.getHolder());
            }
        });

        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Camera.Parameters parameters = mCamera.getParameters();
                List<String> modes = parameters.getSupportedFocusModes();
                for (String mode : modes) {
                    if (mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                        break;
                    }
                }
                mCamera.setParameters(parameters);
            }
        });
    }

    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) releaseCameraAndPreview();
        Camera cam = Camera.open(position);
        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);
        Camera.Parameters parameters = cam.getParameters();
        Camera.Size size = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), mSurfaceView.getWidth(), mSurfaceView.getHeight());
        parameters.setPreviewSize(size.width, size.height);
        return cam;
    }

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;
        } else {
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
//        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }



    private void startPreview(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "startPreview: "+ e.getMessage() );
        }
    }

    private Camera.PictureCallback mPicture = (data, camera) -> {
        mPictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (mPictureFile == null) {
            return;
        }
        try {
            //图片旋转
            Matrix matrix = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            matrix.postRotate(getRotateDegree(mPhotoPath)+90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            FileOutputStream fos = new FileOutputStream(mPictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.d("mPicture", "Error accessing file: " + e.getMessage());
        }
        MediaScannerConnection.scanFile(this, new String[] { mPictureFile.toString()},null,null);
        mCamera.startPreview();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TakePhotoActivity.this, MainActivity.class);
                intent.putExtra(getString(R.string.photo_path),mPhotoPath);
                if (mPhotoPath.isEmpty()){
                    setResult(RESULT_CANCELED, intent);
                } else {

                    releaseCameraAndPreview();
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        },1000);
    };

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MiniTikTok");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mPhotoPath =  mediaStorageDir.getPath() + File.separator + "MiniTikTok_" + timeStamp + ".jpg";
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mPhotoPath);
        } else {
            return null;
        }
        return mediaFile;
    }


    public int getRotateDegree(String path) {
        ExifInterface srcExif = null;
        try {
            srcExif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        Matrix matrix = new Matrix();
        int angle = 0;
        int orientation = srcExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = DEGREE_90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = DEGREE_180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = DEGREE_270;
                break;
            default:
                break;
        }
        matrix.postRotate(angle);
        return angle;
    }
}