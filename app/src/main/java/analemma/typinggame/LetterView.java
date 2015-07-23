package analemma.typinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * TODO: document your custom view class.
 */
public class LetterView extends View {

    private static final int LET_SIZE = 40;
    private Context mContext;
    private float x;
    private float y;
    private float dx;
    private float dy;
    TextView letter;
    int winHeight;
    private Handler h;
    private static final int FRAME_RATE = 30;
    private boolean alive;

    public LetterView(Context context, int visHeight, float ndy, String let) {
        super(context);
        mContext = context;
        h = new Handler();
        letter = new TextView(context);
        letter.setText(let);
        winHeight = visHeight;
        dy = ndy;
        initialize();
    }

    public void initialize() {
        x = 0;
        y = 0;
        dx = 0;
        alive = false;
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        letter.setX(x);
        letter.setY(y);

        x += dx;
        y += dy;

        if (y > 0 && y < winHeight){
            alive = true;
        }

        if (y >= winHeight) {
            ImageView bloodView = new ImageView(mContext);
            bloodView.setX(x);
            bloodView.setY(y);
            bloodView.setImageDrawable(getResources().getDrawable(R.drawable.blood));
            alive = false;
        }
        h.postDelayed(r, FRAME_RATE);
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.letter));
        p.setTextSize(LET_SIZE);
        canvas.drawText(letter.getText().toString(), x, y, p);
    }


    public void setDx(float ndx) { dx = ndx; }
    public float getDx() { return dx; }

    public void setDy(float ndy) { dy = ndy; }
    public float getDy() { return dy; }

    public void setLetterX(float nx) { x = nx; }
    public float getLetterX() { return x; }

    public void setLetterY(float ny) { y = ny; }
    public float getLetterY() { return y; }

    public void setLetter(String let) { letter.setText(let); }
    public String getLetter() { return letter.getText() + ""; }

    public void setAlive(boolean al) { alive = al; }
    public boolean isAlive() { return alive; }
}