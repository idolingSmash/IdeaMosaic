package satoshi.app.ideamosaic.english;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import common.function.sato.var2.CommonClass;
import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonOperateEdit;
import common.function.sato.var2.CommonWhereQuerySentence;

public class IdeaMosaicSearchListView extends AppCompatActivity implements OnItemClickListener, OnClickListener {

	//ボタンの定義
	private static ImageButton btn_Search_ideabook;

	//エディットボックスの定義
	private static EditText et_Input_IdeaBook;

	private static EditText et_searchIdea;

	//リストビューの定義
	private static ListView Listview_IdeaBook;
	private static ArrayList<IdeaMosaicListViewOneCell> al_items = new ArrayList<IdeaMosaicListViewOneCell>();
	private static IdeaMosaicSearchListAdapter ima_searchAdapter;

	//DBの定義
	private static SQLiteDatabase db;
	private static Cursor RS;
	private static Cursor RS2;
	private static IdeaMosaicDBHelper im_DBHelp;

	//ConstClassのインスタンスを生成
	//IdeaMosaicCommonConst im_comst =  new IdeaMosaicCommonConst();
	private static CommonOperateEdit opeEdit = new CommonOperateEdit(1);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_searchlistview);

		ActionBar abar = this.getSupportActionBar();
		abar.setTitle("");
		abar.show();

		Layout_EditText();
		Layout_Button();
		Layout_ListView();

		//dbの設定
		im_DBHelp = new IdeaMosaicDBHelper(this);
		db = im_DBHelp.getWritableDatabase();

		db.execSQL(CommonDBClass.createCreateTableQuerySentence(
				IdeaMosaicCommonConst.str_DB_BS,
				IdeaMosaicCommonConst.brainstroming_fieldNames,
				IdeaMosaicCommonConst.brainstroming_fieldType
		));

		ima_searchAdapter = new IdeaMosaicSearchListAdapter(this, 0, al_items);
	}

	/***
	 * 画面破棄時の動作
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();

		if(!ima_searchAdapter.isEmpty()){
			ima_searchAdapter.clear();
		}
		if(!al_items.isEmpty()){
			al_items.clear();
		}
		if(RS != null){
			if(!RS.isClosed()){
				RS.close();
			}
		}
		if(RS2 != null){
			if(!RS2.isClosed()){
				RS2.close();
			}
		}
		if(db != null){
			if(db.isOpen()){
				db.close();
			}
		}
	}

	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		IdeaMosaicListViewOneCell click_cell = (IdeaMosaicListViewOneCell)Listview_IdeaBook.getItemAtPosition(position);
		Intent intent = new Intent(this, IdeaMosaicMatrixButton.class);
		intent.putExtra("Matirx_Idea", click_cell.getListitem().toString());
		intent.putExtra("Matrix_Idea_Query", click_cell.getItemMatrix().toString().split(",", -1)[4]);
		startActivityForResult(intent,
				IdeaMosaicCommonConst.RequestCode_MATRIX_IDEA);
	}

	public void onClick(View view) {
		if(view == btn_Search_ideabook){
			al_items.clear();
			InsertQueryKeywordForListView();
		}
	}

	/**
	 * キーワードが持つブックを表示
	 */
	private void InsertQueryKeywordForListView() {
		boolean err_flag = true;

		RS  = db.query(
				IdeaMosaicCommonConst.str_DB_ListTable,
				IdeaMosaicCommonConst.Listtable_fieldNames,
				null, null, null, null,
				IdeaMosaicCommonConst.Listtable_fieldNames[IdeaMosaicCommonConst.INT_List_Index_TimeStamp]);
		StringBuilder sb = new StringBuilder();

		if( 0 < RS.getCount()) {
			IdeaMosaicListViewOneCell temp_cell[] = new IdeaMosaicListViewOneCell[RS.getCount()];
			for (int i = 0; i < RS.getCount(); i++)
				temp_cell[i] = new IdeaMosaicListViewOneCell();

			RS.moveToFirst();
			for (int cnt = 0; cnt < RS.getCount(); cnt++) {

				CommonDBClass commonDB = new CommonDBClass(db,
						RS2,
						IdeaMosaicCommonConst.createInnerTableName(RS.getInt(1)),
						IdeaMosaicCommonConst.Matrix_fieldNames,
						IdeaMosaicCommonConst.Matrix_fieldTypes);

				//中央のワードをキーとしたインスタンス
				CommonWhereQuerySentence where_coreword_query = new CommonWhereQuerySentence(
						IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_Mat4],
						et_searchIdea.getText().toString());

				if (commonDB.isOneCountQueryWithWhereQuery(where_coreword_query)) {
					RS2 = db.query(
							IdeaMosaicCommonConst.createInnerTableName(RS.getInt(1)),
							IdeaMosaicCommonConst.Matrix_fieldNames,
							where_coreword_query.createSentence(),
							null, null, null, null);

					temp_cell[cnt].setListitem(RS.getString(0));
					RS2.moveToFirst();
					for (int i = 0; i < 9; i++) {
						if (i > 0) {
							sb.append(" ,");
						}
						if (RS2.getString(i) != null) {
							sb.append(RS2.getString(i));
						}
					}
					temp_cell[cnt].setItemMatrix(new String(sb));
					al_items.add(temp_cell[cnt]);
					sb.delete(0, sb.length());
					err_flag = false;
					RS2.close();
				}
				RS.moveToNext();
			}
			temp_cell = null;
			//アダプターの設定
			Listview_IdeaBook.setAdapter(ima_searchAdapter);
			et_searchIdea.setText("");
		}

		if(err_flag) {
			Toast.makeText(this, this.getString(R.string.toast_messageNotFoundKeyword), Toast.LENGTH_LONG).show();
		}

		RS.close();


	}

	/**
	 * エディットテキストのレイアウト
	 */
	private void Layout_EditText(){
		et_searchIdea = (EditText)this.findViewById(R.id.editText_QueryKeyword);
		et_searchIdea.setInputType(InputType.TYPE_CLASS_TEXT);
		et_searchIdea.setHint("only " + String.valueOf(
				IdeaMosaicCommonConst.NameMaxLength_IdeaBookName) + "words");
	}


	/**
	 * リストビューのレイアウト
	 */
	private void Layout_ListView() {
		// TODO 自動生成されたメソッド・スタブ
		Listview_IdeaBook = (ListView)this.findViewById(R.id.ListView_searchIdea);
		Listview_IdeaBook.setOnItemClickListener(this);
	}

	/**
	 * ボタンのレイアウト
	 */
	private void Layout_Button() {
		btn_Search_ideabook = (ImageButton)this.findViewById(R.id.list_button_search);
		btn_Search_ideabook.setOnClickListener(this);
	}

	/**
	 * エディットテキスト（ダイアログ用）のレイアウト
	 * @param context コンテクスト
	 *
	 */
	private void Layout_EditTextInDialog(Context context){
		et_Input_IdeaBook = new EditText(context);
		et_Input_IdeaBook.setInputType(InputType.TYPE_CLASS_TEXT);
		et_Input_IdeaBook.setHint(
				String.valueOf("only " + IdeaMosaicCommonConst.NameMaxLength_IdeaBookName) + " words");
		CommonClass.Text_MaxLength(et_Input_IdeaBook, IdeaMosaicCommonConst.NameMaxLength_IdeaBookName);
	}

	/**
	 * クリックしたリストビューアイテムのテキストを取得する
	 * @param parent   親のListView
	 * @param position 押した場所
	 * @return テキスト
	 */
	private String getListItem(AdapterView<?> parent,  int position){
		final ListView list_AddTable = (ListView)parent;
		return (String)list_AddTable.getItemAtPosition(position);
	}
}