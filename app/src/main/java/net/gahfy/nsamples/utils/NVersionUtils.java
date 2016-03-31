package net.gahfy.nsamples.utils;

import android.os.Build;

/**
 * This class is a library of useful methods about N preview of Android.
 */
public class NVersionUtils {
    /**
     * This method checks if Android version is at least N or not.
     *
     * We decided to recode the existing method of BuildCompat, as it does not exist under N, so it
     * would be impossible to compile our code using it with a SDK version before N.
     * @return whether the current version is at least N or not
     */
    public static boolean isAtLeastN(){
        return Build.VERSION.RELEASE.startsWith("N") || Build.VERSION.RELEASE.startsWith("7");
    }
}
