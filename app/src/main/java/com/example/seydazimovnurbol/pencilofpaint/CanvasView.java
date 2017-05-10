package com.example.seydazimovnurbol.pencilofpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

import static android.view.MotionEvent.*;
import static android.view.MotionEvent.ACTION_DOWN;

/**
 * Created by seydazimovnurbol on 4/7/17.
 */

public class CanvasView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    Context context;
    private Paint mPaint;
    private int paintWidth = 1;
    private int posX, posY;

    public int getCurrentColor() {
        return currentColor;
    }
    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
        mPaint.setColor(currentColor);
    }

    private int currentColor = Color.BLACK;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private int canvasWidth = -1;
    private int canvasHeight = -1;
    private boolean[][] used = new boolean[1111][1111];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createCanvas(w, h);
        canvasWidth = w;
        canvasHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }

    private void startTouch(float x, float y) {

        posX = (int) x;
        posY = (int) y;
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, x, y);
            mX = x;
            mY = y;
        }

    }

    public void clearCanvas() {
        createCanvas(getWidth(), getHeight());
        invalidate();
    }
    private void upTouch() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startTouch(x, y);
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            moveTouch(x, y);
            invalidate();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            upTouch();
            invalidate();
        }

        return true;
    }

    public void setColorToCanvas(int x, int y) {
        System.out.println(x + " " + y);
        mBitmap.setPixel(x, y, Color.rgb(255, 0, 0));
        for (int i = -10; i <= 10; ++i)
            for (int j = -10; j <= 10; ++j) {
                mBitmap.setPixel(x + i, y + j, Color.rgb(255, 0, 0));
                System.out.println(x + i + " " + y + j);
            }
    }

    public int getColor(int x, int y) {
        return mBitmap.getPixel(x, y);
    }

    public void getColorFromCanvas(int x, int y) {
        int pixel = getColor(x, y);
        System.out.println(pixel);
        int redVal = Color.red(pixel);
        int greenVal = Color.green(pixel);
        int blueVal = Color.blue(pixel);
        System.out.println(redVal + " " + greenVal + " " + blueVal);
    }

    public void createCanvas(int w, int h) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mPath = new Path();
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(currentColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }


    public boolean inCanvas(int x, int y) {
      return (0 <= x && x < canvasWidth && 0 <= y && y < canvasHeight);
    }

    private int[] dx = {-1, 0, 1, 0};
    private int[] dy = {0, -1, 0, 1};
    private int[] queX = new int[2000111];
    private int[] queY = new int[2000111];

    public void bfs(int x, int y) {
        for(boolean row[]:used){
            Arrays.fill(row, false);
        }
        int curColor = getCurrentColor();
        int redVal = Color.red(curColor);
        int greenVal = Color.green(curColor);
        int blueVal = Color.blue(curColor);


        System.out.println(curColor);
        System.out.println("before while " + x + " " + y);
        System.out.println("before while " + canvasWidth + " " + canvasHeight);

        int head = 0, tail = 1;
        queX[tail] = x;
        queY[tail] = y;
        used[x][y] = true;
        mBitmap.setPixel(x, y, Color.rgb(redVal, greenVal, blueVal));
        while (head < tail) {
            head++;
            int curX = queX[head];
            int curY = queY[head];
            for (int i = 0; i < 4; ++i) {
                int toX = dx[i] + curX;
                int toY = dy[i] + curY;
                if (inCanvas(toX, toY)) {
                    if (!used[toX][toY]) {
                        int curPixel = getColor(toX, toY);
                        if (curPixel == 0) {
                            used[toX][toY] = true;
                            mBitmap.setPixel(toX, toY, Color.rgb(redVal, greenVal, blueVal));
                            ++tail;
                            queX[tail] = toX;
                            queY[tail] = toY;
                        }
                    }
                }
            }
        }
        Toast.makeText(getContext(), "Filling has been done", Toast.LENGTH_LONG).show();
    }


    public void fillShape() {
        bfs(posX, posY);
    }

    public void setWidth(int width) {
        this.paintWidth = width;
        mPaint.setStrokeWidth(4f * width);
    }
}
