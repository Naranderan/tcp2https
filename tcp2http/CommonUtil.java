import java.io.InputStream;
import java.io.OutputStream;

//$Id$

public class CommonUtil {
	public static void pipe(InputStream is, OutputStream os) {
		byte[] buffer = new byte[Constants.BUFFER_SIZE];
		try {
			int read = 0;
             while ((read = is.read(buffer)) != -1) {
                     os.write(buffer, 0, read);//, offset, read);
             }
            os.flush();
     } catch(Exception e) {
             System.out.println("Exception occurred while piping te streams :: " + e);
     }
	}
}
