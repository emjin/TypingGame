package analemma.typinggame;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Anna on 8/1/2015.
 */
public class Letter {

    private boolean isPowerUp;
    private boolean firstRound;
    private int let; //A-Z
    private TextView txt;
    private int pos; //x position
    private int numRegens = 0;

    public Letter(Context c){
        firstRound = true;
        txt = new TextView(c);
        pos = 0;
    }

    public boolean isFirstRound(){return firstRound;}
    public void setFirstRound(boolean fr){firstRound = fr;}

    public int getLet(){return let;}
    public void setLet(int nLet){
        let = nLet;
        txt.setText((char)let+"");
    }

    public TextView getTextView(){return txt;}

    public int getPos(){return pos;}
    public void setPos(int p){pos = p;}

    public int getNumRegens(){return numRegens;} //regenerations
    public void setNumRegens(int n){numRegens = n;}
    public void incNumRegens(){numRegens++;} //increment the number of regenerations

    public boolean isPowerUp(){return isPowerUp;}
    public void setIsPowerUp(boolean b){isPowerUp=b;}

}
