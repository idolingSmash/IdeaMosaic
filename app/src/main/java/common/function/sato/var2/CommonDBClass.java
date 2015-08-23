package common.function.sato.var2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonDBClass{


	private static SQLiteDatabase db;			//Database
	private static Cursor c;					//RecordSet
	private static String str_Table;			//テーブル名
	private static String[] str_Field;			//各種項目
	private static String[] str_Type;			//各種タイプ


	/**
	 * コンストラクタ
	 * @param db						Database
	 * @param c							RecordSet
	 * @param str_Table					テーブル名
	 * @param str_Field					各種項目
	 */
	public CommonDBClass(SQLiteDatabase db, Cursor c, String str_Table,
			String[] str_Field, String[] str_Type) {
		super();
		CommonDBClass.db = db;
		CommonDBClass.c = c;
		CommonDBClass.str_Table = str_Table;
		CommonDBClass.str_Field = str_Field;
		CommonDBClass.str_Type = str_Type;
	}

	/***
	 *
	 * 検索件数が０件であるかチェックする(全件対象の時)
	 *
	 * @return
	 */
	public boolean isZeroCountQuery(){

		//レコードセット
		c = db.query(str_Table, str_Field, null, null, null, null, null);

		if(c.getCount() == 0){
			c.close();
			return true;
		}
		else{
			c.close();
			return false;
		}
	}

	/***
	 *
	 * 検索件数が０件であるかチェックする(検索対象がある場合)
	 *
	 * @param db					データベース
	 * @param c						レコードセット
	 * @param str_Table				テーブル名
	 * @param str_Field				各項目（配列）
	 * @param str_ColumnName		クエリ対象の項目
	 * @param str_ColumnValue		クエリ対象の値
	 * @return
	 */
	public boolean isZeroCountQueryWithWhereQuery(String str_ColumnName, String str_ColumnValue){

		CommonWhereQuerySentence where_query = new CommonWhereQuerySentence(str_ColumnName, str_ColumnValue);
		//レコードセット
		c = db.query(str_Table, str_Field, where_query.createSentence(), null, null, null, null);

		if(c.getCount() == 0){
			c.close();
			return true;
		}
		else{
			c.close();
			return false;
		}
	}

	/***
	 *
	 * 検索件数が０件であるかチェックする(検索対象がある場合)
	 *
	 * @param db					データベース
	 * @param c						レコードセット
	 * @param str_Table				テーブル名
	 * @param str_Field				各項目（配列）
	 * @param str_ColumnName		クエリ対象の項目
	 * @param str_ColumnValue		クエリ対象の値
	 * @return
	 */
	public boolean isZeroCountQueryWithWhereQuery(CommonWhereQuerySentence where_query){

		//レコードセット
		c = db.query(str_Table, str_Field, where_query.createSentence(), null, null, null, null);

		if(c.getCount() == 0){
			c.close();
			return true;
		}
		else{
			c.close();
			return false;
		}
	}

	/***
	 *
	 * 検索件数が1件であるかチェックする(全件対象の時)
	 *
	 *
	 * @param db					データベース
	 * @param c						レコードセット
	 * @param str_Table				テーブル名
	 * @param str_Field				各項目（配列）
	 * @return
	 */
	public boolean isOneCountQuery(){

		//レコードセット
		c = db.query(str_Table, str_Field, null, null, null, null, null);

		if(c.getCount() == 1){
			c.close();
			return true;
		}
		else{
			c.close();
			return false;
		}
	}

	/***
	 *
	 * 検索件数が１件であるかチェックする(検索対象がある場合)
	 *
	 * @return
	 */
	public boolean isOneCountQueryWithWhereQuery(String str_ColumnName, String str_ColumnValue){

		CommonWhereQuerySentence where_query = new CommonWhereQuerySentence(str_ColumnName, str_ColumnValue);
		//レコードセット
		c = db.query(str_Table, str_Field, where_query.createSentence(), null, null, null, null);

		if(c.getCount() == 1){
			c.close();
			return true;
		}
		else{
			c.close();
			return false;
		}
	}

	/***
	 *
	 * 検索件数が１件であるかチェックする(検索対象がある場合)
	 *
	 * @return
	 */
	public boolean isOneCountQueryWithWhereQuery(CommonWhereQuerySentence where_query){

		//レコードセット
		c = db.query(str_Table, str_Field, where_query.createSentence(), null, null, null, null);

		if(c.getCount() == 1){
			c.close();
			return true;
		}
		else{
			c.close();
			return false;
		}
	}


	/***
	 *　レコードを更新するメソッド
	 *
	 * @param cv					コンテントバリュー
	 * @param str_ColumnName		クエリ対象の項目
	 * @param str_ColumnValue		クエリ対象の値
	 * @return　成功:True　失敗:False
	 */

	public boolean isRecordUpdate(ContentValues cv, String str_ColumnName, String str_ColumnValue){

		CommonWhereQuerySentence where_query = new CommonWhereQuerySentence(str_ColumnName, str_ColumnValue);

		int int_upd = db.update(str_Table, cv, where_query.createSentence(), null);
		if(int_upd != 0){
			return true;
		}else{
			return false;
		}

	}

	/***
	 *　レコードを更新するメソッド
	 *
	 * @param cv					コンテントバリュー
	 * @param str_ColumnName		クエリ対象の項目
	 * @param str_ColumnValue		クエリ対象の値
	 * @return　成功:True　失敗:False
	 */

	public boolean isRecordUpdate(ContentValues cv, CommonWhereQuerySentence where_query){

		int int_upd = db.update(str_Table, cv, where_query.createSentence(), null);
		if(int_upd != 0){
			return true;
		}else{
			return false;
		}

	}


	/**
	 *  レコードを削除するメソッド
	 *
	 * @return　成功:True　失敗:False
	 */
	public boolean isRecordDelete(String str_ColumnName, String str_ColumnValue){

		CommonWhereQuerySentence where_query = new CommonWhereQuerySentence(str_ColumnName, str_ColumnValue);

		int int_del = db.delete(str_Table, where_query.createSentence(), null);
		if(int_del != 0){
			return true;
		}else{
			return false;
		}
	}


	/**
	 *  レコードを削除するメソッド
	 *
	 * @return　成功:True　失敗:False
	 */
	public boolean isRecordDelete(CommonWhereQuerySentence where_query){

		int int_del = db.delete(str_Table, where_query.createSentence(), null);
		if(int_del != 0){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * 検索件数表示
	 * @return
	 */
	public String getDBAllCount(){
		int int_count;
		String str_count;
		c = db.query(str_Table, str_Field, null, null, null, null, null);

		int_count = c.getCount();
		str_count = String.valueOf(int_count);
		c.close();
		return str_count;
	}


	/**
	 * 検索件数表示
	 * @return
	 */
	public String getDBCount(CommonWhereQuerySentence where_query){
		int int_count;
		String str_count;

		c = db.query(str_Table, str_Field, where_query.createSentence(), null, null, null, null);

		int_count = c.getCount();
		str_count = CommonClass.IntToString(int_count);
		c.close();
		return str_count;
	}

	/**
	 * 検索件数表示
	 * @param str_ColumnName
	 * @param str_ColumnValue
	 * @return
	 */
	public String getDBCount(String str_ColumnName, String str_ColumnValue){
		int int_count;
		String str_count;
		CommonWhereQuerySentence where_query = new CommonWhereQuerySentence(str_ColumnName, str_ColumnValue);

		c = db.query(str_Table, str_Field, where_query.createSentence(),
				null, null, null, null);

		int_count = c.getCount();
		str_count = CommonClass.IntToString(int_count);
		c.close();
		return str_count;
	}



	/**
	 * 一意のIndexを取得するメソッド
	 * @param where_query
	 * @param int_IndexCol
	 * @return
	 */
	public int getDBUniqueIndexId(CommonWhereQuerySentence where_query, int int_IndexCol){

		int index = 0;

		c = db.query(str_Table, str_Field, where_query.createSentence(),
				null, null, null, null);

		//検索件数が１件の時
		if(c.getCount() == 1){
			c.moveToFirst();
			index = c.getInt(int_IndexCol);
		}

		return index;

	}

	/**
	 * 一意のIndexを取得するメソッド
	 * @param where_query
	 * @param int_IndexCol
	 * @return
	 */
	public int getDBUniqueIndexId(String str_ColumnName, String str_ColumnValue, int int_IndexCol){

		int index = 0;

		CommonWhereQuerySentence where_query = new CommonWhereQuerySentence(str_ColumnName, str_ColumnValue);

		c = db.query(str_Table, str_Field, where_query.createSentence(),
				null, null, null, null);

		//検索件数が１件の時
		if(c.getCount() == 1){
			c.moveToFirst();
			index = c.getInt(int_IndexCol);
		}

		return index;

	}


	/***
	 * テーブル作成のSQL文を生成
	 * @param DBTableName		テーブル名
	 * @param fieldNames	フィールド名（配列）
	 * @param fieldTypes	項目のタイプ（配列）
	 * @return
	 */
	public String createCreateTableQuerySentence(){

		final StringBuilder convineStringBuilder = new StringBuilder();
		final String SQL_Sentence;

		convineStringBuilder.append("create table if not exists ");
		convineStringBuilder.append(str_Table);
		convineStringBuilder.append(" (");

		for(int i = 0; i < str_Field.length ;i++){
			if(i != 0){
				convineStringBuilder.append(", ");
			}
			convineStringBuilder.append(str_Field[i]);
			convineStringBuilder.append(" ");
			convineStringBuilder.append(str_Type[i]);
		}

		convineStringBuilder.append(") ");

		SQL_Sentence = new String(convineStringBuilder);

		return SQL_Sentence;
	}


	/***
	 * テーブル作成のSQL文を生成
	 * @param DBTableName		テーブル名
	 * @param fieldNames	フィールド名（配列）
	 * @param fieldTypes	項目のタイプ（配列）
	 * @return
	 */
	public static String createCreateTableQuerySentence(String str_Table, String[] str_Field, String[] str_Type){

		final StringBuilder convineStringBuilder = new StringBuilder();
		final String SQL_Sentence;

		convineStringBuilder.append("create table if not exists ");
		convineStringBuilder.append(str_Table);
		convineStringBuilder.append(" (");

		for(int i = 0; i < str_Field.length ;i++){
			if(i != 0){
				convineStringBuilder.append(", ");
			}
			convineStringBuilder.append(str_Field[i]);
			convineStringBuilder.append(" ");
			convineStringBuilder.append(str_Type[i]);
		}

		convineStringBuilder.append(") ");

		SQL_Sentence = new String(convineStringBuilder);

		return SQL_Sentence;
	}


	/**
	 * テーブル削除のSQL文を生成
	 * @return
	 */
	public String createDeleteTableQuerySentence(){

		final StringBuilder convineStringBuilder = new StringBuilder();
		final String SQL_Sentence;

		convineStringBuilder.append("drop table if exists ");
		convineStringBuilder.append(str_Table);

		SQL_Sentence = new String(convineStringBuilder);
		return SQL_Sentence;
	}

	/**
	 * テーブル削除のSQL文を生成
	 * @param str_Table
	 * @return
	 */
	public static String createDeleteTableQuerySentence(String str_Table){

		final StringBuilder convineStringBuilder = new StringBuilder();
		final String SQL_Sentence;

		convineStringBuilder.append("drop table if exists ");
		convineStringBuilder.append(str_Table);

		SQL_Sentence = new String(convineStringBuilder);
		return SQL_Sentence;
	}

	/**
	 * テーブル名生成のメソッド
	 * @param str_src
	 * @param str_ID
	 * @return
	 */
	public static String createTableNamePlusID(String str_src, String str_ID){

		final StringBuilder convineStringBuilder = new StringBuilder();
		final String SQL_Sentence;

		convineStringBuilder.append(str_src);
		convineStringBuilder.append("_");
		convineStringBuilder.append(str_ID);

		SQL_Sentence = new String(convineStringBuilder);
		return SQL_Sentence;
	}


	/***
	 * IDの最大値を求める
	 * @param str_IDName
	 * @return
	 */
	public int getMaximumID(String str_IDName, int int_IndexId){

		int max_ID;
		c = db.query(str_Table, str_Field, null, null, null, null, null);

		//件数0の時は0を返す
		if(c.getCount() == 0){
			max_ID = 0;
		}else{
			c.close();
			c = db.query(str_Table, str_Field, null, null, null, null,  "abs("+ str_IDName + ") desc");
			c.moveToFirst();
			max_ID = c.getInt(int_IndexId);
		}
		c.close();
		return max_ID;
	}


	/***
	 * テーブルIDから欠番を探す
	 * ただし、項目がunique（一意）であることが条件
	 *
	 * @param db
	 * @param c
	 * @param str_Table
	 * @param str_Field
	 * @param str_IDName
	 * @param max
	 * @return
	 */
	public int getMissingNo(String str_IDName, int max){

		boolean miss_flag = false;
		int int_missno = 1;

		for(int i = 1; i <= max ;i++){
			if(this.isZeroCountQueryWithWhereQuery(str_IDName, String.valueOf(i)) == true){
				int_missno = i;
				miss_flag = true;
				break;
			}
		}

		if(miss_flag == false){
			int_missno = max + 1;
		}

		return int_missno;
	}


	/**
	 * OrderBy文作成メソッド
	 * @param str_colName
	 * @return [columnName] desc
	 */
	public static String createOrderByDescQuerySentence(String str_ColumnName){

		StringBuffer sb = new StringBuffer();
		sb.append(str_ColumnName);
		sb.append(" desc");

		String convertStr = new String(sb);
		return convertStr;

	}

	/**
	 * 入力した文字をContentValuesに追加するメソッド
	 * @param  table_values
	 * @param  str_ColumnName
	 * @param str_ColumnValue
	 */
	public void setDBValueInContentValues(ContentValues table_values, String str_ColumnName, String str_ColumnValue){
		table_values.put(str_ColumnName, str_ColumnValue);
	}

	/**
	 * 入力した文字をContentValuesに追加するメソッド
	 * @param  table_values
	 * @param  str_ColumnName
	 * @param str_ColumnValue
	 */
	public void setDBValueInContentValues(ContentValues table_values, CommonWhereQuerySentence where_query){
		table_values.put(where_query.getStr_ColumnName(), where_query.getStr_ColumnValue());
	}



	/**
	 * IndexをContentValuesに追加するメソッド
	 * @param  table_values
	 * @param  str_ColumnName
	 * @param table_Id
	 */
	public void setDBIndexInContentValues(ContentValues table_values, String str_ColumnName, int table_Id){
		table_values.put(str_ColumnName, table_Id);
	}

	/**
	 * TimeStampをContentValues追加するメソッド
	 * @param  table_values
	 * @param  str_ColumnName
	 */
	public void setDBTimeStampInContentValues(ContentValues table_values, String str_ColumnName){
		String timestamp = CommonClass.strsTimeStamp();	//タイムスタンプ
		table_values.put(str_ColumnName, timestamp);
	}

	/**
	 * ContentsValueをDBに挿入するメソッド
	 */
	public void InsertContentValuesInDB(ContentValues table_values, String str_Table){
		db.insert(str_Table, "", table_values);
	}

	/**
	 * カーソルを取得するメソッド
	 * @return
	 */
	public Cursor getCursor(){
		return this.c;
	}

}
