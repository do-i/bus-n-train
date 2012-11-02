package org.djd.busntrain.train;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import org.djd.busntrain.R;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/31/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainPredictionsActivity extends Activity {
  public static final String EXTRA_DATA_STATIONS_KEY = "EXTRA_DATA_STATIONS_KEY";

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_prediction_list_view);

    Intent intent = getIntent();
    if (intent != null) {
      StationModel stationModel = (StationModel) intent.getSerializableExtra(EXTRA_DATA_STATIONS_KEY);
      // TODO need to get parentStopId from stationModel.stopid.
      Toast.makeText(this,stationModel.getStopId(),Toast.LENGTH_SHORT);

    }
  }
}