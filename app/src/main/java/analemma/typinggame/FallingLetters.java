package analemma.typinggame;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FallingLetters extends ActionBarActivity implements KeyEvent.Callback {

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 1; //as a fraction of letter size

    private RelativeLayout rl;
    private int visHeight; //bc i need it all over the place - height of visible area

    private int gameScore;
    private int numWrong;

    private int[] currLets; //letters that are currently on screen, as ints from 'A' to 'Z'
    private TextView[] lets;
    private int[] positions; //positions for each letter. is this the smart way? nah. is it easy tho? yeaaaah.
    private int[] keyEvents = {KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_B, KeyEvent.KEYCODE_C, KeyEvent.KEYCODE_D,
            KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_G, KeyEvent.KEYCODE_H, KeyEvent.KEYCODE_I,
            KeyEvent.KEYCODE_J, KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L, KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_N,
            KeyEvent.KEYCODE_O, KeyEvent.KEYCODE_P, KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_S,
            KeyEvent.KEYCODE_T, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_V, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_X,
            KeyEvent.KEYCODE_Y, KeyEvent.KEYCODE_Z}; //ints representing key events


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_letters);//idk i think this line is not needed but dont feel ike removing
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
                        visHeight = r.height(); //TODO ???????????? :-/ :^3
                        // r.left, r.top, r.right, r.bottom
                    }
                });


        int numLets = (int)(scrWidth/((1+LET_SPACING)*LET_SIZE));
        lets = new TextView[numLets];
        currLets = new int[numLets];
        positions = new int[numLets];

        //rl = new RelativeLayout(this);
        rl = (RelativeLayout) findViewById(R.id.activity_falling_letters);
        rl.setBackgroundColor(getResources().getColor(R.color.background));//black background
        //add our beautiful analemma
        //ImageView bg = new ImageView(this);
        //bg.setImageResource(R.drawable.analemma);
        //bg.setAdjustViewBounds(true); //this makes the object's size match the actual image's size
        //rl.addView(bg);

        //force keyboard to show. thanks stackoverflow
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //inits the appropriate positions for each letter
        int pos = 0;
        for(int i=0;i<lets.length;i++){
            positions[i] = pos;
            pos += (1+LET_SPACING)*LET_SIZE;
        }

        //go letters go
        for(int i=0;i<lets.length;i++){
            createLetter(i);
        }

       // setContentView(rl);
    }

    //chooses and animates a letter for a given textview in lets
    private void createLetter(int i){
        //init
        lets[i] = new TextView(this);
        TextView let = lets[i];
        let.setTextSize(LET_SIZE);
        //choose a letter
        int letterNum = (int) (26*Math.random()) + 'A';
        let.setText((char) letterNum + "");
        currLets[i] = letterNum;
        //add to layout in proper location
        rl.addView(let);
        let.setX(positions[i]);
        let.setY(0); //idk if this is necessary
        let.setTextColor(getResources().getColor(R.color.letter));
        //animate
        lets[i].animate().setStartDelay((long)(1000*Math.random()) + 2000).setDuration((long)(8000*Math.random())).y(visHeight);
        //I'm assuming it breaks from this if you press the right key
        //Not sure what happens to this function since we just called another one from outside...meh
        ImageView bloodView = new ImageView(this);
        bloodView.setX(lets[i].getX());
        bloodView.setY(lets[i].getY());
        bloodView.setImageDrawable(getResources().getDrawable(R.drawable.blood));
    }

    //gets the keyevent associated with given capital letter
    private int getKeyEvent(int letter){
        return keyEvents[letter - 'A'];
    }

    //implementation of KeyEvent.Callback method
    //I personally prefer onKeyDown, though I guess it doesn't really matter
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        for(int i=0;i<currLets.length;i++){
            if(keyCode == getKeyEvent(currLets[i])){
                gameScore++;
                lets[i].setText("");//for now, the text should just disappear
                TextView scoreText = (TextView)findViewById(R.id.score);
                scoreText.setText("Score: " + gameScore + " ");
                createLetter(i);
                return true;
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
}
