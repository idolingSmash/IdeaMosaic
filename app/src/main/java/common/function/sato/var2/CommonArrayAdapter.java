package common.function.sato.var2;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class CommonArrayAdapter {


	private CommonArrayAdapter(){

	}

	/***
	 * リスト項目追加
	 * (ジェネリック型)
	 * @param <T> 型情報
	 * @param aa アダプター
	 * @param lv リスト
	 * @param add_T リスト項目
	 */
	public static <T> void AddDataSetChenge(ArrayAdapter<T> aa, ArrayList<T> lv, T add_T){
		lv.add(add_T);
		aa.notifyDataSetChanged();
	}



	/**
	 * リスト項目更新
	 * (ジェネリック型)
	 *
	 * @param <T> 型情報
	 * @param aa アダプター
	 * @param prv_T 以前の項目
	 * @param next_T 次の項目
	 * @param index インデックス
	 */
	public static <T> void UpdateDataSetChenge(ArrayAdapter<T> aa, T prv_T, T next_T, int index){

		aa.remove(prv_T);
		aa.insert(next_T, index);
		aa.notifyDataSetChanged();

	}



	/**
	 * リスト項目削除
	 * @param <T> 型情報
	 * @param aa アダプター
	 * @param del_T 削除項目
	 */
	public static <T> void DeleteDataSetChenge(ArrayAdapter<T> aa, T del_T){

		aa.remove(del_T);
		aa.notifyDataSetChanged();

	}

}
