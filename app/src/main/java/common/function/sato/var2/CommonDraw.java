package common.function.sato.var2;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


public class CommonDraw extends Activity{


	private Context context;
	private int int_ObjectLength;
	private float float_ObjectLength;

	public CommonDraw(Context context, int int_length) {
		super();
		this.context = context;
		this.int_ObjectLength = int_length;
	}
	public CommonDraw(Context context, float float_length) {
		super();
		this.context = context;
		this.float_ObjectLength = float_length;
	}

	/*
	 * 		実行サンプル
	 *
	 * 			CommonDraw draw_height = new CommonDraw(this, listbtn_Matrix.get(0).getHeight());
			CommonDraw draw_width = new CommonDraw(this, listbtn_Matrix.get(0).getWidth());

			Toast.makeText(this, String.valueOf(draw_height.intPixcelToDip()), Toast.LENGTH_SHORT).show();
			Toast.makeText(this, String.valueOf(draw_width.intPixcelToDip()), Toast.LENGTH_SHORT).show();

	 */

	/**
	 * dip(Density-independent Pixels) から px(pixcel) に変換するメソッド
	 * @return　戻り値は float型
	 */
	public float floatDipToPixcel(){
		if(this.float_ObjectLength > 0){
			return this.float_ObjectLength * context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

	/**
	 * dip(Density-independent Pixels) から px(pixcel) に変換するメソッド
	 * @return　戻り値は int型
	 */
	public float intDipToPixcel(){
		if(this.int_ObjectLength > 0){
			return (int)this.int_ObjectLength * context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

	/**
	 * px(pixcel) から dip(Density-independent Pixels) に変換するメソッド
	 * @return
	 */
	public float floatPixcelToDip(){
		if(this.float_ObjectLength > 0){
			return this.float_ObjectLength / context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

	/**
	 * px(pixcel) から dip(Density-independent Pixels) に変換するメソッド
	 * @return　戻り値は int型
	 */
	public float intPixcelToDip(){
		if(this.int_ObjectLength > 0){
			return (int)this.int_ObjectLength / context.getResources().getDisplayMetrics().density;
		}else{
			return 0;
		}
	}

}
