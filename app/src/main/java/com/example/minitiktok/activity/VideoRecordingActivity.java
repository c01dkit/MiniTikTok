package com.example.minitiktok.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;

import com.example.minitiktok.R;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class VideoRecordingActivity extends BaseActivity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private Button btnRecord, btnChange, btnZoom;
    private MediaRecorder mMediaRecorder;
    private static final String TAG = "VideoRecordingActivity";
    private boolean isRecording = false;
    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int rotationDegree = 0;
    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;
    private static String mVideoPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recording);

        mSurfaceView = findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        btnRecord = findViewById(R.id.btnRecord);
        btnChange = findViewById(R.id.btnCameraChange);
        btnZoom = findViewById(R.id.btnZoom);
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
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    releaseMediaRecorder();
                    Toast.makeText(VideoRecordingActivity.this,"视频已保存: "+mVideoPath,Toast.LENGTH_LONG).show();
                    btnRecord.setText(R.string.record_start);
                    isRecording = false;
                } else {
                    prepareVideoRecorder();
                    btnRecord.setText(R.string.record_stop);
                    isRecording = true;
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(VideoRecordingActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(VideoRecordingActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(VideoRecordingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(VideoRecordingActivity.this, "请授予全部权限后再启动视频录制功能！", Toast.LENGTH_SHORT).show();
            finish();
        }
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
        mCamera.stopPreview();
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




    private void prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        File mVideoFile = getOutputMediaFile(MEDIA_TYPE_VIDEO);
        assert mVideoFile != null;
        mMediaRecorder.setOutputFile(mVideoFile.toString());

        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            releaseMediaRecorder();
            return;
        }
        MediaScannerConnection.scanFile(this, new String[]{mVideoFile.toString()}, null, null);
    }


    private void releaseMediaRecorder() {
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();
    }

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
                Environment.DIRECTORY_PICTURES), "Chapter_8");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mVideoPath =  mediaStorageDir.getPath() + File.separator + "MiniTikTok_" + timeStamp + ".mp4";
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mVideoPath);
        } else {
            return null;
        }
        return mediaFile;
    }
}