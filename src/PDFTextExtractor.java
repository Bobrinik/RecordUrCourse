import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;

public class PDFTextExtractor {
	public static void parseToStringExample() {
	    Tika tika = new Tika();
	    try 
	    {
	        FileInputStream fileInputStream = new FileInputStream(new File("421-er.pdf"));
	        
			System.out.println(tika.parseToString( fileInputStream));
	    }
	    catch(Exception e){
	    	System.out.println(e.toString());
	    }
	}
	
	public static void parseToHTML(){
	    ContentHandler handler = new ToXMLContentHandler();
	 
	    AutoDetectParser parser = new AutoDetectParser();
	    Metadata metadata = new Metadata();
	    try
	    {
	    	FileInputStream fileInputStream = new FileInputStream(new File("421-er.pdf"));
	        parser.parse(fileInputStream, handler, metadata);
	        System.out.println(handler.toString());
	    }
	    catch(Exception e){
	    	System.out.println(e.toString());
	    }
	}
	
	public static void main(String args[]){
		parseToHTML();
		
	}

}
