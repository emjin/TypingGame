package analemma.typinggame;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FallingLetters extends ActionBarActivity implements KeyEvent.Callback {

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 1; //as a fraction of letter size

    private RelativeLayout rl;
    private int visHeight; //height of visible area

    private int gameScore;
    private int numWrong;

    private Letter[] letters;
    private int[] keyEvents = {KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C, KeyEvent.KEYCODE_D,
            KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_G, KeyEvent.KEYCODE_H, KeyEvent.KEYCODE_I,
            KeyEvent.KEYCODE_J, KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L, KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_N,
            KeyEvent.KEYCODE_O, KeyEvent.KEYCODE_P, KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_S,
            KeyEvent.KEYCODE_T, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_V, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_X,
            KeyEvent.KEYCODE_Y, KeyEvent.KEYCODE_Z}; //ints representing key events


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_letters);
        gameScore = 0;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int scrWidth = size.x;
        visHeight = size.y;


        final Window mRootWindow = getWindow();
        View mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout(){
                        Rect r = new Rect();
                        View view = mRootWindow.getDecorView();
                        view.getWindowVisibleDisplayFrame(r);
                        visHeight = r.height(); //TODO ????????????
                    }
                });

        rl = (RelativeLayout) findViewById(R.id.activity_falling_letters);
        rl.setBackgroundColor(getResources().getColor(R.color.background));//black background

        //force keyboard to show. thanks stackoverflow
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //inits the letters and their positions
        int numLets = (int)(scrWidth/((1+LET_SPACING)*LET_SIZE));
        letters = new Letter[numLets];
        //int pos = 0;
        for(int i=0;i<letters.length;i++){
            letters[i] = new Letter(this); //init
            createLetter(i);
        }
    }

    //chooses and animates a letter for a given textview in lets
    private void createLetter(int i){
        letters[i].setPos(i * (1+LET_SPACING)*LET_SIZE); // set position

        //choose a letter
        int letterNum = (int) (26*Math.random()) + 'A';
        letters[i].setLet(letterNum);

        //add to layout in proper location
        TextView letterView = letters[i].getTextView();

        letterView.setX(letters[i].getPos());
        letterView.setY(-LET_SIZE);
        letterView.setTextColor(getResources().getColor(R.color.letter));
        rl.addView(letterView);
        letterView.setTextSize(LET_SIZE);
        //animate
        if(letters[i].isFirstRound()){

            letterView.animate().setStartDelay((long)(2000*Math.random()) + 2000);
            letters[i].setFirstRound(false);
        }
        letterView.animate().setDuration((long)(8000*Math.random())).y(visHeight);
        letterView.animate().setListener(new Listener(i)); //listens for the end of the animations
        //I'm assuming it breaks from this if you press the right key
        //Not sure what happens to this function since we just called another one from outside...meh
    }

    //gets the keyevent associated with given capital letter
    private int getKeyEvent(int letter){
        return keyEvents[letter - 'A'];
    }

    //implementation of KeyEvent.Callback method
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        for(int i=0;i<letters.length;i++){
            if(letters[i].getLet() != -1) {
                if (keyCode == getKeyEvent(letters[i].getLet())) {
                    gameScore++;
                    letters[i].getTextView().setText("");
                    letters[i].getTextView().animate().cancel();
                    TextView scoreText = (TextView) findViewById(R.id.score);
                    scoreText.setText("Score: " + gameScore + " ");
                    createLetter(i);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onStop(){
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        //this should hide the keyboard when the activity ends
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //inner class so we can tell when the animation ends
    public class Listener implements Animator.AnimatorListener {
        private int let; //id for the letter this is listening to

        public Listener(int l){
                     let = l;
                }

        @Override
        public void	onAnimationCancel(Animator anim){
            ImageView bloodView = new ImageView(rl.getContext());
            bloodView.setX(letters[let].getTextView().getX()-(float)(LET_SIZE*2.5));
            bloodView.setY(letters[let].getTextView().getY()-(float)(LET_SIZE*2.5));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LET_SIZE*5, LET_SIZE*5);//use real vals l8r
            bloodView.setLayoutParams(layoutParams);

            bloodView.setImageDrawable(getResources().getDrawable(R.drawable.bloodsplatter));
            rl.addView(bloodView);
        }

        @Override
        public void onAnimationRepeat(Animator anim){
        }

        @Override
        public void onAnimationEnd(Animator anim){
            letters[let].setLet(-1); //letter no longer on screen, can't type it
            rl.removeView(letters[let].getTextView());
        }

        @Override
        public void onAnimationStart(Animator anim){
        }

    }
}
