package common.function.sato.var2;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CommonScrollView extends View{

	//元の画像
	public Bitmap mSrcBitmap;
	private Resources mSrcRes;
	private int initWidth = 1000;
	private int initHeight = 1000;

	//表示用(View)資源
	public Bitmap mViewBitmap;
	private Paint  mViewPaint = new Paint();
	private int mViewWidth = 320;
	private int mViewHeight = 240;
	private int mViewPointX = 0;
	private int mViewPointY = 0;
	private boolean initViewSizeGet = false;

	//タッチした位置を示す属性
	private float currentDotX = 0;
	private float currentDotY = 0;
	private static final float TOUCH_TOLERANCE = 0;

	//コンテキスト
	private Context mContext;

	//拡大縮小用の属性
	private static final float MAX_SCALE = 5;
	private static final float MIN_SCALE = 0.5f;
	private static final float MIN_LENGTH = 30f;
	private float initZoomDistance = 0;

	//タッチの種類
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int touchMode = NONE;

	private float[] values = new float[9];	 /** MatrixのgetValues用 */
	private Matrix matrixRate = new Matrix();    /** マトリックス */
	private PointF zoomPoint = new PointF();    /** ズーム時の座標 */

	private static float MIN_WIDTH = 0;
	private static float MIN_HEIGHT = 0;
	private static float MAX_WIDTH = 2000;
	private static float MAX_HEIGHT = 2000;



	/**
	 * コンストラクタ
	 * @param context
	 * @param attrs
	 */
	public CommonScrollView(Context context, Bitmap image) {
		super(context);
		setFocusable(true);

		this.mContext = context;
		this.mSrcRes = this.mContext.getResources();
		this.mSrcBitmap = image;

	}

	public CommonScrollView(Context context) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(context);
		setFocusable(true);
		this.mContext = context;
		this.mSrcBitmap = Bitmap.createBitmap(initWidth, initHeight, Bitmap.Config.ARGB_8888);
		for(int x = 0;x < initWidth;x++)
			for(int y = 0;y < initHeight;y++)
				this.mSrcBitmap.setPixel(x, y, android.graphics.Color.GREEN);
	}


	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO 自動生成されたメソッド・スタブ
		super.onSizeChanged(w, h, oldw, oldh);
		Log.v("View", "onSizeChanged Width:" + w + ",Height:" + h );
		this.mViewWidth = w;
		this.mViewHeight = h;
		if(!initViewSizeGet){
			MIN_WIDTH = this.mViewWidth + 50;
			MIN_HEIGHT = this.mViewHeight + 50;
		}

		//画像を切り抜く
		this.mViewBitmap = Bitmap.createBitmap(this.mSrcBitmap, this.mViewPointX, this.mViewPointY, this.mViewWidth, this.mViewHeight);
	}

	/*
	 * 描画関数
	 * */
	@Override
	protected void onDraw(Canvas canvas) {
		/*
		 * canvas.drawColor(0xFFAAAAAA);
		 * canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		 */
		canvas.drawBitmap(mViewBitmap, 0, 0, mViewPaint);
	}


	/**
	 * タッチした最初の場所を取得するメソッド
	 * @param x
	 * @param y
	 */
	private void getTouchStart(float x, float y) {
		Log.v("View", "getTouchStart");
		this.currentDotX = x;
		this.currentDotY = y;
	}


	/**
	 * タッチ後のの移動量を取得するメソッド
	 * @param x
	 * @param y
	 */
	private void getTouchMove(float x, float y) {
		Log.d("View", "getTouchMove");
		float dx = Math.abs(x - this.currentDotX);
		float dy = Math.abs(y - this.currentDotY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			translateBitmap((int)(this.currentDotX - x), (int)(this.currentDotY - y), 1.0f);
			this.currentDotX = x;
			this.currentDotY = y;
		}
	}

	/**
	 * 指が離れた時のメソッド
	 * @param x
	 * @param y
	 */
	private void getTouchEnd(float x, float y) {
		Log.i("View", "getTouchEnd");
		initZoomDistance = 0;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("Action",  "ACTION_DOWN");
			getTouchStart(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("Action",  "ACTION_MOVE");
			getTouchMove(x, y);
			break;
		case MotionEvent.ACTION_UP:
			getTouchEnd(x, y);
			break;
		}

		return true;

	}

	/* 描画範囲の変更 */
	private boolean translateBitmap(int dx,int dy, float scale){
		Log.v("View", "translateBitmap");

		//現在位置の更新
		if( this.mViewPointX + dx > this.mSrcBitmap.getWidth() - this.mViewWidth ){
			mViewPointX = mSrcBitmap.getWidth() - mViewWidth;
		}else if( this.mViewPointX + dx < 0){
			this.mViewPointX = 0;
		}else{
			this.mViewPointX = this.mViewPointX + dx;
		}

		if( this.mViewPointY + dy > this.mSrcBitmap.getHeight() - this.mViewHeight ){
			this.mViewPointY = this.mSrcBitmap.getHeight() - this.mViewHeight;
		}else if( mViewPointY + dy < 0){
			this.mViewPointY = 0;
		}else{
			this.mViewPointY = this.mViewPointY + dy;
		}

		Log.v("ViewPoint", "(" +String.valueOf(mViewPointX) + "," + String.valueOf(mViewPointY) + ")");
		Log.v("ViewWH", "(" +String.valueOf(mViewWidth) + "," + String.valueOf(mViewHeight) + ")");

		//描画範囲を更新
		this.mViewBitmap = Bitmap.createBitmap(this.mSrcBitmap, this.mViewPointX, this.mViewPointY, this.mViewWidth, this.mViewHeight);
		//再描画の指示
		invalidate();

		return true;
	}

	/**
	 * 二点間の移動量を計算
	 * @param x
	 * @param y
	 * @return
	 */
	private float getZoomDistance(MotionEvent e) {
		float floatLength = 0;
		if(e.getPointerCount() >= 2){
			float xx = e.getX(1) - e.getX(0);
			float yy = e.getY(1) - e.getY(0);
			floatLength =  FloatMath.sqrt(xx * xx + yy * yy);
		}
		return floatLength;
	}

	/**
	 * 拡大縮小の比率を計算
	 */
	private float getZoomRate(Matrix matrix, float scale){
		matrix.getValues(values);
		float nextScale = values[0] * scale;
		if(nextScale > MAX_SCALE){
			scale = MAX_SCALE/values[0];
		}
		else if(nextScale < MIN_SCALE){
			scale = MIN_SCALE/values[0];
		}
		return scale;
	}

	/**
	 * 中間点を求める
	 * @param e
	 * @param p
	 * @return
	 */
	private PointF getZoomMiddle(MotionEvent e, PointF p) {
		if(e.getPointerCount() >= 2){
			float x = e.getX(0) + e.getX(1);
			float y = e.getY(0) + e.getY(1);
			p.set(x / 2, y / 2);
		}
		return p;
	}

	/**
	 * 画像を設定するメソッド
	 * @param image
	 */
	public void setImage(Bitmap image){
		this.mSrcBitmap = image;
		this.mViewBitmap = Bitmap.createBitmap(this.mSrcBitmap, this.mViewPointX, this.mViewPointY, this.mViewWidth, this.mViewHeight);
	}

	/**
	 * View接触の信号を送るメソッド
	 */
	public void signalTouchStart(){
		getTouchStart(currentDotX, currentDotY);
	}

}
