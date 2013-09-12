package com.gorgeous.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

import com.gorgeous.R;
import com.gorgeous.activity.Loge;

public class PolaroidView extends View {

	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;

	private int viewWidth;
	private int viewHeight;

	public PolaroidView(Context context, int width, int height) {
		super(context);
		viewWidth = width;
		viewHeight = height;

		mBitmap = Bitmap.createBitmap(viewWidth, viewHeight,
				Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mCanvas.save();
		drawOutline();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmap != null)
			canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	private void drawOutline() {
		Loge.i("drawOutline");
		mCanvas.setBitmap(mBitmap);
		mCanvas.drawColor(R.color.transparent);
		mCanvas.drawARGB(0xff, 0xff, 0xfb, 0xe5);
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth(6);
		mPaint.setAlpha(200);
		mCanvas.drawLine(0, 0, 0, viewHeight, mPaint);
		mCanvas.drawLine(0, viewHeight, viewWidth, viewHeight, mPaint);
		mCanvas.drawLine(viewWidth, viewHeight, viewWidth, 0, mPaint);
		mCanvas.drawLine(viewWidth, 0, 0, 0, mPaint);
		mPaint.setStrokeWidth(3);
		mCanvas.drawLine(25 - 3 / 2, 25 - 3 / 2, 25, viewWidth - 25 + 3, mPaint);
		mCanvas.drawLine(25, viewWidth - 25 + 3 / 2, viewWidth - 25 + 3,
				viewWidth - 25 + 3 / 2, mPaint);
		mCanvas.drawLine(viewWidth - 25 + 3 / 2, viewWidth - 25 + 3 / 2,
				viewWidth - 25 + 3 / 2, 25, mPaint);
		mCanvas.drawLine(viewWidth - 25 + 3, 25 - 3 / 2, 25 - 3 / 2,
				25 - 3 / 2, mPaint);
		invalidate();
	}

	public void setScrText(String text) {
		Loge.i("setScrText: " + text);

		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(255);
		mPaint.setStrokeWidth(50);
		mCanvas.drawLine(30, viewWidth + 15, viewWidth - 3, viewWidth + 15,
				mPaint);

		mPaint = new Paint();
		mPaint.setTextSize(45);
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC);
		mPaint.setTypeface(font);
		mPaint.setColor(Color.BLACK);
		mPaint.setAntiAlias(true);
		mCanvas.drawText(text, 40, viewWidth + 30, mPaint);

		invalidate();
	}

	public Bitmap getBitmap() {
		mCanvas.drawBitmap(mBitmap, 0, 0, null);
		return mBitmap;
	}

}
