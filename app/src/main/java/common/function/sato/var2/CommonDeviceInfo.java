package common.function.sato.var2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

@SuppressLint("NewApi")

public class CommonDeviceInfo {

	private Activity avtivity;
	private WindowManager windowManeger;
	private	Display disp;
	private int sdkVersion = 0;


	/**
	 * コンストラクタ
	 * @param activity
	 */
	public CommonDeviceInfo(Activity activity) {
		this.avtivity = activity;
		this.windowManeger = (WindowManager)this.avtivity.getSystemService(Context.WINDOW_SERVICE);
		this.disp = this.windowManeger.getDefaultDisplay();
		this.sdkVersion = Build.VERSION.SDK_INT;
	}

	public int getSDKVersion(){
		return this.sdkVersion;
	}

	/**
	 * 画面サイズの幅を取得するメソッド
	 * @return
	 */
	public int getWindowWidth(){

		int windowWidth = 0;
		Point windowEdgePoint = new Point();

		if( 12 < this.sdkVersion){
			disp.getSize(windowEdgePoint);
			windowWidth = windowEdgePoint.x;
		}else{
			windowWidth = this.disp.getWidth();
		}
		return windowWidth;
	}

	/**
	 * 画面サイズの高さを取得するメソッド
	 * @return
	 */
	public int getWindowHeight(){
		int windowHeight = 0;
		Point windowEdgePoint = new Point();

		if( 12 < this.sdkVersion){
			disp.getSize(windowEdgePoint);
			windowHeight = windowEdgePoint.y;
		}else{
			windowHeight = this.disp.getHeight();
		}

		return windowHeight;
	}
}
