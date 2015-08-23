package common.function.sato.var2;

public class CommonButtonOperateFlag {


	//追加・削除操作に用いるboolean
	private static boolean bool_ButtonFlag_Add;
	private static boolean bool_ButtonFlag_update;
	private static boolean bool_ButtonFlag_delete;

	//インデックス
	private static int int_ButtonDefault;
	private static int int_Update;
	private static int int_Delete;

	public CommonButtonOperateFlag() {
		// TODO 自動生成されたコンストラクター・スタブ

		bool_ButtonFlag_Add = true;
		bool_ButtonFlag_update = false;
		bool_ButtonFlag_delete = false;

		int_ButtonDefault = 0;
		int_Update = 1;
		int_Delete = 2;
	}

	/**
	 * @return bool_ButtonFlag_Add
	 */
	public static boolean isBool_ButtonFlag_Add() {
		return bool_ButtonFlag_Add;
	}
	/**
	 * @param bool_ButtonFlag_Add セットする bool_ButtonFlag_Add
	 */
	public static void setBool_ButtonFlag_Add(boolean bool_ButtonFlag_Add) {
		CommonButtonOperateFlag.bool_ButtonFlag_Add = bool_ButtonFlag_Add;
	}
	/**
	 * @return bool_ButtonFlag_update
	 */
	public static boolean isBool_ButtonFlag_update() {
		return bool_ButtonFlag_update;
	}
	/**
	 * @param bool_ButtonFlag_update セットする bool_ButtonFlag_update
	 */
	public static void setBool_ButtonFlag_update(boolean bool_ButtonFlag_update) {
		CommonButtonOperateFlag.bool_ButtonFlag_update = bool_ButtonFlag_update;
	}
	/**
	 * @return bool_ButtonFlag_delete
	 */
	public static boolean isBool_ButtonFlag_delete() {
		return bool_ButtonFlag_delete;
	}
	/**
	 * @param bool_ButtonFlag_delete セットする bool_ButtonFlag_delete
	 */
	public static void setBool_ButtonFlag_delete(boolean bool_ButtonFlag_delete) {
		CommonButtonOperateFlag.bool_ButtonFlag_delete = bool_ButtonFlag_delete;
	}

	/**
	 * @return int_ButtonDefault
	 */
	public static int getInt_ButtonDefault() {
		return int_ButtonDefault;
	}

	/**
	 * @return int_Update
	 */
	public static int getInt_Update() {
		return int_Update;
	}

	/**
	 * @return int_Delete
	 */
	public static int getInt_Delete() {
		return int_Delete;
	}



}
