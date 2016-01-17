package satoshi.app.ideamosaic.sample;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TriggerSerendipityDBHelper extends SQLiteOpenHelper{

	//ConstClassのインスタンスを生成
	private static int db_version = 1;
	private static String DB_PATH;
	private static String DB_PATH_EXCEPT_EXTENTION;
	private static String DB_NAME = "TriggerSerendipity";
	private final static String DB_NAME_ASSET = "TriggerSerendipity.db";
	private SQLiteDatabase mDataBase;
	private final Context mContext;


	public TriggerSerendipityDBHelper(Context context) {
		super(context, DB_NAME_ASSET, null, db_version);
		// TODO 自動生成されたコンストラクター・スタブ

		this.mContext = context;
		this.DB_PATH = this.mContext.getDatabasePath(DB_NAME_ASSET).getPath();
		this.DB_PATH_EXCEPT_EXTENTION = this.DB_PATH.substring(0, this.DB_PATH.length() - 3);
	}



	/**
     * asset に格納したデータベースをコピーするための空のデータベースを作成する
     *
     **/
    public void createEmptyDataBase() throws IOException{
        boolean dbExist = checkDataBaseExists();

        if(dbExist){
            // すでにデータベースは作成されている
        }else{
            // このメソッドを呼ぶことで、空のデータベースが
            // アプリのデフォルトシステムパスに作られる
            this.getReadableDatabase();

            try {
                // asset に格納したデータベースをコピーする
                copyDataBaseFromAsset();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * 再コピーを防止するために、すでにデータベースがあるかどうか判定する
     *
     * @return 存在している場合 {@code true}
     */
    private boolean checkDataBaseExists() {
        SQLiteDatabase checkDb = null;
        try{
            checkDb = SQLiteDatabase.openDatabase(this.DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            // データベースはまだ存在していない
        }

        if(checkDb != null){
            checkDb.close();
        }
        return checkDb != null ? true : false;
    }

    /**
     * asset に格納したデーだベースをデフォルトの
     * データベースパスに作成したからのデータベースにコピーする
     * */
    private void copyDataBaseFromAsset() throws IOException{

        // asset 内のデータベースファイルにアクセス
        InputStream mInput = this.mContext.getAssets().open(DB_NAME_ASSET);

        OutputStream mOutput = new FileOutputStream(this.DB_PATH);

        // コピー
        byte[] buffer = new byte[1024];
        int size;
        while ((size = mInput.read(buffer)) > 0){
            mOutput.write(buffer, 0, size);
        }

        //Close the streams
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException{
        //Open the database
        mDataBase = SQLiteDatabase.openDatabase(DB_NAME_ASSET, null, SQLiteDatabase.OPEN_READONLY);
        return mDataBase;
    }


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.close();

        super.close();
    }

}
