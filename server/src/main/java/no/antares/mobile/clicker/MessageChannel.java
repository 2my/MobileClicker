/* MessageChannel.java
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
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/** Channel for receiving and sending Message(s).
 * @author tommy skodje
 */
public class MessageChannel implements Closeable {
	private static final Logger logger	= Logger.getLogger( MessageChannel.class.getName() );
	private final ServerSocket serverSocket;

	/** Send message to server, @return response */
	public static String send(String host, int port, String message) throws IOException {
		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			kkSocket = new Socket(host, port);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			out.println(message);

			if (StringUtils.isBlank(reply2(message)))
				return "";

			in = new BufferedReader(new InputStreamReader( kkSocket.getInputStream()));
			StringBuffer reply = new StringBuffer();
			String fromServer;
			while ((fromServer = in.readLine()) != null) {
				logger.trace("received: " + fromServer);
				reply.append(fromServer);
			}
			return reply.toString();
		} catch (IOException e) {
			logger.error("send() couldn not get I/O for the connection to: " + host + port, e);
			throw e;
		} finally {
			close(out);
			close(in);
			close(kkSocket);
		}
	}

	private static String reply2( String msg ) {
		if ( "PING".equalsIgnoreCase( msg ) )
			return "PONG";
		return null;
	}

	/** Open a channel - close when done */
	protected static MessageChannel openInbound( int port ) {
		try {
			return new MessageChannel( new ServerSocket( port ) );
		} catch (IOException e) {
			logger.fatal( "(): " + port, e );
			throw new RuntimeException("Could not listen on port: " + port, e);
		}
	}

	private MessageChannel( ServerSocket serverSocket ) {
		this.serverSocket	= serverSocket;
	}

	/** Blocks while waiting for message */
	protected String waitForNextMessage() {
		Socket clientSocket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			clientSocket = serverSocket.accept();
			in = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
			String received	= in.readLine();
			if ( received != null ) {
				String reply	= reply2( received );
				if ( ! StringUtils.isBlank( reply ) ) {
					out = new PrintWriter( clientSocket.getOutputStream(), true);
					out.println( reply );
				}
				return received;
			}
		} catch (IOException e) {
			logger.error( "waitForNextMessage() got Exception", e );
		} finally {
			close( clientSocket );
			close( out );
			close( in );
		}
		return "";
	}

	/** Close the channel */
	public void close() {
		try {
			if ( serverSocket != null)
				serverSocket.close();
		} catch (IOException ioe) {
		}
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
