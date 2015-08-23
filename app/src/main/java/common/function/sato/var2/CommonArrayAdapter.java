package common.function.sato.var2;

import java.util.ArrayList;

import android.widget.ArrayAdapter;

public class CommonArrayAdapter {



	/***
	 * リスト項目追加
	 * @param aa
	 * @param lv
	 * @param add_str
	 */
	public static void AddDataSetChenge(ArrayAdapter<String> aa, ArrayList<String> lv, String add_str){

		lv.add(add_str);
		aa.notifyDataSetChanged();

	}



	/**
	 * リスト項目更新
	 * @param aa
	 * @param prv_str
	 * @param next_str
	 * @param index
	 */
	public static void UpdateDataSetChenge(ArrayAdapter<String> aa, String prv_str, String next_str, int index){

		aa.remove(prv_str);
		aa.insert(next_str, index);
		aa.notifyDataSetChanged();

	}



	/**
	 * リスト項目削除
	 * @param aa
	 * @param del_str
	 */
	public static void DeleteDataSetChenge(ArrayAdapter<String> aa, String del_str){

		aa.remove(del_str);
		aa.notifyDataSetChanged();

	}

	/***
	 * リスト項目追加
	 * @param <T>
	 * @param aa
	 * @param lv
	 * @param add_str
	 */
	public static <T> void AddDataSetChenge(ArrayAdapter<T> aa, ArrayList<T> lv, T add_T){

		lv.add(add_T);
		aa.notifyDataSetChanged();

	}



	/**
	 * リスト項目更新
	 * @param <T>
	 * @param aa
	 * @param prv_str
	 * @param next_str
	 * @param index
	 */
	public static <T> void UpdateDataSetChenge(ArrayAdapter<T> aa, T prv_T, T next_T, int index){

		aa.remove(prv_T);
		aa.insert(next_T, index);
		aa.notifyDataSetChanged();

	}



	/**
	 * リスト項目削除
	 * @param <T>
	 * @param aa
	 * @param del_str
	 */
	public static <T> void DeleteDataSetChenge(ArrayAdapter<T> aa, T del_T){

		aa.remove(del_T);
		aa.notifyDataSetChanged();

	}



}
