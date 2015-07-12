package analemma.typinggame;

import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FallingLetters extends ActionBarActivity {

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 1; //as a fraction of letter size
    //private static final int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int scrWidth = size.x;
        int numLets = (int)(scrWidth/((1+LET_SPACING)*LET_SIZE));
        TextView[] lets = new TextView[numLets];

        RelativeLayout rl = new RelativeLayout(this);
        rl.setBackgroundColor(getResources().getColor(R.color.background));//black background
        //add our beautiful analemma
        ImageView bg = new ImageView(this);
        bg.setImageResource(R.drawable.analemma);
        bg.setAdjustViewBounds(true); //this makes the object's size match the actual image's size
        rl.addView(bg);

        //makes letters appear
        int pos = 0;
        for(int i=0;i<lets.length;i++){
            lets[i] = new TextView(this);
            TextView let = lets[i];
            let.setTextSize(LET_SIZE);
            int letterNum = (int) (26*Math.random()) + 'A';
            let.setText((char) letterNum + "");
            rl.addView(let);
            let.setX(pos);
            let.setY(0); //idk if this is necessary
            let.setTextColor(getResources().getColor(R.color.letter));
            pos += (1+LET_SPACING)*LET_SIZE;
        }

        //makes letters fall
        for(int i=0;i<lets.length;i++){
            lets[i].animate().setStartDelay((long)(1000*Math.random())).setDuration((long)(5000*Math.random())).y(size.y);
        }

        setContentView(rl);


    }

    private void setLetter(TextView let, RelativeLayout rl){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_falling_letters, menu);
        return true;
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
