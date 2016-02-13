package satoshi.app.ideamosaic;

import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import common.function.layout.KKLayout;
import common.function.layout.NodeF;

public class IdeaMosaicCreateMindMapAsyncTask extends AsyncTask<Void, Void, String>{

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private ArrayList<ArrayList<String>> IdeaEdge;
	private ArrayList<NodeF> pointNode;
	private KKLayout kklayout;
	private int stepCount = 200;
	private ProgressBar pBar;

	public IdeaMosaicCreateMindMapAsyncTask( ArrayList<ArrayList<String>> IdeaEdge, ArrayList<NodeF> pointNode, ProgressBar pBar, int count) {
		this.pBar = pBar;
		this.IdeaEdge = IdeaEdge;
		this.pointNode = pointNode;
		this.stepCount = count;
	}

	/*
	 * 実行前の事前処理
	 */
	@Override
	protected void onPreExecute() {

		this.pBar.setMax(stepCount);
		this.pBar.incrementProgressBy(0);
		return;
	}

	/*
	 * バックグラウンドで実行する処理
	 *
	 *  @param params: Activityから受け渡されるデータ
	 *  @return onPostExecute()へ受け渡すデータ
	 */
	@Override
	protected String doInBackground(Void... params) {

		String ret = "";

		try {
			// Sleep処理（例：HTTP通信）
			Thread.sleep(1000);

			createMindMap();
			// 取得してきた文字列・画像などをreturnでonPostExecuteへ渡す
			ret = "読み込み完了！";
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return ret;
	}

	private void createMindMap() {
		this.kklayout = new KKLayout(this.pointNode, this.IdeaEdge, 1000, 1000);

		for(int i = 0; i < stepCount ;i++){
			this.kklayout.runStep();
			this.pBar.setProgress(i);
			if(isCancelled()) break;
		}

	}

	/**
	 * KKレイアウトを取得するメソッド
	 * @return
	 */
    public KKLayout getKKlayout(){
    	return this.kklayout;
    }


}
