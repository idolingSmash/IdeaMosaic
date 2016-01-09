package common.function.sato.var2;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class CommonAlartDiagram extends AlertDialog.Builder{

	private Context context;		//コンテンツ
	private EditText et_Text; 		//エディットボックス
	private int int_Index_ID;

	/**
	 * コンストラクタ（EditTextがある場合）
	 * @param context コンテクスト
	 * @param et_Text テキスト
	 */
	public CommonAlartDiagram(Context context, EditText et_Text, int int_Index_ID) {
		super(context);
		this.context = context;
		this.et_Text = et_Text;
		this.int_Index_ID = int_Index_ID;
	}

	/**
	 * 入力した文字列を判定するメソッド
	 *
	 * 【エラー条件】
	 * 1>入力した文字列が未入力
	 * 2>入力した文字列が既にDB内に存在する
	 *
	 * @return flag
	 */
	public ErrorCheckFlag isErrorCheck(CommonDBClass DBInstance, String db_IDname, CommonWhereQuerySentence where_query){

		ErrorCheckFlag check_flag = new ErrorCheckFlag();

		//未入力判定
		if(isEditTextNullString()){
			CommonToastComment.NullString(context);
		}else{
			//空テーブル判定
			if(DBInstance.isZeroCountQuery()){
				check_flag.setTable_ID(1);
				check_flag.setCheck_flag(true);
				CommonToastComment.AddItem(context, et_Text.getText().toString());
			}else{
				if(DBInstance.isZeroCountQueryWithWhereQuery(where_query)){
					//欠番のIndexを挿入
					check_flag.setTable_ID(DBInstance.getMissingNo(db_IDname, DBInstance.getMaximumID(db_IDname, int_Index_ID)));

					check_flag.setCheck_flag(true);
					CommonToastComment.AddItem(context, et_Text.getText().toString());
				}else{
 					CommonToastComment.FalseAdd(context, et_Text.getText().toString());
				}
			}
		}
		return check_flag;
	}

	/**
	 * 入力した文字列を判定するメソッド
	 *
	 * 【エラー条件】
	 * 1>入力した文字列が未入力
	 * 2>入力した文字列が既にDB内に存在する
	 *
	 * @return フラグ
	 */
	public ErrorCheckFlag isErrorCheckWithoutMessage(CommonDBClass DBInstance, String db_IDname, CommonWhereQuerySentence where_query){

		ErrorCheckFlag check_flag = new ErrorCheckFlag();

		//未入力判定
		if(!isEditTextNullString()){
			//空テーブル判定
			if(DBInstance.isZeroCountQuery()){
				check_flag.setTable_ID(1);
				check_flag.setCheck_flag(true);
			}else{
				if(DBInstance.isZeroCountQueryWithWhereQuery(where_query)){
					//欠番のIndexを挿入
					check_flag.setTable_ID(DBInstance.getMissingNo(db_IDname, DBInstance.getMaximumID(db_IDname, int_Index_ID)));

					check_flag.setCheck_flag(true);
				}
			}
		}
		return check_flag;
	}


	/**
	 * エディットボックスがNullであるかチェックするメソッド
	 * @return flag
	 */
	public boolean isEditTextNullString(){
		boolean flag = false;
		if(CommonErrorCheck.isNullString(et_Text.getText().toString())){
			flag = true;
		}
		return flag;
	}

	public class ErrorCheckFlag{

		private boolean check_flag;
		private int table_ID;

		public ErrorCheckFlag() {
			this.check_flag = false;
			this.table_ID = 1;
		}

		/**
		 * @return check_flag
		 */
		public boolean isCheck_flag() {
			return check_flag;
		}
		/**
		 * @param check_flag セットする check_flag
		 */
		public void setCheck_flag(boolean check_flag) {
			this.check_flag = check_flag;
		}
		/**
		 * @return table_ID
		 */
		public int getTable_ID() {
			return table_ID;
		}
		/**
		 * @param table_ID セットする table_ID
		 */
		public void setTable_ID(int table_ID) {
			this.table_ID = table_ID;
		}

	}

}
