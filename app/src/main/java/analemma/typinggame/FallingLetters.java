package analemma.typinggame;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
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

import org.w3c.dom.Text;

public class FallingLetters extends ActionBarActivity implements KeyEvent.Callback {
    public static final String SEND_LEVEL_MESSAGE = "com.analemma.typinggame.send_level";
    public static final String SCORE_MESSAGE = "com.analemma.typinggame.game_score";

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 1; //as a fraction of letter size
    private static final float FONT_TO_PIXELS = 3.5f; //font pts:pixels
    private final int THRESHOLD = 3;

    private RelativeLayout rl;
    private int visHeight; //height of visible area
    private int scrWidth;

    private int level;
    private int gameScore;
    private int escapedLets = 0;
    private int numLets;

    int[] powerUpChoices = {'#', '+', '='};
    private Letter[] letters;
    private int[] keyEvents = {KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C, KeyEvent.KEYCODE_D,
            KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_G, KeyEvent.KEYCODE_H, KeyEvent.KEYCODE_I,
            KeyEvent.KEYCODE_J, KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L, KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_N,
            KeyEvent.KEYCODE_O, KeyEvent.KEYCODE_P, KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_S,
            KeyEvent.KEYCODE_T, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_V, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_X,
            KeyEvent.KEYCODE_Y, KeyEvent.KEYCODE_Z, KeyEvent.KEYCODE_POUND, KeyEvent.KEYCODE_PLUS, KeyEvent.KEYCODE_EQUALS};
            //ints representing key events

    //these are all in milliseconds
    private static int delayMin = 2000;
    private static int delayRange = 2000;
    private static int[] durationRanges = {8000, 7000, 6000, 5000, 4000, 3000};
    private int[] durationMins = {2000, 1000, 800, 500, 400, 300};
    private int durationRange;
    private int durationMin;
    private int durationTimer = -1;

    private static int splatterRange = 100; //in pixels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_letters);
        Intent intent = getIntent();
        level = intent.getIntExtra(MainActivity.LEVEL_MESSAGE, 1); //This means that if there's ever an error, the user is automatically put to level 1
        if (level <= durationRanges.length) {
            durationRange = durationRanges[level - 1];
            durationMin = durationMins[level - 1];
        }else {
            durationRange = 1000;
            durationMin = 200;
        }
        gameScore = 0;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        scrWidth = size.x;
        visHeight = size.y;

        rl = (RelativeLayout) findViewById(R.id.activity_falling_letters);
        rl.setBackgroundColor(getResources().getColor(R.color.background));//black background

        //force keyboard to show. thanks stackoverflow
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        final Window mRootWindow = getWindow();
        View mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        View view = mRootWindow.getDecorView();
                        view.getWindowVisibleDisplayFrame(r);
                        visHeight = r.height();
                    }
                });
        startGame();
    }

    private void startGame(){
        //inits the letters and their positions
        numLets = (scrWidth/((1+LET_SPACING)*LET_SIZE));
        int lets = numLets + (int)(Math.random()*10); //multiple is, like, max powerups
        letters = new Letter[lets];
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
        int letterNum;

        if(i < numLets) letterNum = (int) (26 * Math.random()) + 'A';
        else letterNum = powerUpChoices[(int)(Math.random()*powerUpChoices.length)];
        letters[i].setLet(letterNum);

        //add to layout in proper location
        TextView letterView = letters[i].getTextView();

        letterView.setX(letters[i].getPos());
        letterView.setY(-LET_SIZE * FONT_TO_PIXELS);
        letterView.setTextColor(getResources().getColor(R.color.letter));
        rl.addView(letterView);
        letterView.setTextSize(LET_SIZE);
        //animate
        if(i < numLets || Math.random()*10 < 4) {
            if (letters[i].isFirstRound()) {
                letterView.animate().setStartDelay((long) (delayRange * Math.random()) + delayMin);
                letters[i].setFirstRound(false);
            } else {
                letterView.animate().setStartDelay(0);
            }
            letterView.animate().setDuration((long) (durationRange * Math.random() + durationMin)).y(visHeight);
            letterView.animate().setListener(new Listener(i)); //listens for the end of the animations
        }
    }

    //gets the keyevent associated with given capital letter or powerup
    private int getKeyEvent(int letter){
        if(letter >= 'A' && letter <= 'Z') return keyEvents[letter - 'A'];
        if(letter == '#') return KeyEvent.KEYCODE_POUND;
        if(letter == '+') return KeyEvent.KEYCODE_PLUS;
        if(letter == '=') return KeyEvent.KEYCODE_EQUALS;
        return -50;//poopbag return statement
    }

    //implementation of KeyEvent.Callback method
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        for(int i=0;i<letters.length;i++){
            if(letters[i].getLet() != -1) {
                if(keyCode == getKeyEvent(letters[i].getLet())) {
                    if(i < numLets) {
                        //Increment durationTimer if necessary (powerup)
                        if(durationTimer != -1) durationTimer += 1;
                        if(durationTimer > Math.random()*3+2){
                            durationRange /= 2;
                            durationTimer = -1;
                        }
                        //Adjust stuff
                        gameScore++;
                        escapedLets--;
                        TextView scoreText = (TextView) findViewById(R.id.score);
                        scoreText.setText("Score: " + gameScore + " ");
                        //Make new letter
                        letters[i].getTextView().setText("");
                        letters[i].getTextView().animate().cancel();
                        createLetter(i);
                        return true;
                    }
                    else {
                        durationRange *= 2;
                        durationTimer = 0;
                    }
                }
            }
        }
        return false;
    }

    public void showScore(){
        Intent intent = new Intent(this, ScorePage.class);
        intent.putExtra(SEND_LEVEL_MESSAGE, level);
        intent.putExtra(SCORE_MESSAGE, gameScore);
        startActivity(intent);
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
            bloodView.setX(letters[let].getTextView().getX() - (float) (LET_SIZE * FONT_TO_PIXELS / 2));
            bloodView.setY(letters[let].getTextView().getY()-(float)(LET_SIZE*FONT_TO_PIXELS/2));

            int splatterSize = (int)(splatterRange *Math.random()) + LET_SIZE*(int)FONT_TO_PIXELS; //min size is font size
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(splatterSize, splatterSize);
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
            escapedLets++;
            //TextView scoreText = (TextView) findViewById(R.id.score);
           // scoreText.setText("Escaped: "+escapedLets+"/"+numLets+" ");//TODO remove, just for testing
            if(letters.length-escapedLets <= THRESHOLD) showScore();
            //TODO ^^ i think this may be causing problems, since something happened with letters.length & the powerups
        }

        @Override
        public void onAnimationStart(Animator anim){
        }

    }
}
