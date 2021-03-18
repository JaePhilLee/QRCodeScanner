package com.hstelnet.qrcodescanner;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * for QRCode Scanning
 *
 * build.gradle
 *		implementation 'com.journeyapps:zxing-android-embedded:3.4.0'
 *
 * QR Code Sample URL : http://goqr.me/
 * 		   Sample Data : {"name":"Hoseo Telnet", "address":"http://www.hstelnet.com"}
 * */

public class MainActivity extends AppCompatActivity {
	//View Objects
	private FloatingActionButton fab;
	private TextView textViewName, textViewAddress, textViewResult;

	//scanner Object
	private IntentIntegrator qrScan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initializationView();
		initializationViewEvents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}



	private void initializationView() {
		//View Objects
		fab = findViewById(R.id.fab);
		textViewName = (TextView) findViewById(R.id.textViewName);
		textViewAddress = (TextView) findViewById(R.id.textViewAddress);
		textViewResult = (TextView)  findViewById(R.id.textViewResult);

		//intializing scan object
		qrScan = new IntentIntegrator(this);
	}

	private void initializationViewEvents() {
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//scan option
				qrScan.setPrompt("Scanning...");

				//qrScan.setOrientationLocked(false);
				qrScan.initiateScan();
			}
		});
	}

	//Getting the scan results
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			//qrcode 가 없으면
			if (result.getContents() == null) {
				Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
			} else {
				//qrcode 결과가 있으면
				Toast.makeText(MainActivity.this, "Scan succeed!", Toast.LENGTH_SHORT).show();
				try {
					//data를 json으로 변환
					JSONObject obj = new JSONObject(result.getContents());
					textViewName.setText(obj.getString("name"));
					textViewAddress.setText(obj.getString("address"));
				} catch (JSONException e) {
					e.printStackTrace();
					//Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
					textViewResult.setText(result.getContents());
				}
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}