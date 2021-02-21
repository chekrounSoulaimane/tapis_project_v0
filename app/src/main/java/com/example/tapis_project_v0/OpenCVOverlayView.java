package com.example.tapis_project_v0;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * The overlay view is responsible for displaying
 * information on top of the camera
 *
 * @author Michael Troger
 */
public class OpenCVOverlayView extends View {
    /**
     * class name for debugging with logcat
     */
    private static final String TAG = com.example.tapis_project_v0.OpenCVOverlayView.class.getName();
    /**
     * holds the rectangle image
     */
    private Drawable rectangle;
    /**
     * holds the triangle image
     */
    private Drawable triangle;
    /**
     * command by which the canvas should be changed
     * e.g. to switch the image
     */
    private String changingType;
    /**
     * the context
     */
    private Context mContext;
    /**
     * the sound of a bear
     */
    private MediaPlayer bearSound;
    /**
     * the sound of a chicken
     */
    private MediaPlayer chickenSound;

    private final Matrix mMatrix = new Matrix();
    protected int mCameraIndex = CAMERA_ID_ANY;
    public static final int CAMERA_ID_ANY = -1;
    public static final int CAMERA_ID_BACK = 99;
    public static final int CAMERA_ID_FRONT = 98;


    /**
     * creates an instance of the OverlayView
     *
     * @param context the context
     * @param attrs   the attributes
     */
    public OpenCVOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        // preload sounds and images
        loadSounds();
        loadImages();

        Log.d(TAG, "started :)");
    }

    /**
     * sounds are preloaded so that they must no be loaded in the main loop
     */
    private void loadSounds() {
        bearSound = MediaPlayer.create(mContext, R.raw.bear);
        chickenSound = MediaPlayer.create(mContext, R.raw.chicken);
    }

    /**
     * images are preloaded so that they must no be loaded in the main loop
     */
    private void loadImages() {
        rectangle = mContext.getResources().getDrawable(R.drawable.rectangle);
        triangle = mContext.getResources().getDrawable(R.drawable.triangle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (changingType != null) {
            switch (changingType) { // looking for the chosen command
                case "rectangle":
                    clearCanvas(canvas);
                    drawRectangle(canvas);
                    bearSound.start();
                    break;
                case "triangle":
                    clearCanvas(canvas);
                    drawTriangle(canvas);
                    chickenSound.start();
                    break;
                default:
            }
        }

    }

    private void drawRectangle(Canvas canvas) {
        Rect imageBounds = canvas.getClipBounds();
        rectangle.setBounds(imageBounds);
        rectangle.draw(canvas);
    }

    private void drawTriangle(Canvas canvas) {
        Rect imageBounds = canvas.getClipBounds();
        triangle.setBounds(imageBounds);
        triangle.draw(canvas);
    }

    private void clearCanvas(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    /**
     * change the canvas by given command
     *
     * @param changingType the command as String
     */
    public void changeCanvas(String changingType) {
        // force redraw with the given command
        // but only if same command has not been used before
        if (this.changingType == null
                || !this.changingType.equals(changingType)) {
            this.invalidate();
            this.changingType = changingType;
        }
    }

}