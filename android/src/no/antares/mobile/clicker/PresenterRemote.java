/* PresenterRemote.java
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/** Activity runs the Presentation Clicker screen (next / prev buttons and notes)
 * @author tommy skodje
*/
public class PresenterRemote extends Activity {
	private static final String TAG	= PresenterRemote.class.getSimpleName();
	private TextView notes	= null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presenter_remote);

        notes = (TextView) findViewById( R.id.text_notes );
        attachStepButtonListener( R.id.button_next, "+1" );
        attachStepButtonListener( R.id.button_previous, "-1" );
    }

    private void attachStepButtonListener( int buttonId, final String text ) {
        Button next = (Button) findViewById( buttonId );
        next.setOnClickListener(
    		new OnClickListener() {
		    	public void onClick(View v) {
					Log.d( TAG, text + " button clicked!");
					notes.setText( text );
		    	}
	    	}
        );
    };

}
