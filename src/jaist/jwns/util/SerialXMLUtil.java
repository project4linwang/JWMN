package jaist.jwns.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Serialize the object to the XML file
 * @author lin
 *
 */
public class SerialXMLUtil {
  
	public static void save(Object network, String filename) throws IOException{
		XMLEncoder os=new XMLEncoder(
				new BufferedOutputStream(
						new FileOutputStream(filename)));
		os.writeObject(network);
		os.close();
	}
	
	public static Object load(String filename) throws IOException{
		Object network=null;
		XMLDecoder imp=new XMLDecoder(
				new BufferedInputStream(new FileInputStream(filename)));
		network=imp.readObject();
		imp.close();
		return network;
	}
}
