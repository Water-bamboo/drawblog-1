package com.gorgeous.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
	private Paint mPaint;
	private Bitmap mBitmap;
	private Bitmap srcBitmap;

	private int screenWidth;
	private int screenHeight;
	private int curColor;
	private int curWidth;

	private float touchX;
	private float touchY;

	Canvas canvas;

	public PaintView(Context context, int width, int height) {
		super(context);
		screenWidth = width;
		screenHeight = height;
		mPaint = new Paint();
		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		canvas = new Canvas(mBitmap);
		canvas.save();
	}

	public void setSrcPic(Bitmap src) {
		srcBitmap = src;
		drawPic(srcBitmap);
	}

	public void setPaint(int color, int width) {
		mPaint.reset();
		if (color != -2) {
			mPaint.setColor(color);
			curColor = color;
		} else {
			mPaint.setColor(curColor);
		}
		if (width != -2) {
			mPaint.setStrokeWidth(width);
			curWidth = width;
		} else {
			mPaint.setStrokeWidth(curWidth);
		}
		if (color == Color.WHITE || curColor == Color.WHITE) {
			mPaint.setColor(Color.BLACK);
			mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		}
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(255);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmap != null)
			canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = 0;
		float y = 0;
		x = event.getX();
		y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			reDraw(x, y, false);
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			reDraw(x, y, true);
		}

		return true;
	}

	private void reDraw(float x, float y, boolean move) {
		canvas.restore();
		if (move) {
			final float dx = (x - touchX) / 8;
			final float dy = (y - touchY) / 8;
			canvas.drawCircle(touchX, touchY, mPaint.getStrokeWidth() / 2,
					mPaint);

			for (int i = 1; i <= 8; i++) {
				canvas.drawCircle(touchX + i * dx, touchY + i * dy,
						mPaint.getStrokeWidth() / 2, mPaint);
				canvas.drawLine(touchX + (i - 1) * dx, touchY + (i - 1) * dy,
						touchX + i * dx, touchY + i * dy, mPaint);
			}

			touchX = x;
			touchY = y;
		} else {
			touchX = x;
			touchY = y;
			canvas.drawCircle(x, y, mPaint.getStrokeWidth() / 2, mPaint);
		}
		invalidate();
		canvas.save();
	}

	private void drawPic(Bitmap src) {
		canvas.setBitmap(mBitmap);
		if (src != null) {
			canvas.drawBitmap(src, 0, 0, null);
		} else {
			canvas.drawColor(Color.WHITE);
		}

	}

	public Bitmap getBitmap() {
		canvas.drawBitmap(mBitmap, 0, 0, null);
		return mBitmap;
	}

	public void dust() {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		paint.setAntiAlias(true);
		paint.setAlpha(255);
		canvas.setBitmap(mBitmap);
		canvas.drawCircle(screenHeight / 2, screenHeight / 2, screenHeight,
				paint);
		invalidate();
	}
}
