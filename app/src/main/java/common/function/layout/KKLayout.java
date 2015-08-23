package common.function.layout;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.Log;

import edu.umbc.cs.maple.utils.JamaUtils;

import Jama.Matrix;

public class KKLayout {

	private static double EPSILON = 0.1d;
	private static double L;			// the ideal length of an edge
	private static double K = 1;		// arbitrary const number
	private static boolean exchangeVertices = true;
	protected static double diameter;
	private double length_factor = 0.9;
	private static double disconnected_multiplier = 0.5;

	private static int countStep = 1000;

	private static ArrayList<NodeF> nodeList = new ArrayList<NodeF>();
	private ArrayList<ArrayList<String>> IdeaEdge = new ArrayList<ArrayList<String>>();
	private static Matrix shortestPathMatrix;
	private static Matrix distanceMatrix;     // distance matrix
	private static int width = 0;
	private static int height = 0;


	public KKLayout(ArrayList<NodeF> nodeList,
			ArrayList<ArrayList<String>> ideaEdge, int width, int height) {
		this.nodeList = nodeList;
		this.IdeaEdge = ideaEdge;
		this.width = width;
		this.height = height;
		initialize();
	}

	public void countStep(int cnt){
		if(0 < cnt){
			int i = 0;
			do{
				step();
			}while(i < cnt);
		}
		else{
			Log.e("countMinus", "指定されたカウントがマイナスです。");
		}

		adjustForGravity(width, height);
	}

	public void countStep(){
		int cnt = 0;
		do{
			step();
		}while(cnt < KKLayout.countStep);

		adjustForGravity(width, height);
	}

	public void runStep(){
		step();
	}

	public void runAdjustForGravity(){
		adjustForGravity(width, height);
	}

	public void createMindMap(Bitmap bitmap, int nodeColorID, int edgeColorID, int fontColorID){

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);

		// Paintの作成
		Paint paint;
		paint = new Paint();

		paint.setColor(edgeColorID);
		paintEdge(canvas, paint);

		paint.setColor(nodeColorID);
		paintNode(canvas, paint);

		paint.setColor(fontColorID);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);
		paintText(canvas, paint);

	}


	private void initialize(){

		int L0 = 1000;
		Matrix incidenceMatrix = createIncidenceMatrix();
		shortestPathMatrix = createShortestMatrix(incidenceMatrix);

		diameter = JamaUtils.getMax(shortestPathMatrix);
		L = (L0 / diameter) * length_factor;  // length_factor used to be hardcoded to 0.9
		distanceMatrix = calcDiatanceMatrix(shortestPathMatrix);
	}

	private static void step() {
		// TODO 自動生成されたメソッド・スタブ
		double energy = calcEnergy();
		double maxDeltaM = 0.0D;
		int cnt = 0;
		int pm = -1;

		for(int i = 0; i < shortestPathMatrix.getRowDimension();i++){
			double deltam = calcDeltaM(i);
			if(maxDeltaM < deltam ){
				maxDeltaM = deltam;
				pm = i;
			}
		}

		if(pm != -1){
			do{
				double dxy[] = calcDeltaXY(pm);
				nodeList.get(pm).setPointF(
						nodeList.get(pm).getPointF().x + (float)dxy[0],
						nodeList.get(pm).getPointF().y + (float)dxy[1]);
				double deltam = calcDeltaM(pm);
				if(deltam < EPSILON)
					break;
				cnt++;
			}while(cnt < 100);
		}

		if (exchangeVertices && maxDeltaM < EPSILON) {
			energy = calcEnergy();
			for (int i = 0; i < nodeList.size() - 1; i++) {
				for (int j = i + 1; j < nodeList.size(); j++) {
					double xenergy = calcEnergyIfExchanged(i, j);
					if (energy > xenergy) {
						double sx = nodeList.get(pm).getPointF().x;
						double sy = nodeList.get(pm).getPointF().y;
						nodeList.get(i).setPointF(
								nodeList.get(j).getPointF().x,
								nodeList.get(j).getPointF().y);
						nodeList.get(j).setPointF((float)sx, (float)sy);
						return;
					}
				}
			}
		}
	}



	/**
	 * 隣接行列を作成するメソッド
	 * @return
	 */
	private Matrix createIncidenceMatrix() {
		Matrix incidenceMatrix = new Matrix(nodeList.size(), nodeList.size());
		for(int i = 0; i < nodeList.size() ;i++){
			for(int j = 0; j < IdeaEdge.size() ;j++){
				if(nodeList.get(i).getName().equals(IdeaEdge.get(j).get(0))){
					for(int k = 0; k < nodeList.size() ;k++)
						if(IdeaEdge.get(j).get(1).equals(nodeList.get(k).getName()))
							incidenceMatrix.set(i, k, 1.0);

				}else if(nodeList.get(i).getName().equals(IdeaEdge.get(j).get(1))){
					for(int k = 0; k < nodeList.size() ;k++)
						if(IdeaEdge.get(j).get(0).equals(nodeList.get(k).getName()))
							incidenceMatrix.set(i, k, 1.0);
				}
			}
		}

		return incidenceMatrix;
	}

	/**
	 * 最短経路行列を作成するメソッド
	 * @param mat
	 * @return
	 */
	private static Matrix createShortestMatrix(Matrix mat) {
		Matrix shortestPathMat = (Matrix) mat.clone();

		//初期設定
		for(int i = 0; i < shortestPathMat.getRowDimension() ;i++)
			for(int j = 0; j < shortestPathMat.getColumnDimension();j++)
				if(i != j && shortestPathMat.get(i, j) == 0)
						shortestPathMat.set(i, j ,100.0);


		for(int k = 0; k < shortestPathMat.getRowDimension() ;k++)
			for(int i = 0; i < shortestPathMat.getRowDimension() ;i++)
				for(int j = 0; j < shortestPathMat.getColumnDimension();j++)
					shortestPathMat.set(i,j, Math.min(shortestPathMat.get(i, j), shortestPathMat.get(i, k) + shortestPathMat.get(k, j) ));

		return shortestPathMat;
	}

	/**
	 * 距離行列を作成するメソッド
	 * @param mat
	 * @return
	 */
	private static Matrix calcDiatanceMatrix(Matrix mat){
		Matrix dm = new Matrix(mat.getRowDimension(), mat.getColumnDimension());
		double dist = diameter * disconnected_multiplier;

		for(int i = 0; i < mat.getRowDimension() ;i++){
			for(int j = 0; j < mat.getColumnDimension() ;j++){
				if(i != j)
					dm.set(i, j, Math.min(mat.get(i, j), dist));
			}
		}
		return dm;
	}


	/**
	 * Determines a step to new position of the vertex m.
	 */
	private static double[] calcDeltaXY(int m) {
		double dE_dxm = 0;
		double dE_dym = 0;
		double d2E_d2xm = 0;
		double d2E_dxmdym = 0;
		double d2E_dymdxm = 0;
		double d2E_d2ym = 0;

		for (int i = 0; i < nodeList.size(); i++) {
			if (i != m) {

				double dist = distanceMatrix.get(m, i);
				double l_mi = L * dist;
				double k_mi = K / (dist * dist);
				double dx = nodeList.get(m).getPointF().x - nodeList.get(i).getPointF().x;
				double dy = nodeList.get(m).getPointF().y - nodeList.get(i).getPointF().y;
				double d = Math.sqrt(dx * dx + dy * dy);
				double ddd = d * d * d;

				dE_dxm += k_mi * (1 - l_mi / d) * dx;
				dE_dym += k_mi * (1 - l_mi / d) * dy;
				d2E_d2xm += k_mi * (1 - l_mi * dy * dy / ddd);
				d2E_dxmdym += k_mi * l_mi * dx * dy / ddd;
				d2E_d2ym += k_mi * (1 - l_mi * dx * dx / ddd);
			}
		}
		// d2E_dymdxm equals to d2E_dxmdym.
		d2E_dymdxm = d2E_dxmdym;

		double denomi = d2E_d2xm * d2E_d2ym - d2E_dxmdym * d2E_dymdxm;
		double deltaX = (d2E_dxmdym * dE_dym - d2E_d2ym * dE_dxm) / denomi;
		double deltaY = (d2E_dymdxm * dE_dxm - d2E_d2xm * dE_dym) / denomi;
		return new double[]{deltaX, deltaY};
	}


	private static double calcDeltaM(int m) {
		double dEdxm = 0;
		double dEdym = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			if (i != m) {
				double dist = distanceMatrix.get(m, i);
				double l_mi = L * dist;
				double k_mi = K / (dist * dist);
				double dx = nodeList.get(m).getPointF().x - nodeList.get(i).getPointF().x;
				double dy = nodeList.get(m).getPointF().y - nodeList.get(i).getPointF().y;
				double d = Math.sqrt(dx * dx + dy * dy);

				double common = k_mi * (1 - l_mi / d);
				dEdxm += common * dx;
				dEdym += common * dy;
			}
		}
		return Math.sqrt(dEdxm * dEdxm + dEdym * dEdym);
	}


	private static double calcEnergy() {
		double energy = 0.0D;

		for(int i = 0; i < distanceMatrix.getRowDimension() - 1;i++){
			for(int j = 0; j < distanceMatrix.getColumnDimension();j++){
				double	 dist = distanceMatrix.get(i, j);
				double	l_ij = L * dist;
				double	k_ij = K / Math.pow(dist , 2.0);
				double  dx = nodeList.get(i).getPointF().x - nodeList.get(j).getPointF().x;
				double  dy = nodeList.get(i).getPointF().y - nodeList.get(j).getPointF().y;
				double	d = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
				energy += k_ij / 2 * (dx * dx + dy * dy + l_ij * l_ij -  2 * l_ij * d);
			}
		}
		return energy;
	}

	/**
	 * Calculates the energy function E as if positions of the
	 * specified vertices are exchanged.
	 */
	private static double calcEnergyIfExchanged(int p, int q) {
		if (p >= q)
			throw new RuntimeException("p should be < q");
		double energy = 0;		// < 0
		for (int i = 0; i < nodeList.size() - 1; i++) {
			for (int j = i + 1; j < nodeList.size(); j++) {
				int ii = i;
				int jj = j;
				if (i == p) ii = q;
				if (j == q) jj = p;

				double dist = distanceMatrix.get(i, j);
				double l_ij = L * dist;
				double k_ij = K / (dist * dist);
				double  dx = nodeList.get(ii).getPointF().x - nodeList.get(jj).getPointF().x;
				double  dy = nodeList.get(ii).getPointF().y - nodeList.get(jj).getPointF().y;
				double d = Math.sqrt(dx * dx + dy * dy);
				energy += k_ij / 2 * (dx * dx + dy * dy + l_ij * l_ij -
						2 * l_ij * d);
			}
		}
		return energy;
	}


	private void paintNode(Canvas canvas, Paint paint) {
		for(int i = 0; i < nodeList.size()  ;i++)
			canvas.drawCircle(nodeList.get(i).getPointF().x, nodeList.get(i).getPointF().y, setPointScale(), paint);
	}

	private void paintText(Canvas canvas, Paint paint){
		float txt_x = 0;
		float txt_y = 0;
		for(int i = 0; i < nodeList.size()  ;i++){
			if( nodeList.get(i).getPointF().x < 5)
				txt_x = nodeList.get(i).getPointF().x + 6;
			else
				txt_x = nodeList.get(i).getPointF().x - 4;

			if( nodeList.get(i).getPointF().y < 5)
				txt_y = nodeList.get(i).getPointF().y + 6;
			else
				txt_y = nodeList.get(i).getPointF().y - 4;

			paint.setTextSize(setTextScale());
			canvas.drawText(nodeList.get(i).getName(), txt_x, txt_y, paint);
		}
	}


	private void paintEdge(Canvas canvas, Paint paint) {
		PointF lineA = new PointF();
		PointF lineB = new PointF();
		float strokeWidth = paint.getStrokeWidth();
		paint.setStrokeWidth(2.0f);
		Iterator<ArrayList<String>> iteratorEdge = IdeaEdge.iterator();
		while(iteratorEdge.hasNext()){
			ArrayList<String> tempEdge = iteratorEdge.next();
			boolean boolVisible = true;
			for(int i = 0; i < nodeList.size();i++){
				if(nodeList.get(i).getName().equals(tempEdge.get(0))){
					lineA = nodeList.get(i).getPointF();
					boolVisible = true;
					break;
				}
				else{
					boolVisible = false;
				}
			}
			for(int i = 0; i < nodeList.size();i++){
				if(nodeList.get(i).getName().equals(tempEdge.get(1))){
					lineB = nodeList.get(i).getPointF();
					boolVisible = true;
					break;
				}
				else{
					boolVisible = false;
				}
			}

			if(boolVisible)
				canvas.drawLine(lineA.x, lineA.y, lineB.x, lineB.y, paint);
		}

		paint.setStrokeWidth(strokeWidth);
	}

	/**
	 * Shift all vertices so that the center of gravity is located at
	 * the center of the screen.
	 */
	private void adjustForGravity(double width, double height) {
		double gx = 0;
		double gy = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			gx += nodeList.get(i).getPointF().x;
			gy += nodeList.get(i).getPointF().y;
		}
		gx /= nodeList.size();
		gy /= nodeList.size();
		double diffx = width / 2 - gx;
		double diffy = height / 2 - gy;
		for (int i = 0; i < nodeList.size(); i++) {
			nodeList.get(i).setPointF((float)(nodeList.get(i).getPointF().x + diffx)
					,(float)(nodeList.get(i).getPointF().y + diffy));
		}
	}

	private int setPointScale(){
		int scale = 5;
		int nodeSize = nodeList.size();
		if(nodeSize < 50 ){
			scale = 10;
		}else if( 50 <= nodeSize   &&  nodeSize < 100){
			scale = 9;
		}else if( 100 <= nodeSize   &&  nodeSize < 150){
			scale = 8;
		}else if( 150 <= nodeSize   &&  nodeSize < 200){
			scale = 7;
		}else if( 200 <= nodeSize   &&  nodeSize < 250){
			scale = 6;
		}else if( 250 <= nodeSize){
			scale = 5;
		}
		return scale;
	}

	private int setTextScale(){
		int size = 12;
		int nodeSize = nodeList.size();
		if(nodeSize < 50 ){
			size = 24;
		}else if( 50 <= nodeSize   &&  nodeSize < 100){
			size = 22;
		}else if( 100 <= nodeSize   &&  nodeSize < 150){
			size = 20;
		}else if( 150 <= nodeSize   &&  nodeSize < 200){
			size = 16;
		}else if( 200 <= nodeSize   &&  nodeSize < 250){
			size = 14;
		}else if( 250 <= nodeSize){
			size = 12;
		}
		return size;
	}


}
