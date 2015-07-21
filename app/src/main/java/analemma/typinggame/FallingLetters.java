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

    private RelativeLayout rl;
    private int visHeight; //bc i need it all over the place - height of visible area

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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int scrWidth = size.x;
        visHeight = size.y;
        int numLets = (scrWidth/((1+LET_SPACING)*LET_SIZE));
        lets = new TextView[numLets];
        currLets = new int[numLets];
        positions = new int[numLets];


        rl = new RelativeLayout(this);
        rl.setBackgroundColor(getResources().getColor(R.color.background));//black background
        //add our beautiful analemma
        ImageView bg = new ImageView(this);
        bg.setImageResource(R.drawable.analemma);
        bg.setAdjustViewBounds(true); //this makes the object's size match the actual image's size
        rl.addView(bg);

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

        setContentView(rl);

    }

    //chooses and animates a letter for a given textview in lets
    //return value is the letterNum value to be stored in currLets
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
        lets[i].animate().setStartDelay((long)(1000*Math.random()) + 2000).setDuration((long) (8000 * Math.random())).y(visHeight);
        lets[i].animate().setListener(new Listener(i)); //listens for the end of the animation
    }

    //gets the keyevent associated with given capital letter
    private int getKeyEvent(int letter){
        return keyEvents[letter - 'A'];
    }

    //implementation of KeyEvent.Callback method
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        for(int i=0;i<currLets.length;i++){
            if(currLets[i] != -1) { //don't call on a dead letter
                if (keyCode == getKeyEvent(currLets[i])) {
                    lets[i].setText("");
                    lets[i].animate().cancel();
                    createLetter(i);//put the next letter in motion and store the correct currLets value
                    return true;
                }
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
