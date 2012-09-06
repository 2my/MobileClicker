/* MessageChannelTest.java
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.*;

public class MessageChannelTest {
	int sensibleWait	= 20;

	String host	= "localhost";
	int port	= 3333;
	String msg	= "TEST MESSAGE";

	ConcurrentLinkedQueue<String> received	= new ConcurrentLinkedQueue<String>();

	boolean done	= false;
	Thread channelThread	= new Thread( "ChannelThread" ) {
		public void run() {
			MessageChannel channel	= MessageChannel.openInbound( port );
			while ( ! done )
				received.add( channel.waitForNextMessage() );
			channel.close();
		}
	};

	@Before public void setUp() throws Exception {
		done	= false;
	}

	@After public void tearDown() throws Exception {
		done	= true;
		// channelThread.interrupt();
	}

	@Test public void send() throws Exception {
		channelThread.start();
		String response	= MessageChannel.send( host, port, msg );
		Thread.sleep( sensibleWait );
		assertThat( response, is( "" ) );
		assertThat( received.size(), is( 1 ) );
		assertThat( received.remove(), is( msg ) );
	}

	@Test public void send_no_server() throws Exception {
		String response	= MessageChannel.send( host, port, msg );
		Thread.sleep( sensibleWait );
		assertThat( response, is( "" ) );
		assertThat( received.size(), is( 0 ) );
	}

}
