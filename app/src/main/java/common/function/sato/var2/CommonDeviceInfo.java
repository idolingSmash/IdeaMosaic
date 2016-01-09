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
	 * @param activity アクティビティ
	 */
	@SuppressWarnings("unused")
	public CommonDeviceInfo(Activity activity) {
		this.avtivity = activity;
		this.windowManeger = (WindowManager)this.avtivity.getSystemService(Context.WINDOW_SERVICE);
		this.disp = this.windowManeger.getDefaultDisplay();
		this.sdkVersion = Build.VERSION.SDK_INT;
	}

	@SuppressWarnings("unused")
	public int getSDKVersion(){
		return this.sdkVersion;
	}

	/**
	 * 画面サイズの幅を取得するメソッド
	 * @return screen width
	 */
	@SuppressWarnings("unused")
	public int getWindowWidth(){

		int windowWidth;
		Point windowEdgePoint = new Point();

		disp.getSize(windowEdgePoint);
		windowWidth = windowEdgePoint.x;

		return windowWidth;
	}

	/**
	 * 画面サイズの高さを取得するメソッド
	 * @return screen height
	 */
	public int getWindowHeight(){
		int windowHeight;
		Point windowEdgePoint = new Point();

		disp.getSize(windowEdgePoint);
		windowHeight = windowEdgePoint.y;

		return windowHeight;
	}

	@SuppressWarnings("unused")
	public WindowManager getWindowManeger() {
		return windowManeger;
	}

	@SuppressWarnings("unused")
	public Activity getAvtivity() {
		return avtivity;
	}

}
