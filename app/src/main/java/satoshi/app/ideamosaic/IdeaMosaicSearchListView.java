package satoshi.app.ideamosaic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import common.function.sato.var2.CommonAlartDiagram;
import common.function.sato.var2.CommonArrayAdapter;
import common.function.sato.var2.CommonClass;
import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonErrorCheck;
import common.function.sato.var2.CommonOperateEdit;
import common.function.sato.var2.CommonToastComment;
import common.function.sato.var2.CommonWhereQuerySentence;
import common.function.sato.var2.CommonAlartDiagram.ErrorCheckFlag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class IdeaMosaicSearchListView extends Activity implements OnItemClickListener, OnClickListener {

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
	static IdeaMosaicCommonConst im_comst =  new IdeaMosaicCommonConst();
	private static CommonOperateEdit opeEdit = new CommonOperateEdit(1);

	//広告用
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_searchlistview);

		Layout_EditText();
		Layout_Button();
		Layout_ListView();

		//		// adView を作成する
		//		adView = new AdView(this, AdSize.BANNER, "a1513b3a75207b9");
		//		// 属性 android:id="@+id/mainLayout" が与えられているものとして
		//		// LinearLayout をルックアップする
		//		// adView を追加
		//		adView.loadAd(new AdRequest());
		//		LinearLayout adPosition = (LinearLayout) findViewById(R.id.admob_searchlistview);
		//		adPosition.addView(adView);

		//dbの設定
		im_DBHelp = new IdeaMosaicDBHelper(this);
		db = im_DBHelp.getWritableDatabase();

		db.execSQL(CommonDBClass.createCreateTableQuerySentence(
				im_comst.getStrDbBs(),
				im_comst.getBrainstromingFieldnames(),
				im_comst.getBrainstromingFieldtype()
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
		// TODO 自動生成されたメソッド・スタブ
		IdeaMosaicListViewOneCell click_cell = (IdeaMosaicListViewOneCell)Listview_IdeaBook.getItemAtPosition(position);
		Intent intent = new Intent(this, satoshi.app.ideamosaic.IdeaMosaicMatrixButton.class);
		intent.putExtra("Matirx_Idea", click_cell.getListitem().toString());
		intent.putExtra("Matrix_Idea_Query", click_cell.getItemMatrix().toString().split(",", -1)[4]);
		startActivityForResult(intent, im_comst.getRequestCode_MATRIX_IDEA());
	}

	public void onClick(View view) {
		// TODO 自動生成されたメソッド・スタブ

		if(view == btn_Search_ideabook){
			al_items.clear();
			InsertQueryKeywordForListView();
		}

	}

	/**
	 * キーワードが持つブックを表示
	 */
	private void InsertQueryKeywordForListView() {
		// TODO 自動生成されたメソッド・スタブ
		RS  = db.query(im_comst.getstrDbListtable(),
				im_comst.getListtableFieldnames(),
				null, null, null, null,
				im_comst.getListtableFieldnames()[im_comst.getIntListIndexTimestamp()]);
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();

		if (RS.getCount() != 0){
			IdeaMosaicListViewOneCell temp_cell[] = new IdeaMosaicListViewOneCell[RS.getCount()];
			for(int i = 0; i < RS.getCount();i++)
				temp_cell[i] = new IdeaMosaicListViewOneCell();

			RS.moveToFirst();
			for(int cnt = 0; cnt < RS.getCount() ;cnt++){

				CommonDBClass commonDB = new CommonDBClass(db,
						RS2,
						im_comst.createInnerTableName(RS.getInt(1)),
						im_comst.getMatrixFieldnames(),
						im_comst.getMatrixFieldtypes());

				//中央のワードをキーとしたインスタンス
				CommonWhereQuerySentence where_coreword_query = new CommonWhereQuerySentence(
						im_comst.getMatrixFieldnames()[im_comst.getIntMatrixIndexMat4()],
						et_searchIdea.getText().toString());

				if(commonDB.isOneCountQueryWithWhereQuery(where_coreword_query)){

					RS2  = db.query(im_comst.createInnerTableName(RS.getInt(1)),
							im_comst.getMatrixFieldnames(),
							where_coreword_query.createSentence(),
							null, null, null,null);

					temp_cell[cnt].setListitem(RS.getString(0));
					RS2.moveToFirst();
					for(int i = 0;i < 9;i++){
						if(i > 0){
							sb.append(" ,");
						}
						if(RS2.getString(i) != null){
							sb.append(RS2.getString(i));
						}
					}
					temp_cell[cnt].setItemMatrix(new String(sb));
					al_items.add(temp_cell[cnt]);
					sb.delete(0, sb.length());
					RS2.close();
				}
				RS.moveToNext();
			}
			temp_cell = null;
			//アダプターの設定
			Listview_IdeaBook.setAdapter(ima_searchAdapter);
			et_searchIdea.setText("");
		}else{
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
		et_searchIdea.setHint(String.valueOf(im_comst.getNameMaxLength_IdeaBookName()) + "字まで");
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
	 */
	private void Layout_EditTextInDialog(Context context){
		et_Input_IdeaBook = new EditText(context);
		et_Input_IdeaBook.setInputType(InputType.TYPE_CLASS_TEXT);
		et_Input_IdeaBook.setHint(String.valueOf(im_comst.getNameMaxLength_IdeaBookName()) + "字まで");
		CommonClass.Text_MaxLength(et_Input_IdeaBook, im_comst.getNameMaxLength_IdeaBookName());
	}

	/**
	 * クリックしたリストビューアイテムのテキストを取得する
	 * @param parent
	 * @param position
	 * @return
	 */
	private String getListItem(AdapterView<?> parent,  int position){
		final ListView list_AddTable = (ListView)parent;
		return (String)list_AddTable.getItemAtPosition(position);
	}
}