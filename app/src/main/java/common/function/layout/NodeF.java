package common.function.layout;

import android.graphics.PointF;

public class NodeF {

	private String name;
	private PointF pointf;

	public NodeF() {
		// TODO 自動生成されたコンストラクター・スタブ
		this.name = "";
		this.pointf = new PointF(0, 0);
	}

	public NodeF(String name, float x, float y) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.name = name;
		this.pointf = new PointF(x, y);
	}

	public String getName() {
		return name;
	}

	public PointF getPointF() {
		return pointf;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPointF(PointF pointf) {
		this.pointf = pointf;
	}

	public void setPointF(float x, float y) {
		this.pointf.x = x;
		this.pointf.y = y;
	}

}
