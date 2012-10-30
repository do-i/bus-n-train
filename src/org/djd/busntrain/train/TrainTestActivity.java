package org.djd.busntrain.train;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class TrainTestActivity extends Activity {
  private static final String TAG = TrainTestActivity.class.getSimpleName();


  private static final String URL_STATIONS_TXT = "http://shielded-taiga-4473.herokuapp.com/v1/stations/";
  private static final String URL_RED_STATIONS_TXT = URL_STATIONS_TXT + "Red";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Reader reader = null;
    List<StationModel> stations = null;

    try {
      reader = new InputStreamReader(new URL(URL_RED_STATIONS_TXT).openStream());
      stations = new Gson().fromJson(reader, StationModel.TYPE);


    } catch (MalformedURLException e) {

    } catch (IOException e) {

    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          reader = null;
        }
      }
    }
    if (stations != null) {
      Log.i(TAG, stations.toString());
    } else {
      Log.e(TAG, "no stations available.");
    }
//	   setContentView(view)
  }


}
