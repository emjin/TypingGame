
package analemma.typinggame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ScorePage extends ActionBarActivity {
    int level;
    int score;
    int threshold = 20;
    Intent restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        Intent intent = getIntent();
        level = intent.getIntExtra(FallingLetters.SEND_LEVEL_MESSAGE, 1);
        score = intent.getIntExtra(FallingLetters.SCORE_MESSAGE, 0);

        restart = new Intent(this, FallingLetters.class);
        TextView scoreView = (TextView)findViewById(R.id.scoreBox);
        TextView levelView = (TextView)findViewById(R.id.levelMessage);
        Button contButton = (Button)findViewById(R.id.contButton);
        scoreView.setText("Score:" + score);
        if(score > threshold){
            levelView.setText("You've passed level "+level+"!");
            levelView.setTextColor(getResources().getColor(R.color.yay));
            contButton.setText(getResources().getString(R.string.next_button));
            restart.putExtra(MainActivity.LEVEL_MESSAGE, level + 1);
        }else{
            levelView.setText("You need " + (threshold - score) + " more points to move on to the next level. Better luck next time!");
            levelView.setTextSize(40);
            contButton.setText(getResources().getString(R.string.back_button));
            restart.putExtra(MainActivity.LEVEL_MESSAGE, level);
        }
    }

    public void begin(View view){
        startActivity(restart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_page, menu);
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

