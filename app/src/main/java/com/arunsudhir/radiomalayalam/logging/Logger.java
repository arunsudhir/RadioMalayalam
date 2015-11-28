package com.arunsudhir.radiomalayalam.logging;

import android.util.Log;

import static java.lang.String.format;

/**
 * Created by ullatil on 11/27/2015.
 */
public class Logger {
    private final String tag;

    public Logger(Class<?> baseClass) {
        this.tag = baseClass.getSimpleName();
    }

    public void verbose(String message, Object... args) {
        Log.v(tag, format(message, args));
    }

    public void verbase(Throwable error, String message, Object... args) {
        Log.v(tag, format(message, args), error);
    }

    public void debug(String message, Object... args) {
        Log.d(tag, format(message, args));
    }

    public void debug(Throwable error, String message, Object... args) {
        Log.d(tag, format(message, args), error);
    }

    public void info(String message, Object... args) {
        Log.i(tag, format(message, args));
    }

    public void info(Throwable error, String message, Object... args) {
        Log.i(tag, format(message, args), error);
    }

    public void warn(String message, Object... args) {
        Log.w(tag, format(message, args));
    }

    public void warn(Throwable error, String message, Object... args) {
        Log.w(tag, format(message, args), error);
    }

    public void error(String message, Object... args) {
        Log.e(tag, format(message, args));
    }

    public void error(Throwable error, String message, Object... args) {
        Log.e(tag, format(message, args), error);
    }

    public void fatal(String message, Object... args) {
        Log.wtf(tag, format(message, args));
    }

    public void fatal(Throwable error, String message, Object... args) {
        Log.wtf(tag, format(message, args), error);
    }
}
