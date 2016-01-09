package common.function.sato.var2;

import android.app.Activity;
import android.content.Context;


public class CommonDraw extends Activity{


	private Context context;
	private int int_ObjectLength;
	private float float_ObjectLength;

	public CommonDraw(Context context, int int_length) {
		super();
		this.context = context;
		this.int_ObjectLength = int_length;
	}

	@SuppressWarnings("unused")
	public CommonDraw(Context context, float float_length) {
		super();
		this.context = context;
		this.float_ObjectLength = float_length;
	}


	/**
	 * dip(Density-independent Pixels) から px(pixcel) に変換するメソッド
	 * @return 戻り値は float型
	 */
	@SuppressWarnings("unused")
	public float floatDipToPixcel(){
		if(this.float_ObjectLength > 0){
			return this.float_ObjectLength * context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

	/**
	 * dip(Density-independent Pixels) から px(pixcel) に変換するメソッド
	 * @return 戻り値は int型
	 */
	public float intDipToPixcel(){
		if(this.int_ObjectLength > 0){
			return this.int_ObjectLength * context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

	/**
	 * px(pixcel) から dip(Density-independent Pixels) に変換するメソッド
	 * @return 戻り値は float型
	 */
	@SuppressWarnings("unused")
	public float floatPixcelToDip(){
		if(this.float_ObjectLength > 0){
			return this.float_ObjectLength / context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

	/**
	 * px(pixcel) から dip(Density-independent Pixels) に変換するメソッド
	 * @return 戻り値は int型
	 */
	@SuppressWarnings("unused")
	public float intPixcelToDip(){
		if(this.int_ObjectLength > 0) {
			return this.int_ObjectLength / context.getResources().getDisplayMetrics().density;
		}
		else{
			return 0;
		}
	}

}
