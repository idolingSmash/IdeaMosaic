package satoshi.app.ideamosaic.sample.english;

import java.util.List;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IdeaMosaicListAdapter extends ArrayAdapter<IdeaMosaicListViewOneCell>{

	private LayoutInflater inflater;

	public IdeaMosaicListAdapter(Context context, int textViewResourceId,
			List<IdeaMosaicListViewOneCell> objects) {
		super(context, textViewResourceId, objects);
		// TODO 自動生成されたコンストラクター・スタブ
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// ビューを受け取る
		View view = convertView;
		if (view == null) view = inflater.inflate(R.layout.listview_row, null);

		// 表示すべきデータの取得
		IdeaMosaicListViewOneCell Cell_item = (IdeaMosaicListViewOneCell)getItem(position);
		if (Cell_item != null) {
			TextView txt_Item = (TextView)view.findViewById(R.id.txt_ListItem);
			TextView txt_Count = (TextView)view.findViewById(R.id.txt_ItemCount);
			TextView txt_Stamp = (TextView)view.findViewById(R.id.txt_ListTimeStamp);

			txt_Item.setTypeface(Typeface.DEFAULT_BOLD);

			if (txt_Item != null) txt_Item.setText(Cell_item.getListitem());
			if (txt_Count != null) txt_Count.setText(Cell_item.getItemCountInList());
			if (txt_Stamp != null) txt_Stamp.setText(Cell_item.getTimeStamp());

		}
		return view;
	}

	@Override
	public long getItemId(int position) {
		// TODO 自動生成されたメソッド・スタブ
		return super.getItemId(position);
	}

	@Override
	public int getPosition(IdeaMosaicListViewOneCell item) {
		// TODO 自動生成されたメソッド・スタブ
		return super.getPosition(item);
	}

}
