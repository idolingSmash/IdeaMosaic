package satoshi.app.ideamosaic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import common.function.sato.var2.CommonClass;
import common.function.sato.var2.CommonDBClass;

public class IdeaMosaicContainUniqueItem extends CommonDBClass{

	private static SQLiteDatabase db;			//Database
	private static Cursor c;					//RecordSet
	private static String str_Table;			//テーブル名
	private static String[] str_Field;			//各種項目
	private static String[] str_Type;			//各種タイプ

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
	 * @return
	 */
	public int getUniqueItemcount(){

		int int_recordCount = 0;
		int int_UniqueCount = 0;
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
					if(CommonClass.isNullOrZeroLength(c.getString(j)) == false){
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


	public ArrayList<String> getUniqueListItem(){

		int int_recordCount = 0;
		int int_UniqueCount = 0;
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
					if(CommonClass.isNullOrZeroLength(c.getString(j)) == false){
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

//		al_UniqueItemContainer.clear();
		set_UniqueItemContainer.clear();

		return al_UniqueItemContainer;

	}

	public ArrayList<ArrayList<String>> getEdgeListItem(){

		int int_recordCount = 0;

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
					if(j != 4 && CommonClass.isNullOrZeroLength(c.getString(j)) == false){
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
}
