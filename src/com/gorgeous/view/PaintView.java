package com.gorgeous.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gorgeous.R;
import com.gorgeous.activity.Loge;

public class PaintView extends View {

	public static int TARGET_PATTERN_WIDTH = 60;

	public static final int DRAW_LINE = 1310;
	public static final int DRAW_PATTERN = 1311;

	private int curColor = Color.BLACK;
	private float curWidth;
	private float halfCurWidth;

	private Paint bitmapPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint paint = new Paint();
	private Path path = new Path();

	/**
	 * Optimizes painting by invalidating the smallest possible area.
	 */
	private float lastTouchX;
	private float lastTouchY;
	private final RectF dirtyRect = new RectF();

	private int mPictureHeight;
	private int mPictureWidth;

	private Canvas mCanvas;
	private Bitmap mBitmap;

	private int mPattern;

	private float formerTouchX;
	private float formerTouchY;
	private float patternGap;
	private Bitmap mPatternBitmap;

	private Context mCtx;

	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCtx = context;

		mPattern = DRAW_LINE;
		TARGET_PATTERN_WIDTH = mCtx.getResources().getDisplayMetrics().widthPixels / 20;
		Loge.i("TARGET_PATTERN_WIDTH = " + TARGET_PATTERN_WIDTH);
		patternGap = (float) Math.sqrt(TARGET_PATTERN_WIDTH
				* TARGET_PATTERN_WIDTH * 2);

		curWidth = 5f;
		halfCurWidth = 5f / 2;

		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(curWidth);

		circlePaint.setColor(Color.BLACK);
		circlePaint.setStrokeWidth(curWidth);
		circlePaint.setAntiAlias(true);

		bitmapPaint.setFilterBitmap(true);
	}

	/**
	 * Erases the signature.
	 */
	public void clear() {
		path.reset();
		// Repaints the entire view.
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mPictureWidth = w;
		mPictureHeight = h;

		Loge.i("mPictureWidth = " + mPictureWidth + " mPictureHeight = "
				+ mPictureHeight);
		if (mCanvas == null && mBitmap == null && mPictureWidth > 0
				&& mPictureHeight > 0) {
			mBitmap = Bitmap.createBitmap(mPictureWidth, mPictureHeight,
					Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			mCanvas.save();
		}
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mCanvas.restore();
		if (mPattern == DRAW_LINE) {
			mCanvas.drawPath(path, paint);
		}
		mCanvas.save();
		if (mBitmap != null)
			canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mPattern == DRAW_LINE) {
			return drawLines(event);
		} else if (mPattern == DRAW_PATTERN) {
			return drawPattern(event);
		}
		return false;
	}

	private boolean drawLines(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(eventX, eventY);
			lastTouchX = eventX;
			lastTouchY = eventY;
			// There is no end point yet, so don't waste cycles invalidating.

			// draw a point for the draw begin
			mCanvas.drawCircle(eventX, eventY,
					circlePaint.getStrokeWidth() / 2, circlePaint);
			return true;

		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			// Start tracking the dirty region.
			resetDirtyRect(eventX, eventY);

			// When the hardware tracks events faster than they are delivered,
			// the
			// event will contain a history of those skipped points.
			int historySize = event.getHistorySize();
			for (int i = 0; i < historySize; i++) {
				float historicalX = event.getHistoricalX(i);
				float historicalY = event.getHistoricalY(i);
				expandDirtyRect(historicalX, historicalY);
				path.lineTo(historicalX, historicalY);
			}

			// After replaying history, connect the line to the touch point.
			path.lineTo(eventX, eventY);

			// draw a point for the draw end to finish the path
			mCanvas.drawCircle(eventX, eventY,
					circlePaint.getStrokeWidth() / 2, circlePaint);
			break;

		default:
			Loge.i("Ignored touch event: " + event.toString());
			return false;
		}

		// Include half the stroke width to avoid clipping.
		invalidate((int) (dirtyRect.left - halfCurWidth),
				(int) (dirtyRect.top - halfCurWidth),
				(int) (dirtyRect.right + halfCurWidth),
				(int) (dirtyRect.bottom + halfCurWidth));

		lastTouchX = eventX;
		lastTouchY = eventY;

		return true;
	}

	private boolean drawPattern(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mCanvas.drawBitmap(mPatternBitmap, eventX, eventY, bitmapPaint);
			formerTouchX = eventX;
			formerTouchY = eventY;
			break;
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:

			if (formerTouchX > 0 && formerTouchY > 0) {
				float distace = distanceBetween2Point(eventX, eventY,
						formerTouchX, formerTouchY);
				if (patternGap < distace) {
					mCanvas.drawBitmap(mPatternBitmap, eventX, eventY,
							bitmapPaint);
					formerTouchX = eventX;
					formerTouchY = eventY;
				}
			}

			break;
		default:
			Loge.i("Ignored touch event: " + event.toString());
			return false;
		}
		invalidate();
		return true;
	}

	/**
	 * Called when replaying history to ensure the dirty region includes all
	 * points.
	 */
	private void expandDirtyRect(float historicalX, float historicalY) {
		if (historicalX < dirtyRect.left) {
			dirtyRect.left = historicalX;
		} else if (historicalX > dirtyRect.right) {
			dirtyRect.right = historicalX;
		}
		if (historicalY < dirtyRect.top) {
			dirtyRect.top = historicalY;
		} else if (historicalY > dirtyRect.bottom) {
			dirtyRect.bottom = historicalY;
		}
	}

	/**
	 * Resets the dirty region when the motion event occurs.
	 */
	private void resetDirtyRect(float eventX, float eventY) {

		// The lastTouchX and lastTouchY were set when the ACTION_DOWN
		// motion event occurred.
		dirtyRect.left = Math.min(lastTouchX, eventX);
		dirtyRect.right = Math.max(lastTouchX, eventX);
		dirtyRect.top = Math.min(lastTouchY, eventY);
		dirtyRect.bottom = Math.max(lastTouchY, eventY);
	}

	public void setPattern(int pattern) {
		mPattern = DRAW_PATTERN;
		if (mPatternBitmap != null) {
			mPatternBitmap.recycle();
		}
		mPatternBitmap = getBitmapFromResources(R.drawable.bird1);
	}

	private Bitmap getBitmapFromResources(int resId) {
		Resources res = mCtx.getResources();
		Bitmap origin = BitmapFactory.decodeResource(res, resId);
		if (origin.getWidth() > TARGET_PATTERN_WIDTH) {
			Bitmap finalRes = compressBitmap(origin);
			origin.recycle();
			return finalRes;
		}
		return origin;
	}

	private Bitmap compressBitmap(Bitmap originBitmap) {
		float sampleSize = TARGET_PATTERN_WIDTH
				/ (float) originBitmap.getWidth();
		Loge.i("sampleSize = " + sampleSize);
		if (sampleSize > 1) {
			sampleSize = 1;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(sampleSize, sampleSize);
		Bitmap resizeBmp = Bitmap
				.createBitmap(originBitmap, 0, 0, originBitmap.getWidth(),
						originBitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private float distanceBetween2Point(float x1, float y1, float x2, float y2) {
		float distance = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)
				* (y1 - y2));
		Loge.i("distanceBetween2Point = " + distance);
		return distance;
	}

	public void setPaint(int color, int width) {
		mPattern = DRAW_LINE;

		path = new Path();

		if (color != -2) {
			paint.setColor(color);
			circlePaint.setColor(color);
			curColor = color;
		} else {
			paint.setColor(curColor);
			circlePaint.setColor(curColor);
		}
		if (width != -2) {
			paint.setStrokeWidth(width);
			circlePaint.setStrokeWidth(width);
			curWidth = width;
		} else {
			paint.setStrokeWidth(curWidth);
			circlePaint.setStrokeWidth(curWidth);
		}
		if (color == Color.WHITE || curColor == Color.WHITE) {
			paint.setColor(Color.BLACK);
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));

			circlePaint.setColor(Color.BLACK);
			circlePaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		} else {
			paint.setXfermode(null);
			circlePaint.setXfermode(null);
		}
		halfCurWidth = curWidth / 2;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void dust() {
		path.reset();
		mBitmap.recycle();
		mBitmap = Bitmap.createBitmap(mPictureWidth, mPictureHeight,
				Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
		invalidate();
	}
}
