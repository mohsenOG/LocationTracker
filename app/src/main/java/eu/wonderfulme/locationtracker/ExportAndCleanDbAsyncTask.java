package eu.wonderfulme.locationtracker;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import eu.wonderfulme.locationtracker.database.LocationData;
import eu.wonderfulme.locationtracker.database.RoomDbSingleton;

public class ExportAndCleanDbAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private boolean isSuccessful = false;
    private Snackbar mSnackbarMain;
    private String mFilename;

    public ExportAndCleanDbAsyncTask(Context context, Snackbar snackbar) {
        mContext = context;
        mSnackbarMain = snackbar;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<LocationData> dataList = RoomDbSingleton.getInstance(mContext).locationDao().getAllDbData();

        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            exportDir.mkdir();
        }
        mFilename = Utils.getFormattedFileName();
        try {
            String filePath = exportDir.getAbsolutePath() + "/" + mFilename;
            CSVWriter writer = new CSVWriter(new FileWriter(filePath));
            writer.writeNext(LocationData.getDbHeaders(mContext));
            for (LocationData data: dataList) {
                String[] dbRow = LocationData.locationCsvRowBuilder(data.getTimestamp(), data.getLatitude(),
                        data.getLongitude(), data.getAltitude(), data.getSpeed());
                writer.writeNext(dbRow);
            }
            writer.close();
            isSuccessful = true;
        } catch (IOException e) {
            isSuccessful = false;
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (isSuccessful) {
            mSnackbarMain.setText(mContext.getResources().getString(R.string.snackbar_export_csv_successful) + " " + mFilename).setDuration(Snackbar.LENGTH_LONG);
            mSnackbarMain.setAction(R.string.snackbar_goto_downloads, new SnackBarOnClickListener(SnackbarActionType.GOTO_DOWNLOADS));
            mSnackbarMain.show();
            // clear DB
            new NukeDatabaseTask().execute();
        } else {
            mSnackbarMain.setText(R.string.snackbar_export_csv_failed).setDuration(Snackbar.LENGTH_LONG);
            //TODO Make retry work!
            //mSnackbarMain.setAction(R.string.snackbar_retry, new SnackBarOnClickListener(SnackbarActionType.RETRY));
            mSnackbarMain.show();
        }

    }

    private class NukeDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            RoomDbSingleton.getInstance(mContext).locationDao().deteleAllRecords();
            return null;
        }
    }

    private enum SnackbarActionType {
        RETRY,
        GOTO_DOWNLOADS,
        INVALID
    }

    private class SnackBarOnClickListener implements View.OnClickListener {

        private SnackbarActionType mActionType;

        public SnackBarOnClickListener(SnackbarActionType actionType) {
            mActionType = actionType;
        }

        @Override
        public void onClick(View v) {
            switch (mActionType) {
                case RETRY:
                    new ExportAndCleanDbAsyncTask(mContext, mSnackbarMain).execute();
                    mSnackbarMain.dismiss();
                    break;
                case GOTO_DOWNLOADS:
                    Intent dm = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                    dm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(dm);
                    mSnackbarMain.dismiss();
                    break;
                case INVALID:
                default:
                    mSnackbarMain.dismiss();
                    break;
            }
        }
    }
}
