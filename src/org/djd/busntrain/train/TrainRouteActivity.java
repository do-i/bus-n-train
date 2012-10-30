package org.djd.busntrain.train;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.train_route_list_item_view);
  }


  public void onClick(View v) {
    Toast.makeText(this, ((TextView)v).getText(), Toast.LENGTH_SHORT).show();

  }
}