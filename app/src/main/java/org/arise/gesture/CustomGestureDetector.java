package org.arise.gesture;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by phandaa on 10/11/15.
 */
public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            return false;
        }
        // right to left swipe
        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
            Log.d("ARISE", "Swipe Up");
            return true;
        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
            Log.d("ARISE", "Swipe Down");
            return true;
        }

        return false;
    }

}