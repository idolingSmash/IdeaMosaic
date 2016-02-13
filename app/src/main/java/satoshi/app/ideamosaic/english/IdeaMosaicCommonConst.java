package satoshi.app.ideamosaic.english;

import java.util.Arrays;
import java.util.List;

/**
 * @author KSato
 */
public class IdeaMosaicCommonConst {


    // Singleton
    private IdeaMosaicCommonConst() {

    }

    //リクエストコード
    public final static int RequestCode_LISTVIEW_IDEA = 10;
    public final static int RequestCode_MATRIX_IDEA = 11;
    public final static int RequestCode_LISTVIEW_SEARCH = 20;
    public final static int RequestCode_LISTVIEW_TUTORIAL = 30;

    //<<ADD 2013/08/09
    public final static int RequestCode_LISTVIEW_MINDMAP = 40;
    public final static int RequestCode_LISTVIEW_MINDMAPCREATE = 41;
    //END>>

    //DBの定義
    public final static String DB_NAME = "IdeaMosaic.db";
    public final static String str_DB_ListTable = "im_ListTable";
    public final static String str_DB_IdeaMosaic = "IdeaMosaic";
    public final static String str_DB_IMColor = "IMColor";
    public final static String str_DB_BS = "BrainStorming";
    public final static int DB_VERSION = 1;

    //データベースのストラクチャ
    public final static String[] brainstroming_fieldNames = {"brainstorming"};
    public final static String[] brainstroming_fieldType = {"integer"};

    //データベースのストラクチャ
    public final static String[] Listtable_fieldNames = {"table_name", "table_id", "time_stamp"};
    public final static String[] Listtable_fieldTypes = {"text not null", "integer primary key", "text not null"};

    //データベースのストラクチャ
    public final static String[] Matrix_fieldNames = {
            "Matrix0",                            //ボタン：１行１列目			0
            "Matrix1",                            //ボタン：１行２列目			1
            "Matrix2",                            //ボタン：１行３列目			2
            "Matrix3",                            //ボタン：２行１列目			3
            "Matrix4_Main",                        //ボタン：２行２列目			4
            "Matrix5",                            //ボタン：２行３列目			5
            "Matrix6",                            //ボタン：３行１列目			6
            "Matrix7",                            //ボタン：３行２列目			7
            "Matrix8",                            //ボタン：３行３列目			8
            "marix_id",                            //行列ID						9
            "time_stamp",                        //タイムスタンプ				10
    };

    public final static String[] Matrix_fieldTypes = {
            "text",                                        //0
            "text",                                        //1
            "text",                                        //2
            "text",                                        //3
            "text praimary key",                        //4
            "text",                                        //5
            "text",                                        //6
            "text",                                        //7
            "text",                                        //8
            "integer praimary key",                        //9
            "integer",                                    //10
    };

    //データベースのストラクチャ
    public final static String[] MatrixButtonColor_fieldNames = {
            "Mat_Color0",                            //ボタン：１行１列目			0
            "Mat_Color1",                            //ボタン：１行２列目			1
            "Mat_Color2",                            //ボタン：１行３列目			2
            "Mat_Color3",                            //ボタン：２行１列目			3
            "Mat_Color4_Main",                        //ボタン：２行２列目			4
            "Mat_Color5",                            //ボタン：２行３列目			5
            "Mat_Color6",                            //ボタン：３行１列目			6
            "Mat_Color7",                            //ボタン：３行２列目			7
            "Mat_Color8",                            //ボタン：３行３列目			8
            "matcolor_id",                            //行列ID						9
            "time_stamp",                            //タイムスタンプ				10
    };

    public final static String[] MatrixButtonColor_fieldTypes = {
            "text",                                        //0
            "text",                                        //1
            "text",                                        //2
            "text",                                        //3
            "text",                                        //4
            "text",                                        //5
            "text",                                        //6
            "text",                                        //7
            "text",                                        //8
            "integer praimary key",                        //9
            "integer",                                    //10
    };


    //データベースストラクチャのインデックス(Matrix)
    public final static int INT_List_Index_TableName = 0;
    public final static int INT_List_Index_TableID = 1;
    public final static int INT_List_Index_TimeStamp = 2;

    //データベースストラクチャのインデックス(Matrix)
    public final static int INT_Matrix_Index_Mat0 = 0;
    public final static int INT_Matrix_Index_Mat1 = 1;
    public final static int INT_Matrix_Index_Mat2 = 2;
    public final static int INT_Matrix_Index_Mat3 = 3;
    public final static int INT_Matrix_Index_Mat4 = 4;
    public final static int INT_Matrix_Index_Mat5 = 5;
    public final static int INT_Matrix_Index_Mat6 = 6;
    public final static int INT_Matrix_Index_Mat7 = 7;
    public final static int INT_Matrix_Index_Mat8 = 8;
    public final static int INT_Matrix_Index_ID = 9;
    public final static int INT_Matrix_Index_TimeStamp = 10;

    //テキスト入力の最大文字数
    public final static int NameMaxLength_IdeaBookName = 20;
    public final static int NameMaxLength_MatrixWord = 20;

    //R.id.***をリスト化したものを定義する
    public final static List<Integer> alist_RIDButton = Arrays.asList(
            R.id.idea_button0,
            R.id.idea_button1,
            R.id.idea_button2,
            R.id.idea_button3,
            R.id.idea_button4,
            R.id.idea_button5,
            R.id.idea_button6,
            R.id.idea_button7,
            R.id.idea_button8
    );

    //R.color.***をリスト化したものを定義する
    public final static List<Integer> alist_RColor = Arrays.asList(
            R.color.TUTUJI,
            R.color.SORA,
            R.color.HIMAWARI,
            R.color.MOEGI,
            R.color.AYAME,
            R.color.KINARI,
            R.color.SUMI,
            R.color.EDOCHA,
            R.color.AKEBOMO
    );

    //色の名前をリスト化するものを定義する
    public final static List<String> alist_ColorName = Arrays.asList(
            "TUTUJI",
            "SORA",
            "HIMAWARI",
            "MOEGI",
            "AYAME",
            "KINARI",
            "SUMI",
            "EDOCHA",
            "AKEBONO"
    );

    //オプションボタンのインデックス
    public final static int OPTION_SELECT_0 = 0;
    public final static int OPTION_SELECT_1 = 1;
    public final static int OPTION_SELECT_2 = 2;
    public final static int OPTION_SELECT_3 = 3;
    public final static int OPTION_SELECT_4 = 4;
    public final static int OPTION_SELECT_5 = 5;
    public final static int OPTION_SELECT_6 = 6;

/*--					Getter and Sette					sr--*/
/*
    public int getRequestCode_LISTVIEW_IDEA() {
		return RequestCode_LISTVIEW_IDEA;
	}
	public int getRequestCode_MATRIX_IDEA() {
		return RequestCode_MATRIX_IDEA;
	}
	public int getRequestCode_LISTVIEW_SEARCH() {
		return RequestCode_LISTVIEW_SEARCH;
	}
	public int getRequestCode_LISTVIEW_TUTORIAL() {
		return RequestCode_LISTVIEW_TUTORIAL;
	}
	public int getRequestCode_LISTVIEW_MINDMAP() {
		return RequestCode_LISTVIEW_MINDMAP;
	}

	public int getRequestCode_LISTVIEW_MINDMAPCREATE() {
		return RequestCode_LISTVIEW_MINDMAPCREATE;
	}

	public int getNameMaxLength_IdeaBookName() {
		return NameMaxLength_IdeaBookName;
	}
	public int getNameMaxLength_MatrixWord() {
		return NameMaxLength_MatrixWord;
	}
	public String getDbName() {
		return DB_NAME;
	}
	public String getstrDbListtable() {
		return str_DB_ListTable;
	}
	public String getstrDbIdeamosaic() {
		return str_DB_IdeaMosaic;
	}
	public String getStrDbImcolor() {
		return str_DB_IMColor;
	}
	public String getStrDbBs() {
		return str_DB_BS;
	}
	public int getDbVersion() {
		return DB_VERSION;
	}
	public String[] getListtableFieldnames() {
		return Listtable_fieldNames;
	}
	public String[] getListtableFieldtypes() {
		return Listtable_fieldTypes;
	}
	public String[] getMatrixFieldnames() {
		return Matrix_fieldNames;
	}
	public String[] getMatrixFieldtypes() {
		return Matrix_fieldTypes;
	}
	*/
/**
 * @return matrixbuttoncolorFieldnames
 *//*

	public String[] getMatrixbuttoncolorFieldnames() {
		return MatrixButtonColor_fieldNames;
	}
	*/
/**
 * @return matrixbuttoncolorFieldtypes
 *//*

	public String[] getMatrixbuttoncolorFieldtypes() {
		return MatrixButtonColor_fieldTypes;
	}

	*/
/**
 * @return brainstromingFieldnames
 *//*

	public String[] getBrainstromingFieldnames() {
		return brainstroming_fieldNames;
	}
	*/
/**
 * @return brainstromingFieldtype
 *//*

	public String[] getBrainstromingFieldtype() {
		return brainstroming_fieldType;
	}
	*/
/**
 * @return
 *//*

	public int getIntListIndexTableName() {
		return INT_List_Index_TableName;
	}
	*/
/**
 * @return intListIndexMat1
 *//*

	public int getIntListIndexTableID() {
		return INT_List_Index_TableID;
	}
	*/
/**
 * @return intListIndexMat2
 *//*

	public int getIntListIndexTimestamp() {
		return INT_List_Index_TimeStamp;
	}
	*/
/**
 * @return intMatrixIndexMat0
 *//*

	public int getIntMatrixIndexMat0() {
		return INT_Matrix_Index_Mat0;
	}
	*/
/**
 * @return intMatrixIndexMat1
 *//*

	public int getIntMatrixIndexMat1() {
		return INT_Matrix_Index_Mat1;
	}
	*/
/**
 * @return intMatrixIndexMat2
 *//*

	public int getIntMatrixIndexMat2() {
		return INT_Matrix_Index_Mat2;
	}
	*/
/**
 * @return intMatrixIndexMat3
 *//*

	public int getIntMatrixIndexMat3() {
		return INT_Matrix_Index_Mat3;
	}
	*/
/**
 * @return intMatrixIndexMat4
 *//*

	public int getIntMatrixIndexMat4() {
		return INT_Matrix_Index_Mat4;
	}
	*/
/**
 * @return intMatrixIndexMat5
 *//*

	public int getIntMatrixIndexMat5() {
		return INT_Matrix_Index_Mat5;
	}
	*/
/**
 * @return intMatrixIndexMat6
 *//*

	public int getIntMatrixIndexMat6() {
		return INT_Matrix_Index_Mat6;
	}
	*/
/**
 * @return intMatrixIndexMat7
 *//*

	public int getIntMatrixIndexMat7() {
		return INT_Matrix_Index_Mat7;
	}
	*/
/**
 * @return intMatrixIndexMat8
 *//*

	public int getIntMatrixIndexMat8() {
		return INT_Matrix_Index_Mat8;
	}
	*/
/**
 * @return intMatrixIndexId
 *//*

	public int getIntMatrixIndexId() {
		return INT_Matrix_Index_ID;
	}
	*/
/**
 * @return intMatrixIndexTimestamp
 *//*

	public int getIntMatrixIndexTimestamp() {
		return INT_Matrix_Index_TimeStamp;
	}

	*/
/**
 * @return oPTION_SELECT_0
 *//*

	public int getOPTION_SELECT_0() {
		return OPTION_SELECT_0;
	}
	*/
/**
 * @return oPTION_SELECT_1
 *//*

	public int getOPTION_SELECT_1() {
		return OPTION_SELECT_1;
	}
	*/
/**
 * @return oPTION_SELECT_2
 *//*

	public int getOPTION_SELECT_2() {
		return OPTION_SELECT_2;
	}
	*/
/**
 * @return oPTION_SELECT_3
 *//*

	public int getOPTION_SELECT_3() {
		return OPTION_SELECT_3;
	}
	*/
/**
 * @return oPTION_SELECT_4
 *//*

	public int getOPTION_SELECT_4() {
		return OPTION_SELECT_4;
	}
	*/
/**
 * @return oPTION_SELECT_5
 *//*

	public int getOPTION_SELECT_5() {
		return OPTION_SELECT_5;
	}

	public int getOPTION_SELECT_6() {
		return OPTION_SELECT_6;
	}
*/


    /**
     * @return alist_RIDButton
     */
/*    public ArrayList<Integer> getAlist_RIDButton() {
        alist_RIDButton.add(R.id.idea_button0);
        alist_RIDButton.add(R.id.idea_button1);
        alist_RIDButton.add(R.id.idea_button2);
        alist_RIDButton.add(R.id.idea_button3);
        alist_RIDButton.add(R.id.idea_button4);
        alist_RIDButton.add(R.id.idea_button5);
        alist_RIDButton.add(R.id.idea_button6);
        alist_RIDButton.add(R.id.idea_button7);
        alist_RIDButton.add(R.id.idea_button8);
        return alist_RIDButton;
    }*/


    /**
     * @return alist_RColor
     */
/*
    public ArrayList<Integer> getAlistRcolor() {
        alist_RColor.add(R.color.TUTUJI);
        alist_RColor.add(R.color.SORA);
        alist_RColor.add(R.color.HIMAWARI);
        alist_RColor.add(R.color.MOEGI);
        alist_RColor.add(R.color.AYAME);
        alist_RColor.add(R.color.KINARI);
        alist_RColor.add(R.color.SUMI);
        alist_RColor.add(R.color.EDOCHA);
        alist_RColor.add(R.color.AKEBOMO);
        return alist_RColor;
    }
*/
    /**
     * @return alistColorname
     */
/*
    public ArrayList<String> getAlistColorname() {
        alist_ColorName.add("TUTUJI");
        alist_ColorName.add("SORA");
        alist_ColorName.add("HIMAWARI");
        alist_ColorName.add("MOEGI");
        alist_ColorName.add("AYAME");
        alist_ColorName.add("KINARI");
        alist_ColorName.add("SUMI");
        alist_ColorName.add("EDOCHA");
        alist_ColorName.add("AKEBONO");
        return alist_ColorName;
    }
*/

    /**
     * 内部テーブル名を作成
     *
     * @param int_listid リストid
     * @return テーブル名
     */
    public static String createInnerTableName(int int_listid) {
        return str_DB_IdeaMosaic + "_" + String.valueOf(int_listid);
    }

}
