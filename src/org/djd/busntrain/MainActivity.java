package org.djd.busntrain;

import org.djd.busntrain.bus.BusFavoriteActivity;
import org.djd.busntrain.bus.BusRouteActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrance_view);
	}

	/**
	 * Callback method in the event of any button is clicked.
	 * 
	 * @param view
	 */
	public void onButtonClicked(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.main_favorite_button_id:
			intent = new Intent(this, BusFavoriteActivity.class);
			break;
		case R.id.main_bus_button_id:
			intent = new Intent(this, BusRouteActivity.class);
			break;
		case R.id.main_train_button_id:
			Toast.makeText(this, "Not Supported Yet.", Toast.LENGTH_SHORT).show();
			return ;
		}

		if (null != intent) {
			startActivity(intent);
		} else {
			Toast.makeText(this, R.string.msg_sys_no_case_defined_error, Toast.LENGTH_SHORT).show();
			Log.e(TAG, getString(R.string.msg_sys_no_case_defined_error));

		}
	}
}