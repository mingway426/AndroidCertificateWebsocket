package com.example.mingwaytestproject;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * 自定义时长的Toast
 *
 * @author DexYang
 *
 */
public class CToast {
	public static final int LENGTH_SHORT = 2000;
	private final Handler mHandler = new Handler();
	private int mDuration = LENGTH_SHORT;
	private int mGravity = Gravity.CENTER;
	private int mX, mY;
	private float mHorizontalMargin;
	private float mVerticalMargin;
	private View mView;
	private View mNextView;

	private WindowManager mWM;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

	private boolean isShowing = false;

	public CToast(Context context) {
		init(context);
	}

	/**
	 * Set the view to show.
	 *
	 * @see #getView
	 */
	public void setView(View view) {
		mNextView = view;
	}

	/**
	 * Return the view.
	 *
	 * @see #setView
	 */
	public View getView() {
		return mNextView;
	}

	/**
	 * Set how long to show the view for.
	 *
	 * @see #LENGTH_SHORT
	 */
	public void setDuration(int duration) {
		mDuration = duration;
	}

	/**
	 * Return the duration.
	 *
	 * @see #setDuration
	 */
	public int getDuration() {
		return mDuration;
	}

	/**
	 * Set the margins of the view.
	 *
	 * @param horizontalMargin
	 *            The horizontal margin, in percentage of the container width,
	 *            between the container's edges and the notification
	 * @param verticalMargin
	 *            The vertical margin, in percentage of the container height,
	 *            between the container's edges and the notification
	 */
	public void setMargin(float horizontalMargin, float verticalMargin) {
		mHorizontalMargin = horizontalMargin;
		mVerticalMargin = verticalMargin;
	}

	/**
	 * Return the horizontal margin.
	 */
	public float getHorizontalMargin() {
		return mHorizontalMargin;
	}

	/**
	 * Return the vertical margin.
	 */
	public float getVerticalMargin() {
		return mVerticalMargin;
	}

	/**
	 * Set the location at which the notification should appear on the screen.
	 *
	 * @see android.view.Gravity
	 * @see #getGravity
	 */
	public void setGravity(int gravity, int xOffset, int yOffset) {
		mGravity = gravity;
		mX = xOffset;
		mY = yOffset;
	}

	/**
	 * Get the location at which the notification should appear on the screen.
	 *
	 * @see android.view.Gravity
	 * @see #getGravity
	 */
	public int getGravity() {
		return mGravity;
	}

	/**
	 * Return the X offset in pixels to apply to the gravity's location.
	 */
	public int getXOffset() {
		return mX;
	}

	/**
	 * Return the Y offset in pixels to apply to the gravity's location.
	 */
	public int getYOffset() {
		return mY;
	}

	/**
	 * schedule handleShow into the right thread
	 */
	public void show() {
		mHandler.removeCallbacksAndMessages(null);
		mHandler.post(mShow);
		isShowing = true;

		/*if (mDuration > 0) {
			mHandler.postDelayed(mHide, mDuration);
		}*/
	}

	/**
	 * schedule handleHide into the right thread
	 */
	public void hide() {
		mHandler.removeCallbacksAndMessages(null);
		mHandler.post(mHide);
		isShowing = false;
	}

	private final Runnable mShow = new Runnable() {
		public void run() {
			handleShow();
		}
	};

	private final Runnable mHide = new Runnable() {
		public void run() {
			isShowing = false;
			handleHide();
		}
	};

	private void init(Context context) {
		final WindowManager.LayoutParams params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.windowAnimations = android.R.style.Animation_Toast;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");

		mWM = (WindowManager) context.getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
	}

	private void handleShow() {

		if (mView != mNextView) {
			// remove the old view if necessary
			handleHide();
			mView = mNextView;
			// mWM = WindowManagerImpl.getDefault();
			final int gravity = mGravity;
			mParams.gravity = gravity;
			if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
				mParams.horizontalWeight = 1.0f;
			}
			if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
				mParams.verticalWeight = 1.0f;
			}
			mParams.x = mX;
			mParams.y = mY;
			mParams.verticalMargin = mVerticalMargin;
			mParams.horizontalMargin = mHorizontalMargin;
			if (mView.getParent() != null) {
				mWM.removeView(mView);
			}
			mWM.addView(mView, mParams);
		}
		if (mDuration > 0) {
			mHandler.postDelayed(mHide, mDuration);
		}
	}

	private void handleHide() {
		if (mView != null) {
			if (mView.getParent() != null) {
				mWM.removeView(mView);
			}
			mView = null;
		}
	}

	public boolean isShowing(){
		return isShowing;
	}
}
