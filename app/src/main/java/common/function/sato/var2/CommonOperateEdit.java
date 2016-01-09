package common.function.sato.var2;

import android.graphics.Color;
import android.widget.Button;

public class CommonOperateEdit {

	int int_operateEdit; //編集用のフラグ
	private boolean bool_add;		//追加ボタン用のフラグ
	private boolean bool_update;	//修正ボタン用のフラグ
	private boolean bool_delete;	//削除ボタン用のフラグ

	private final static int Default = 1;
	private final static int Update = 2;
	private final static int Delete = 3;

	/**
	 * コンストラクタ
	 * @param int_operateEdit
	 *
	 * 1:追加
	 * 2:修正
	 * 3:削除
	 * それ以外は1
	 *
	 */
	public CommonOperateEdit(int int_operateEdit) {
		this.int_operateEdit = int_operateEdit;

		if(int_operateEdit < 1 && 3 < int_operateEdit ){
			this.int_operateEdit = 1;
		}

		this.bool_add = true;
		this.bool_update = false;
		this.bool_delete = false;

	}


	/****
	 * ボタンを押下した際、フォントカラーをオレンジに変える
	 * @param int_operateEdit
	 *
	 * デフォルト:1
	 * 修正:2
	 * 削除:3
	 *
	 */
	public void FontColorOrengeInSelectButton(int int_operateEdit, Button btn_udt, Button btn_del){
		setInt_operateEdit(int_operateEdit);
		switch(int_operateEdit){
			case Default:
				btn_udt.setTextColor(Color.rgb(0,0,0));
				btn_del.setTextColor(Color.rgb(0,0,0));
				changeIntoDefaultFlag();
				break;
			case Update:
				btn_udt.setTextColor(Color.rgb(200,100,0));
				btn_del.setTextColor(Color.rgb(0,0,0));
				changeIntoUpdateFlag();
				break;

			case Delete:
				btn_udt.setTextColor(Color.rgb(0,0,0));
				btn_del.setTextColor(Color.rgb(200,100,0));
				changeIntoDeleteFlag();
				break;

			default:
				btn_udt.setTextColor(Color.rgb(0,0,0));
				btn_del.setTextColor(Color.rgb(0,0,0));
				changeIntoDefaultFlag();
				break;
		}
	}

	/***
	 *
	 * ボタン用のフラグ
	 *
	 * @param int_operateEdit フラグ
	 * デフォルト:1
	 * 修正:2
	 * 削除:3
	 */
/*

	public void EditThreeFlag(int int_operateEdit){
		switch(int_operateEdit){
		case Default:	changeIntoDefaultFlag();
		break;
		case Update:	changeIntoUpdateFlag();
		break;
		case Delete:	changeIntoDeleteFlag();
		break;
		default:		changeIntoDefaultFlag();
		break;
		}
	}
*/

	/**
	 * フラグをdefault型にするメソッド
	 */
	public void changeIntoDefaultFlag(){
		this.bool_add = true; this.bool_update = false; this.bool_delete = false;
	}

	/**
	 * フラグをupdate型にするメソッド
	 */
	public void changeIntoUpdateFlag(){
		this.bool_add = false; this.bool_update = true; this.bool_delete = false;
	}

	/**
	 * フラグをdelete型にするメソッド
	 */
	public void changeIntoDeleteFlag(){
		bool_add = false; bool_update = false; bool_delete = true;
	}

	/**
	 * @return int_operateEdit
	 */
/*
	public int getInt_operateEdit() {
		return int_operateEdit;
	}
*/

	/**
	 * @param int_operateEdit セットする int_operateEdit
	 */
	public void setInt_operateEdit(int int_operateEdit) {
		this.int_operateEdit = int_operateEdit;

		if(int_operateEdit < 1 && 3 < int_operateEdit ){
			this.int_operateEdit = 1;
		}
	}

	/**
	 * フラグを判定するメソッド
	 * @param int_operateEdit フラグ
	 * @return editできるか判定
	 */
	public boolean isEditFlag(int int_operateEdit) {
		switch (int_operateEdit) {
			case 1:
				return this.bool_add && !this.bool_update && !this.bool_delete;
			case 2:
				return !this.bool_add && this.bool_update && !this.bool_delete;
			case 3:
				return !this.bool_add && !this.bool_update && this.bool_delete;
			default:
				return false;
		}
	}
}
