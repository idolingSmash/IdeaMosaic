package satoshi.app.ideamosaic;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonOperateEdit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class IdeaMosaicMindMapListView extends Activity implements OnItemClickListener{


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
	static IdeaMosaicCommonConst im_comst =  new IdeaMosaicCommonConst();
	private static CommonOperateEdit opeEdit = new CommonOperateEdit(1);

	//広告用
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_mindmaplistview);

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

		RS = db.query(im_comst.getstrDbListtable(),
				im_comst.getListtableFieldnames(),
				null, null, null, null,
				im_comst.getListtableFieldnames()[im_comst.getIntListIndexTimestamp()]);

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
		// TODO 自動生成されたメソッド・スタブ
		IdeaMosaicListViewOneCell click_cell = (IdeaMosaicListViewOneCell)Listview_IdeaBook.getItemAtPosition(position);
		Intent intent = new Intent(this, satoshi.app.ideamosaic.IdeaMosaicCreateMindMap.class);
		intent.putExtra("Matirx_Idea", click_cell.getListitem().toString());
		startActivityForResult(intent, im_comst.getRequestCode_LISTVIEW_MINDMAPCREATE());
	}


	/**
	 * リストビューのレイアウト
	 */
	private void Layout_ListView() {
		// TODO 自動生成されたメソッド・スタブ
		Listview_IdeaBook = (ListView)this.findViewById(R.id.ListView_mindmapIdea);
		Listview_IdeaBook.setOnItemClickListener(this);
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