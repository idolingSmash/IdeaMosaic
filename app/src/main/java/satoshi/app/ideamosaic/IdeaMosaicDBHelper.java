package satoshi.app.ideamosaic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IdeaMosaicDBHelper extends SQLiteOpenHelper{

	//ConstClassのインスタンスを生成
	static IdeaMosaicCommonConst im_comst =  new IdeaMosaicCommonConst();
	private int db_version = 1;
	private String DB_PATH;
	private String DB_PATH_EXCEPT_EXTENTION;
	private final String DB_NAME_ASSET = "IdeaMosaic.db";
	private SQLiteDatabase mDataBase;
	private final Context mContext;



	public IdeaMosaicDBHelper(Context context) {
		super(context, im_comst.getDbName(), null, im_comst.getDbVersion());
		// TODO 自動生成されたコンストラクター・スタブ
		this.mContext = context;
		this.DB_PATH = this.mContext.getDatabasePath(DB_NAME_ASSET).getPath();
		this.DB_PATH_EXCEPT_EXTENTION = this.DB_PATH.substring(0, this.DB_PATH.length() - 3);

	}

    public int getDb_version() {
        return this.db_version;
    }

    public String getDbPathExceptExtention() {
        return this.DB_PATH_EXCEPT_EXTENTION;
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
        SQLiteDatabase checkDb;
        checkDb = null;
        try{
//            String dbPath = DB_PATH + DB_NAME_ASSET;
            checkDb = SQLiteDatabase.openDatabase(this.DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
            if(checkDb != null){
                checkDb.close();
            }
        }catch(SQLiteException e){
            // データベースはまだ存在していない
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


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
//		db.execSQL(CommonDBClass.createCreateTableQuerySentence(
//				im_comst.getstrDbListtable(),
//				im_comst.getListtableFieldnames(),
//				im_comst.getListtableFieldtypes()));
//
//		db.execSQL(CommonDBClass.createCreateTableQuerySentence(
//				im_comst.getStrDbBs(),
//				im_comst.getBrainstromingFieldnames(),
//				im_comst.getBrainstromingFieldtype()
//				));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ
		db.execSQL("drop table if exists " + im_comst.getstrDbListtable());
		onCreate(db);
	}

}
