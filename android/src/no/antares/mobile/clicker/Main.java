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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/** Startup activity connects to server and fires up the PresenterRemote Clicker Activity
 * @author tommy skodje
*/
public class Main extends Activity {
	private static final String TAG	= Main.class.getSimpleName();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // FixMe: QR-scan to get at connection
        Button next = (Button) findViewById( R.id.button_start );
        next.setOnClickListener(
    		new OnClickListener() {
		    	public void onClick(View v) {
					Log.d( TAG, "Start button clicked!");
					scanQRCode();
					// go2presenter();
		    	}
	    	}
        );
    }

    public void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator( this );
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanResult != null) {
    		Log.d( TAG, scanResult.getContents() );
    		go2presenter();
    	}
    }

	private void go2presenter() {
		Intent i = new Intent( Main.this, PresenterRemote.class );
		startActivity(i);
	}

    /*
    public Button.OnClickListener mScan = new Button.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent( "com.google.zxing.client.android.SCAN" );
            intent.putExtra( "com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE" );
            startActivityForResult( intent, 0 );
        }
    };
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }*/

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
