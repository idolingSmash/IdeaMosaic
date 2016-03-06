package satoshi.app.ideamosaic.sample.english;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import common.function.sato.var2.CommonAlartDiagram;
import common.function.sato.var2.CommonClass;
import common.function.sato.var2.CommonDBClass;
import common.function.sato.var2.CommonDraw;
import common.function.sato.var2.CommonErrorCheck;
import common.function.sato.var2.CommonToastComment;
import common.function.sato.var2.CommonWhereQuerySentence;

public class IdeaMosaicMatrixButton extends AppCompatActivity implements OnClickListener, OnTouchListener, OnDoubleTapListener, OnLongClickListener, OnGestureListener, Runnable, OnItemSelectedListener{

	static ArrayList<Button> listbtn_Matrix = new ArrayList<Button>();

	//DBの定義
	private static SQLiteDatabase db;
	private static Cursor RS;
	private static IdeaMosaicDBHelper im_DBHelp;

	//内部のテーブル名
	private static String inner_TableName;
	private static String inner_ColorTableName;

	private static EditText et_Input;

	//2回押された時のフラグ
	private GestureDetector gestureDetector;
	private Thread thread;
	private Handler nextstate_handler;
	private int int_ClickButtonIndex_fornext = 4;
	static Handler hand = new Handler();

	//Intentの定義
	Intent im_intent;
	Bundle im_extras;

	//AlartDiagram制御
	private static int AD_count = 0;
	static boolean bool_doubleclick = false;
	static boolean bool_singleclick = false;

	//BSモード用のフラグ
	private int int_BSMode = 1;

	//テキスト編集用のフラグ
	private int int_EditText = 0;

	//パンくずリスト
	private ArrayList<String> AL_BreadCrumb = new ArrayList<String>();
	private TextView txt_breadCrumb;

	//色プルダウン用
	private ArrayAdapter<CharSequence> adapter_Color;
	private Spinner spn_Color;
	private int int_selectColor;

	//検索用クエリ分
	private String querySentenceFromSearchList = null;

	//ヒント ADD 2013/09/27
	private Button btn_hint;
	private String sampleIdeaKeyword = "";

	private final String strClickHintMessageString = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {


		int im_id = 1;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_mosaic);

		this.gestureDetector = new GestureDetector(this, this);

		im_intent = this.getIntent();
		im_extras = im_intent.getExtras();


		if(im_intent.hasExtra("Matrix_Idea_Query"))
			querySentenceFromSearchList = new CommonWhereQuerySentence(
					IdeaMosaicCommonConst.Matrix_fieldNames[
														IdeaMosaicCommonConst.INT_Matrix_Index_Mat4
							]
					,im_extras.getString("Matrix_Idea_Query").toString().trim()).createSentence();

		ActionBar abar = this.getSupportActionBar();
		abar.setTitle("");
		abar.show();
		Layout_Text();
		Layout_Button();
		Layout_MatrixButton();

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		//dbの設定
		im_DBHelp = new IdeaMosaicDBHelper(this);
		db = im_DBHelp.getWritableDatabase();

		getInnerTableName();
		getInnerColorTableName();

		if(querySentenceFromSearchList != null){
			RS = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames, querySentenceFromSearchList, null, null, null, null);
		}else{
			RS = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames, null, null, null, null, null);
		}

		if (RS.getCount() != 0){
			RS.moveToFirst();
			for(int cnt = 0; cnt < 9 ;cnt++){
				listbtn_Matrix.get(cnt).setText(RS.getString(cnt));
			}
			im_id = RS.getInt(9);
		}else{
			InitDialog(this);
		}
		RS.close();

		getButtonColor(im_id);

		AL_BreadCrumb.add(listbtn_Matrix.get(4).getText().toString());		//パンくずリスト

		setTextBreadCrumb();
		InitBSMode();

	}

	/**
	 * テキスト設定
	 */
	private void Layout_Text() {

		txt_breadCrumb = (TextView) findViewById(R.id.txt_breadcrumb);
	}
	
	/**
	 * ボタン設定
	 */
	private void Layout_Button(){
		btn_hint = (Button) findViewById(R.id.btn_trigger_serendipity);
		btn_hint.setTextSize(14);
		btn_hint.setBackgroundColor(getResources().getColor(R.color.MOEGI));
		btn_hint.setText(getResources().getString(R.string.buttontext_initHintMessage));
		btn_hint.setOnClickListener(this);
	}
	
	/**
	 * ブレストモードの初期設定
	 */
	private void InitBSMode(){
		//ブレストモード
		final ContentValues table_values = new ContentValues();
		RS = db.query(IdeaMosaicCommonConst.str_DB_BS, IdeaMosaicCommonConst.brainstroming_fieldNames, null, null, null, null, null);
		if(RS.getCount() == 0){
			table_values.put(IdeaMosaicCommonConst.brainstroming_fieldNames[0], int_BSMode);
			db.insert(IdeaMosaicCommonConst.str_DB_BS, "", table_values);
		}else{
			RS.moveToFirst();
			int_BSMode = RS.getInt(0);
		}
		table_values.clear();
		RS.close();
	}

	/**
	 * パンくずテキストの作成
	 */
	private void setTextBreadCrumb(){

		StringBuilder sb = new StringBuilder();

		if(AL_BreadCrumb.size() != 0){
			for(int i = 0; i < AL_BreadCrumb.size();i++){
				if(i != 0){
					sb.append(" > ");
				}
				sb.append(AL_BreadCrumb.get(i).toString());
			}
		}
		txt_breadCrumb.setText(new String(sb));
	}



	/***
	 * 画面破棄時の動作
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		listbtn_Matrix.clear();
		RS.close();
		db.close();
	}

	@Override
	public void onClick(View v) {

		if (v == btn_hint) {
			StringBuilder sbMessageBuffer = new StringBuilder();
			long seed = Runtime.getRuntime().freeMemory(); // 空きメモリ量
			Random r = new Random(seed);
			int i_rand = r.nextInt(1000);
			if (i_rand % 5 == 0 && sampleIdeaKeyword != null) {
				sbMessageBuffer.append(this.getString(R.string.pay_hint_message));
				sbMessageBuffer.append(System.getProperty("line.separator"));
				sbMessageBuffer.append("ex)");
				sbMessageBuffer.append(System.getProperty("line.separator"));
				sbMessageBuffer.append("* Please copy [");
				sbMessageBuffer.append(sampleIdeaKeyword);
				sbMessageBuffer.append("].");
				sbMessageBuffer.append(System.getProperty("line.separator"));
				sbMessageBuffer.append("* Please move [");
				sbMessageBuffer.append(sampleIdeaKeyword);
				sbMessageBuffer.append("] by will.");
				sbMessageBuffer.append(System.getProperty("line.separator"));
				sbMessageBuffer.append("* Please protect an important place by [");
				sbMessageBuffer.append(sampleIdeaKeyword);
				sbMessageBuffer.append("].");
				CommonAlartDiagram.ToMyAppLink(this, sbMessageBuffer.toString());
			} else {
				FlashIdeaHint();
			}
		} else {
			for (int i = 0; i < 9; i++) {
				if (v == listbtn_Matrix.get(i)) {
					int_ClickButtonIndex_fornext = i;
				}
			}
		}
	}

	protected void onPause() {

		super.onPause();
		onResume();
	}

	@Override
	protected void onResume() {

		super.onResume();
		AD_count = 0;
		bool_singleclick = false;
		bool_doubleclick = false;
	}

	public boolean onDoubleTap(MotionEvent e) {

		bool_doubleclick = true;
		bool_singleclick = false;
		AD_count = 1;
		return false;
	}


	public boolean onDoubleTapEvent(MotionEvent e) {

		return false;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {

		bool_singleclick = true;
		onTouch(listbtn_Matrix.get(int_ClickButtonIndex_fornext),e);
		return false;
	}


	/**
	 * 長押ししたとき
	 */
	public boolean onLongClick(View v) {

		for(int i = 0; i < 9;i++){
			if(v == listbtn_Matrix.get(i)){
				EditText move_text = new EditText(this);
				move_text.setText(listbtn_Matrix.get(i).getText().toString());
			}
		}
		return false;
	}

	/**
	 * ダブルタッチしたとき
	 */
	public boolean onTouch(View v, MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		float lastTouchX = 0.0f;
		float currentX = 0.0f;


		if(bool_doubleclick){
			for(int i = 0;i < 9;i++){
				if(v == listbtn_Matrix.get(i)){
				if(!CommonClass.isNullOrZeroLength(listbtn_Matrix.get(i).getText().toString()) && i != 4){
						int_ClickButtonIndex_fornext = i;
						//次遷移へのアニメーション
						nextStageAnimation();
						hand.postDelayed(new Runnable() {
							public void run() {

								AL_BreadCrumb.add(listbtn_Matrix.get(int_ClickButtonIndex_fornext).getText().toString()); //パンくずリストにストック
								setTextBreadCrumb();
								NextStateMatrix(int_ClickButtonIndex_fornext);	//次の遷移のメソッド
								btn_hint.setText(getResources().getString(R.string.buttontext_initHintMessage)); //ADD 2013/09/28
								onPause();
							}
						}, 800);
					}
				}
			}
			//ボタンの配置を初期化
			for(int k = 0; k < 9 ; k++){
				listbtn_Matrix.get(k).invalidate();
			}
		}

		if(bool_singleclick && AD_count == 0){
			if(int_ClickButtonIndex_fornext != 4){
				InputDialog(this, int_ClickButtonIndex_fornext);
			}else{
				Toast.makeText(this, this.getString(R.string.toast_messageNotChanegeCentralWord), Toast.LENGTH_SHORT).show();
			}

		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastTouchX = event.getX();

			break;
		case MotionEvent.ACTION_UP:
			currentX = event.getX();
			if (lastTouchX > currentX) {
				if(AL_BreadCrumb.size() > 1)
					PreviousSatgeMatrix();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			currentX = event.getX();
			if (lastTouchX > currentX) {
				if(AL_BreadCrumb.size() > 1)
					PreviousSatgeMatrix();
			}
			break;

		}

		bool_doubleclick = false;
		bool_singleclick = false;

		return false;
	}

	private void nextStageAnimation() {

		for(int j = 0; j < 9;j++){
			AnimationSet as = new AnimationSet(true);
			RotateAnimation rotate = new RotateAnimation(0, 360, listbtn_Matrix.get(j).getWidth()/2, listbtn_Matrix.get(j).getWidth()/2); // imgの中心を軸に、0度から360度にかけて回転
			ScaleAnimation scale = new ScaleAnimation(1, 0.5f, 1, 0.5f); // imgを1倍から0.5倍に縮小
			AlphaAnimation alpha = new AlphaAnimation(1, 0.1f); // 透明度を1から0.1に変化させる

			as.addAnimation(rotate);
			as.addAnimation(scale);
			as.addAnimation(alpha);
			as.setDuration(1000); // 1000msかけてアニメーションする
			listbtn_Matrix.get(j).startAnimation(as);
		}
		
		AnimationSet asLeftFade = new AnimationSet(true);
		AlphaAnimation alphaLeftFade = new AlphaAnimation(1, 0.1f); // 透明度を1から0.1に変化させる
		long seed = Runtime.getRuntime().freeMemory(); // 空きメモリ量
		Random r = new Random(seed);
		int i_rand = r.nextInt(1000);
		int moveX = 0;
		if(i_rand % 2 == 0){
			moveX = - btn_hint.getWidth();
		}else{
			moveX = btn_hint.getWidth();
		}
		TranslateAnimation transLeftFade = new TranslateAnimation(0, moveX, 0, 0); 
		asLeftFade.addAnimation(alphaLeftFade);
		asLeftFade.addAnimation(transLeftFade);
		asLeftFade.setDuration(1000); // 1000msかけてアニメーションする
		btn_hint.startAnimation(asLeftFade);
	}

	/**
	 * プログレスダイアログの設定
	 */
	private void setWait(){
		//		// プログレスダイアログの設定
		//		waitDialog = new ProgressDialog(this);
		//		// プログレスダイアログのメッセージを設定します
		//		waitDialog.setMessage("Wait, Wait and Wait...");
		//		// 円スタイル（くるくる回るタイプ）に設定します
		//		waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//		// プログレスダイアログを表示
		//		waitDialog.show();

		thread = new Thread(this);
		/* show()メソッドでプログレスダイアログを表示しつつ、
		 * 別スレッドを使い、裏で重い処理を行う。
		 */
		thread.start();
	}

	public void run() {

		try {
			//ダイアログがしっかり見えるように少しだけスリープ
			//2秒待つ
			Thread.sleep(500);

		} catch (InterruptedException e) {
			//スレッドの割り込み処理を行った場合に発生、catchの実装は割愛
		}
		//run内でUIの操作をしてしまうと、例外が発生する為、
		//Handlerにバトンタッチ
		nextstate_handler.sendEmptyMessage(0);
	}


	public boolean onDown(MotionEvent e) {

		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {

		return false;
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
	 * 内部カラーテーブル名を取得するメソッド
	 * フィールド「inner_TableName」に格納
	 */
	private void getInnerColorTableName(){
		//内部テーブル名 = IdeaMosaic + ID
		inner_ColorTableName = CommonDBClass.createTableNamePlusID(
				IdeaMosaicCommonConst.str_DB_IMColor,
				String.valueOf(getListUniqueID()));
	}

	/**
	 * 選択されたリストのUniqueIDを取得するメソッド
	 * @return 選択されたリストのUniqueID
	 */
	private int getListUniqueID(){
		//Query(Where)を作成
		CommonDBClass DB_List_Instance = new CommonDBClass(db, RS, IdeaMosaicCommonConst.str_DB_ListTable,
				IdeaMosaicCommonConst.Listtable_fieldNames, IdeaMosaicCommonConst.Listtable_fieldTypes);
		CommonWhereQuerySentence where_index_query = new  CommonWhereQuerySentence(IdeaMosaicCommonConst.Listtable_fieldNames[
				IdeaMosaicCommonConst.INT_List_Index_TableName],
				im_extras.getString("Matirx_Idea"));
		return DB_List_Instance.getDBUniqueIndexId(where_index_query, IdeaMosaicCommonConst.INT_List_Index_TableID);
	}


	/**
	 * マトリックスボタンのレイアウトを生成
	 */
	private void Layout_MatrixButton() {


		CommonDraw int_buttonLength = new CommonDraw(this, 100);

		for(int i = 0; i < 9 ;i++){
			listbtn_Matrix.add((Button)this.findViewById(
					IdeaMosaicCommonConst.alist_RIDButton.get(i)));
			listbtn_Matrix.get(i).setWidth((int) int_buttonLength.intDipToPixcel());
			listbtn_Matrix.get(i).setHeight((int) int_buttonLength.intDipToPixcel());
			listbtn_Matrix.get(i).setOnClickListener(this);
			listbtn_Matrix.get(i).setOnLongClickListener(this);
			listbtn_Matrix.get(i).setOnTouchListener(this);
		}

	}

	/***
	 * 初期項目追加ダイアログ
	 * @param context コンテクスト
	 */
	private void InitDialog(final Context context){

		AlertDialog.Builder ad = new AlertDialog.Builder(context);

		et_Input = new EditText(context);
		et_Input.setInputType(InputType.TYPE_CLASS_TEXT);

		ad.setTitle(this.getString(R.string.dialog_titleAddKeyword));
		ad.setView(et_Input);
		Init_setPositiveButton(ad, context);
		ad.setNegativeButton(this.getString(R.string.dialog_negativeButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				finish();
			}
		});
		ad.create();
		ad.show();
	}



	/**
	 * 追加が完了したときのメソッド
	 * @param ad 		アラートダイアログ
	 * @param context	コンテクスト
	 */
	private void Init_setPositiveButton(final AlertDialog.Builder ad, final Context context){

		ad.setPositiveButton(this.getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				if(CommonClass.isNullOrZeroLength(et_Input.getText().toString())){
					//アクティブティを破棄する
					finish();
				}else{
					CommonDBClass commonDB = new CommonDBClass(db,RS,inner_TableName,
							IdeaMosaicCommonConst.Matrix_fieldNames,
							IdeaMosaicCommonConst.Matrix_fieldTypes);
					String str_IDname = IdeaMosaicCommonConst.Matrix_fieldNames[
							IdeaMosaicCommonConst.INT_Matrix_Index_ID];
					int int_Index = 							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4;
					ContentValues table_values = new ContentValues();

					commonDB.setDBValueInContentValues(
							table_values, IdeaMosaicCommonConst.Matrix_fieldNames[int_Index], et_Input.getText().toString());
					commonDB.setDBIndexInContentValues(table_values, str_IDname,
							commonDB.getMissingNo(str_IDname, commonDB.getMaximumID(str_IDname, IdeaMosaicCommonConst.INT_Matrix_Index_ID)));
					commonDB.setDBTimeStampInContentValues(table_values,
							IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_TimeStamp]);
					commonDB.InsertContentValuesInDB(table_values, inner_TableName);
					listbtn_Matrix.get(int_Index).setText(et_Input.getText().toString());

					setButtonColor(4,1);

				}
			}
		});
	}


	/***
	 * 修正ダイアログ
	 * @param context
	 * @param int_ClickButtonIndex 押したボタン
	 */

	private void InputDialog(final Context context, int int_ClickButtonIndex){

		final CommonAlartDiagram commonAD = new CommonAlartDiagram(context, et_Input,
				IdeaMosaicCommonConst.INT_Matrix_Index_ID);

		LayoutInflater factory = LayoutInflater.from(this);
		final View inputView = factory.inflate(R.layout.input_dialog, null);

		int im_id = 1;
		int color_index = 3;

		CommonWhereQuerySentence where_query = new  CommonWhereQuerySentence(IdeaMosaicCommonConst.Matrix_fieldNames[4],
				listbtn_Matrix.get(4).getText().toString().trim());

		RS = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames, where_query.createSentence(),
				null, null, null, null);
		RS.moveToFirst();
		im_id = RS.getInt(9);

		CommonWhereQuerySentence where_id_query = new  CommonWhereQuerySentence(IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[9],
				String.valueOf(im_id));

		//		Toast.makeText(this, where_id_query.createSentence(), 1).show();

		RS = db.query(inner_ColorTableName, IdeaMosaicCommonConst.MatrixButtonColor_fieldNames, where_id_query.createSentence(),
				null, null, null, null);
		RS.moveToFirst();
		//		Toast.makeText(this, RS.getString(int_ClickButtonIndex), 1).show();

		if(RS.getCount() == 0){
			color_index = 3;
		}else{
			if(RS.isNull(int_ClickButtonIndex)){
				color_index = 3;
			}else{
				color_index = convertColorNameToIndex(RS.getString(int_ClickButtonIndex));
			}
		}

		et_Input = (EditText)inputView.findViewById(R.id.et_Inpput);
		et_Input.setInputType(InputType.TYPE_CLASS_TEXT);

		adapter_Color = ArrayAdapter.createFromResource(this, R.array.str_Colors, android.R.layout.simple_spinner_item);
		adapter_Color.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_Color = (Spinner)inputView.findViewById(R.id.spn_color);
		spn_Color.setAdapter(adapter_Color);
		spn_Color.setSelection(color_index);
		spn_Color.setOnItemSelectedListener(this);

		if(listbtn_Matrix.get(int_ClickButtonIndex).getText().toString() == ""){
			commonAD.setTitle(this.getString(R.string.dialog_titleAdd));
			int_EditText = 1;
		}else{
			commonAD.setTitle(this.getString(R.string.dialog_titleUpdate));
			et_Input.setText(listbtn_Matrix.get(int_ClickButtonIndex).getText().toString());
			int_EditText = 2;
		}

		commonAD.setView(inputView);
		Input_setPositiveButton(commonAD, context, int_ClickButtonIndex);
		if(int_EditText == 2){
			Delete_setPositiveButton(commonAD, context, int_ClickButtonIndex);
		}

		commonAD.setNegativeButton(this.getString(R.string.dialog_negativeButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				/* Cancel ボタンをクリックした時の処理 */
				onPause();
			}
		});

		commonAD.create();
		commonAD.show();
		AD_count = 1;
	}

	/***
	 * 修正が完了したときのメソッド
	 * @param commonAD アラートダイアグラム
	 * @param context  コンテクスト
	 * @param int_ClickButtonIndex 押したボタン
	 */
	private void Input_setPositiveButton(CommonAlartDiagram commonAD, final Context context, final int int_ClickButtonIndex){

		final ContentValues table_values = new ContentValues();
		String str_DialogButtonText = "";
		if(int_EditText == 2){
			str_DialogButtonText = getString(R.string.dialog_positiveModifyButton);
		}
		else {
			str_DialogButtonText = getString(R.string.dialog_positiveButton);
		}

		commonAD.setPositiveButton(str_DialogButtonText,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				//未入力判定
				if(CommonErrorCheck.isNullString(et_Input.getText().toString())){
					CommonToastComment.NullString(context);
				}
				//入力した文字がセンターワードと同じ場合はエラー
				else if(listbtn_Matrix.get(
						IdeaMosaicCommonConst.INT_Matrix_Index_Mat4).getText().toString().
						equals(et_Input.getText().toString())){
					Toast.makeText(context, context.getString(R.string.toast_messageDoubleBookingWithCentralWord), Toast.LENGTH_LONG).show();
				}
				else{
					CommonDBClass commonDB = new CommonDBClass(db,
							RS,
							inner_TableName,
							IdeaMosaicCommonConst.Matrix_fieldNames,
							IdeaMosaicCommonConst.Matrix_fieldTypes);

					//中央のワードをキーとしたインスタンス
					CommonWhereQuerySentence where_coreword_query = new CommonWhereQuerySentence(
							IdeaMosaicCommonConst.Matrix_fieldNames[							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4],
							listbtn_Matrix.get(							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4).getText().toString());

					commonDB.setDBValueInContentValues(table_values,
							IdeaMosaicCommonConst.Matrix_fieldNames[int_ClickButtonIndex],
							et_Input.getText().toString());
					commonDB.setDBTimeStampInContentValues(table_values, IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_TimeStamp]);

					//ボタンの更新
					if(commonDB.isRecordUpdate(table_values, where_coreword_query)){

						if(int_EditText == 1){
							CommonToastComment.AddItem(context, et_Input.getText().toString());
						}else if(int_EditText == 2){
							if(!et_Input.getText().toString().equals(
									listbtn_Matrix.get(int_ClickButtonIndex).getText().toString())){
								CommonToastComment.UpdateItem(context, listbtn_Matrix.get(int_ClickButtonIndex).getText().toString(),et_Input.getText().toString());
							}
						}
						listbtn_Matrix.get(int_ClickButtonIndex).setText(et_Input.getText().toString());
						setButtonColor(int_ClickButtonIndex,
								commonDB.getDBUniqueIndexId(where_coreword_query, IdeaMosaicCommonConst.INT_Matrix_Index_ID));
					}else{
						CommonToastComment.FalseUpdate(context);
					}

				}

				onPause();
			}
		}
				);
	}

	/**
	 * ボタンの色をセットするメソッド
	 * @param position 押した場所
	 * @param im_id マトリックスID
	 */
	private void setButtonColor(int position, int im_id) {


		CommonDBClass colorDB = new CommonDBClass(db, RS,
				inner_ColorTableName,
								IdeaMosaicCommonConst.MatrixButtonColor_fieldNames,
				IdeaMosaicCommonConst.MatrixButtonColor_fieldTypes);

		ContentValues cv_color = new ContentValues();
		CommonWhereQuerySentence where_color_query = new CommonWhereQuerySentence(
								IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[position],
				IdeaMosaicCommonConst.alist_ColorName.get(int_selectColor));

		CommonWhereQuerySentence where_colorid_query = new CommonWhereQuerySentence(
								IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[9],
				String.valueOf(im_id));

		colorDB.setDBValueInContentValues(cv_color, where_color_query);
		colorDB.setDBTimeStampInContentValues(cv_color, 				IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[10]);

		if(colorDB.isOneCountQueryWithWhereQuery(where_colorid_query)){
			//更新処理
			if(colorDB.isRecordUpdate(cv_color, where_colorid_query)){
				listbtn_Matrix.get(position).setBackgroundColor(
						getResources().getColor(
								IdeaMosaicCommonConst.alist_RColor.get(int_selectColor)));
			}else{
				listbtn_Matrix.get(position).setBackgroundColor(
						getResources().getColor(IdeaMosaicCommonConst.alist_RColor.get(3)));//萌黄色
			}
		}else{
			//追加処理
			colorDB.setDBIndexInContentValues(cv_color, IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[9], im_id);
			colorDB.InsertContentValuesInDB(cv_color, inner_ColorTableName);
			//			Toast.makeText(this, String.valueOf(im_id), Toast.LENGTH_SHORT).show();
		}
	}


	/**
	 * ボタン色を得るメソッド
	 * @param im_id
	 */
	private void getButtonColor(int im_id){

		int int_colorindex = 3;	 //初期値は萌黄色

		CommonWhereQuerySentence where_id_query = new CommonWhereQuerySentence(
								IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[9], String.valueOf(im_id));

		RS = db.query(inner_ColorTableName, IdeaMosaicCommonConst.MatrixButtonColor_fieldNames,
				where_id_query.createSentence(), null, null, null, null);

		//				RS.moveToFirst();
		//				Toast.makeText(this,  "convert = "+ String.valueOf(convertColorNameToIndex(RS.getString(4))), Toast.LENGTH_SHORT).show();

		if(RS.getCount() == 1){
			RS.moveToFirst();
			for(int i = 0; i < 9 ;i++){
				if(RS.getString(i) == null){
					if(i == 4){
						listbtn_Matrix.get(i).setBackgroundColor(getResources().getColor(
								IdeaMosaicCommonConst.alist_RColor.get(0)));
					}else{
						listbtn_Matrix.get(i).setBackgroundColor(getResources().getColor(
								IdeaMosaicCommonConst.alist_RColor.get(3)));
					}
				}
				else{
					int_colorindex = convertColorNameToIndex(RS.getString(i));
					listbtn_Matrix.get(i).setBackgroundColor(getResources().getColor(
							IdeaMosaicCommonConst.alist_RColor.get(int_colorindex)));
				}
			}

		}else{
			for(int i = 0; i < 9 ;i++){
				if(i == 4){
					listbtn_Matrix.get(i).setBackgroundColor(getResources().getColor(
							IdeaMosaicCommonConst.alist_RColor.get(0)));
				}else{
					listbtn_Matrix.get(i).setBackgroundColor(getResources().getColor(
							IdeaMosaicCommonConst.alist_RColor.get(3)));
				}
			}
		}
		RS.close();

	}

	/**
	 * 色名称　→　色インデックスに変換するメソッド
	 * @param i_color
	 * @return
	 */
	private int convertColorNameToIndex(String i_color){

		if(i_color.equals("TUTUJI"))
			return 0;
		else if(i_color.equals("SORA"))
			return 1;
		else if(i_color.equals("HIMAWARI"))
			return 2;
		else if(i_color.equals("MOEGI"))
			return 3;
		else if(i_color.equals("AYAME"))
			return 4;
		else if(i_color.equals("KINARI"))
			return 5;
		else if(i_color.equals("SUMI"))
			return 6;
		else if(i_color.equals("EDOCHA"))
			return 7;
		else if(i_color.equals("AKEBONO"))
			return 8;
		else
			return 3;
	}


	/**
	 * 削除が完了したときのメソッド
	 * @param commonAD
	 * @param context
	 * @param int_ClickButtonIndex
	 */
	private void Delete_setPositiveButton(CommonAlartDiagram commonAD,
			final Context context, final int int_ClickButtonIndex) {

		final ContentValues table_values_text = new ContentValues();
		final ContentValues table_values_color = new ContentValues();

		commonAD.setNeutralButton(context.getString(R.string.dialog_deleteButton),new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				int im_id = 0;
				Cursor RS2;
				CommonDBClass commonDB = new CommonDBClass(db,
						RS,
						inner_TableName,
						IdeaMosaicCommonConst.Matrix_fieldNames,
						IdeaMosaicCommonConst.Matrix_fieldTypes);

				//中央のワードをキーとしたインスタンス
				CommonWhereQuerySentence where_coreword_query = new CommonWhereQuerySentence(
						IdeaMosaicCommonConst.Matrix_fieldNames[							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4],
						listbtn_Matrix.get(							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4).getText().toString());

				commonDB.setDBValueInContentValues(table_values_text,
						IdeaMosaicCommonConst.Matrix_fieldNames[int_ClickButtonIndex],
						"");
				commonDB.setDBTimeStampInContentValues(table_values_text,
						IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_TimeStamp]);

				RS2 = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames, where_coreword_query.createSentence(),
						null, null, null, null);
				RS2.moveToFirst();
				im_id = RS2.getInt(9);

				//ボタンの更新
				if(commonDB.isRecordUpdate(table_values_text, where_coreword_query) == true){
					CommonToastComment.DeleteItem(context, et_Input.getText().toString());
					listbtn_Matrix.get(int_ClickButtonIndex).setText("");
				}else{
					CommonToastComment.FalseUpdate(context);//3:萌黄色
				}

				CommonDBClass commonDBcolor = new CommonDBClass(db,
						RS2,
						inner_ColorTableName,
										IdeaMosaicCommonConst.MatrixButtonColor_fieldNames,
						IdeaMosaicCommonConst.MatrixButtonColor_fieldTypes);

				commonDBcolor.setDBValueInContentValues(table_values_color,
										IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[int_ClickButtonIndex],
						IdeaMosaicCommonConst.alist_ColorName.get(3));
				commonDBcolor.setDBTimeStampInContentValues(table_values_color,
										IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[10]);

				CommonWhereQuerySentence where_color_query = new CommonWhereQuerySentence(
										IdeaMosaicCommonConst.MatrixButtonColor_fieldNames[9],
						String.valueOf(im_id));

				if(commonDBcolor.isRecordUpdate(table_values_color, where_color_query)){
					listbtn_Matrix.get(int_ClickButtonIndex).setBackgroundColor(getResources().getColor(IdeaMosaicCommonConst.alist_RColor.get(3)));
				}
				RS2.close();
				onPause();
			}
		}
				);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0, IdeaMosaicCommonConst.OPTION_SELECT_0, 0, R.string.optionmenu_previousCard).setIcon(android.R.drawable.ic_menu_revert);;
//		menu.add(0, im_comst.getOPTION_SELECT_1(), 0, R.string.optionmenu_hint).setIcon(R.drawable.ic_menu_notifications);
		menu.add(0, IdeaMosaicCommonConst.OPTION_SELECT_2, 0, R.string.optionmenu_searchKeyword).setIcon(android.R.drawable.ic_menu_search);
		menu.add(0, IdeaMosaicCommonConst.OPTION_SELECT_3, 0, R.string.optionmenu_outputCSV).setIcon(android.R.drawable.ic_menu_save);
		menu.add(0, IdeaMosaicCommonConst.OPTION_SELECT_4, 0, R.string.optionmenu_saveDB).setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(0, IdeaMosaicCommonConst.OPTION_SELECT_5, 0, R.string.optionmenu_shiftBSmode).setIcon(android.R.drawable.ic_menu_slideshow);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId()){
		case 0:
			//一つ前のキーワードへ戻るメソッドを呼び出す
			if(AL_BreadCrumb.size() > 1){
				PreviousSatgeMatrix();
			}
			break;
//		case 1:
//			//アイディアのヒントを出すメソッドを呼び出す
//			FlashIdeaHint();
//			break;				DEL : 2013/09/28
		case 2:
			CommonAlartDiagram.ToMyAppLink(this,this.getString(R.string.pay_message));
			break;
		case 3:
			CommonAlartDiagram.ToMyAppLink(this,this.getString(R.string.pay_message));
			break;
		case 4:
			CommonAlartDiagram.ToMyAppLink(this,this.getString(R.string.pay_message));
			break;
		case 5:
			setBrainStormingMode();
			break;
		default:
			break;
		}

		return true;
	}

	/**
	 * ブレストモードに切り替えるメソッド
	 */
	private void setBrainStormingMode() {


		String[] str_items = {getString(R.string.dialog_radioBSON),getString(R.string.dialog_radioBSOFF)};
		final ContentValues table_values = new ContentValues();

		AlertDialog.Builder	ad = new Builder(this);
		ad.setSingleChoiceItems(str_items, int_BSMode, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				int_BSMode = which;
			}
		});
		ad.setPositiveButton(this.getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {

				//BSModeテーブルにフラグを格納
				RS = db.query(IdeaMosaicCommonConst.str_DB_BS, IdeaMosaicCommonConst.brainstroming_fieldNames, null, null, null, null, null);
				if(RS.getCount() != 0){
					RS.moveToFirst();
					table_values.put(IdeaMosaicCommonConst.brainstroming_fieldNames[0], int_BSMode);
					db.update(IdeaMosaicCommonConst.str_DB_BS, table_values,
							IdeaMosaicCommonConst.brainstroming_fieldNames[0] +" = '"+ String.valueOf(RS.getInt(0)) +"'", null);
					table_values.clear();
				}
				RS.close();
			}
		});
		ad.setNegativeButton(this.getString(R.string.dialog_negativeButton), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ad.create();
		ad.show();

	}

	private void OutputDBFile() throws FileNotFoundException {

		final String fileDb = this.getDatabasePath(
				IdeaMosaicCommonConst.DB_NAME).getPath();
		final String fileSd = getIMFolderPath() + "/" + IdeaMosaicCommonConst.DB_NAME;

		FileChannel channelSource = new FileInputStream(fileDb).getChannel();
		FileChannel channelTarget = new FileOutputStream(fileSd).getChannel();

		try {
			channelSource.transferTo(0, channelSource.size(), channelTarget);
			Toast.makeText(this, this.getString(R.string.toast_messageOutputDB)
					, Toast.LENGTH_SHORT).show();
			channelSource.close();
			channelTarget.close();
		} catch (IOException e) {
			Toast.makeText(this, this.getString(R.string.toast_messageFailToOutputDB), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	/**
	 * CSVファイル出力を確認するアラート画面をセットするメソッド
	 */
	private void setAlertConfirmOutputCSV() {

		AlertDialog.Builder ad_outputCSV = new AlertDialog.Builder(this);
		ad_outputCSV.setTitle(this.getString(R.string.dialog_titleOutputCSV));
		ad_outputCSV.setMessage(this.getString(R.string.dialog_messageOutputCSV));
		ad_outputCSV.setPositiveButton(this.getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				try {
					OutputCSVFile();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
				}
			}
		});
		ad_outputCSV.setNegativeButton(this.getString(R.string.dialog_negativeButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		ad_outputCSV.create();
		ad_outputCSV.show();

	}

	/**
	 * CSVファイル出力を確認するアラート画面をセットするメソッド
	 */
	private void setAlertConfirmOutputDB() {

		AlertDialog.Builder ad_outputDB = new AlertDialog.Builder(this);
		ad_outputDB.setTitle(this.getString(R.string.dialog_titleOutputDB));
		ad_outputDB.setMessage(this.getString(R.string.dialog_messageOutputDB));
		ad_outputDB.setPositiveButton(this.getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				try {
					OutputDBFile();
				} catch (IOException e) {
					// TODO 自動生成された catch ブロック
				}
			}
		});
		ad_outputDB.setNegativeButton(this.getString(R.string.dialog_negativeButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		ad_outputDB.create();
		ad_outputDB.show();

	}

	/**
	 * 次の画面へ擬似的に遷移するメソッド
	 * @param int_ClickButtonIndex
	 */
	private void NextStateMatrix(int int_ClickButtonIndex) {

		CommonDBClass commonDB = new CommonDBClass(db,
				RS,
				inner_TableName,
				IdeaMosaicCommonConst.Matrix_fieldNames,
				IdeaMosaicCommonConst.Matrix_fieldTypes);

		CommonWhereQuerySentence where_iscenter_query = new CommonWhereQuerySentence(
				IdeaMosaicCommonConst.Matrix_fieldNames[							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4],
				listbtn_Matrix.get(int_ClickButtonIndex).getText().toString());

		CommonWhereQuerySentence where_addaround_query = new CommonWhereQuerySentence(
				IdeaMosaicCommonConst.Matrix_fieldNames[Math.abs(int_ClickButtonIndex - 8)],
				listbtn_Matrix.get(							IdeaMosaicCommonConst.INT_Matrix_Index_Mat4).getText().toString());

		final ContentValues table_values = new ContentValues();

		String str_IDname = IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_ID];

		//センターワードに該当するクエリがない
		if(commonDB.isZeroCountQueryWithWhereQuery(where_iscenter_query) == true){
			//センターワード登録（レコードを追加する。）
			commonDB.setDBValueInContentValues(table_values,where_iscenter_query);

			if(int_BSMode == 0){
				commonDB.setDBValueInContentValues(table_values,where_addaround_query);
			}
			commonDB.setDBIndexInContentValues(table_values, str_IDname,
					commonDB.getMissingNo(str_IDname, commonDB.getMaximumID(str_IDname, IdeaMosaicCommonConst.INT_Matrix_Index_ID)));
			commonDB.setDBTimeStampInContentValues(table_values,
					IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_TimeStamp]);
			commonDB.InsertContentValuesInDB(table_values, inner_TableName);
		}

		//指定したクエリをもとに各ボタンへテキストを代入
		InsertTextInMatrixButton(where_iscenter_query);

	}

	/**
	 * CSVファイルを出力するメソッド
	 */
	private void OutputCSVFile() throws IOException {

		final String fileSd = getIMFolderPath();
		final String fileName = im_extras.getString("Matirx_Idea").toString() + ".csv";
		RS = db.query(inner_TableName,
				IdeaMosaicCommonConst.Matrix_fieldNames,
				null, null, null, null, null);
		File Im_Folder = new File(fileSd);
		File output_csv = new File( fileSd + "/" + fileName); // CSVデータファイル

		if(Im_Folder.exists() == false){
			Im_Folder.mkdir();
		}
		if(output_csv.exists() == true){
			output_csv.delete();
		}

		BufferedWriter bw
		= new BufferedWriter(new FileWriter(output_csv, true));
		if(RS.getCount() == 0){
			Toast.makeText(this, this.getString(R.string.toast_messageFailToOutputCSV), Toast.LENGTH_SHORT).show();
		}else{
			RS.moveToFirst();
			for(int i = 0; i < RS.getCount() ;i++){
				bw.write(convertRecordToStringWithComma(RS));
				bw.newLine();
				RS.moveToNext();
			}
			bw.close();
			Toast.makeText(this, this.getString(R.string.toast_messageOutputCSVForward) +
					fileName + this.getString(R.string.toast_messageOutputCSVBackward)
					, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * レコードをカンマ区切りの文字列にするメソッド
	 * @return
	 */
	private String convertRecordToStringWithComma(Cursor C){
		StringBuilder sb_oneline = new StringBuilder();

		//1レコードの内容をString化
		for(int j = 0; j < 9 ;j++){
			if(C.getString(j) != null){
				sb_oneline.append(C.getString(j));
			}
			if(j < 8){
				//カンマで区切る
				sb_oneline.append(",");
			}
		}
		return new String(sb_oneline);

	}

	/**
	 * キーワードを検索するメソッド
	 */
	private void SearchIdeaItem() {

		AlertDialog.Builder ad_outputDB = new AlertDialog.Builder(this);
		ad_outputDB.setTitle(this.getString(R.string.dialog_titleSearch));
		ad_outputDB.setMessage(this.getString(R.string.dialog_messageSearch));

		et_Input = new EditText(this);
		et_Input.setInputType(InputType.TYPE_CLASS_TEXT);
		ad_outputDB.setView(et_Input);
		ad_outputDB.setPositiveButton(this.getString(R.string.dialog_positiveButton), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				CommonWhereQuerySentence where_center_query =
						new CommonWhereQuerySentence(IdeaMosaicCommonConst.Matrix_fieldNames[4], et_Input.getText().toString());
				boolean bool_search = false;

				//中央のキーワードを検索
				RS = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames, where_center_query.createSentence(),
						null, null, null, null);
				if(RS.getCount() == 1){
					RS.moveToFirst();
					for(int cnt = 0; cnt < 9 ;cnt++){
						listbtn_Matrix.get(cnt).setText(RS.getString(cnt));
					}
					AL_BreadCrumb.clear();
					AL_BreadCrumb.add(RS.getString(4));
					setTextBreadCrumb();
					RS.close();
					bool_search = true;
					Toast.makeText(IdeaMosaicMatrixButton.this, R.string.toast_messageHitKeyword,
							Toast.LENGTH_SHORT).show();
				}
				if(bool_search == false){
					//周辺のキーワードを検索
					for(int i = 0; i < 9 ;i++){
						if(i != 4){
							RS = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames,
									new CommonWhereQuerySentence(IdeaMosaicCommonConst.Matrix_fieldNames[i], et_Input.getText().toString()).createSentence(),
									null, null, null, null);
							if(RS.getCount() != 0){
								RS.moveToFirst();
								for(int cnt = 0; cnt < 9 ;cnt++){
									listbtn_Matrix.get(cnt).setText(RS.getString(cnt));
								}
								AL_BreadCrumb.clear();
								AL_BreadCrumb.add(RS.getString(4));
								setTextBreadCrumb();

								RS.close();
								bool_search = true;
								Toast.makeText(IdeaMosaicMatrixButton.this,R.string.toast_messageHitKeyword,
										Toast.LENGTH_SHORT).show();
								break;
							}
						}
					}

					if(bool_search == false){
						Toast.makeText(IdeaMosaicMatrixButton.this,
								R.string.toast_messageNotFoundKeyword,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		ad_outputDB.setNegativeButton(R.string.dialog_negativeButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		ad_outputDB.create();
		ad_outputDB.show();
	}

	private void FlashIdeaHint() {

		String src_hintSentence = "";
		String[] replaceWord = {"※","☆","＃"};

		//テーブル内のキーワードを抽出
		ArrayList<String> al_uniqueKeyword
				= new IdeaMosaicContainUniqueItem(db, RS, inner_TableName,
				IdeaMosaicCommonConst.Matrix_fieldNames, IdeaMosaicCommonConst.brainstroming_fieldType).getUniqueListItem();
		Collections.shuffle(al_uniqueKeyword);
		long seed = Runtime.getRuntime().freeMemory(); // 空きメモリ量
		Random r = new Random(seed);
		int i_rand = r.nextInt(1000);
		switch(i_rand % 10){
			case 0:
			case 1:
			case 2:
			case 3:
				src_hintSentence = "Please raise worth of ※.";
				break;
			case 4:
			case 5:
				src_hintSentence = "Please feed back ※.";
				break;
			case 6:
			case 7:
				src_hintSentence = "Please let me finish ※ for a short time.";
				break;
			case 8:
			case 9:
				src_hintSentence = "Please automate ※.";
				break;
			default:
				src_hintSentence = "Please raise worth of ※.";
				break;
		}

		src_hintSentence = src_hintSentence.replaceAll(replaceWord[0], "[" + al_uniqueKeyword.get(0) + "]");
		sampleIdeaKeyword = al_uniqueKeyword.get(0);

		btn_hint.setText(src_hintSentence);
	}

	/**
	 * 前の画面に戻るメソッド
	 */
	private void PreviousSatgeMatrix() {


		int int_BreadCrumb_Length = AL_BreadCrumb.size();

		//前のセンターワードのクエリを取得する
		CommonWhereQuerySentence where_previous_query = new CommonWhereQuerySentence(
				IdeaMosaicCommonConst.Matrix_fieldNames[IdeaMosaicCommonConst.INT_Matrix_Index_Mat4],
				AL_BreadCrumb.get(int_BreadCrumb_Length - 2));

		//前のセンターワードに戻る
		InsertTextInMatrixButton(where_previous_query);

		//パンくずの末尾を削除する
		AL_BreadCrumb.remove(int_BreadCrumb_Length - 1);
		setTextBreadCrumb();
		btn_hint.setText(getResources().getString(R.string.buttontext_initHintMessage)); //ADD 2013/09/28
	}



	/**
	 * DBデータをマトリックスボタンに反映する
	 * @param where_query
	 */
	private void InsertTextInMatrixButton(CommonWhereQuerySentence where_query){

		int im_id = 1;

		RS = db.query(inner_TableName, IdeaMosaicCommonConst.Matrix_fieldNames, where_query.createSentence(),
				null, null, null, null);

		if(RS.getCount() == 1){
			RS.moveToFirst();
			for(int cnt = 0; cnt < 9 ;cnt++){
				listbtn_Matrix.get(cnt).setText(RS.getString(cnt));
			}
			im_id = RS.getInt(9);

		}
		RS.close();

		getButtonColor(im_id);
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

	/**
	 * ここで選択された色のインデックスを取得する
	 */
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		Spinner spinner = (Spinner) arg0;
		int_selectColor = spinner.getSelectedItemPosition();
	}

	public void onNothingSelected(AdapterView<?> arg0) {


	}

}