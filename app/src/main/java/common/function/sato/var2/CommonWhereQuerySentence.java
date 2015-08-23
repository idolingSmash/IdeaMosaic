package common.function.sato.var2;

public class CommonWhereQuerySentence {

	private	String str_ColumnName;		//クエリ対象の項目
	private	String str_ColumnValue;		//クエリ対象の値

	/**
	 * コンストラクタ
	 * @param str_ColumnName
	 * @param str_ColumnValue
	 */
	public CommonWhereQuerySentence(String str_ColumnName, String str_ColumnValue) {
		super();
		this.str_ColumnName = str_ColumnName;
		this.str_ColumnValue = str_ColumnValue;
	}


	/***
	 *　クエリ文(where)の作成
	 * @param str_colName
	 * @param str_colV
	 * @return colName = 'str_colV'
	 */

	public String createSentence(){
		StringBuffer sb = new StringBuffer();
		sb.append(str_ColumnName);
		sb.append(" = '");
		sb.append(str_ColumnValue);
		sb.append("'");

		String convertStr = new String(sb);
		return convertStr;
	}


	/**
	 * @return str_ColumnName
	 */
	public String getStr_ColumnName() {
		return str_ColumnName;
	}

	/**
	 * @return str_ColumnValue
	 */
	public String getStr_ColumnValue() {
		return str_ColumnValue;
	}


}
