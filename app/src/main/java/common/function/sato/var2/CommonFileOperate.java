package common.function.sato.var2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class CommonFileOperate {


	/***
	 *　読み込んだファイルからデータを配列として取得する
	 * @param filename	ファイル名
	 * @param encode	文字コードの形式
	 * @return read_line
	 */
	public static ArrayList<String> getReadFileContentsForArray(String filename, String encode){

        BufferedReader br_CSV;
        String read_line;
        ArrayList<String> readline_store = new ArrayList<String>();

        //ファイルの読み込み
        try {
        	//
        	//いまのところWindouws-31j
        	br_CSV = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)), encode));

            do{
            	read_line = br_CSV.readLine();
            	if(read_line != null){
            		readline_store.add(read_line);
            	}
            }while(read_line != null);

        	br_CSV.close();

        }catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return readline_store;
	}

}
