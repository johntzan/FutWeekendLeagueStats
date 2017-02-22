package com.futchampionsstats.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * Created by yiannitzan on 1/9/17.
 */

public class Utils {

    // Prevents instantiation.
    private Utils() {}

    public static void hideKeyboard(Context ctx, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    /*
    * method to hide soft keyboard programmatically
    * */
    public static void hideSoftInput(Activity ctx)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ctx.getCurrentFocus()!= null)
            inputMethodManager.hideSoftInputFromWindow(ctx.getCurrentFocus().getWindowToken(), 0);
    }

    public static double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    public static int getTotal(List<Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum;
        }
        return sum;
    }
}
