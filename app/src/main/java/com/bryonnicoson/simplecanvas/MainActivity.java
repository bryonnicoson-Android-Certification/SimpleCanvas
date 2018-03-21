package com.bryonnicoson.simplecanvas;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 *  The SimpleCanvas app demonstrates how to create and draw on a Canvas,
 *  and display the result in an ImageView.
 *
 *  When the user taps the screen, shapes and text are drawn.
 *  All the canvas interaction is implemented in the click handler,
 *  and you do not need a custom view.
 *
 *  Note:
 *  For simplicity, app does not use a custom view.
 *  This app does not teach you to draw.
 * */

public class MainActivity extends AppCompatActivity {

    // initial offset for rectangle
    private static final int OFFSET = 120;

    // multiplier for randomizing color
    private static final int MULTIPLIER = 100;

    // the canvas object stores information on WHAT to draw (lines, circles, text, custom paths...)
    private Canvas mCanvas;

    // the paint object stores HOW to draw (color, style, thickness, text size)
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);

    // the bitmap represents the pixels that will be displayed
    private Bitmap mBitmap;

    // the view is the container for the bitmap
    private ImageView mImageView;

    private Rect mRect = new Rect();
    // distance of rectangle from edge of canvas
    private int mOffset = OFFSET;
    // bounding box for text
    private Rect mBounds = new Rect();

    private int mColorBackground;
    private int mColorRectangle;
    private int mColorAccent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);

        mColorRectangle = ResourcesCompat.getColor(getResources(),
                R.color.colorRectangle, null);

        mColorAccent = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);

        // set properties of the paint used to draw on the canvas
        mPaint.setColor(mColorBackground);
        mPaintText.setColor(ResourcesCompat.getColor(getResources(),
                R.color.colorPrimaryDark, null));
        mPaintText.setTextSize(70);

        // get a reference to the imageview
        mImageView = findViewById(R.id.myimageview);

        // you cannot create a canvas in onCreate() because views have not been laid out,
        // so their final size is not available
    }

    /**
     * click handler that responds to user taps by drawing an increasingly smaller rectangle
     * until it runs out of room.  then it draws a circle with the text "Done!"
     *  1. create bitmap - each pixel takes 4 bytes with alpha channel - can use RGB_565 otherwise
     *  2. associate bitmap with view
     *  3. create canvas with bitmap
     *  4. draw on canvas
     *  5. invalidate view to force redraw
     *
     * @param view The view in which we are drawing
     */
    public void drawSomething(View view) {
        int vWidth = view.getWidth();
        int vHeight = view.getHeight();
        int halfWidth = vWidth / 2;
        int halfHeight = vHeight / 2;
        // only do this the first time view is clicked
        if (mOffset == OFFSET) {
            // 1
            mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
            // 2
            mImageView.setImageBitmap(mBitmap);
            // 3
            mCanvas = new Canvas(mBitmap);
            // 4
            mCanvas.drawColor(mColorBackground);
            mCanvas.drawText(getString(R.string.keep_tapping), 100, 100, mPaintText);
            // increase indent
            mOffset += OFFSET;
        } else {
            if (mOffset < halfWidth && mOffset < halfHeight) {
                // change the color by subtracting an integer
                mPaint.setColor(mColorRectangle - MULTIPLIER*mOffset);
                mRect.set(mOffset, mOffset, vWidth - mOffset, vHeight - mOffset);
                mCanvas.drawRect(mRect, mPaint);
                mOffset += OFFSET;
            } else {
                mPaint.setColor(mColorAccent);
                mCanvas.drawCircle(halfWidth, halfHeight, halfWidth / 3, mPaint);
                String text = getString(R.string.done);
                // get bounding box for text to calculate where to draw it
                mPaintText.getTextBounds(text, 0, text.length(), mBounds);
                // calculate the x and y to center text
                int x = halfWidth - mBounds.centerX();
                int y = halfHeight - mBounds.centerY();
                mCanvas.drawText(text, x, y, mPaintText);
            }
        }
        // 5
        view.invalidate();
    }
}
