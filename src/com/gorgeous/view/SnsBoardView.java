package com.gorgeous.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.gorgeous.activity.Loge;

public class SnsBoardView extends View {

	private Bitmap mSrcPic;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Paint mPaint;

	private int viewWidth = 200;
	private int viewHeight = 200;
	private int footHeight;
	private int mColor;
	private Handler mHandler;

	private String mText = "";
	GestureDetector mGestureDetector;

	public SnsBoardView(Context context, int width, int height) {
		super(context);
		Loge.i("new SnsBoardView" + this.getHeight());
		initView(width, height);
		mHandler = new Handler();
		mGestureDetector = new GestureDetector(context,
				new GestureDetector.OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						Loge.i("onSingleTapUp");
						drawOutline();
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								cleanOutline();
							}
						}, 100);
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						Loge.i("onScroll");
						cleanOutline();
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						drawOutline();
						mHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								cleanOutline();
							}
						}, 100);
						mHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								drawOutline();
							}
						}, 200);
						mHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								cleanOutline();
							}
						}, 300);

					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						Loge.i("onFling");
						cleanOutline();
						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						return false;
					}
				});
	}

	private void initView(int width, int height) {
		viewWidth = width;
		viewHeight = height;
		footHeight = (int) height / 5;

		mBitmap = Bitmap.createBitmap(viewWidth, viewHeight,
				Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		mCanvas.drawColor(Color.RED);
		mCanvas.save();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mBitmap != null)
			canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return true;
	}

	private void drawOutline() {
		Loge.i("drawOutline");
		mCanvas.setBitmap(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(3);
		mPaint.setAlpha(255);
		mCanvas.drawLine(0, 0, 0, viewHeight, mPaint);
		mCanvas.drawLine(0, viewHeight, viewWidth, viewHeight, mPaint);
		mCanvas.drawLine(viewWidth, viewHeight, viewWidth, 0, mPaint);
		mCanvas.drawLine(viewWidth, 0, 0, 0, mPaint);
		invalidate();

	}

	private void cleanOutline() {
		Loge.i("cleanOutline");
		mCanvas.setBitmap(mBitmap);
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		mPaint.setStrokeWidth(3);
		mPaint.setAlpha(255);
		mCanvas.drawLine(0, 0, 0, viewHeight, mPaint);
		mCanvas.drawLine(0, viewHeight, viewWidth, viewHeight, mPaint);
		mCanvas.drawLine(viewWidth, viewHeight, viewWidth, 0, mPaint);
		mCanvas.drawLine(viewWidth, 0, 0, 0, mPaint);
		invalidate();
	}

	public void setScrText(String text) {
		mText = text;
		dust();
		mCanvas.setBitmap(mBitmap);
		if (mSrcPic != null) {
			mCanvas.drawBitmap(Bitmap.createScaledBitmap(mSrcPic, viewWidth,
					viewHeight, true), 0, 0, null);
		}
		mPaint = new Paint();
		if (mColor != 0) {
			mPaint.setColor(mColor);
		} else {
			mPaint.setColor(Color.RED);
		}
		mPaint.setStrokeWidth(footHeight);
		mPaint.setAlpha(120);
		mCanvas.drawLine(0, viewHeight - footHeight / 2, viewWidth - 0,
				viewHeight - footHeight / 2, mPaint);

		mPaint = new Paint();
		mPaint.setTextSize(footHeight - 12);
		mPaint.setColor(Color.WHITE);
		mCanvas.drawText(text, 12, viewHeight - (footHeight - 16) / 2, mPaint);
		invalidate();
	}

	public void setFootColor(int color) {
		mCanvas.setBitmap(mBitmap);
		dust();
		if (mSrcPic != null) {
			mCanvas.drawBitmap(Bitmap.createScaledBitmap(mSrcPic, viewWidth,
					viewHeight, true), 0, 0, null);
		}
		mColor = color;

		mPaint = new Paint();
		mPaint.setColor(color);
		mPaint.setStrokeWidth(footHeight);
		mPaint.setAlpha(120);
		mCanvas.drawLine(0, viewHeight - footHeight / 2, viewWidth - 0,
				viewHeight - footHeight / 2, mPaint);

		mPaint = new Paint();
		mPaint.setTextSize(footHeight - 12);
		mPaint.setColor(Color.WHITE);
		mCanvas.drawText(mText, 12, viewHeight - (footHeight - 16) / 2, mPaint);
		invalidate();
	}

	public void setSrcPic(Bitmap src) {
		dust();
		mSrcPic = src;
		mCanvas.setBitmap(mBitmap);
		mCanvas.drawBitmap(
				Bitmap.createScaledBitmap(mSrcPic, viewWidth, viewHeight, true),
				0, 0, null);
		setScrText(mText);
	}

	public void dust() {
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		paint.setAntiAlias(true);
		paint.setAlpha(255);
		mCanvas.setBitmap(mBitmap);
		mCanvas.drawCircle(viewWidth / 2, viewWidth / 2, viewWidth, paint);
		invalidate();
	}

}
