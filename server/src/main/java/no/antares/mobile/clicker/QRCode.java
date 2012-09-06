/* from http://stackoverflow.com/questions/2489048/qr-code-encoding-and-decoding-using-zxing */
package no.antares.mobile.clicker;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

/* Generates QR-code for a given string
@see http://stackoverflow.com/questions/2489048/qr-code-encoding-and-decoding-using-zxing
 */
public class QRCode {
	final static String charSetName	= "UTF-8";
	// public static final String fileName = "/Users/tommyskodje/Downloads/TSK_QR_S.png";
	public static final String fileName = "QR.png";
	// TODO: use streams, not file

	final int h;
	final int w;

	/**  */
	public QRCode( int height, int width ) {
		this.h = height;
		this.w = width;
	}

	public void generate( String text ) {
		byte[] b = text.getBytes( Charset.forName( charSetName ) );

		try {
			String data = new String(b, charSetName );
			// get a byte matrix for the data
			BitMatrix matrix = null;
			Writer writer = new MultiFormatWriter();
			try {
				Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
				hints.put(EncodeHintType.CHARACTER_SET, charSetName);
				matrix = writer.encode( data, BarcodeFormat.QR_CODE, w, h, hints );
			} catch ( WriterException e ) {
				System.out.println(e.getMessage());
			}

			try {
				File file = new File( fileName );
				MatrixToImageWriter.writeToFile(matrix, "PNG", file);
				System.out.println("printing to " + file.getAbsolutePath());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
	}

}
