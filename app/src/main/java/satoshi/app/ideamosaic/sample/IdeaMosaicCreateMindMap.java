package satoshi.app.ideamosaic.sample;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import common.function.layout.KKLayout;
import common.function.layout.NodeF;
import common.function.sato.var2.CommonAlartDiagram;
import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonDeviceInfo;
import common.function.sato.var2.CommonScrollView;
import common.function.sato.var2.CommonWhereQuerySentence;

public class IdeaMosaicCreateMindMap extends AppCompatActivity implements OnClickListener{

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private String strFrontProgressMessage;
	private String initMessage;
	private String strProgressMessage = "";

	private static TextView txt_listName;
	private static TextView txt_waitMessage;

	private static ProgressBar pbar_wait;

	private static Button btn_save;
	private static Button btn_cancel;

	//Intentの定義
	private Intent im_intent;
	private Bundle im_extras;

	private static final int bmpWidth = 1000;
	private static final int bmpHeight = 1000;

	private static SQLiteDatabase db;
	private static Cursor RS;
	private static IdeaMosaicDBHelper im_DBHelp;

	//内部のテーブル名
	private static String inner_TableName;

	private static ArrayList<String> IdeaNode;
	private static ArrayList<ArrayList<String>> IdeaEdge;
	private static ArrayList<NodeF> pointNode;
	private KKLayout kklayout;
	private final int layoutProgressCount = 500;
	private int initHandlerCount = 0;

	private static IdeaMosaicCreateMindMapAsyncTask mindMapAsyncTask;
	private static CommonScrollView scrollView;
	private static RelativeLayout layoutParent;
	private static Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_mindmap);

		ActionBar abar = this.getSupportActionBar();
		abar.setTitle("");
		abar.show();
		strFrontProgressMessage = this.getString(R.string.mindmap_createmessage);
		initMessage = this.getString(R.string.mindmap_initMessage);

		layoutParent = (RelativeLayout)findViewById(R.id.linear_mindmap);
		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);


		setLayout();
		setListName();
		initDB();
		initCreateMindMap();

		initThread();		// 非同期(スレッド)処理クラスの生成

	}


	/**
	 * スレッドの初期設定
	 */
	private void initThread() {

		this.mindMapAsyncTask = new IdeaMosaicCreateMindMapAsyncTask(IdeaEdge, pointNode, pbar_wait, layoutProgressCount);
		this.mindMapAsyncTask.execute();

		handler.sendEmptyMessage(0);
	}


	/*
	 * onPause時の処理
	 */
	@Override
	public void onPause() {
		super.onPause();
		Log.v("ActivityMain", "onPause()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(handler != null)
			handler = null;
		if(RS != null)
			RS.close();
		if(db != null)
			db.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v("ActivityMain", "onResume()");

	}

	/**
	 * マインドマップを描画するメソッド
	 */
	private void draw() {
		Log.v("ActivityMain", "done()");
		txt_waitMessage.setVisibility(View.INVISIBLE);
		pbar_wait.setVisibility(View.INVISIBLE);

		IdeaMosaicCreateMindMap.mindMapAsyncTask.getKKlayout().runAdjustForGravity();
		bitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
		IdeaMosaicCreateMindMap.mindMapAsyncTask.getKKlayout().createMindMap(bitmap,
				getResources().getColor(R.color.TUTUJI),
				getResources().getColor(R.color.MOEGI),
				getResources().getColor(R.color.SUMI));
		scrollView.setVisibility(View.VISIBLE);
		scrollView.setFocusable(true);
		scrollView.setImage(Bitmap.createScaledBitmap(bitmap, (int)(bmpWidth * 2), (int)(bmpHeight * 2), false));
		btn_save.setVisibility(View.VISIBLE);

	}

	public void onClick(View view) {
		if(btn_save == view ){
			CommonAlartDiagram.ToMyAppLink(this,this.getString(R.string.pay_message));
		}else if(btn_cancel == view){
			onCancel();
		}

	}


	private void saveBitmap() throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		final String fileSd = getIMFolderPath();
		final String fileName = txt_listName.getText().toString() + "_" + System.currentTimeMillis() + ".png";

		File Im_Folder = new File(fileSd);
		File outputImageSrc = new File( fileSd , fileName);
		FileOutputStream outputStream = null;

		if(!Im_Folder.exists())		Im_Folder.mkdir();

		if(outputImageSrc.exists())		outputImageSrc.delete();

		try {
			outputStream = new FileOutputStream(outputImageSrc);
			bitmap.compress(CompressFormat.PNG, 100, outputStream);
			Toast.makeText(this, this.getString(R.string.toast_messageMindmap), Toast.LENGTH_LONG).show();
			outputStream.flush();
		} catch (FileNotFoundException e ) {
		} catch (IOException e ) {
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			}catch (IOException ex) {}
		}

	}



	private void setLayout() {

		CommonDeviceInfo deviceInfo = new CommonDeviceInfo(this);
		btn_save = (Button)this.findViewById(R.id.button_mindmap_save);
		btn_cancel = (Button)this.findViewById(R.id.button_mindmap_cancel);
		btn_save.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		txt_listName = (TextView)this.findViewById(R.id.txt_mindmap_title);
		txt_waitMessage = (TextView)this.findViewById(R.id.txt_mindmap_waitmassege);
		pbar_wait = (ProgressBar)this.findViewById(R.id.progressBar_mindmap);

		scrollView = new CommonScrollView(this);
		RelativeLayout.LayoutParams rules = new RelativeLayout.LayoutParams(WC, (int) (deviceInfo.getWindowHeight() * 0.6));
		rules.addRule(RelativeLayout.BELOW, R.id.txt_mindmap_title);
		rules.addRule(RelativeLayout.CENTER_HORIZONTAL, R.id.txt_mindmap_title);
		layoutParent.addView(scrollView, rules);

		scrollView.setVisibility(View.INVISIBLE);
		btn_save.setVisibility(View.INVISIBLE);

	}

	private void setListName() {
		// TODO 自動生成されたメソッド・スタブ
		im_intent = this.getIntent();
		im_extras = im_intent.getExtras();
		if(im_intent.hasExtra("Matirx_Idea")){
			txt_listName.setText(im_extras.getString("Matirx_Idea").toString().trim());
		}

	}

	private void initDB() {
		// TODO 自動生成されたメソッド・スタブ
		im_DBHelp = new IdeaMosaicDBHelper(this);
		db = im_DBHelp.getWritableDatabase();

		getInnerTableName();
	}


	/**
	 * マインドマップ作成時の初期値
	 */
	private void initCreateMindMap(){

		IdeaNode = new IdeaMosaicContainUniqueItem(
				db,
				RS,
				inner_TableName,
				IdeaMosaicCommonConst.Matrix_fieldNames,
				IdeaMosaicCommonConst.Matrix_fieldTypes).getUniqueListItem();

		debagRemoveIsolateNode();

		IdeaEdge
		= new IdeaMosaicContainUniqueItem(
				db,
				RS,
				inner_TableName,
				IdeaMosaicCommonConst.Matrix_fieldNames,
				IdeaMosaicCommonConst.Matrix_fieldTypes).getEdgeListItem();

		pointNode = new ArrayList<NodeF>();

		createNode(IdeaNode, pointNode);
	}

	/**
	 * 不要なノードを削除するためのメソッド
	 * ADD 2013/09/14
	 */
	private void debagRemoveIsolateNode() {
		if(inner_TableName.equals("IdeaMosaic_3") && txt_listName.getText().toString().equals("暗記力を高めるには？")){
			int searchIndex = IdeaNode.indexOf("Hero");
			if(searchIndex != -1)
				IdeaNode.remove(searchIndex);
		}
	}

	/**
	 * 内部テーブル名を取得するメソッド
	 * フィールド「inner_TableName」に格納
	 */
	private void getInnerTableName(){
		//内部テーブル名 = IdeaMosaic + ID
		inner_TableName = CommonDBClass.createTableNamePlusID(
				IdeaMosaicCommonConst.str_DB_IdeaMosaic,
				String.valueOf(getListUniqueID()));
	}

	/**
	 * 選択されたリストのUniqueIDを取得するメソッド
	 * @return 選択されたリストのUniqueID
	 */
	private int getListUniqueID(){
		//Query(Where)を作成
		CommonDBClass DB_List_Instance = new CommonDBClass(db, RS,
				IdeaMosaicCommonConst.str_DB_ListTable,
				IdeaMosaicCommonConst.Listtable_fieldNames,
				IdeaMosaicCommonConst.Listtable_fieldTypes);

		CommonWhereQuerySentence where_index_query
				= new  CommonWhereQuerySentence(
				IdeaMosaicCommonConst.Listtable_fieldNames[
						IdeaMosaicCommonConst.INT_List_Index_TableName],
				im_extras.getString("Matirx_Idea").toString());

		return DB_List_Instance.getDBUniqueIndexId(
				where_index_query,
				IdeaMosaicCommonConst.INT_List_Index_TableID);
	}

	/**
	 * SDカード内にある"IdeaMosaic"フォルダのパスを
	 * 取得するメソッド
	 * @return
	 */
	private String getIMFolderPath(){
		return new StringBuilder()
		.append(Environment.getExternalStorageDirectory().getPath())
		.append("/")
		.append("IdeaMosaic")
		.toString();
	}


	private void createNode(ArrayList<String> string, ArrayList<NodeF> node) {
		// TODO 自動生成されたメソッド・スタブ

		Iterator<String> iteratorNode = string.iterator();
		long seed = 0;
		int i = 0;
		while(iteratorNode.hasNext()){
			seed = System.currentTimeMillis() + Runtime.getRuntime().freeMemory() + 1;
			Random rand = new Random(seed);
			int x = rand.nextInt(990) + 5;
			int y = rand.nextInt(990) + 5;
			node.add(new NodeF(iteratorNode.next(), x, y));
		}

	}

	private Handler handler = new Handler() {

		String strLoading = "";
		public void handleMessage(Message msg){
			if(pbar_wait.getProgress() >= layoutProgressCount - 1){
				try {
					draw();
					Thread.sleep(1000);
					handler = null;
					txt_waitMessage.setVisibility(View.INVISIBLE);
					pbar_wait.setVisibility(View.INVISIBLE);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}else{
				if(pbar_wait.getProgress() == 0){

					strProgressMessage = strFrontProgressMessage + " " + initMessage + strLoading;
					txt_waitMessage.setText(strProgressMessage);

					strLoading = strLoading +  ">";
					initHandlerCount++;

					if(initHandlerCount > 5){
						initHandlerCount = 0;
						strLoading = "";
					}
					onResume();
				}else{
					strProgressMessage = strFrontProgressMessage + " " + String.valueOf((int)(pbar_wait.getProgress() * 100 / layoutProgressCount )) + "％";
					txt_waitMessage.setText(strProgressMessage);
				}
				if(handler != null){
					handler.sendEmptyMessageDelayed(1, 1000);
				}
			}

		}
	};

	/**
	 * バックキーの認知メソッド
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == event.KEYCODE_BACK){
			onCancel();
		}
		return false;
	};

	/**
	 * キャンセルしたときの動作メソッド
	 */
	private void onCancel(){
		this.mindMapAsyncTask.cancel(true);
		handler = null;
		if(bitmap != null){
			bitmap.recycle();
			bitmap = null;
		}
		this.finish();
	}

}

