package analemma.typinggame;

import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FallingLetters extends ActionBarActivity {

    private static final int LET_SIZE = 40;
    private static final int LET_SPACING = 1; //as a fraction of letter size

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

        //no bg for now lol, easy to add tho

        //makes letters appear
        int pos = 0;
        for(int i=0;i<lets.length;i++){
            lets[i] = new TextView(this);
            prepLetter(lets[i], rl);
            lets[i].setX(pos);
            lets[i].setY(0); //idk if this is necessary
            pos += (1+LET_SPACING)*LET_SIZE;
        }

        //makes letters fall
        for(int i=0;i<lets.length;i++){
            lets[i].animate().setStartDelay(500).y(size.y);
        }

        setContentView(rl);


    }

    private void prepLetter(TextView let, RelativeLayout rl){
        //let = new TextView(this);
        let.setTextSize(LET_SIZE);
        int letterNum = (int) (26*Math.random()) + 'A';
        let.setText((char) letterNum + "");
        rl.addView(let);
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
