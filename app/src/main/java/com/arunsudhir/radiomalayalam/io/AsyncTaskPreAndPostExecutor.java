package com.arunsudhir.radiomalayalam.io;

import android.app.Activity;
import android.app.ProgressDialog;

import java.util.List;

/**
 * Created by Arun on 9/22/2015.
 */
public interface AsyncTaskPreAndPostExecutor<T> {
    void PreExecute();
    void PostExecute(List<T> result);
    Activity getContainingActivity();
    ProgressDialog getProgressDialog();
}
