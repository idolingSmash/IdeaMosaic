package satoshi.app.ideamosaic.sample.english;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class IdeaMosaicTutorial extends Activity implements OnGestureListener, OnClickListener{

	private GestureDetector gestureDetector;
	private ViewFlipper viewFlipper;
	private Animation slideInFromLeft;
	private Animation slideInFromRight;
	private Animation slideOutToLeft;
	private Animation slideOutToRight;
	int slidecount = 1;
	final int SLIDEMAXCOUNT = 19;

	Button nextButton;
	Button backButton;
	Button menuButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ideamosaic_tutorial);

		Button_Layout();

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);

		slideInFromLeft =
				AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left);
		slideInFromRight =
				AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right);
		slideOutToLeft =
				AnimationUtils.loadAnimation(this, R.anim.slide_out_to_left);
		slideOutToRight =
				AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);

		gestureDetector = new GestureDetector(this, this);
		//        viewFlipper.setAutoStart(true);     //自動でスライドショーを開始
		//        viewFlipper.setFlipInterval(3000);  //更新間隔(ms単位)
		visibleButtonLayout();
	}

	private void Button_Layout() {
		// TODO 自動生成されたメソッド・スタブ
		backButton = (Button)findViewById(R.id.tutrial_button_previous);
		nextButton = (Button)findViewById(R.id.tutrial_button_next);
		menuButton = (Button)findViewById(R.id.tutrial_button_returntomenu);

		backButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);

	}

	private void visibleButtonLayout(){
		switch(slidecount){
		case 1:
			backButton.setVisibility(Button.GONE);
			nextButton.setVisibility(Button.VISIBLE);
			menuButton.setVisibility(Button.GONE);
			break;
		case SLIDEMAXCOUNT:
			backButton.setVisibility(Button.VISIBLE);
			nextButton.setVisibility(Button.GONE);
			menuButton.setVisibility(Button.VISIBLE);
			break;
		default:
			backButton.setVisibility(Button.VISIBLE);
			nextButton.setVisibility(Button.VISIBLE);
			menuButton.setVisibility(Button.GONE);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event)
				|| super.dispatchTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO 自動生成されたメソッド・スタブ
		float dx = Math.abs(e1.getX() - e2.getX());
		float dy = Math.abs(e1.getY() - e2.getY());
		if (dx > dy) {
			if (velocityX > 0) {
				if( slidecount != 1){
					viewFlipper.setInAnimation(slideInFromLeft);
					viewFlipper.setOutAnimation(slideOutToRight);
					viewFlipper.showPrevious();
					slidecount--;
				}
			}
			else {
				if(slidecount != SLIDEMAXCOUNT){
					viewFlipper.setInAnimation(slideInFromRight);
					viewFlipper.setOutAnimation(slideOutToLeft);
					viewFlipper.showNext();
					slidecount++;
				}
			}
			visibleButtonLayout();
			return true;
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	public void onClick(View arg0) {
		// TODO 自動生成されたメソッド・スタブ

		if(arg0 == menuButton){
			this.finish();
		}else if(arg0 == backButton){
			viewFlipper.setInAnimation(slideInFromLeft);
			viewFlipper.setOutAnimation(slideOutToRight);
			viewFlipper.showPrevious();
			slidecount--;
			visibleButtonLayout();
		}else if(arg0 == nextButton){
			viewFlipper.setInAnimation(slideInFromRight);
			viewFlipper.setOutAnimation(slideOutToLeft);
			viewFlipper.showNext();
			slidecount++;
			visibleButtonLayout();
		}

	}

}
