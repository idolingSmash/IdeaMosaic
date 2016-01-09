package common.function.sato.var2;

public class CommonErrorCheck {

	private CommonErrorCheck(){

	}

    /**
     * 文字列制限を行うメソッド
     * @param str 文字列制限
     * @param max 最大値
     * @return boolean
     */
	@SuppressWarnings("unused")
	public static boolean isMaxLengthString(String str, int max){
		boolean flag = false;

		if(str.length() > max) {
			flag = true;
		}
		return flag;

	}

   /**
    * Nullであるかの判定を行うメソッド
    * @param str 文字列
    * @return boolean
    */
   public static boolean isNullString(String str){
		boolean flag = false;
		if(str.trim().equals("") || str.length() == 0) {
			flag = true;
		}
   		return flag;

   }

	/**
	 * 単語の両端を検索するメソッド
	 * @param str_src　　検索被対象のワード
	 * @param str_word　　検索対象のワード（一文字）
	 *
	 * 例） str_src = "ABC"		str_word = "\""		→　True
	 * 				  ~	  ~
	 * 		str_src = "ABC		str_word = "\""		→　False
	 * 				  ~	  ~
	 * @return boolean
	 */
	@SuppressWarnings("unused")
	public static Boolean isBothEndsChar(String str_src, String str_word){
		boolean bool = false;
		if(str_src.indexOf(str_word) == 0 && str_src.lastIndexOf(str_word) == (str_src.length() - 1)){
			bool = true;
		}
		return bool;
	}


}
