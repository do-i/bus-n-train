package org.djd.busntrain.train;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.djd.busntrain.R;

import java.util.ArrayList;
import java.util.List;

import static org.djd.busntrain.commons.ApplicationCommons.setTextToTextView;

/**
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/30/12
 * Time: 10:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainStationAdapter extends ArrayAdapter<StationModel> {
  private static final String TAG = TrainStationAdapter.class.getSimpleName();
  private ArrayList<StationModel> stations;
  private Context context;

  public TrainStationAdapter(Context context, ArrayList<StationModel> stations) {
    super(context, R.layout.train_stop_list_item_view, stations);
    this.stations = stations;
    this.context = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.train_stop_list_item_view, null);
      Log.w(TAG, "view was null.");
    }
    StationModel stationModel = stations.get(position);
    if (stationModel != null) {
      setTextToTextView(view, R.id.train_stop_name_id, stationModel.getStopName());
      setTextToTextView(view, R.id.train_stop_id, stationModel.getStopId());
    }
    return view;
  }
}
