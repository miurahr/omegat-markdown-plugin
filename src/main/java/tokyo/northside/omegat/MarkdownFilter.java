package tokyo.northside.omegat;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.omegat.core.Core;
import org.omegat.filters2.*;
import org.omegat.util.NullBufferedWriter;

import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;


/**
 * Filter for Markdown.
 *
 * @author Hiroshi Miura
 */
public class MarkdownFilter implements IFilter {

    /**
     * Callback for parse.
     */
    protected IParseCallback entryParseCallback;

    /**
     * Callback for translate.
     */
    protected ITranslateCallback entryTranslateCallback;

    /**
     * Callback for align.
     */
    protected IAlignCallback entryAlignCallback;

    /**
     * Options for processing time.
     */
    protected Map<String, String> processOptions;

    protected String inEncodingLastParsedFile;

    private BufferedWriter outfile;

    private char[] articleBuf;

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
    public String getFileFormatName() {
        return "Markdown Filter";
    }

    /**
     * Returns the hint displayed while the user edits the filter, and when she
     * adds/edits the instance of this filter. The hint may be any string,
     * preferably in a non-geek language.
     *
     * @return The hint for editing the filter in a non-geek language.
     */
    public String getHint() {
        return "Note: Filter to translate Markdown files.";
    }

    /**
     * Define fuzzy mark prefix for source which will be stored in TM. It's 'fuzzy' by default, but each
     * filter can redefine it.
     *
     * @return fuzzy mark prefix
     */
    public String getFuzzyMark() {
        return "fuzzy";
    }

    /**
     * OmegaT calls this to see whether the filter has any options. By default returns false, so filter
     * authors should override this to tell OmegaT core that this filter has options.
     *
     * @return True if the filter has any options, and false otherwise.
     */
    public boolean hasOptions() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, String> changeOptions(Dialog parent, Map<String, String> config) {
        return null;
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
        return new Instance[]{new Instance("*.md")};
    }

    /**
     * Either the encoding can be read, or it is UTF-8..
     *
     * @return <code>false</code>
     */
    public boolean isSourceEncodingVariable() {
        return false;
    }

    /**
     * @return <code>false</code>
     */
    public boolean isTargetEncodingVariable() {
        return false;
    }

    /**
     * Returns whether the file is supported by the filter, given the file and possible file's encoding (
     * <code>null</code> encoding means autodetect).
     *
     * @param inFile Source file.
     * @param fc     Filter context.
     * @return Does the filter support the file.
     */
    public boolean isFileSupported(File inFile, Map<String, String> config, FilterContext fc) {
        String inEncoding = fc.getInEncoding();
        try (BufferedReader reader = new BufferedReader(
                    (inEncoding == null)?
                        new InputStreamReader(new FileInputStream(inFile)):
                        new InputStreamReader(new FileInputStream(inFile), inEncoding))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (getHeadingLevel(trimmed) > 0) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getHeadingLevel(String trimmed) {
        String[] headers = {"######", "#####", "####", "###", "##", "#"};
        return Arrays.stream(headers)
                .filter(trimmed::startsWith)
                .findFirst()
                .map(String::length)
                .orElse(0);
    }

    public String getChars(final int start, final int end) {
        char[] buf = new char[end - start];
        System.arraycopy(articleBuf, start, buf, 0, end - start);
        return String.valueOf(buf);
    }

    public final void parseFile(File inFile, Map<String, String> config, FilterContext fc,
                                IParseCallback callback) throws Exception {
        entryParseCallback = callback;
        entryTranslateCallback = null;
        entryAlignCallback = null;
        processOptions = config;
        try {
            processFile(inFile, null, fc);
        } finally {
            entryParseCallback = null;
            processOptions = null;
        }
    }

    public final void alignFile(File inFile, File outFile, Map<String, String> config,
                                FilterContext fc, IAlignCallback callback) throws Exception {
        entryParseCallback = null;
        entryTranslateCallback = null;
        entryAlignCallback = callback;
        processOptions = config;
        try {
            // TODO: Implement me
        } finally {
            entryAlignCallback = null;
            processOptions = null;
        }
    }

    public final void translateFile(File inFile, File outFile, Map<String, String> config,
                                    FilterContext fc, ITranslateCallback callback) throws Exception {
        entryParseCallback = null;
        entryTranslateCallback = callback;
        entryAlignCallback = null;
        processOptions = config;
        try {
            entryTranslateCallback.setPass(1);
            processFile(inFile, outFile, fc);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            entryTranslateCallback = null;
            processOptions = null;
        }
    }

    private void processFile(File infile, File outfile, FilterContext fc) throws IOException {
        this.articleBuf = IOUtils.toCharArray(new BufferedReader(new FileReader(infile)));
        if (outfile != null) {
            String outEncoding = getOutputEncoding(fc);
            if (outEncoding == null) {
                this.outfile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile)));
            } else {
                this.outfile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), outEncoding));
            }
        } else {
            this.outfile = new NullBufferedWriter();
        }
        MarkdownSerializer serializer = new MarkdownSerializer(this);
        PegDownProcessor processor = new PegDownProcessor();
        RootNode astRoot = processor.parseMarkdown(articleBuf);
        serializer.processNodes(astRoot);
    }


    void writeTranslate(String text, boolean trans) throws IOException {
        writeTranslate(outfile, text, trans);
    }

    void writeTranslate(final int start, final int end, boolean trans) throws IOException {
        char[] buf = new char[end - start];
        System.arraycopy(articleBuf, start, buf, 0, end - start);
        writeTranslate(outfile, String.valueOf(buf), trans);
    }

    private void writeTranslate(BufferedWriter outfile, String value, boolean trans) throws IOException {
        value = value.trim();
        if (!value.isEmpty()) {
            if (trans) {
                String translated = processEntry(value, null);
                outfile.write(translated);
            } else {
                outfile.write(value);
            }
        }
    }

    private String processEntry(String entry, String comment) {
        if (entryParseCallback != null) {
            entryParseCallback.addEntry(null, entry, null, false, comment, null, this, null);
            return entry;
        } else {
            String translation = entryTranslateCallback.getTranslation(null, entry, null);
            return translation != null ? translation : entry;
        }
    }

    public String getInEncodingLastParsedFile() {
        return inEncodingLastParsedFile;
    }

    /**
     * Get the output encoding. If it's not set in the FilterContext (setting is "&lt;auto&gt;")
     * and the filter allows ({@link #isTargetEncodingVariable()}):
     * <ul><li>Reuse the input encoding if it's Unicode
     * <li>If the input was not Unicode, fall back to UTF-8.
     * </ul>
     * The result may be null.
     *
     * @param fc
     * @return
     */
    public String getOutputEncoding(FilterContext fc) {
        String encoding = fc.getOutEncoding();
        if (encoding == null && isTargetEncodingVariable()) {
            // Use input encoding if it's Unicode; otherwise default to UTF-8
            if (inEncodingLastParsedFile != null && inEncodingLastParsedFile.toLowerCase().startsWith("utf-")) {
                encoding = inEncodingLastParsedFile;
            } else {
                encoding = "UTF-8";
            }
        }
        return encoding;
    }
}
