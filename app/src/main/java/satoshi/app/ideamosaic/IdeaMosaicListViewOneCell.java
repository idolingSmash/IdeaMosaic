package satoshi.app.ideamosaic;

public class IdeaMosaicListViewOneCell {

	private String Listitem;
	private String ItemCountInList;
	private String TimeStamp;

	private String itemMatrix;

	public IdeaMosaicListViewOneCell() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public IdeaMosaicListViewOneCell(String listitem, String itemCountInList,
			String timeStamp) {
		//super();
		Listitem = listitem;
		ItemCountInList = itemCountInList;
		TimeStamp = timeStamp;
	}

	public IdeaMosaicListViewOneCell(String listitem, String itemMatrix){
		this.Listitem = listitem;
		this.itemMatrix = itemMatrix;
	}

	/**
	 * @param listitem セットする listitem
	 */
	public void setListitem(String listitem) {
		Listitem = listitem;
	}

	/**
	 * @param itemCountInList セットする itemCountInList
	 */
	public void setItemCountInList(String itemCountInList) {
		ItemCountInList = itemCountInList;
	}

	/**
	 * @param timeStamp セットする timeStamp
	 */
	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

	/**
	 * @param itemMatrix セットする itemMatrix
	 * @param itemMatrix
	 */
	public void setItemMatrix(String itemMatrix){
		this.itemMatrix = itemMatrix;
	}

	/**
	 * @return listitem
	 */
	public String getListitem() {
		return Listitem;
	}

	/**
	 * @return itemCountInList
	 */
	public String getItemCountInList() {
		return ItemCountInList;
	}

	/**
	 * @return timeStamp
	 */
	public String getTimeStamp() {
		return TimeStamp;
	}

	/**
	 * @return itemMatrix
	 */
	public String getItemMatrix(){
		return this.itemMatrix;
	}

	/**
	 * セル内の文字列をクリア
	 */
	public void clear(){
		Listitem = null;
		ItemCountInList = null;
		TimeStamp = null;
		this.itemMatrix = null;
	}

}
