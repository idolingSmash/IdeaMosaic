package common.function.sato.var2;

public class CommonErrorCheck {

    /**
     * 文字列制限を行うメソッド
     * @param str
     * @param max
     * @return
     */
	public static boolean isMaxLengthString(String str, int max){

		if(str.length() > max)
			return true;
		else
			return false;

	}

   /**
    * Nullであるかの判定を行うメソッド
    * @param str
    * @return
    */
   public static boolean isNullString(String str){

   	if(str.trim() == "" || str.length() == 0)
   		return true;
   	else
   		return false;

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
	 * @return
	 */
	public static Boolean isBothEndsChar(String str_src, String str_word){
		Boolean bool = true;
		if(str_src.indexOf(str_word) == 0 && str_src.lastIndexOf(str_word) == (str_src.length() - 1)){
			bool = true;
		}else{
			bool = false;
		}
		return bool;
	}


}
