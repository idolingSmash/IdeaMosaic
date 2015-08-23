package common.function.layout;

import android.graphics.Point;

public class Node {

	private String name;
	private Point point;

	public Node() {
		// TODO 自動生成されたコンストラクター・スタブ
		this.name = "";
		this.point = new Point(0, 0);
	}

	public Node(String name, int x, int y) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.name = name;
		this.point = new Point(x, y);
	}

	public String getName() {
		return name;
	}

	public Point getPoint() {
		return point;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public void setPoint(int x, int y) {
		this.point.x = x;
		this.point.y = y;
	}

}
