import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTest {
    // use the classname for the logger, this way you can refactor
    private final static Logger LOGGER = Logger.getLogger("LoggerTest");
    
    private void setup() {
    	FileHandler fh;  

        try {  

            // This block configure the logger with handler and formatter  
            fh = new FileHandler("./log/MyLogFile.log");  
            LOGGER.addHandler(fh);
            // the following statement is used to log any messages  
            LOGGER.info("My first log");  

        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
    }	

    public void doSomeThingAndLog() {
    	setup();
        // ... more code

        // now we demo the logging

        // set the LogLevel to Severe, only severe Messages will be written
        LOGGER.setLevel(Level.SEVERE);
        LOGGER.severe("Info Log1");
        LOGGER.warning("Info Log1");
        LOGGER.info("Info Log");
        LOGGER.finest("Really not important");

        // set the LogLevel to Info, severe, warning and info will be written
        // finest is still not written
        LOGGER.setLevel(Level.INFO);
        LOGGER.severe("Info Log");
        LOGGER.warning("Info Log");
        LOGGER.info("Info Log");
        LOGGER.finest("Really not important");
    }

    public static void main(String[] args) {
    	LoggerTest tester = new LoggerTest();
//        try {
////            MyLogger.setup();
//        	LoggerTest test = new LoggerTest();
//        	test.doSomeThingAndLog();
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Problems with creating the log files");
//        }
        tester.doSomeThingAndLog();
    }
}