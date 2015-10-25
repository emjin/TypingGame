package analemma.typinggame;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {
    public static final String LEVEL_MESSAGE = "com.analemma.typinggame.level";
    public static final int NEW_LEVEL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ImageView analemma = (ImageView)findViewById(R.id.image);
        //analemma.animate().x(0).y(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void begin(View view){
        int start_level = 1;
        //An intent represents an app's "intent to do something", usually start another activity
        Intent intent = new Intent(this, FallingLetters.class);
        intent.putExtra(LEVEL_MESSAGE, start_level);
        //Finish the intent
        startActivityForResult(intent, NEW_LEVEL);
    }

    //Get the new level and start another activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.NEW_LEVEL && resultCode == Activity.RESULT_OK) {
            int newLevel = data.getIntExtra(FallingLetters.SEND_LEVEL_MESSAGE, 1); //1 is default
            Intent intent = new Intent(this, FallingLetters.class);
            intent.putExtra(LEVEL_MESSAGE, newLevel);
            startActivityForResult(intent, NEW_LEVEL);
        }
    }

    private void openSearch() {
        startActivity(new Intent(SearchManager.INTENT_ACTION_GLOBAL_SEARCH));
    }

    private void openSettings() {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        } else if (id == R.id.action_search) {
            openSearch();
            return true;
        }
            return super.onOptionsItemSelected(item);
    }
}