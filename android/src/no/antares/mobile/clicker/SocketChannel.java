/* SocketChannel.java
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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

/** Communicates with server via socket connection.
 * @author tommy skodje
*/
public class SocketChannel {
	private static final String TAG	= SocketChannel.class.getSimpleName();

	private final String host;
	private final int port;
	private final UserFeedBack ui;

	public SocketChannel( String host, int port, UserFeedBack ui ) {
		this.host = host;
		this.port = port;
		this.ui = ui;
	}


	public void sendInBackGround( String text ) {
		( new BackGroundTask() ).execute( text );
	}

	private class BackGroundTask extends AsyncTask<String, Void, String> {
		@Override protected String doInBackground( String... params ) {
			return send( params[ 0 ] );
		}
	
		@Override protected void onPreExecute() {
		}
	
		@Override protected void onPostExecute( String result ) {
			ui.showFeedBack( result );
		}
	}

	/** Send message to server, @return response */
	private String send( String message ) {
		Socket clientSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			clientSocket = new Socket(host, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			out.println(message);

			if ( expectReply2( message ) )
				return "";

			in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream() ) );
			StringBuffer reply = new StringBuffer();
			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				Log.v( TAG, "received: " + fromServer);
				reply.append(fromServer);
			}
			return reply.toString();
		} catch (IOException e) {
			Log.e( TAG, "send() could not get I/O for the connection to: " + host + port, e);
			return "ERROR";
		} finally {
			close(out);
			close(in);
			close(clientSocket);
		}
	}

	private boolean expectReply2( String message ) {
		return false;
	}

	private static void close(Closeable s) {
		try {
			if (s != null)
				s.close();
		} catch (IOException ioe) {
		}
	}

	private static void close(Socket s) {
		try {
			if (s != null)
				s.close();
		} catch (IOException ioe) {
		}
	}

}
