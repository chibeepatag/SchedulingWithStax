package edu.cmu.xml;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class Validate extends DefaultHandler 
{
    public static boolean valid = true;


    public void error(SAXParseException exception) { 
         System.out.println("Received notification of a recoverable error." + exception);
         valid = false;
    }

    public void fatalError(SAXParseException exception) {
         System.out.println("Received notification of a non-recoverable error."+ exception);
         valid = false;
    }
          
    public void warning(SAXParseException exception) { 
          System.out.println("Received notification of a warning."+ exception);
    }


    public static void main (String argv [])  
    {
        if (argv.length != 1) {
            System.err.println ("Usage: java Validate [filename.xml | URLToFile]");
            System.exit (1);
        }

        try {
               // get a parser
               XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

               // request validation
               reader.setFeature("http://xml.org/sax/features/validation",true);
               reader.setFeature("http://apache.org/xml/features/validation/schema",true);
               reader.setErrorHandler(new Validate());
             
               // associate an InputSource object with the file name or URL
               InputSource inputSource = new InputSource(argv[0]);

               // go ahead and parse
               reader.parse(inputSource);
        }
        catch(org.xml.sax.SAXException e) {
              System.out.println("Error in parsing " + e);
              valid = false;
              
        }
        catch(java.io.IOException e) {
              System.out.println("Error in I/O " + e);
              System.exit(0);
        }
        System.out.println("Valid Document is " + valid);
    }
}