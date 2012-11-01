package org.djd.busntrain.train;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import org.djd.busntrain.R;

/**
 * View for picking a color.
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/28/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainRouteActivity extends Activity {
  private static final String TAG = TrainRouteActivity.class.getSimpleName();
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_route_list_view);
  }

  public void onClick(View v) {
    Intent intent = new Intent(this, TrainStationActivity.class);
    intent.setData(Uri.parse(color(v.getId())));
    startActivity(intent);
  }

  private String color(int viewId) {
    switch (viewId) {
      case R.id.train_route_red_id: return "Red/95th";
      case R.id.train_route_blue_id: return "Blue/ForestPark";
      case R.id.train_route_brown_id: return "Brown/Loop";
      case R.id.train_route_green_id: return "Green/63rd";
      case R.id.train_route_orange_id: return "Orange/Loop";
      case R.id.train_route_purple_id: return "PurpleExp/Linden";
      case R.id.train_route_pink_id: return "Pink/Loop";
      case R.id.train_route_yellow_id: return "Yellow/Howard";
      default:
        Log.e(TAG, "Undefined viewId: " + viewId);
        return "";
    }
  }
}