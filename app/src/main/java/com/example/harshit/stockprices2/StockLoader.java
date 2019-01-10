package com.example.harshit.stockprices2;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StockLoader extends AsyncTaskLoader<List<StockData>> {
    private static final String LOG_TAG = StockLoader.class.getName();
    private String mUrl;
    private BroadcastReceiver mObserver;
    private List<StockData> mData;

    public StockLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        /*forceLoad();

        if (mFileObserver == null) {
            String path = new File(getContext().getFilesDir(), mUrl).getPath();
            mFileObserver = new FileObserver(path) {
                @Override
                public void onEvent(int event, String path) {
                    // Notify the loader to reload the data
                    onContentChanged();
                    // If the loader is started, this will kick off
                    // loadInBackground() immediately. Otherwise,
                    // the fact that something changed will be cached
                    // and can be later retrieved via takeContentChanged()
                }
            };
            mFileObserver.startWatching();
        }*/
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData);
        }

        // Begin monitoring the underlying data source.
        if (mObserver == null) {
//            mObserver = new SampleObserver();
            // TODO: register the observer
        }

        if (takeContentChanged() || mData == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    public List<StockData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<StockData> stocks = new ArrayList<StockData>();
        stocks = QueryUtils.fetchStockData(mUrl);
        return stocks;
    }

    @Override
    public void deliverResult(List<StockData> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<StockData> oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }

        // Stop watching for file changes
        if (mObserver != null) {
            //mObserver.stopWatching();
            mObserver = null;
        }
    }

    private void releaseResources(List<StockData> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    public void onCanceled(List<StockData> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);

    }
}