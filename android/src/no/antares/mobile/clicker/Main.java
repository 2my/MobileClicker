/* Main.java
   Copyright 2012 Tommy Skodje

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package no.antares.mobile.clicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/** Startup activity has button to start QR scan - fires up the PresenterRemote Clicker Activity
 * @author tommy skodje
 */
public class Main extends Activity {
	private static final String TAG	= Main.class.getSimpleName();
	private Button reconnect;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		reconnect = (Button) findViewById( R.id.button_last );
		reconnect.setVisibility( View.GONE );

		Button connect = (Button) findViewById( R.id.button_start );
		connect.setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						scanQRCode();
					}
				}
			);
	}

	@Override protected void onStart() {
		super.onStart();
		final String lastUrl	= fetchLastUrl();
		Log.d( TAG, "fetched LastUrl: " + lastUrl );
		if ( lastUrl != null ) {
			reconnect.setVisibility( View.VISIBLE );
			reconnect.setOnClickListener(
					new OnClickListener() {
						public void onClick(View v) {
							go2presenter( lastUrl );
						}
					}
				);
		}
	}

	public void scanQRCode() {
		Log.d( TAG, "scanQRCode()");
		// @see http://code.google.com/p/zxing/wiki/GettingStarted
		IntentIntegrator integrator = new IntentIntegrator( this );
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// @see http://code.google.com/p/zxing/wiki/GettingStarted
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		String remoteUrl	= null;
		if (scanResult != null)
			remoteUrl	= scanResult.getContents();
		if (remoteUrl != null) {
			storeLastUrl( remoteUrl );
			go2presenter( remoteUrl );
		}
	}

	private void go2presenter( String remoteUrl ) {
		Log.d( TAG, "go2presenter(): " + remoteUrl );
		Intent intent = new Intent( Main.this, PresenterRemote.class );
		intent.putExtra( PresenterRemote.KEY_SERVER_URL, remoteUrl );
		startActivity( intent );
	}

	private String fetchLastUrl() {
		SharedPreferences prefs	= getSharedPreferences( "AndroidClicker", Context.MODE_PRIVATE );
		return prefs.getString( "last.url", null );
	}

	private void storeLastUrl( String remoteUrl ) {
		Log.d( TAG, "storeLastUrl(): " + remoteUrl );
		SharedPreferences prefs	= getSharedPreferences( "AndroidClicker", Context.MODE_PRIVATE );
		Editor editor	= prefs.edit();
		editor.putString( "last.url", remoteUrl );
		editor.commit();
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
