/* SwingGui.java
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

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * @author tommy skodje
*/
public class SwingGui implements UserFeedBack {

	private final JScrollPane feedbackScrollPane;
  private final JTextArea feedbackTextArea;
  private final JFrame frame;

	/**  */
	protected SwingGui() {
		frame = new JFrame("Mobile Clicker Server");
    feedbackTextArea = new JTextArea();
    feedbackScrollPane = new JScrollPane();
    setup();
	}

	/**  */
	protected void show() {
		frame.setVisible(true);
	}


	@Override public void showFeedBack(String message) {
		feedbackTextArea.setText( message + "<br/>" + feedbackTextArea.getText() );
	}

	@Override public void showAlert(String message) {
		feedbackTextArea.setText( "ALERT: " + message + "<br/>" + feedbackTextArea.getText() );
	}

	@Override public void showError( String message, Throwable t ) {
		feedbackTextArea.setText( "ERROR: " + message + "<br/>" + t.getMessage() + "<br/>" + feedbackTextArea.getText() );
		t.printStackTrace();
	}

	@Override public void showDebug(String message) {
		feedbackTextArea.setText( "DEBUG: " + message + "<br/>" + feedbackTextArea.getText() );
	}

	private void setup() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel label = new JLabel("Hello World");
		label.setHorizontalAlignment( SwingConstants.CENTER );
		frame.getContentPane().add( label, BorderLayout.PAGE_START );

		try {
			BufferedImage myPicture = ImageIO.read( new File( QRCode.fileName ) );
			JLabel picLabel = new JLabel(new ImageIcon( myPicture ));
			frame.getContentPane().add( picLabel, BorderLayout.CENTER );
		} catch ( IOException ioe ) {
			// whatever
		}

    feedbackTextArea.setColumns(20);
    feedbackTextArea.setEditable(false);
    feedbackTextArea.setRows(5);
    feedbackTextArea.setToolTipText("feedback area");
    feedbackTextArea.setName("feedbackTextArea"); // NOI18N

    feedbackScrollPane.setName("feedbackScrollPane"); // NOI18N
    feedbackScrollPane.setViewportView(feedbackTextArea);

    frame.getContentPane().add( feedbackScrollPane, BorderLayout.PAGE_END );
		frame.pack();
	}

}
