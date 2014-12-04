package jaist.jwns.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class WriteToFile {

	public boolean write(String filename,LinkedList<String> content){
		try {			 			 
			File file = new File(filename);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),false);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String str:content){
				if(str!=null){
					bw.write(str);
					bw.newLine();
				}
				else{
					bw.write("NULL");
					bw.newLine();
				}
				
			}
			
			bw.close();
 
			return true;
 
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	public boolean write_csv(String filename,LinkedList<String> content){
		try {			 			 
			File file = new File(filename);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),false);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String str:content){
				if(str!=null){
					bw.write(str);					
				}
				else{
					//bw.write("NULL");
				}
				
			}
			
			bw.close();
 
			return true;
 
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
}
