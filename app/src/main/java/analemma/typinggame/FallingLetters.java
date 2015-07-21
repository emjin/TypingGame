package analemma.typinggame;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FallingLetters extends ActionBarActivity implements KeyEvent.Callback {

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 2; //as a fraction of letter size

    private static final int MIN_SPEED = 2;
    private static final int SPEED_RANGE = 3;

    private RelativeLayout rl;
    private int visHeight; //bc i need it all over the place - height of visible area
    private int scrWidth;

    private int gameScore;
    private int numDeaths;

    private int[] currLets; //letters that are currently on screen, as ints from 'A' to 'Z'
    private LetterView[] lets;
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

        //Get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        scrWidth = size.x;
        visHeight = size.y;

        //force keyboard to show. thanks stackoverflow
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        startGame();
    }

    private void startGame() {
        //Initialize game stuff
        gameScore = 0;
        numDeaths = 0;

        //Initialize letters
        int numLets = (int)(scrWidth/((1+LET_SPACING)*LET_SIZE));
        lets = new LetterView[numLets];
        for(int i = 0; i < numLets; i++) {
            String letter = " "  +((int)(Math.random()*keyEvents.length) + 'A');
            float dy = (float)(Math.random()*SPEED_RANGE)+MIN_SPEED;
            lets[i] = new LetterView(this, visHeight, dy, letter);

            lets[i].setLetterX((float)(Math.random()*(scrWidth-lets[i].getX())));
            lets[i].setLetterY((float)(-Math.random()*visHeight)); //this will cause the delays
            lets[i].letter.setTextSize(LET_SIZE);
            lets[i].letter.setTextColor(getResources().getColor(R.color.letter));
        }

        while(numDeaths < 3) {
            for(int i = 0; i < numLets; i++) {
                if(!lets[i].isAlive()) {
                    lets[i].setLetterX((float)(Math.random()*(scrWidth-lets[i].getX())));
                    lets[i].setLetterY((float)(-Math.random()*visHeight)); //this will cause the delays
                    String letter = " "  +((int)(Math.random()*keyEvents.length) + 'A');
                    float dy = (float)(Math.random()*SPEED_RANGE)+MIN_SPEED;
                    lets[i].setLetter(letter);
                    lets[i].setDy(dy);
                }
            }
        }
    }

    //gets the keyevent associated with given capital letter
    private int getKeyEvent(int letter){
        return keyEvents[letter - 'A'];
    }

    //implementation of KeyEvent.Callback method
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        for(int i=0;i<currLets.length;i++){
            if(keyCode == getKeyEvent(currLets[i])){
                gameScore++;
                TextView scoreText = (TextView) findViewById(R.id.score);
                scoreText.setText("Score: " + gameScore + " ");
                return true;
            }
        }
        return true;
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
        }

        @Override
        public void onAnimationRepeat(Animator anim){
        }

        @Override
        public void onAnimationEnd(Animator anim){
            currLets[let] = -1; //letter no longer on screen, can't type it
        }

        @Override
        public void onAnimationStart(Animator anim){
        }
    }


}
