package common.function.sato.var2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

public class CommonClass extends Activity{



	/***
	 *  数値から文字列型へ変換するメソッド
	 * @param i
	 * @return
	 */

	public static String IntToString(int i){

		final String str;

		str = String.valueOf(i);

		return str;
	}


	/***
	 *  ブール型から文字列型へ変換するメソッド
	 * @param i
	 * @return
	 */

	public static String BoolToString(boolean b){

		final String str;

		str = String.valueOf(b);

		return str;
	}



	/**
	 * null文字判定のメソッド
	 * @param s 判定したい文字列
	 * @return
	 */
	public static boolean isNullOrZeroLength(String s) {
	    return TextUtils.isEmpty(s);
	}

	/**
	 * ひとつ前のアクティブティに戻るアラート表示メソッド
	 * showReturnDialog作成
	 * @param context
	 * @param title
	 * @param msg
	 */

	public static void showReturnDialog(Context context, String title, String msg, final Activity act){

		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(msg);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
				act.finish();
			}
		});
		ad.create();
		ad.show();
	}


	/**
	 *
	 * showDialog作成
	 * @param context
	 * @param title
	 * @param msg
	 */

	public static void showDialog(Context context, String title, String msg){

		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle(title);
		ad.setMessage(msg);
		ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO 自動生成されたメソッド・スタブ
			}
		});
		ad.create();
		ad.show();
	}


    /**
     * DBファイルをSDカードにコピーする
     * AndroidManifest.xmlにWRITE_EXTERNAL_STORAGEを設定すること
     *
     * @param Context context メソッド呼び出し元(Activity等)のContext
     * @param String dbName コピー元となるデータベースファイル名
     * @return コピーに成功した場合true
     * @throws IOException なんかエラーが起きた場合にthrow
     */
    public static boolean copyDb2Sd(Context context, String dbName) throws IOException {

        //保存先(SDカード)のディレクトリを確保
        String pathSd = new StringBuilder()
                            .append(Environment.getExternalStorageDirectory().getPath())
                            .append("/")
                            .append(context.getPackageName())
                            .toString();
        File filePathToSaved = new File(pathSd);
        if (!filePathToSaved.exists() && !filePathToSaved.mkdirs()) {
            throw new IOException("FAILED_TO_CREATE_PATH_ON_SD");
        }

        final String fileDb = context.getDatabasePath(dbName).getPath();
        final String fileSd = new StringBuilder()
                                .append(pathSd)
                                .append("/")
                                .append(dbName)
                                .toString();
        showDialog(context, "表示", "copy from(DB): "+fileDb);
        showDialog(context, "表示", "copy to(SD)  : "+fileSd);

        FileChannel channelSource = new FileInputStream(fileDb).getChannel();
        FileChannel channelTarget = new FileOutputStream(fileSd).getChannel();

        channelSource.transferTo(0, channelSource.size(), channelTarget);

        channelSource.close();
        channelTarget.close();

        return true;
    }

    /**
     * DBファイルをSDカードにコピーする
     * AndroidManifest.xmlにWRITE_EXTERNAL_STORAGEを設定すること
     *
     * @param Context context メソッド呼び出し元(Activity等)のContext
     * @param String dbName コピー元となるデータベースファイル名
     * @return コピーに成功した場合true
     * @throws IOException なんかエラーが起きた場合にthrow
     */
    public static boolean copyAssets2DB(Context context, String dbName) throws IOException {

        String fileAssets = context.getAssets().open(dbName).toString();
        String fileDb = context.getDatabasePath(dbName).getPath();

        showDialog(context, "表示", "copy from(Assets): "+fileAssets);
        showDialog(context, "表示", "copy to(DB)  : "+fileDb);

        FileChannel channelSource = new FileInputStream(fileAssets).getChannel();
        FileChannel channelTarget = new FileOutputStream(fileDb).getChannel();

        channelSource.transferTo(0, channelSource.size(), channelTarget);

        channelSource.close();
        channelTarget.close();

        return true;
    }

    /***
     *
     * 現在時刻を発行するタイムスタンプ関数
     *
     * 年月日 " " 時分秒ミリ秒
     *
     * @return タイムスタンプ文字列
     */

    public static String strsTimeStamp(){

    	//現在の時刻を取得
		final long currentTimeMillis = System.currentTimeMillis();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTimeMillis);

    	//日付取得
		final String str_date = String.format("%04d" , calendar.get(Calendar.YEAR)) + "/" +
							String.format("%02d" , calendar.get(Calendar.MONTH) + 1) + "/" +
								String.format("%02d" , calendar.get(Calendar.DATE));

		//時間取得(文字列)
		final String str_time = String.format("%02d" , calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
				String.format("%02d" , calendar.get(Calendar.MINUTE)) + ":" +
						String.format("%02d" , calendar.get(Calendar.SECOND)) + "." +
						String.format("%02d" , calendar.get(Calendar.MILLISECOND));

		return str_date + " " + str_time;

    }


    /***
     * エディットボックスの文字数制限を付加
     * @param ed エディットボックス
     * @param max 最大文字数
     */
    public static void Text_MaxLength (EditText ed, int  max){

		InputFilter[] maxlength_inputFilter = new InputFilter[1];
		maxlength_inputFilter[0] = new InputFilter.LengthFilter(max);
	    ed.setFilters(maxlength_inputFilter);

    }

}
