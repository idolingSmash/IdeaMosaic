package common.function.sato.var2;

import android.content.Context;
import android.widget.Toast;

import satoshi.app.ideamosaic.sample.english.R;


/***
 *
 * @author KSato
 *
 * トースト（コメント表示）用の共通クラス
 *
 */

public class CommonToastComment {


	private CommonToastComment(){

	}

	/***
	 * 未入力の時
	 * @param context コンテキスト
	 */
	public static void NullString(final Context context){
		Toast.makeText(context, context.getString(R.string.toast_notInputWord), Toast.LENGTH_SHORT).show();
	}


	/**
	 * 追加が完了したとき
	 *
	 * @param context コンテキスト
	 * @param item    追加する文字列
	 */
	public static void AddItem(final Context context, final String item){
		Toast.makeText(context, item + context.getString(R.string.toast_addItem), Toast.LENGTH_SHORT).show();
	}


	/**
	 * 追加が失敗したとき
	 *
	 * @param context  コンテキスト
	 * @param item    すでに存在している文字列
	 */
	public static void FalseAdd(final Context context, final String item){
		Toast.makeText(context, item + context.getString(R.string.toast_falseAdd), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 修正が完了したとき
	 * @param context	コンテキスト
	 * @param prv_item 	修正前の文字列
	 * @param next_item 修正後の文字列
	 */

	public static void UpdateItem(final Context context, final String prv_item, final String next_item){
		Toast.makeText(context, "「"+ prv_item + "」"+ context.getString(R.string.toast_updateFromItem) +
				"「"+ next_item +"」" + context.getString(R.string.toast_updateToItem), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 修正が失敗したとき
	 * @param context コンテキスト
	 */
	public static void FalseUpdate(final Context context){
		Toast.makeText(context, context.getString(R.string.toast_falseUpdate), Toast.LENGTH_SHORT).show();
	}


	/**
	 * 削除が完了したとき
	 * @param context コンテキスト
	 * @param item　　削除したアイテム
	 */
	public static void DeleteItem(final Context context, final String item){
		Toast.makeText(context, "「" + item + "」" + context.getString(R.string.toast_deleteItem), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 削除が失敗したとき
	 * @param context コンテキスト
	 */
	public static void FalseDelete(final Context context){
		Toast.makeText(context,context.getString(R.string.toast_falseDelete), Toast.LENGTH_SHORT).show();
	}

	/**
	 * ListViewにアイテムがないとき
	 * @param context コンテキスト
	 */
	public static void notExistListItem(final Context context){
		Toast.makeText(context, context.getString(R.string.toast_notExistListItem), Toast.LENGTH_SHORT).show();
	}

	/**
	 * ListView表示で修正ボタンを押下したときのメッセージ
	 * @param context コンテキスト
	 */
	public static void selectListItemForPrepareToUpdate(final Context context){
		Toast.makeText(context,
				context.getString(R.string.toast_selectListItemForPrepareToUpdate), Toast.LENGTH_SHORT).show();
	}

	/**
	 * ListView表示で修正ボタンを押下したときのメッセージ
	 * @param context コンテキスト
	 */
	public static void selectListItemForPrepareToDelete(final Context context){
		Toast.makeText(context,
				context.getString(R.string.toast_selectListItemForPrepareToDelete), Toast.LENGTH_SHORT).show();
	}


}
