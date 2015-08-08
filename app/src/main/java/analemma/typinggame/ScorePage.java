package analemma.typinggame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class ScorePage extends ActionBarActivity {
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        Intent intent = getIntent();
        level = intent.getIntExtra(FallingLetters.SEND_LEVEL_MESSAGE, 1);
    }

    public void begin(View view){
        //An intent represents an app's "intent to do something", usually start another activity
        Intent intent = new Intent(this, FallingLetters.class);
        intent.putExtra(MainActivity.LEVEL_MESSAGE, level+1);
        //Finish the intent
        startActivity(intent);
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
