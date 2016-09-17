package tokyo.northside.omegat.markdown;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


/**
 * Utility class for markdown filter.
 * @author Hiroshi Miura
 */
public class MarkdownFilterUtils {

    /**
     * Create BufferedReader from specified file and encoding.
     *
     * @param inFile file to read.
     * @param inEncoding file encoding.
     * @return BufferReader object.
     * @throws IOException when file I/O error happened.
     */
    public static BufferedReader getBufferedReader(final File inFile, final String inEncoding)
            throws IOException {
        InputStreamReader isr;
        if (inEncoding == null) {
            isr = new InputStreamReader(new FileInputStream(inFile), Charset.defaultCharset());
        } else {
            isr = new InputStreamReader(new FileInputStream(inFile), inEncoding);
        }
        return new BufferedReader(isr);
    }

    /**
     * Create BufferedWriter object from specified file and encoding.
     *
     * @param outFile file to output.
     * @param outEncoding file encoding.
     * @return BufferedWiter object.
     * @throws IOException when file I/O error happened.
     */
    public static BufferedWriter getBufferedWriter(final File outFile, final String outEncoding)
            throws IOException {
        OutputStreamWriter osw;
        if (outEncoding == null) {
           osw = new OutputStreamWriter(new FileOutputStream(outFile), Charset.defaultCharset());
        } else {
            osw = new OutputStreamWriter(new FileOutputStream(outFile), outEncoding);
        }
        return new BufferedWriter(osw);
    }
}
