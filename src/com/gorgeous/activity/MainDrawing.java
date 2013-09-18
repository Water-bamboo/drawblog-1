package com.gorgeous.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gorgeous.R;
import com.gorgeous.adapter.SnsAdapter;
import com.gorgeous.view.PaintView;
import com.gorgeous.view.Panel;

public class MainDrawing extends Activity {

	private final static int RESULT_CAMERA = 1231;
	private final static int RESULT_ALBUM = 1232;
	private final static int RESULT_CLIP = 1233;
	private final static int SAVE_DIALOG = 2335;
	private final static int NETWORK_DIALOG = 2336;
	private final static int anicatinoTime = 100;
	private final static String PATH_SD = "/mnt/sdcard/drawblog/";
	private final static String SHARE_PHOTO = "com.gorgeous.sharephoto";

	private String[] snsName = { "Sina", "Renren", "Tencent", "Instagram" };
	private final static int[] colorList = { Color.BLACK, Color.RED,
			Color.YELLOW, Color.BLUE, Color.GREEN, Color.CYAN };
	private final static int[] penList = { 5, 20, 35 };

	private Context mContext;
	private LayoutInflater mLayoutInflater;

	private ImageView blackImage, redImage, yellowImage, blueImage, greenImage,
			purpleImage, plusImage, penNormal, penSmall, penLarge, dustButton,
			earButton;
	private ImageButton carButton, galButton, shareButton;
	private FrameLayout drawFrameLayout;
	private PaintView paintView;
	private Panel mPanel;

	private int screenWidth;
	private int screenHeight;
	private int paintHeight;
	private ImageView photoImageView;

	private GridView snsGridView;
	private SnsAdapter snsGridAdapter;

	private GestureDetector mGestureDetector;

	private boolean sign = false;

	private static String filePath;
	private Bitmap finalB;

	private String mText = "";

	private AsyncTask<Void, Void, IOException> saveDataAsyncTask;

	private ScaleAnimation sAnimation = new ScaleAnimation(0.8f, 0.8f, 0.8f,
			0.8f, Animation.RELATIVE_TO_SELF, 0.8f, Animation.RELATIVE_TO_SELF,
			0.8f);

	int blackX, blackY, redX, redY, yellowX, yellowY, blueX, blueY, greenX,
			greenY, purpleY, purpleX, penSmallX, penSmallY, penNormalX,
			penNormalY, penLargeX, penLargeY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MainDrawing.this;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		blackX = this.getResources().getDimensionPixelSize(R.dimen.blackX);
		blackY = this.getResources().getDimensionPixelSize(R.dimen.blackY);

		redX = this.getResources().getDimensionPixelSize(R.dimen.redX);
		redY = this.getResources().getDimensionPixelSize(R.dimen.redY);

		yellowX = this.getResources().getDimensionPixelSize(R.dimen.yellowX);
		yellowY = this.getResources().getDimensionPixelSize(R.dimen.yellowY);

		blueX = this.getResources().getDimensionPixelSize(R.dimen.blueX);
		blueY = this.getResources().getDimensionPixelSize(R.dimen.blueY);

		greenX = this.getResources().getDimensionPixelSize(R.dimen.greenX);
		greenY = this.getResources().getDimensionPixelSize(R.dimen.greenY);

		purpleX = this.getResources().getDimensionPixelSize(R.dimen.purpleX);
		purpleY = this.getResources().getDimensionPixelSize(R.dimen.purpleY);

		penSmallX = this.getResources()
				.getDimensionPixelSize(R.dimen.penSmallX);
		penSmallY = this.getResources()
				.getDimensionPixelSize(R.dimen.penSmallY);

		penNormalX = this.getResources().getDimensionPixelSize(
				R.dimen.penNormalX);
		penNormalY = this.getResources().getDimensionPixelSize(
				R.dimen.penNormalY);

		penLargeX = this.getResources()
				.getDimensionPixelSize(R.dimen.penLargeX);
		penLargeY = this.getResources()
				.getDimensionPixelSize(R.dimen.penLargeY);

		initLayout();
	}

	private void initLayout() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		final float scale = mContext.getResources().getDisplayMetrics().density;
		paintHeight = screenHeight - (int) (75 * scale + 0.5f);
		Loge.i("paintHeight: " + paintHeight);

		setContentView(R.layout.main_draw);

		mPanel = (Panel) findViewById(R.id.top);
		mPanel.setVisibility(View.GONE);

		plusImage = (ImageView) findViewById(R.id.myplus);
		blackImage = (ImageView) findViewById(R.id.black_pen);
		redImage = (ImageView) findViewById(R.id.red_pen);
		yellowImage = (ImageView) findViewById(R.id.yellow_pen);
		blueImage = (ImageView) findViewById(R.id.blue_pen);
		greenImage = (ImageView) findViewById(R.id.green_pen);
		purpleImage = (ImageView) findViewById(R.id.purple_pen);
		penNormal = (ImageView) findViewById(R.id.normal);
		penSmall = (ImageView) findViewById(R.id.small);
		penLarge = (ImageView) findViewById(R.id.large);

		carButton = (ImageButton) findViewById(R.id.head_camera);
		galButton = (ImageButton) findViewById(R.id.head_gallary);
		shareButton = (ImageButton) findViewById(R.id.head_post);
		dustButton = (ImageView) findViewById(R.id.tool_dust);
		earButton = (ImageView) findViewById(R.id.tool_eraser);

		drawFrameLayout = (FrameLayout) findViewById(R.id.drawing_layout);

		photoImageView = new ImageView(mContext);
		photoImageView.setDrawingCacheEnabled(true);
		photoImageView.setBackgroundResource(R.drawable.outline);

		paintView = new PaintView(this, null);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				screenWidth, paintHeight);
		layoutParams.gravity = Gravity.TOP;

		drawFrameLayout.addView(photoImageView, layoutParams);
		drawFrameLayout.addView(paintView, layoutParams);

		sAnimation.setDuration(anicatinoTime);

		snsGridView = (GridView) findViewById(R.id.sns_gridview);
		snsGridAdapter = new SnsAdapter(getBaseContext(), snsName, screenWidth);
		snsGridView.setAdapter(snsGridAdapter);

		initAction();

	}

	private void initAction() {
		carButton.setOnClickListener(caClicked);
		galButton.setOnClickListener(gaClicked);
		shareButton.setOnClickListener(shareClicked);
		dustButton.setOnClickListener(dustClicked);
		earButton.setOnClickListener(earClicked);

		blackImage.setOnClickListener(blackClicked);
		redImage.setOnClickListener(redClicked);
		yellowImage.setOnClickListener(yellowClicked);
		blueImage.setOnClickListener(blueClicked);
		greenImage.setOnClickListener(greenClicked);
		purpleImage.setOnClickListener(purpleClicked);
		penSmall.setOnClickListener(smallClicked);
		penNormal.setOnClickListener(normalClicked);
		penLarge.setOnClickListener(largeClicked);

		mGestureDetector = new GestureDetector(mContext,
				new GestureDetector.OnGestureListener() {

					@Override
					public void onShowPress(MotionEvent e) {

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2,
							float distanceX, float distanceY) {
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {

					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						Loge.i("Grid onFling");
						if (velocityX > 5) {
							mPanel.setOpen(false, true);
							return true;
						}
						return false;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						return false;
					}

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return false;
					}
				});

		snsGridView.setOnTouchListener(gridTouched);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case RESULT_ALBUM:
			Loge.i("RESULT_ALBUM");
			clipPhoto(data.getData());
			break;
		case RESULT_CAMERA:
			Loge.i("RESULT_CAMERA");
			clipPhoto(data.getData());
			break;
		case RESULT_CLIP:
			Loge.i("RESULT_CLIP");
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Drawable d = new BitmapDrawable(photo);
				photoImageView.setBackgroundDrawable(d);
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Loge.i("onCreateDialog");
		switch (id) {
		case SAVE_DIALOG:
			View editView;
			editView = mLayoutInflater.inflate(R.layout.edit_layout, null);
			final EditText edit = (EditText) editView
					.findViewById(R.id.editView);
			edit.setHint(getResources().getString(R.string.share_hint));
			edit.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence paramCharSequence,
						int paramInt1, int paramInt2, int paramInt3) {
					mText = edit.getText().toString();
				}

				@Override
				public void beforeTextChanged(CharSequence paramCharSequence,
						int paramInt1, int paramInt2, int paramInt3) {
				}

				@Override
				public void afterTextChanged(Editable paramEditable) {
					mText = edit.getText().toString();
				}
			});
			return new AlertDialog.Builder(this)
					.setMessage(getResources().getString(R.string.share))
					.setView(editView)
					.setPositiveButton(
							getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setNegativeButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Bitmap photo = ((BitmapDrawable) photoImageView
											.getBackground()).getBitmap();
									Bitmap draw = paintView.getBitmap();
									finalB = overlay(photo, draw);
									getSaveDataTask().execute();
									// dialog.dismiss();
								}
							}).create();
		case NETWORK_DIALOG:
			return new AlertDialog.Builder(this)
					.setTitle(getResources().getString(R.string.network_error))
					.setMessage(
							getResources().getString(R.string.network_check))
					.setPositiveButton(
							getResources().getString(R.string.setting),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									final Intent i = new Intent(
											android.provider.Settings.ACTION_WIRELESS_SETTINGS);
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									mContext.startActivity(i);
								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create();

		}
		return null;
	}

	private OnClickListener shareClicked = new OnClickListener() {

		@Override
		public void onClick(View paramView) {
			showPlusAnim();
			showDialog(SAVE_DIALOG);
		}
	};

	private OnTouchListener gridTouched = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			mGestureDetector.onTouchEvent(event);
			return true;
		}
	};

	private OnClickListener caClicked = new OnClickListener() {

		@Override
		public void onClick(View paramView) {
			Loge.i("menu_camera");
			showPlusAnim();
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, RESULT_CAMERA);
		}
	};

	private OnClickListener gaClicked = new OnClickListener() {

		@Override
		public void onClick(View paramView) {
			Loge.i("menu_album");
			showPlusAnim();
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/jpeg");
			startActivityForResult(intent, RESULT_ALBUM);
		}
	};

	private OnClickListener earClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			earButton.startAnimation(sAnimation);
			showPlusAnim();
			paintView.setPaint(Color.WHITE, -2);

		}
	};

	private OnClickListener dustClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dustButton.startAnimation(sAnimation);
			showPlusAnim();
			paintView.dust();
		}
	};

	private OnClickListener blackClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			blackImage.startAnimation(sAnimation);
			paintView.setPaint(colorList[0], -2);
		}
	};

	private OnClickListener redClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			redImage.startAnimation(sAnimation);
			paintView.setPaint(colorList[1], -2);
		}
	};

	private OnClickListener yellowClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			yellowImage.startAnimation(sAnimation);
			paintView.setPaint(colorList[2], -2);
		}
	};

	private OnClickListener blueClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			blueImage.startAnimation(sAnimation);
			paintView.setPaint(colorList[3], -2);
		}
	};

	private OnClickListener greenClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			greenImage.startAnimation(sAnimation);
			paintView.setPaint(colorList[4], -2);
		}
	};

	private OnClickListener purpleClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			purpleImage.startAnimation(sAnimation);
			paintView.setPaint(colorList[5], -2);
		}
	};

	private OnClickListener smallClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			penSmall.startAnimation(sAnimation);
			paintView.setPaint(-2, penList[0]);
		}
	};

	private OnClickListener normalClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			penNormal.startAnimation(sAnimation);
			paintView.setPaint(-2, penList[1]);
		}
	};

	private OnClickListener largeClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			penLarge.startAnimation(sAnimation);
			paintView.setPaint(-2, penList[2]);
		}
	};

	public void clipPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", screenWidth);
		intent.putExtra("aspectY", paintHeight);
		intent.putExtra("outputX", screenWidth);
		intent.putExtra("outputY", paintHeight);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_CLIP);
	}

	public Bitmap overlay(Bitmap photo, Bitmap draw) {
		if (photo != null || draw != null) {
			Bitmap overlay = Bitmap.createBitmap(draw.getWidth(),
					draw.getHeight(), draw.getConfig());
			Canvas canvas = new Canvas(overlay);
			Bitmap sPhoto = Bitmap.createScaledBitmap(photo, draw.getWidth(),
					draw.getHeight(), true);
			canvas.setBitmap(overlay);
			canvas.drawBitmap(sPhoto, 0, 0, null);
			canvas.drawBitmap(draw, 0, 0, null);
			return overlay;
		}
		return null;
	}

	private AsyncTask<Void, Void, IOException> getSaveDataTask() {
		saveDataAsyncTask = new AsyncTask<Void, Void, IOException>() {

			protected void onPreExecute() {

			}

			@Override
			protected IOException doInBackground(Void... arg0) {
				return saveToSDcard(finalB);
			}

			@Override
			protected void onPostExecute(IOException result) {
				if (result == null) {
					if (!TextUtils.isEmpty(filePath)) {
						Loge.i("result ok: " + filePath);
						File file = new File(filePath);
						Uri path = Uri.fromFile(file);
						Intent shareIntent = new Intent();
						shareIntent.setAction(Intent.ACTION_SEND);
						if (!TextUtils.isEmpty(mText))
							shareIntent.putExtra(Intent.EXTRA_TEXT, mText);
						shareIntent.putExtra(Intent.EXTRA_STREAM, path);
						shareIntent.setType("image/jpeg");
						startActivity(shareIntent);
					}
				} else {
					Toast.makeText(mContext, "save date failed",
							Toast.LENGTH_SHORT).show();
				}
			}

		};
		return saveDataAsyncTask;
	};

	@SuppressLint("NewApi")
	public static IOException saveToSDcard(Bitmap photo) {
		long date = System.currentTimeMillis();
		String pname = DateFormat.format("yyyy-MM-dd-hh-mm-ss", date)
				.toString();
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)
				&& photo != null) {
			try {
				File file = null;
				String name = pname + ".jpg";
				File dir = new File(PATH_SD, "pic" + File.separator);
				dir.mkdirs();
				file = new File(dir, name);
				file.setWritable(true);
				file.createNewFile();
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(file);
				photo.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
				filePath = file.getAbsolutePath();
			} catch (IOException exe) {
				return exe;
			}
		} else {
			return new IOException();
		}
		return null;
	}

	public void showPlusAnim() {
		if (sign) {
			sign = false;
			inAnimation();
		}
	}

	public void pAnimation(View view) {

		showRotateAnimation(sign);

		if (!sign) {
			sign = true;
			outAnimation();
		} else {
			sign = false;
			inAnimation();
		}

	}

	public void outAnimation() {

		TranslateAnimation translateAnimation = new TranslateAnimation(0,
				blackX, 0, blackY);
		translateAnimation.setInterpolator(new OvershootInterpolator());
		translateAnimation.setDuration(anicatinoTime * 1 + 240);
		translateAnimation.setFillEnabled(true);
		translateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(blackX, 0, 0, blackY);
				blackImage.setLayoutParams(layoutParams);
			}
		});
		blackImage.startAnimation(translateAnimation);

		TranslateAnimation translateAnimation1 = new TranslateAnimation(0,
				redX, 0, -redY);
		translateAnimation1.setInterpolator(new OvershootInterpolator());
		translateAnimation1.setDuration(anicatinoTime * 1 + 180);
		translateAnimation1.setStartOffset(20);
		translateAnimation1.setFillEnabled(true);
		translateAnimation1.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(redX, 0, 0, redY);
				redImage.setLayoutParams(layoutParams);
			}
		});
		redImage.startAnimation(translateAnimation1);

		TranslateAnimation translateAnimation2 = new TranslateAnimation(0,
				yellowX, 0, -yellowY);
		translateAnimation2.setInterpolator(new OvershootInterpolator());
		translateAnimation2.setDuration(anicatinoTime * 1 + 120);
		translateAnimation2.setStartOffset(40);
		translateAnimation2.setFillEnabled(true);
		translateAnimation2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(yellowX, 0, 0, yellowY);
				yellowImage.setLayoutParams(layoutParams);
			}
		});
		yellowImage.startAnimation(translateAnimation2);

		TranslateAnimation translateAnimation3 = new TranslateAnimation(0,
				blueX, 0, -blueY);
		translateAnimation3.setInterpolator(new OvershootInterpolator());
		translateAnimation3.setDuration(anicatinoTime * 1 + 80);
		translateAnimation3.setStartOffset(60);
		translateAnimation3.setFillEnabled(true);
		translateAnimation3.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(blueX, 0, 0, blueY);
				blueImage.setLayoutParams(layoutParams);
			}
		});
		blueImage.startAnimation(translateAnimation3);

		TranslateAnimation translateAnimation4 = new TranslateAnimation(0,
				greenX, 0, -greenY);
		translateAnimation4.setInterpolator(new OvershootInterpolator());
		translateAnimation4.setDuration(anicatinoTime * 1 + 40);
		translateAnimation4.setStartOffset(80);
		translateAnimation4.setFillEnabled(true);
		translateAnimation4.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(greenX, 0, 0, greenY);
				greenImage.setLayoutParams(layoutParams);
			}
		});
		greenImage.startAnimation(translateAnimation4);

		TranslateAnimation translateAnimation5 = new TranslateAnimation(0,
				purpleX, 0, -purpleY);
		translateAnimation5.setInterpolator(new OvershootInterpolator());
		translateAnimation5.setDuration(anicatinoTime * 1);
		translateAnimation5.setStartOffset(100);
		translateAnimation5.setFillEnabled(true);
		translateAnimation5.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(purpleX, 0, 0, purpleY);
				purpleImage.setLayoutParams(layoutParams);
			}
		});
		purpleImage.startAnimation(translateAnimation5);

		TranslateAnimation translateAnimation6 = new TranslateAnimation(0,
				penSmallX, 0, -penSmallY);
		translateAnimation6.setInterpolator(new OvershootInterpolator());
		translateAnimation6.setDuration(anicatinoTime * 1);
		translateAnimation6.setStartOffset(40);
		translateAnimation6.setFillEnabled(true);
		translateAnimation6.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(penSmallX, 0, 0, penSmallY);
				penSmall.setLayoutParams(layoutParams);
			}
		});
		penSmall.startAnimation(translateAnimation6);

		TranslateAnimation translateAnimation7 = new TranslateAnimation(0,
				penNormalX, 0, -penNormalY);
		translateAnimation7.setInterpolator(new OvershootInterpolator());
		translateAnimation7.setDuration(anicatinoTime * 1 + 260);
		translateAnimation7.setStartOffset(60);
		translateAnimation7.setFillEnabled(true);
		translateAnimation7.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(penNormalX, 0, 0, penNormalY);
				penNormal.setLayoutParams(layoutParams);
			}
		});
		penNormal.startAnimation(translateAnimation7);

		TranslateAnimation translateAnimation8 = new TranslateAnimation(0,
				penLargeX, 0, -penLargeY);
		translateAnimation8.setInterpolator(new OvershootInterpolator());
		translateAnimation8.setDuration(anicatinoTime * 1 + 300);
		translateAnimation8.setStartOffset(80);
		translateAnimation8.setFillEnabled(true);
		translateAnimation8.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				layoutParams.gravity = Gravity.BOTTOM;
				layoutParams.setMargins(penLargeX, 0, 0, penLargeY);
				penLarge.setLayoutParams(layoutParams);
			}
		});
		penLarge.startAnimation(translateAnimation8);
	}

	public void inAnimation() {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.BOTTOM;
		layoutParams.setMargins(0, 0, 0, 0);
		blackImage.setLayoutParams(layoutParams);
		redImage.setLayoutParams(layoutParams);
		yellowImage.setLayoutParams(layoutParams);
		blueImage.setLayoutParams(layoutParams);
		greenImage.setLayoutParams(layoutParams);
		purpleImage.setLayoutParams(layoutParams);
		penNormal.setLayoutParams(layoutParams);
		penSmall.setLayoutParams(layoutParams);
		penLarge.setLayoutParams(layoutParams);

		TranslateAnimation translateAnimation = new TranslateAnimation(blackX,
				0, 0, blackY);
		translateAnimation.setDuration(anicatinoTime * 1);
		translateAnimation.setFillEnabled(true);
		blackImage.startAnimation(translateAnimation);

		TranslateAnimation translateAnimation1 = new TranslateAnimation(redX,
				0, -redY, 0);

		translateAnimation1.setDuration(anicatinoTime * 1);
		translateAnimation1.setStartOffset(30);
		translateAnimation1.setFillEnabled(true);
		redImage.startAnimation(translateAnimation1);

		TranslateAnimation translateAnimation2 = new TranslateAnimation(
				yellowX, 0, -yellowY, 0);
		translateAnimation2.setDuration(anicatinoTime * 1);
		translateAnimation2.setStartOffset(60);
		translateAnimation2.setFillEnabled(true);
		yellowImage.startAnimation(translateAnimation2);

		TranslateAnimation translateAnimation3 = new TranslateAnimation(blueX,
				0, -blueY, 0);
		translateAnimation3.setDuration(anicatinoTime * 1);
		translateAnimation3.setStartOffset(90);
		translateAnimation3.setFillEnabled(true);
		blueImage.startAnimation(translateAnimation3);

		TranslateAnimation translateAnimation4 = new TranslateAnimation(greenX,
				0, -greenY, 0);
		translateAnimation4.setDuration(anicatinoTime * 1);
		translateAnimation4.setStartOffset(120);
		translateAnimation4.setFillEnabled(true);
		greenImage.startAnimation(translateAnimation4);

		TranslateAnimation translateAnimation5 = new TranslateAnimation(
				purpleX, 0, -purpleY, 0);
		translateAnimation5.setDuration(anicatinoTime * 1);
		translateAnimation5.setStartOffset(150);
		translateAnimation5.setFillEnabled(true);
		purpleImage.startAnimation(translateAnimation5);

		TranslateAnimation translateAnimation6 = new TranslateAnimation(
				penSmallX, 0, -penSmallY, 0);
		translateAnimation6.setInterpolator(new OvershootInterpolator());
		translateAnimation6.setDuration(anicatinoTime * 1);
		translateAnimation6.setStartOffset(30);
		translateAnimation6.setFillEnabled(true);
		penSmall.startAnimation(translateAnimation6);

		TranslateAnimation translateAnimation7 = new TranslateAnimation(
				penNormalX, 0, -penNormalY, 0);
		translateAnimation7.setInterpolator(new OvershootInterpolator());
		translateAnimation7.setDuration(anicatinoTime * 1);
		translateAnimation7.setStartOffset(60);
		translateAnimation7.setFillEnabled(true);
		penNormal.startAnimation(translateAnimation7);

		TranslateAnimation translateAnimation8 = new TranslateAnimation(
				penLargeX, 0, -penLargeY, 0);
		translateAnimation8.setInterpolator(new OvershootInterpolator());
		translateAnimation8.setDuration(anicatinoTime * 1);
		translateAnimation8.setStartOffset(90);
		translateAnimation8.setFillEnabled(true);
		penLarge.startAnimation(translateAnimation8);
	}

	public void showRotateAnimation(boolean sign) {

		final float centerX = plusImage.getWidth() / 2.0f;
		final float centerY = plusImage.getHeight() / 2.0f;
		RotateAnimation rotateAnimation = null;
		if (!sign) {
			rotateAnimation = new RotateAnimation(0, 405, centerX, centerY);

		} else {
			rotateAnimation = new RotateAnimation(405, 0, centerX, centerY);
		}
		rotateAnimation.setDuration(anicatinoTime + 220);
		rotateAnimation.setFillAfter(true);
		plusImage.startAnimation(rotateAnimation);
	}

}
