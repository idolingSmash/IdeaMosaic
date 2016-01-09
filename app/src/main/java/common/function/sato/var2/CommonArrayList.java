/*
package common.function.sato.var2;

import java.util.ArrayList;
import java.util.Collections;
*/

/*
public class CommonArrayList {


	*/
/**
	 * arraylist内の特定の要素の数を数える（Intバージョン）
	 * @param arraylist		検索対象のarraylist
	 * @param i				検索対象の数値
	 *
	 * @return				検索対象０の時:0
	 * 						検索対象があるとき: 要素数
	 *//*

	public static int CountElement(ArrayList<Integer> arraylist, int i){

		//まずはソート
		Collections.sort(arraylist);

		if(-1 < arraylist.lastIndexOf(i) ||  -1 < arraylist.indexOf(i)){
			return arraylist.lastIndexOf(i) - arraylist.indexOf(i) + 1;
		}else{
			return 0;
		}
	}


	*/
/**
	 * arraylist内の特定の要素の数を数える（Intバージョン）
	 * @param arraylist		検索対象のarraylist
	 * @param str			検索対象の文字列

	 * @return				検索対象０の時:0
	 * 						検索対象があるとき: 要素数
	 *//*

	public static int CountElement(ArrayList<String> arraylist, String str){

		//まずはソート
		Collections.sort(arraylist);

		if(-1 < arraylist.lastIndexOf(str)|| -1 <arraylist.indexOf(str)){
			return arraylist.lastIndexOf(str) - arraylist.indexOf(str) + 1;
		}else{
			return 0;
		}
	}

}
*/
