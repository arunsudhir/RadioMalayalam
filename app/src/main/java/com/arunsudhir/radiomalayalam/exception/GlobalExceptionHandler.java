package com.arunsudhir.radiomalayalam.exception;

import android.app.AlertDialog;
import android.content.Context;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Arun on 2/5/2016.
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    Thread.UncaughtExceptionHandler oldHandler;
    Context contxt;
    public GlobalExceptionHandler(Thread.UncaughtExceptionHandler oldHandler, Context context)
    {
        this.oldHandler = oldHandler;
        this.contxt = context;
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //Do your own error handling here
        // Converts the stack trace into a string.
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(contxt);
        alertDialogBuilder.setMessage(errors.toString());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        if (oldHandler != null)
            oldHandler.uncaughtException(
                    thread,
                    ex
            ); //Delegates to Android's error handling
        else
            System.exit(2); //Prevents the service/app from freezing
    }
}
