package org.djd.busntrain.train;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import org.djd.busntrain.R;
import org.djd.busntrain.commons.ApplicationCommons;

import java.util.ArrayList;

import static org.djd.busntrain.commons.ApplicationCommons.setTextToTextView;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 11/20/12
 * Time: 10:15 PM
 */
public class TrainPredictionsAdapter extends ArrayAdapter<TrainPredictionsModel> {
  private static final String TAG = TrainPredictionsAdapter.class.getSimpleName();
  private ArrayList<TrainPredictionsModel> predictions;
  private Context context;

  public TrainPredictionsAdapter(Context context, ArrayList<TrainPredictionsModel> predictions) {
    super(context, R.layout.train_prediction_list_item_view, predictions);
    this.predictions = predictions;
    this.context = context;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
//    View view = convertView;
    if (view == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.train_prediction_list_item_view, null);
      Log.w(TAG, "view was null.");
    }
    TrainPredictionsModel predictionsModel = predictions.get(position);
    if (predictionsModel != null) {
      setTextToTextView(view, R.id.train_prediction_wait_time_in_minutes, predictionsModel.minutes);
      setTextToTextView(view, R.id.train_prediction_destination, predictionsModel.destNm);
      setTextToTextView(view, R.id.train_prediction_vehicle_id, predictionsModel.rn);
      setTextToTextView(view, R.id.train_prediction_route, predictionsModel.rt);
    }
    view.setBackgroundResource(ApplicationCommons.getColorIdByCode(predictionsModel.rt));
    return view;
  }
}