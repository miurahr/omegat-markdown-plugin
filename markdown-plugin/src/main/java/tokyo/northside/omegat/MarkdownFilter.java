package tokyo.northside.omegat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.omegat.core.Core;
import org.omegat.filters2.AbstractFilter;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.IFilter;
import org.omegat.filters2.Instance;
import org.omegat.util.LinebreakPreservingReader;


/**
 * Filter for Markdown.
 * 
 * @author Hiroshi Miura
 */
public class MarkdownFilter extends AbstractFilter implements IFilter {

    public static void loadPlugins() {
        Core.registerFilterClass(MarkdownFilter.class);
    }

    public static void unloadPlugins() {
    }

    /**
     * Creates a new instance.
     */
    public MarkdownFilter() {
        super();
    }

    /**
     * Human-readable name of the File Format this filter supports.
     * 
     * @return File format name
     */
    @Override
    public String getFileFormatName() {
        return "Markdown Filter";
    }

    /**
     * Returns the hint displayed while the user edits the filter, and when she
     * adds/edits the instance of this filter. The hint may be any string,
     * preferably in a non-geek language.
     * 
     * 
     * @return The hint for editing the filter in a non-geek language.
     */
    public String getHint() {
        return "Note: Filter to translate Markdown files.";
    }

    /**
     * The default list of filter instances that this filter class has. One
     * filter class may have different filter instances, different by source
     * file mask, encoding of the source file etc.
     * <p>
     * Note that the user may change the instances freely.
     * 
     * @return Default filter instances
     */
    public Instance[] getDefaultInstances() {
        return new Instance[] { new Instance("*.md") };
    }

    /**
     * Either the encoding can be read, or it is UTF-8..
     * 
     * @return <code>false</code>
     */
    @Override
    public boolean isSourceEncodingVariable() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    @Override
    public boolean isTargetEncodingVariable() {
        return false;
    }
    
    protected boolean requirePrevNextFields() {
        return false;
    }

    /**
     * @return <code>true</code> or <code>false</code>
     */
    @Override
    public boolean isFileSupported(BufferedReader reader) {
        LinebreakPreservingReader lbpr = new LinebreakPreservingReader(reader);
        try {
            String line;
            while ((line = lbpr.readLine()) != null) {
                String trimmed = line.trim();
                if (getHeadingLevel(trimmed) > 0) {
                    lbpr.close();
                    return true;
                }
            }
            lbpr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected int getHeadingLevel(String trimmed) {
        String[] headers = {"######", "#####", "####", "###", "##", "#"};
        return Arrays.stream(headers)
                .filter(s -> trimmed.startsWith(s))
                .findFirst()
                .map(s -> s.length())
                .orElse(0);
    }

    @Override
    public void processFile(BufferedReader reader, BufferedWriter outfile, FilterContext fc) throws IOException {
        LinebreakPreservingReader lbpr = new LinebreakPreservingReader(reader);
        String line;
        StringBuilder text = new StringBuilder();
        
        while ((line = lbpr.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                writeTranslate(outfile, text, lbpr);
                outfile.write(line + lbpr.getLinebreak());
            }
        }
    }

    private void writeTranslate(BufferedWriter outfile, StringBuilder text, LinebreakPreservingReader lbpr)
            throws IOException {
        if (text.length() > 0) {
            String value = text.toString();
            text.setLength(0);
            writeTranslate(outfile, value, lbpr);
        }
    }

    private void writeTranslate(BufferedWriter outfile, String value, LinebreakPreservingReader lbpr)
            throws IOException {
        value = value.trim();
        if (!value.isEmpty()) {
            String trans = processEntry(value);
            outfile.write(trans);
            if (lbpr != null) {
                outfile.write(lbpr.getLinebreak());
            }

            // etc.
        }
    }
}
