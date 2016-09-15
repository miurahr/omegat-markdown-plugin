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

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FileUtils;

/**
 * Utility class for markdown filter.
 * @author Hiroshi Miura
 */
public class MarkdownFilterUtils {

    public static String getDefaultEncodingName() {
        return Charset.defaultCharset().name();
    }

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

    public static char[] toCharArray(final BufferedReader reader) throws IOException {
        return IOUtils.toCharArray(reader);
    }

    public static String toString(final BufferedReader reader) throws IOException {
        return IOUtils.toString(reader);
    }

}
