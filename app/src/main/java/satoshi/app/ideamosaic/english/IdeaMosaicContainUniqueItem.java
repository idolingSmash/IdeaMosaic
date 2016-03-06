package satoshi.app.ideamosaic.english;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import common.function.sato.var2.CommonClass;
import common.function.sato.var2.CommonDBClass;

public class IdeaMosaicContainUniqueItem extends CommonDBClass{

	private SQLiteDatabase db;			//Database
	private Cursor c;					//RecordSet
	private String str_Table;			//テーブル名
	private String[] str_Field;			//各種項目
	private String[] str_Type;			//各種タイプ

	public IdeaMosaicContainUniqueItem(SQLiteDatabase db, Cursor c,
			String str_Table, String[] str_Field, String[] str_Type) {
		super(db, c, str_Table, str_Field, str_Type);
		this.db = db;
		this.c= c;
		this.str_Table = str_Table;
		this.str_Field = str_Field;
		this.str_Type = str_Type;

	}

	/**
	 * テーブル内に含まれている文字数を計測する
	 * @return 文字数
	 */
	public int getUniqueItemcount(){

		int int_recordCount;
		int int_UniqueCount;
		ArrayList<String> al_NonUniqueItemContainer = new ArrayList<String>();
		ArrayList<String> al_UniqueItemContainer = new ArrayList<String>();
		Set<String> set_UniqueItemContainer = new LinkedHashSet<String>();

		c = db.query(str_Table, str_Field, null, null, null, null, null);
		int_recordCount = c.getCount();

		//AllayListに文字列を格納

		if(int_recordCount > 0){
			c.moveToFirst();
			for(int i = 0; i < int_recordCount; i++){
				for(int j = 0; j < 9  ;j++){
					//null以外の文字列を挿入
					if(!CommonClass.isNullOrZeroLength(c.getString(j))){
						al_NonUniqueItemContainer.add(c.getString(j));
					}
				}
				c.moveToNext();
			}
		}

		//ArrayListをSetインスタンスに格納
		set_UniqueItemContainer.addAll(al_NonUniqueItemContainer);
		al_NonUniqueItemContainer.clear();
		al_UniqueItemContainer.addAll(set_UniqueItemContainer);
		int_UniqueCount = al_UniqueItemContainer.size();

		al_UniqueItemContainer.clear();
		set_UniqueItemContainer.clear();

		return int_UniqueCount;

	}


	/**
	 *
	 * @return 一意のキーワードリスト
	 */
	public ArrayList<String> getUniqueListItem(){

		int int_recordCount;
		ArrayList<String> al_NonUniqueItemContainer = new ArrayList<String>();
		ArrayList<String> al_UniqueItemContainer = new ArrayList<String>();
		Set<String> set_UniqueItemContainer = new LinkedHashSet<String>();


		c = db.query(str_Table, str_Field, null, null, null, null, null);
		int_recordCount = c.getCount();

		//AllayListに文字列を格納
		if(int_recordCount > 0){
			c.moveToFirst();
			for(int i = 0; i < int_recordCount; i++){
				for(int j = 0; j < 9  ;j++){
					//null以外の文字列を挿入
					if(!CommonClass.isNullOrZeroLength(c.getString(j))){
						al_NonUniqueItemContainer.add(c.getString(j));
					}
				}
				c.moveToNext();
			}
		}

		//ArrayListをSetインスタンスに格納
		set_UniqueItemContainer.addAll(al_NonUniqueItemContainer);
		al_NonUniqueItemContainer.clear();
		al_UniqueItemContainer.addAll(set_UniqueItemContainer);

		set_UniqueItemContainer.clear();

		return al_UniqueItemContainer;

	}

	/**
	 *
	 * @return エッジリスト
	 */
	public ArrayList<ArrayList<String>> getEdgeListItem(){

		int int_recordCount;

		ArrayList<ArrayList<String>>
			arrayListEdge = new ArrayList<ArrayList<String>>();
		ArrayList<String> pairTempEdge;
		ArrayList<String> pairMirrorTempEdge;

		this.c = this.db.query(this.str_Table, this.str_Field, null, null, null, null, null);
		int_recordCount = this.c.getCount();

		if(int_recordCount > 0){
			c.moveToFirst();
			for(int i = 0; i < int_recordCount; i++){
				for(int j = 0; j < 9  ;j++){
					//null以外の文字列を挿入
					if(j != 4 && !CommonClass.isNullOrZeroLength(c.getString(j))){
						Log.i("VertexUnique:", c.getString(j));
						pairTempEdge = new ArrayList<String>();
						pairMirrorTempEdge = new ArrayList<String>();
						pairTempEdge.add(c.getString(4));
						pairTempEdge.add(c.getString(j));
						pairMirrorTempEdge.add(c.getString(j));
						pairMirrorTempEdge.add(c.getString(4));
						if(arrayListEdge.indexOf(pairMirrorTempEdge) == -1)
							arrayListEdge.add(pairTempEdge);
					}
				}
				c.moveToNext();
			}
		}

		return arrayListEdge;
	}

	public static class IdeaMosaicMenu extends AppCompatActivity implements View.OnClickListener {
        /** Called when the activity is first created. */

        private Button btn_idea;
        private Button btn_search;
        private Button btn_mindmap;
        private Button btn_tutorial;
        private Button btn_finish;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.ideamosaic_menu);

            ActionBar abar = this.getSupportActionBar();
            abar.setTitle("");
            abar.show();

            Layout_button();
            createIdeaMosaicDB();

        }

        /**
         * DB生成メソッド
         */
        private void createIdeaMosaicDB() {
            String path = this.getDatabasePath(IdeaMosaicCommonConst.DB_NAME).getPath();
            File file = new File(path);
            IdeaMosaicDBHelper imDB = new IdeaMosaicDBHelper(this);
            if(!file.exists()){
                try {
                    imDB.createEmptyDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        public void onClick(View btn_view) {

            Intent Intent_idea = new Intent(this, IdeaMosaicListView.class);
            Intent Intent_search = new Intent(this, IdeaMosaicSearchListView.class);
            Intent Intent_mindmap = new Intent(this, IdeaMosaicMindMapListView.class);
            Intent Intent_tutorial = new Intent(this, IdeaMosaicTutorial.class);

            if(btn_view.equals(btn_idea)){
                startActivityForResult(Intent_idea, IdeaMosaicCommonConst.RequestCode_LISTVIEW_IDEA);
            }else if(btn_view.equals(btn_search)){
                startActivityForResult(Intent_search, IdeaMosaicCommonConst.RequestCode_LISTVIEW_SEARCH);
            }else if (btn_view.equals(btn_mindmap)) {
                startActivityForResult(Intent_mindmap, IdeaMosaicCommonConst.RequestCode_LISTVIEW_MINDMAP);
            }else if (btn_view.equals(btn_tutorial)) {
                startActivityForResult(Intent_tutorial, IdeaMosaicCommonConst.RequestCode_LISTVIEW_TUTORIAL);
            }else if(btn_view.equals(btn_finish)){
                finish();
            }
        }

        /**
         * レイアウト設定
         */
        private void Layout_button() {
            btn_idea = (Button)this.findViewById(R.id.button_menu_idea);
            btn_search = (Button)this.findViewById(R.id.button_menu_search);
            btn_mindmap = (Button)this.findViewById(R.id.button_menu_mindmap);
            btn_tutorial = (Button)this.findViewById(R.id.button_menu_tutorial);
            btn_finish = (Button)this.findViewById(R.id.button_menu_finish);

            btn_idea.setOnClickListener(this);
            btn_search.setOnClickListener(this);
            btn_mindmap.setOnClickListener(this);
            btn_tutorial.setOnClickListener(this);
            btn_finish.setOnClickListener(this);
        }

    }
}
