package satoshi.app.ideamosaic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonOperateEdit;

public class IdeaMosaicMindMapListView extends AppCompatActivity implements OnItemClickListener{


	//リストビューの定義
	private static ListView Listview_IdeaBook;
	private static ArrayList<IdeaMosaicListViewOneCell> al_items = new ArrayList<IdeaMosaicListViewOneCell>();
	private static IdeaMosaicMindMapListAdapter ima_mindmapAdapter;

	//DBの定義
	private static SQLiteDatabase db;
	private static Cursor RS;
	private static Cursor RS2;
	private static IdeaMosaicDBHelper im_DBHelp;

	//ConstClassのインスタンスを生成
	private static CommonOperateEdit opeEdit = new CommonOperateEdit(1);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_mindmaplistview);

		ActionBar abar = this.getSupportActionBar();
		abar.setTitle("");
		abar.show();

		Layout_ListView();

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		//dbの設定
		im_DBHelp = new IdeaMosaicDBHelper(this);
		db = im_DBHelp.getWritableDatabase();

		db.execSQL(CommonDBClass.createCreateTableQuerySentence(
				IdeaMosaicCommonConst.str_DB_BS,
				IdeaMosaicCommonConst.brainstroming_fieldNames,
				IdeaMosaicCommonConst.brainstroming_fieldType
				));

		RS = db.query(
				IdeaMosaicCommonConst.str_DB_ListTable,
				IdeaMosaicCommonConst.Listtable_fieldNames,
				null, null, null, null,
				IdeaMosaicCommonConst.Listtable_fieldNames[IdeaMosaicCommonConst.INT_List_Index_TimeStamp]
		);

		if (RS.getCount() != 0){
			IdeaMosaicListViewOneCell temp_cell[] = new IdeaMosaicListViewOneCell[RS.getCount()];
			for(int i = 0; i < RS.getCount();i++)
				temp_cell[i] = new IdeaMosaicListViewOneCell();

			RS.moveToFirst();
			for(int cnt = 0; cnt < RS.getCount() ;cnt++){
				temp_cell[cnt].setListitem(RS.getString(0));
				al_items.add(temp_cell[cnt]);
				RS.moveToNext();
			}
		}
		RS.close();

		ima_mindmapAdapter = new IdeaMosaicMindMapListAdapter(this, 0, al_items);
		Listview_IdeaBook.setAdapter(ima_mindmapAdapter);
	}

	/***
	 * 画面破棄時の動作
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();

		if(!ima_mindmapAdapter.isEmpty()){
			ima_mindmapAdapter.clear();
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
		Intent intent = new Intent(this, IdeaMosaicCreateMindMap.class);
		intent.putExtra("Matirx_Idea", click_cell.getListitem());
		startActivityForResult(intent,
				IdeaMosaicCommonConst.RequestCode_LISTVIEW_MINDMAPCREATE);
	}


	/**
	 * リストビューのレイアウト
	 */
	private void Layout_ListView() {
		Listview_IdeaBook = (ListView)this.findViewById(R.id.ListView_mindmapIdea);
		Listview_IdeaBook.setOnItemClickListener(this);
	}

	/**
	 * クリックしたリストビューアイテムのテキストを取得する
	 * @param parent   親ListView
	 * @param position 押した場所
	 * @return テキスト
	 */
	@SuppressWarnings("unused")
	private String getListItem(AdapterView<?> parent,  int position){
		final ListView list_AddTable = (ListView)parent;
		return (String)list_AddTable.getItemAtPosition(position);
	}
}