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
    String letter;
    int winHeight;
    private Handler h;
    private static final int FRAME_RATE = 30;
    private boolean alive;

    public LetterView(Context context, int visHeight, float ndy, String let) {
        super(context);
        mContext = context;
        h = new Handler();
        letter = let;
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

        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.letter));
        p.setTextSize(LET_SIZE);
        canvas.drawText(letter, x, y, p);
        h.postDelayed(r, FRAME_RATE);
    }

    public void setDx(float ndx) { dx = ndx; }
    public float getDx() { return dx; }

    public void setDy(float ndy) { dy = ndy; }
    public float getDy() { return dy; }

    public void setLetterX(float nx) { x = nx; }
    public float getLetterX() { return x; }

    public void setLetterY(float ny) { y = ny; }
    public float getLetterY() { return y; }

    public void setLetter(String let) { letter = let; }
    public String getLetter() { return letter; }

    public void setAlive(boolean al) { alive = al; }
    public boolean isAlive() { return alive; }
}
