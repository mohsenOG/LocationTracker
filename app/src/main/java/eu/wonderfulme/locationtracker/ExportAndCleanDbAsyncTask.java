package eu.wonderfulme.locationtracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;

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
    private Snackbar mSnackbar;
    private String mFilename;

    public ExportAndCleanDbAsyncTask(Context context, Snackbar snackbar) {
        mContext = context;
        mSnackbar = snackbar;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List<LocationData> dataList = RoomDbSingleton.getInstance(mContext).locationDao().getAllDbData();

        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            exportDir.mkdir();
        }
        mFilename = Utils.getFormattedFileName();
        File file = new File(exportDir, mFilename);
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(file));
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
            mSnackbar.setText(mContext.getResources().getString(R.string.snackbar_export_csv_successful) + mFilename);
            mSnackbar.show();
            RoomDbSingleton.getInstance(mContext).locationDao().deteleAllRecords();
        } else {
            mSnackbar.setText(R.string.snackbar_export_csv_failed);
            mSnackbar.show();
        }

    }
}
