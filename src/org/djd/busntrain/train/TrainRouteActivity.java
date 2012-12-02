package org.djd.busntrain.train;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import org.djd.busntrain.R;

import static org.djd.busntrain.commons.ApplicationCommons.getColorWithoutDestination;

/**
 * View for picking a color.
 * Created with IntelliJ IDEA.
 * User: acorn
 * Date: 10/28/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrainRouteActivity extends Activity {

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_route_list_view);
  }

  public void onClick(View v) {
    Intent intent = new Intent(this, TrainStationActivity.class);
    intent.setData(Uri.parse(getColorWithoutDestination(v.getId())));
    startActivity(intent);
  }
}