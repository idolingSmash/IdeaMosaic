package satoshi.app.ideamosaic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import common.function.sato.var2.CommonAlartDiagram;
import common.function.sato.var2.CommonAlartDiagram.ErrorCheckFlag;
import common.function.sato.var2.CommonArrayAdapter;
import common.function.sato.var2.CommonClass;
import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonOperateEdit;
import common.function.sato.var2.CommonToastComment;
import common.function.sato.var2.CommonWhereQuerySentence;

public class IdeaMosaicListView extends Activity implements OnItemClickListener, OnClickListener {

	//ボタンの定義
	private static Button btn_Add_ideabook;
	private static Button btn_Update_ideabook;
	private static Button btn_Delete_ideabook;

	//エディットボックスの定義
	private static EditText et_Input_IdeaBook;

	//リストビューの定義
	private static ListView Listview_IdeaBook;
	private static ArrayList<IdeaMosaicListViewOneCell> al_items = new ArrayList<IdeaMosaicListViewOneCell>();
	private static IdeaMosaicListAdapter ima_adapter;

	//DBの定義
	private static SQLiteDatabase db;
	private static Cursor RS;
	private static Cursor RS2;
	private static IdeaMosaicDBHelper im_DBHelp;

	//ConstClassのインスタンスを生成
	private static CommonOperateEdit opeEdit = new CommonOperateEdit(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_listview);

		Layout_ListView();
		Layout_Button();

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		//dbの設定
		im_DBHelp = new IdeaMosaicDBHelper(this);
		db = im_DBHelp.getWritableDatabase();

		db.execSQL(CommonDBClass.createCreateTableQuerySentence(
				IdeaMosaicCommonConst.str_DB_BS,
				IdeaMosaicCommonConst.brainstroming_fieldNames,
				IdeaMosaicCommonConst.brainstroming_fieldType));

		RS = db.query(
				IdeaMosaicCommonConst.str_DB_ListTable,
				IdeaMosaicCommonConst.Listtable_fieldNames,
				null, null, null, null,
				IdeaMosaicCommonConst.Listtable_fieldNames[IdeaMosaicCommonConst.INT_List_Index_TimeStamp]);

		if (RS.getCount() != 0){
			IdeaMosaicListViewOneCell temp_cell[] = new IdeaMosaicListViewOneCell[RS.getCount()];
			for(int i = 0; i < RS.getCount();i++)
				temp_cell[i] = new IdeaMosaicListViewOneCell();

			RS.moveToFirst();
			for(int cnt = 0; cnt < RS.getCount() ;cnt++){
				temp_cell[cnt].setListitem(RS.getString(0));
				temp_cell[cnt].setItemCountInList(this.getString(R.string.txt_listviewElementCount) +
						String.valueOf(getMatrixItemCount(
								RS.getInt(IdeaMosaicCommonConst.INT_List_Index_TableID)
								)));
				temp_cell[cnt].setTimeStamp(
						new String(RS.getString(
								IdeaMosaicCommonConst.INT_List_Index_TimeStamp
						)).substring(0, 10));
				al_items.add(temp_cell[cnt]);
				RS.moveToNext();
			}
		}
		RS.close();

		//アダプターの設定
		ima_adapter = new IdeaMosaicListAdapter(this, 0, al_items);
		Listview_IdeaBook.setAdapter(ima_adapter);

	}

	/***
	 * 画面破棄時の動作
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		ima_adapter.clear();
		al_items.clear();
		RS.close();
		//		RS2.close();
		db.close();

	}

	/**
	 * Matrix内にある項目数を得るメソッド
	 * @param int_listid
	 * @return 項目数
	 */
	private static int getMatrixItemCount(int int_listid){

		String str_InnerTableName;

		str_InnerTableName =
				IdeaMosaicCommonConst.str_DB_IdeaMosaic + "_" + String.valueOf(int_listid);

		IdeaMosaicContainUniqueItem im_uni = new IdeaMosaicContainUniqueItem(db,
				RS2,
				str_InnerTableName,
				IdeaMosaicCommonConst.Matrix_fieldNames,
				IdeaMosaicCommonConst.Matrix_fieldTypes);

		return im_uni.getUniqueItemcount();
	}

	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		IdeaMosaicListViewOneCell click_cell = (IdeaMosaicListViewOneCell)Listview_IdeaBook.getItemAtPosition(position);

		if(opeEdit.isEditFlag(2) == true){
			Dialog_InputListItem(this, 2, click_cell);
		}
		else if(opeEdit.isEditFlag(3) == true){
			Dialog_InputListItem(this, 3, click_cell);
		}else{
			Intent intent = new Intent(this, IdeaMosaicMatrixButton.class);
			intent.putExtra("Matirx_Idea", click_cell.getListitem().toString());
			startActivityForResult(intent,
					IdeaMosaicCommonConst.RequestCode_MATRIX_IDEA);
		}
	}

	public void onClick(View view) {
		// TODO 自動生成されたメソッド・スタブ

		CommonDBClass commonDB = new CommonDBClass(db,
				RS,
				IdeaMosaicCommonConst.str_DB_ListTable,
				IdeaMosaicCommonConst.Listtable_fieldNames,
				IdeaMosaicCommonConst.Listtable_fieldTypes);

		if(view == btn_Add_ideabook){
			if(opeEdit.isEditFlag(1) == true){
				//デフォルトに戻す
				opeEdit.FontColorOrengeInSelectButton(1, btn_Update_ideabook, btn_Delete_ideabook);
				Dialog_InputListItem(this, 1, null);
				//				AddDialog(this);
			}
		}else if(view == btn_Update_ideabook){
			if(opeEdit.isEditFlag(1) == true){
				if(commonDB.isZeroCountQuery() == true){
					CommonToastComment.notExistListItem(this);
				}else{
					//修正ボタンの背景色を変える
					opeEdit.FontColorOrengeInSelectButton(2, btn_Update_ideabook, btn_Delete_ideabook);
					CommonToastComment.selectListItemForPrepareToUpdate(this);
				}
			}else if(opeEdit.isEditFlag(2) == true){
				//デフォルトに戻す
				opeEdit.FontColorOrengeInSelectButton(1, btn_Update_ideabook, btn_Delete_ideabook);
			}
		}else if(view == btn_Delete_ideabook){
			if(opeEdit.isEditFlag(1) == true){
				if(commonDB.isZeroCountQuery() == true){
					CommonToastComment.notExistListItem(this);
				}else{
					//削除ボタンの背景色を変える
					opeEdit.FontColorOrengeInSelectButton(3, btn_Update_ideabook, btn_Delete_ideabook);
					CommonToastComment.selectListItemForPrepareToDelete(this);
				}
			}else if(opeEdit.isEditFlag(3) == true){
				//デフォルトに戻す
				opeEdit.FontColorOrengeInSelectButton(1, btn_Update_ideabook, btn_Delete_ideabook);
			}
		}
	}

	/**
	 * リストビューのレイアウト
	 */
	private void Layout_ListView() {
		// TODO 自動生成されたメソッド・スタブ
		Listview_IdeaBook = (ListView)this.findViewById(R.id.ListView_idea);
		Listview_IdeaBook.setOnItemClickListener(this);
	}

	/**
	 * ボタンのレイアウト
	 */
	private void Layout_Button() {
		// TODO 自動生成されたメソッド・スタブ
		btn_Add_ideabook = (Button)this.findViewById(R.id.list_button_add);
		btn_Update_ideabook = (Button)this.findViewById(R.id.list_button_update);
		btn_Delete_ideabook = (Button)this.findViewById(R.id.list_button_delete);

		btn_Add_ideabook.setOnClickListener(this);
		btn_Update_ideabook.setOnClickListener(this);
		btn_Delete_ideabook.setOnClickListener(this);
	}

	/**
	 * エディットテキスト（ダイアログ用）のレイアウト
	 */
	private void Layout_EditTextInDialog(Context context){
		et_Input_IdeaBook = new EditText(context);
		et_Input_IdeaBook.setInputType(InputType.TYPE_CLASS_TEXT);
		et_Input_IdeaBook.setHint(String.valueOf(
				IdeaMosaicCommonConst.NameMaxLength_IdeaBookName) + this.getString(R.string.edittext_limitLength));
		CommonClass.Text_MaxLength(et_Input_IdeaBook, IdeaMosaicCommonConst.NameMaxLength_IdeaBookName);
	}

	/**
	 * クリックしたリストビューアイテムのテキストを取得する
	 * @param parent  親ListView
	 * @param position 押した場所
	 * @return		   テキスト
	 */
	private String getListItem(AdapterView<?> parent,  int position){
		final ListView list_AddTable = (ListView)parent;
		return (String)list_AddTable.getItemAtPosition(position);
	}

	/**
	 * リストアイテムを編集するDialogメソッド
	 * @param context			コンテクスト
	 * @param int_operateEdit	編集フラグ
	 * 1:追加
	 * 2:修正
	 * 3:削除
	 */
	private void Dialog_InputListItem(final Context context, int int_operateEdit,
			IdeaMosaicListViewOneCell cell_ListClickItem) {
		Layout_EditTextInDialog(context);

		CommonAlartDiagram commonAD = new CommonAlartDiagram(
				context,
				et_Input_IdeaBook,
				IdeaMosaicCommonConst.INT_List_Index_TableID);

		switch(int_operateEdit){
		case 1:
			commonAD.setTitle(this.getString(R.string.dialog_titleAddListItem));
			commonAD.setView(et_Input_IdeaBook);
			IdeaMosaicListView.Add_setPositiveButton(commonAD, context);
			break;
		case 2:
			commonAD.setTitle(this.getString(R.string.dialog_titleUpdateListItem));
			commonAD.setView(et_Input_IdeaBook);
			IdeaMosaicListView.Modified_setPositiveButton(commonAD, context, cell_ListClickItem);
			break;
		case 3:
			commonAD.setTitle(this.getString(R.string.dialog_titleDeleteListItem));
			commonAD.setMessage(this.getString(R.string.dialog_messageDeleteListItemForward)
					+ cell_ListClickItem.getListitem() +
					this.getString(R.string.dialog_messageDeleteListItemBackward));
			IdeaMosaicListView.Delete_setPositiveButton(commonAD, context, cell_ListClickItem);
			break;
		}

		//キャンセルボタンの実装
		if(int_operateEdit == 2 || int_operateEdit == 3){
			commonAD.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* Cancel ボタンをクリックした時の処理 */
					opeEdit.FontColorOrengeInSelectButton(1, btn_Update_ideabook, btn_Delete_ideabook);
				}
			});
		}

		commonAD.create();
		commonAD.show();
	}



	/**
	 * 追加項目ダイアログ作成
	 * @param commonAD
	 * @param context
	 */
	private static void Add_setPositiveButton(final CommonAlartDiagram commonAD, final Context context) {

		commonAD.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				CommonDBClass commonDB = new CommonDBClass(db,
						RS,
						IdeaMosaicCommonConst.str_DB_ListTable,
						IdeaMosaicCommonConst.Listtable_fieldNames,
						IdeaMosaicCommonConst.Listtable_fieldTypes);

				CommonWhereQuerySentence search_query =
						new CommonWhereQuerySentence(
								IdeaMosaicCommonConst.Listtable_fieldNames[0],
								et_Input_IdeaBook.getText().toString());

				ErrorCheckFlag errorflag = commonAD.isErrorCheck(commonDB,
						IdeaMosaicCommonConst.Listtable_fieldNames[1], search_query);
				ContentValues table_values = new ContentValues();

				if(errorflag.isCheck_flag()){

					commonDB.setDBValueInContentValues(
							table_values, IdeaMosaicCommonConst.Listtable_fieldNames[0], et_Input_IdeaBook.getText().toString());
					commonDB.setDBIndexInContentValues(table_values,
							IdeaMosaicCommonConst.Listtable_fieldNames[1],
							errorflag.getTable_ID());
					commonDB.setDBTimeStampInContentValues(table_values,
							IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_TimeStamp]);
					commonDB.InsertContentValuesInDB(table_values,
							IdeaMosaicCommonConst.str_DB_ListTable);

					//IMテーブルの作成
					db.execSQL(CommonDBClass.createCreateTableQuerySentence(
							CommonDBClass.createTableNamePlusID(
									IdeaMosaicCommonConst.str_DB_IdeaMosaic,
									String.valueOf(errorflag.getTable_ID())),
							IdeaMosaicCommonConst.Matrix_fieldNames,
							IdeaMosaicCommonConst.Matrix_fieldTypes));

					//色テーブルの作成
					db.execSQL(CommonDBClass.createCreateTableQuerySentence(
							CommonDBClass.createTableNamePlusID(
									IdeaMosaicCommonConst.str_DB_IMColor,
									String.valueOf(errorflag.getTable_ID())),
							IdeaMosaicCommonConst.MatrixButtonColor_fieldNames,
							IdeaMosaicCommonConst.MatrixButtonColor_fieldTypes));

					IdeaMosaicListViewOneCell add_cell = new IdeaMosaicListViewOneCell(et_Input_IdeaBook.getText().toString(),
							context.getString(R.string.txt_listviewElementCount) + String.valueOf(0),
							new String(CommonClass.strsTimeStamp()).substring(0, 10));


					CommonArrayAdapter.AddDataSetChenge(ima_adapter, al_items, add_cell);	//リストビューの更新
				}
			}
		});

	}



	/***
	 * 修正が完了したときのメソッド
	 * @param commonAD ダイアグラム
	 * @param context  コンテクスト
	 * @param update_prv_cell 以前のリスト項目
	 */
	private static void Modified_setPositiveButton(final CommonAlartDiagram commonAD, final Context context,
			final IdeaMosaicListViewOneCell update_prv_cell){

		final ContentValues table_values = new ContentValues();

		commonAD.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				CommonDBClass commonDB = new CommonDBClass(db,
						RS,
						IdeaMosaicCommonConst.str_DB_ListTable,
						IdeaMosaicCommonConst.Listtable_fieldNames,
						IdeaMosaicCommonConst.Listtable_fieldTypes);

				CommonWhereQuerySentence set_query = new CommonWhereQuerySentence(
						IdeaMosaicCommonConst.Listtable_fieldNames[0], et_Input_IdeaBook.getText().toString());
				CommonWhereQuerySentence search_query = new CommonWhereQuerySentence(
						IdeaMosaicCommonConst.Listtable_fieldNames[0], update_prv_cell.getListitem());

				ErrorCheckFlag errorflag = commonAD.isErrorCheckWithoutMessage(commonDB,
						IdeaMosaicCommonConst.Listtable_fieldNames[1], set_query);

				if(errorflag.isCheck_flag()){
					commonDB.setDBValueInContentValues(table_values, set_query);
					commonDB.setDBTimeStampInContentValues(table_values,
							IdeaMosaicCommonConst.Listtable_fieldNames[2]);
					if(commonDB.isOneCountQueryWithWhereQuery(search_query) && commonDB.isRecordUpdate(table_values, search_query)){

						//updateした項目
						IdeaMosaicListViewOneCell update_cell = new IdeaMosaicListViewOneCell(et_Input_IdeaBook.getText().toString(),
								context.getString(R.string.txt_listviewElementCount) +
										String.valueOf(
												getMatrixItemCount(
														commonDB.getDBUniqueIndexId(set_query,
																IdeaMosaicCommonConst.INT_List_Index_TableID))),
								new String(CommonClass.strsTimeStamp()).substring(0, 10));
						CommonArrayAdapter.UpdateDataSetChenge(ima_adapter, update_prv_cell, update_cell, ima_adapter.getPosition(update_prv_cell));
						CommonToastComment.UpdateItem(context, update_prv_cell.getListitem(), et_Input_IdeaBook.getText().toString());
					}else{
						CommonToastComment.FalseUpdate(context);
					}
				}
				opeEdit.FontColorOrengeInSelectButton(1, btn_Update_ideabook, btn_Delete_ideabook);
			}
		}
				);
	}


	/***
	 * 削除が完了したときのメソッド
	 * @param ad		ダイアログ
	 * @param context	コンテクスト
	 * @param delete_cell 削除する項目
	 */

	private static void Delete_setPositiveButton(AlertDialog.Builder ad, final Context context,
			final IdeaMosaicListViewOneCell delete_cell){

		final ArrayAdapter<String> adapt = (ArrayAdapter<String>)Listview_IdeaBook.getAdapter();

		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			String Del_TableName = "";
			String Del_ColorTableName = "";
			public void onClick(DialogInterface dialog, int which) {

				CommonDBClass commonDB = new CommonDBClass(db,
						RS,
						IdeaMosaicCommonConst.str_DB_ListTable,
						IdeaMosaicCommonConst.Listtable_fieldNames,
						IdeaMosaicCommonConst.Listtable_fieldTypes);

				CommonWhereQuerySentence where_del_query =
						new CommonWhereQuerySentence(
								IdeaMosaicCommonConst.Listtable_fieldNames[0], delete_cell.getListitem());

				//対象のテーブルを削除
				if(commonDB.isOneCountQueryWithWhereQuery(where_del_query)){

					//生成されていたIdeaMosaicテーブルを削除
					Del_TableName = CommonDBClass.createTableNamePlusID(
							IdeaMosaicCommonConst.str_DB_IdeaMosaic,
							String.valueOf(commonDB.getDBUniqueIndexId(where_del_query, 1)));
					//生成されていたIdeaMosaicテーブルを削除
					Del_ColorTableName = CommonDBClass.createTableNamePlusID(
							IdeaMosaicCommonConst.str_DB_IMColor, String.valueOf(commonDB.getDBUniqueIndexId(where_del_query, 1)));
					db.execSQL(CommonDBClass.createDeleteTableQuerySentence(Del_TableName));
					db.execSQL(CommonDBClass.createDeleteTableQuerySentence(Del_ColorTableName));
				}
				RS.close();

				if (commonDB.isRecordDelete(where_del_query)){
					CommonArrayAdapter.DeleteDataSetChenge(ima_adapter, delete_cell); //リストの更新
					CommonToastComment.DeleteItem(context, delete_cell.getListitem());
					//ボタンの色をデフォルト
					//					if(Integer.parseInt(
					//							CommonDBClass.getDBAllCount(db, RS, common.getDbTabletable(), common.getAddtableFieldnames())
					//							) <= common.getMaxTablecount()){
					//						btn_Add.setEnabled(true);
					//					}
				}else{
					CommonToastComment.FalseDelete(context);
				}
				opeEdit.FontColorOrengeInSelectButton(1, btn_Update_ideabook, btn_Delete_ideabook);
			}
		});
	}
}
