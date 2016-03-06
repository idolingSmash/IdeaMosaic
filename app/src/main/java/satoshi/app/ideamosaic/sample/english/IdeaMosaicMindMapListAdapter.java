package satoshi.app.ideamosaic.sample.english;

import java.util.List;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IdeaMosaicMindMapListAdapter extends ArrayAdapter<IdeaMosaicListViewOneCell>{

	private LayoutInflater inflater;

	public IdeaMosaicMindMapListAdapter(Context context, int textViewResourceId,
			List<IdeaMosaicListViewOneCell> objects) {
		super(context, textViewResourceId, objects);
		// TODO 自動生成されたコンストラクター・スタブ
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// ビューを受け取る
		View view = convertView;
		if (view == null) view = inflater.inflate(R.layout.mindmaplistview_row, null);

		// 表示すべきデータの取得
		IdeaMosaicListViewOneCell Cell_item = (IdeaMosaicListViewOneCell)getItem(position);
		if (Cell_item != null) {
			TextView txt_Item = (TextView)view.findViewById(R.id.txt_MindMapListItem);

			txt_Item.setTypeface(Typeface.DEFAULT_BOLD);

			if (txt_Item != null) txt_Item.setText(Cell_item.getListitem());
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
