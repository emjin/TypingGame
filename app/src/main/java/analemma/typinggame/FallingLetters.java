package analemma.typinggame;

import android.content.Context;
import android.graphics.Point;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FallingLetters extends ActionBarActivity implements KeyEvent.Callback {

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 1; //as a fraction of letter size

    private int[] currLets; //letters that are currently on screen, as ints from 'A' to 'Z'
    private TextView[] lets;
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





        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int scrWidth = size.x;
        int numLets = (int)(scrWidth/((1+LET_SPACING)*LET_SIZE));
        lets = new TextView[numLets];
        currLets = new int[numLets];

        RelativeLayout rl = new RelativeLayout(this);
        rl.setBackgroundColor(getResources().getColor(R.color.background));//black background
        //add our beautiful analemma
        ImageView bg = new ImageView(this);
        bg.setImageResource(R.drawable.analemma);
        bg.setAdjustViewBounds(true); //this makes the object's size match the actual image's size
        rl.addView(bg);

        //code to force keyboard - off stackoverflow, who knows
        /*rl.setFocusable(true);
        rl.setFocusableInTouchMode(true);
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(rl, InputMethodManager.SHOW_IMPLICIT);*/

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //makes letters appear
        int pos = 0;
        for(int i=0;i<lets.length;i++){
            lets[i] = new TextView(this);
            TextView let = lets[i];
            let.setTextSize(LET_SIZE);
            //choose a letter
            int letterNum = (int) (26*Math.random()) + 'A';
            let.setText((char) letterNum + "");
            currLets[i] = letterNum;

            rl.addView(let);
            let.setX(pos);
            let.setY(0); //idk if this is necessary
            let.setTextColor(getResources().getColor(R.color.letter));
            pos += (1+LET_SPACING)*LET_SIZE;
        }

        //makes letters fall
        for(int i=0;i<lets.length;i++){
            lets[i].animate().setStartDelay((long)(1000*Math.random()) + 2000).setDuration((long)(5000*Math.random())).y(size.y);
        }

        setContentView(rl);


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
                lets[i].setText("");//for now, the text should just disappear
                //later, ill make it reappear at the top
                return true;
            }

        }
        return false; //idk what returning false will do but it seems appropriate
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
