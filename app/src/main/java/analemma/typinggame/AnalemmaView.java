package analemma.typinggame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.os.Handler;


/**
 * TODO: document your custom view class.
 */
public class AnalemmaView extends View {
    private Context mContext;
    int x = -1;
    int y = -1;
    private int dx = 10;
    private int dy = 10;
    private Handler h;
    private static final int FRAME_RATE = 30;

    public AnalemmaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        h = new Handler();
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

        BitmapDrawable ae = (BitmapDrawable)mContext.getResources().getDrawable(R.drawable.analemmalogo);
        if(x < 0 && y < 0){
            x = this.getWidth()/2;
            y = this.getHeight()/2;
        }
        x += dx;
        y += dy;
        if(x > this.getWidth() - ae.getBitmap().getWidth() || x < 0) dx *= -1;
        if(y > this.getHeight( )- ae.getBitmap().getHeight() || y < 0) dy *= -1;
        canvas.drawBitmap(ae.getBitmap(), x, y, null);
        h.postDelayed(r, FRAME_RATE);
    }
}
