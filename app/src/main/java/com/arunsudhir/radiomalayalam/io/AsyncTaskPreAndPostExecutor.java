package com.arunsudhir.radiomalayalam.io;

import java.util.List;

/**
 * Created by Arun on 9/22/2015.
 */
public interface AsyncTaskPreAndPostExecutor<T> {
    public void PreExecute();

    public void PostExecute(List<T> result);
}
