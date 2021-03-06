/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package hevs.it.SkiRunV2.missions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;

import hevs.it.SkiRunV2.R;
import hevs.it.SkiRunV2.entity.ResultEntity;
import hevs.it.SkiRunV2.firebase.FirebaseResultManager;
import hevs.it.SkiRunV2.missions.camera.AspectFrameLayout;
import hevs.it.SkiRunV2.missions.camera.CameraUtils;
import hevs.it.SkiRunV2.missions.camera.CircularEncoder;
import hevs.it.SkiRunV2.missions.camera.gles.EglCore;
import hevs.it.SkiRunV2.missions.camera.gles.FullFrameRect;
import hevs.it.SkiRunV2.missions.camera.gles.Texture2dProgram;
import hevs.it.SkiRunV2.missions.camera.gles.WindowSurface;
import hevs.it.SkiRunV2.permissions.PermissionHelper;

public class DoorControllerCamera extends Activity implements SurfaceHolder.Callback,
        SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "DoorControllerCamera";

    //dimensions
    private static final int VIDEO_WIDTH = 1920;  // dimensions for 1080p video
    private static final int VIDEO_HEIGHT = 1080;
    private static final int DESIRED_PREVIEW_FPS = 24;

    private EglCore mEglCore;
    private WindowSurface mDisplaySurface;
    private SurfaceTexture mCameraTexture;  // receives the output from the camera preview
    private FullFrameRect mFullFrameBlit;
    private final float[] mTmpMatrix = new float[16];
    private int mTextureId;
    private int mFrameNum;

    private ResultEntity result ;

    Intent i;
    String competitionName, disciplineName, location, missionName;

    ProgressBar pb ;

    private Camera mCamera;
    private int mCameraPreviewThousandFps;

    //private File mOutputFile;
    private CircularEncoder mCircEncoder;
    private WindowSurface mEncoderSurface;
    private boolean mFileSaveInProgress;

    private MainHandler mHandler;
    private float mSecondsOfVideo;

    /**
     * Custom message handler for main UI thread.
     * <p>
     * Used to handle camera preview "frame available" notifications, and implement the
     * blinking "recording" text.  Receives callback messages from the encoder thread.
     */
    private static class MainHandler extends Handler implements CircularEncoder.Callback {
        public static final int MSG_BLINK_TEXT = 0;
        public static final int MSG_FRAME_AVAILABLE = 1;
        public static final int MSG_FILE_SAVE_COMPLETE = 2;
        public static final int MSG_BUFFER_STATUS = 3;


        private WeakReference<DoorControllerCamera> mWeakActivity;

        public MainHandler(DoorControllerCamera activity) {
            mWeakActivity = new WeakReference<DoorControllerCamera>(activity);
        }

        // CircularEncoder.Callback, called on encoder thread
        @Override
        public void fileSaveComplete(int status) {
            sendMessage(obtainMessage(MSG_FILE_SAVE_COMPLETE, status, 0, null));
        }

        // CircularEncoder.Callback, called on encoder thread
        @Override
        public void bufferStatus(long totalTimeMsec) {
            sendMessage(obtainMessage(MSG_BUFFER_STATUS,
                    (int) (totalTimeMsec >> 32), (int) totalTimeMsec));
        }


        @Override
        public void handleMessage(Message msg) {
            DoorControllerCamera activity = mWeakActivity.get();
            if (activity == null) {
                Log.d(TAG, "Got message for dead activity");
                return;
            }

            //handle the red message to display information to the user
            switch (msg.what) {
                case MSG_BLINK_TEXT: {
                    TextView tv = (TextView) activity.findViewById(R.id.recording_text);

                    // Attempting to make it blink by using setEnabled() doesn't work --
                    // it just changes the color.  We want to change the visibility.
                    int visibility = tv.getVisibility();
                    if (visibility == View.VISIBLE) {
                        visibility = View.INVISIBLE;
                    } else {
                        visibility = View.VISIBLE;
                    }
                    tv.setVisibility(visibility);

                    int delay = (visibility == View.VISIBLE) ? 1000 : 200;
                    sendEmptyMessageDelayed(MSG_BLINK_TEXT, delay);
                    break;
                }
                case MSG_FRAME_AVAILABLE: {
                    activity.drawFrame();
                    break;
                }
                case MSG_FILE_SAVE_COMPLETE: {
                    activity.fileSaveComplete(msg.arg1);
                    break;
                }
                case MSG_BUFFER_STATUS: {
                    long duration = (((long) msg.arg1) << 32) |
                            (((long) msg.arg2) & 0xffffffffL);
                    activity.updateBufferStatus(duration);
                    break;
                }
                default:
                    throw new RuntimeException("Unknown message " + msg.what);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_controller_camera);

        i = getIntent();

        //get the current node of the missions
        competitionName= i.getStringExtra("competition");
        disciplineName = i.getStringExtra("discipline");
        missionName = i.getStringExtra("missionName");
        location = i.getStringExtra("location");

        //found the progressbar
        pb = (ProgressBar) findViewById(R.id.progressBarCamera);
        pb.setVisibility(ProgressBar.INVISIBLE);

        SurfaceView sv = (SurfaceView) findViewById(R.id.continuousCapture_surfaceView);
        SurfaceHolder sh = sv.getHolder();

        sh.addCallback(this);

        result = new ResultEntity();

        mHandler = new MainHandler(this);
        mHandler.sendEmptyMessageDelayed(MainHandler.MSG_BLINK_TEXT, 1500);

        //mOutputFile = new File(getFilesDir(), "temp");
        mSecondsOfVideo = 0.0f;
        updateControls();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionHelper.hasCameraPermission(this)) {
            PermissionHelper.requestCameraPermission(this, false);
        } else  {
            if (mCamera == null) {
                // Ideally, the frames from the camera are at the same resolution as the input to
                // the video encoder so we don't have to scale.
                openCamera(VIDEO_WIDTH, VIDEO_HEIGHT, DESIRED_PREVIEW_FPS);
            }
            if (mEglCore != null) {
                startPreview();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();

        if (mCircEncoder != null) {
            mCircEncoder.shutdown();
            mCircEncoder = null;
        }
        if (mCameraTexture != null) {
            mCameraTexture.release();
            mCameraTexture = null;
        }
        if (mDisplaySurface != null) {
            mDisplaySurface.release();
            mDisplaySurface = null;
        }
        if (mFullFrameBlit != null) {
            mFullFrameBlit.release(false);
            mFullFrameBlit = null;
        }
        if (mEglCore != null) {
            mEglCore.release();
            mEglCore = null;
        }
        Log.d(TAG, "onPause() done");
    }

    /**
     * Opens a camera, and attempts to establish preview mode at the specified width and height.
     * <p>
     * Sets mCameraPreviewFps to the expected frame rate (which might actually be variable).
     */
    private void openCamera(int desiredWidth, int desiredHeight, int desiredFps) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized");
        }


        Camera.CameraInfo info = new Camera.CameraInfo();

        // Try to find a front-facing camera (e.g. for videoconferencing).
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(i);
                break;
            }
        }
        if (mCamera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            mCamera = Camera.open();    // opens first back-facing camera
        }
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        Camera.Parameters parms = mCamera.getParameters();

        CameraUtils.choosePreviewSize(parms, desiredWidth, desiredHeight);

        // Try to set the frame rate to a constant value.
        mCameraPreviewThousandFps = CameraUtils.chooseFixedPreviewFps(parms, desiredFps * 1000);

        // Give the camera a hint that we're recording video.  This can have a big
        // impact on frame rate.
        parms.setRecordingHint(true);

        mCamera.setParameters(parms);

        Camera.Size cameraPreviewSize = parms.getPreviewSize();
        String previewFacts = cameraPreviewSize.width + "x" + cameraPreviewSize.height +
                " @" + (mCameraPreviewThousandFps / 1000.0f) + "fps";
        Log.i(TAG, "Camera config: " + previewFacts);

        AspectFrameLayout layout = (AspectFrameLayout) findViewById(R.id.continuousCapture_afl);

        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        // the orientation is set inside the manifest, we should delete those parts
        //TODO: seems that something is wrong here
        if(display.getRotation() == Surface.ROTATION_0) {
            mCamera.setDisplayOrientation(90);
            layout.setAspectRatio((double) cameraPreviewSize.height / cameraPreviewSize.width);
        } else if(display.getRotation() == Surface.ROTATION_270) {
            layout.setAspectRatio((double) cameraPreviewSize.height / cameraPreviewSize.width);
            mCamera.setDisplayOrientation(180);
        } else {
            // Set the preview aspect ratio.
            layout.setAspectRatio((double) cameraPreviewSize.width / cameraPreviewSize.height);
        }

    }

    /**
     * Stops camera preview, and releases the camera to the system.
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.d(TAG, "releaseCamera -- done");
        }
    }

    /**
     * Updates the current state of the controls.
     */
    private void updateControls() {
        String str = getString(R.string.secondsOfVideo, mSecondsOfVideo);
        TextView tv = (TextView) findViewById(R.id.capturedVideoDesc_text);
        tv.setText(str);

        boolean wantEnabled = (mCircEncoder != null) && !mFileSaveInProgress;
        ImageButton button = (ImageButton) findViewById(R.id.capture_button);
        if (button.isEnabled() != wantEnabled) {
            Log.d(TAG, "setting enabled = " + wantEnabled);
            button.setEnabled(wantEnabled);
        }
    }

    /**
     * Handles onClick for "capture" button.
     */
    public void clickCapture(@SuppressWarnings("unused") View unused) {
        Log.d(TAG, "capture");
        if (mFileSaveInProgress) {
            Log.w(TAG, "HEY: file save is already in progress");
            return;
        }

        // The button is disabled in onCreate(), and not enabled until the encoder and output
        // surface is ready, so it shouldn't be possible to get here with a null mCircEncoder.
        mFileSaveInProgress = true;
        updateControls();
        TextView tv = (TextView) findViewById(R.id.recording_text);
        String str = getString(R.string.nowSaving);
        tv.setText(str);


        // alert builder logic
        //build dialog for the comment
        AlertDialog.Builder builder = new AlertDialog.Builder(DoorControllerCamera.this, R.style.AlertDialog);

        builder.setMessage(R.string.enterNumberCamera);

        // Set an EditText view to get user input
        final EditText input = new EditText(DoorControllerCamera.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //Do nothing here because we override this button later to change the close behaviour.
                //However, we still need this because on older versions of Android unless we
                //pass a handler the button doesn't get instantiated
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                fileSaveComplete(-1);
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!input.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "saving the file ", Toast.LENGTH_LONG).show();

                    result.setBibNumber(Integer.parseInt(input.getText().toString()));
                    File tempVideo = new  File(getExternalFilesDir(null),competitionName+"_"+disciplineName+"_"+location+"_"+input.getText()+"_"
                            +DateFormat.format("dd-MM-yy hh:mm", Calendar.getInstance().getTime()).toString()+".mp4");
                    result.setCameraLink(tempVideo.getPath());
                    mCircEncoder.saveVideo(tempVideo);
                    pb.setVisibility(ProgressBar.VISIBLE);
                    dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.enterNumberCamera, Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    /**
     * The file save has completed.  We can resume recording.
     */
    private void fileSaveComplete(int status) {
        Log.d(TAG, "fileSaveComplete " + status);
        if (!mFileSaveInProgress) {
            throw new RuntimeException("WEIRD: got fileSaveCmplete when not in progress");
        }
        mFileSaveInProgress = false;
        updateControls();
        TextView tv = (TextView) findViewById(R.id.recording_text);
        String str = getString(R.string.nowRecording);
        tv.setText(str);

        if (status == 0) {
            str = getString(R.string.recordingSucceeded);
            FirebaseResultManager.updateResult(competitionName, disciplineName, missionName, result);
            pb.setVisibility(ProgressBar.INVISIBLE);
        } else {
            str = getString(R.string.recordingFailed, status);
        }
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Updates the buffer status UI.
     */
    private void updateBufferStatus(long durationUsec) {
        mSecondsOfVideo = durationUsec / 1000000.0f;
        updateControls();
    }


    @Override   // SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated holder=" + holder);

        // Set up everything that requires an EGL context.
        //
        // We had to wait until we had a surface because you can't make an EGL context current
        // without one, and creating a temporary 1x1 pbuffer is a waste of time.
        //
        // The display surface that we use for the SurfaceView, and the encoder surface we
        // use for video, use the same EGL context.
        mEglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);
        mDisplaySurface = new WindowSurface(mEglCore, holder.getSurface(), false);
        mDisplaySurface.makeCurrent();

        mFullFrameBlit = new FullFrameRect(
                new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));
        mTextureId = mFullFrameBlit.createTextureObject();
        mCameraTexture = new SurfaceTexture(mTextureId);
        mCameraTexture.setOnFrameAvailableListener(this);

        startPreview();
    }

    private void startPreview() {
        if (mCamera != null) {
            Log.d(TAG, "starting camera preview");
            try {
                mCamera.setPreviewTexture(mCameraTexture);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
            mCamera.startPreview();
        }

        // TODO: adjust bit rate based on frame rate?
        // TODO: adjust video width/height based on what we're getting from the camera preview?
        // TODO: need to fix the desired spansec
        //       (can we guarantee that camera preview size is compatible with AVC video encoder?)
        try {
            mCircEncoder = new CircularEncoder(VIDEO_WIDTH, VIDEO_HEIGHT, 6000000,
                    mCameraPreviewThousandFps / 1000, 25, mHandler);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        mEncoderSurface = new WindowSurface(mEglCore, mCircEncoder.getInputSurface(), true);

        updateControls();
    }

    @Override   // SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged fmt=" + format + " size=" + width + "x" + height +
                " holder=" + holder);
    }

    @Override   // SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed holder=" + holder);
    }

    @Override   // SurfaceTexture.OnFrameAvailableListener; runs on arbitrary thread
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        //Log.d(TAG, "frame available");
        mHandler.sendEmptyMessage(MainHandler.MSG_FRAME_AVAILABLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!PermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this,
                    "Camera permission is needed to run this application", Toast.LENGTH_LONG).show();
            PermissionHelper.launchPermissionSettings(this);
            finish();
        } else {
            openCamera(VIDEO_WIDTH, VIDEO_HEIGHT, DESIRED_PREVIEW_FPS);
        }
    }

    /**
     * Draws a frame onto the SurfaceView and the encoder surface.
     * <p>
     * This will be called whenever we get a new preview frame from the camera.  This runs
     * on the UI thread, which ordinarily isn't a great idea -- you really want heavy work
     * to be on a different thread -- but we're really just throwing a few things at the GPU.
     * The upside is that we don't have to worry about managing state changes between threads.
     * <p>
     * If there was a pending frame available notification when we shut down, we might get
     * here after onPause().
     */
    private void drawFrame() {
        //Log.d(TAG, "drawFrame");
        if (mEglCore == null) {
            Log.d(TAG, "Skipping drawFrame after shutdown");
            return;
        }

        // Latch the next frame from the camera.
        mDisplaySurface.makeCurrent();
        mCameraTexture.updateTexImage();
        mCameraTexture.getTransformMatrix(mTmpMatrix);

        // Fill the SurfaceView with it.
        SurfaceView sv = (SurfaceView) findViewById(R.id.continuousCapture_surfaceView);
        int viewWidth = sv.getWidth();
        int viewHeight = sv.getHeight();
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mFullFrameBlit.drawFrame(mTextureId, mTmpMatrix);
       // drawExtra(mFrameNum, viewWidth, viewHeight);
        mDisplaySurface.swapBuffers();

        // Send it to the video encoder.
        if (!mFileSaveInProgress) {
            mEncoderSurface.makeCurrent();
            GLES20.glViewport(0, 0, VIDEO_WIDTH, VIDEO_HEIGHT);
            mFullFrameBlit.drawFrame(mTextureId, mTmpMatrix);
            //drawExtra(mFrameNum, VIDEO_WIDTH, VIDEO_HEIGHT);
            mCircEncoder.frameAvailableSoon();
            mEncoderSurface.setPresentationTime(mCameraTexture.getTimestamp());
            mEncoderSurface.swapBuffers();
        }

        mFrameNum++;
    }

    /**
     * Adds a bit of extra stuff to the display just to give it flavor.
     *
    private static void drawExtra(int frameNum, int width, int height) {
        // We "draw" with the scissor rect and clear calls.  Note this uses window coordinates.
        int val = frameNum % 3;
        switch (val) {
            case 0:  GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);   break;
            case 1:  GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);   break;
            case 2:  GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);   break;
        }

        int xpos = (int) (width * ((frameNum % 100) / 100.0f));
        GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(xpos, 0, width / 32, height / 32);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
    }*/
}
