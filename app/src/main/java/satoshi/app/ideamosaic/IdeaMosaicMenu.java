package satoshi.app.ideamosaic;

import java.io.File;
import java.io.IOException;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import common.function.sato.var2.CommonClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class IdeaMosaicMenu extends Activity implements OnClickListener {
	/** Called when the activity is first created. */

	private ImageView im99;
	private Button btn_idea;
	private Button btn_search;
	private Button btn_mindmap;
	private Button btn_tutorial;
	private Button btn_finish;
	private AdView adView;

	IdeaMosaicCommonConst commonConst = new IdeaMosaicCommonConst();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_menu);

		Layout_button();
//		Layout_addBarner();		本番系は未実装
		createIdeaMosaicDB();
	}

//	private void Layout_addBarner() {
//		// adView を作成する
//		adView = new AdView(this, AdSize.BANNER, "a1513b3a75207b9");
//		// 属性 android:id="@+id/mainLayout" が与えられているものとして
//		// LinearLayout をルックアップする
//		// adView を追加
//		adView.loadAd(new AdRequest());
//		LinearLayout adPosition = (LinearLayout) findViewById(R.id.admob_menu);
//		adPosition.addView(adView);
//	}

	private void createIdeaMosaicDB() {
		// TODO 自動生成されたメソッド・スタブ

		String path = this.getDatabasePath(new IdeaMosaicCommonConst().getDbName()).getPath();
		File file = new File(path);
		IdeaMosaicDBHelper imDB = new IdeaMosaicDBHelper(this);

		if(!file.exists()){
			try {
				imDB.createEmptyDataBase();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}


	public void onClick(View btn_view) {
		// TODO 自動生成されたメソッド・スタブ

		Intent Intent_idea = new Intent(this, satoshi.app.ideamosaic.IdeaMosaicListView.class);
		Intent Intent_search = new Intent(this, satoshi.app.ideamosaic.IdeaMosaicSearchListView.class);
		Intent Intent_mindmap = new Intent(this, satoshi.app.ideamosaic.IdeaMosaicMindMapListView.class);
		Intent Intent_tutorial = new Intent(this, satoshi.app.ideamosaic.IdeaMosaicTutorial.class);

		if(btn_view.equals(btn_idea) == true){
			startActivityForResult(Intent_idea, commonConst.getRequestCode_LISTVIEW_IDEA());
		}else if(btn_view.equals(btn_search) == true){
			startActivityForResult(Intent_search, commonConst.getRequestCode_LISTVIEW_SEARCH());
		}else if (btn_view.equals(btn_mindmap) == true) {
			startActivityForResult(Intent_mindmap, commonConst.getRequestCode_LISTVIEW_MINDMAP());
		}else if (btn_view.equals(btn_tutorial) == true) {
			startActivityForResult(Intent_tutorial, commonConst.getRequestCode_LISTVIEW_TUTORIAL());
		}else if(btn_view.equals(btn_finish) == true){
			finish();
		}
	}

	private void Layout_button() {
		// TODO 自動生成されたメソッド・スタブ

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