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

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;



/** Startup method for MobileClicker server
 * @author tommy skodje
 */
public class Main {
	private static boolean done	= false;
	private static final int port	= 4567;

	/** Entry point */
	public static void main(String[] args) throws Exception {
		MessageChannel channel	= MessageChannel.openInbound( port );
		QRCode qr	= new QRCode( 100, 100 );
		qr.generate( myIPAddress() + ":" + port );
		final SwingGui gui	= new SwingGui();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui.show();
			}
		});

		while ( ! done ) {
			String message	= channel.waitForNextMessage();
			gui.showFeedBack( message );
			// done	= StringUtils.isEmpty( message );
			// FixMe: how to close?
			System.err.println( message );
		}
	}

	private static String myIPAddress() throws UnknownHostException {
		String ip	= InetAddress.getLocalHost().getHostAddress();
		return ip;
	}

	/* private static void  getInterfaces() {
  try {
     Enumeration<?> e = NetworkInterface.getNetworkInterfaces();

     while(e.hasMoreElements()) {
        NetworkInterface ni = (NetworkInterface) e.nextElement();
        System.out.println("Net interface: "+ni.getName());

        Enumeration<?> e2 = ni.getInetAddresses();

        while (e2.hasMoreElements()){
           InetAddress ip = (InetAddress) e2.nextElement();
           System.out.println("IP address: "+ ip.toString());
        }
     }
  } catch (Exception e) {
     e.printStackTrace();
  }
}*/

}
