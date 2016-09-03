package tokyo.northside.omegat.markdown;

import java.awt.Dialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.omegat.core.Core;
import org.omegat.core.data.ProtectedPart;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.IAlignCallback;
import org.omegat.filters2.IFilter;
import org.omegat.filters2.IParseCallback;
import org.omegat.filters2.ITranslateCallback;
import org.omegat.filters2.Instance;

import org.pegdown.PegDownProcessor;
import org.pegdown.ast.RootNode;


/**
 * Filter for Markdown.
 *
 * @author Hiroshi Miura
 */
public class OmegatMarkdownFilter implements IFilter {

    /**
     * Callback for parse.
     */
    private IParseCallback entryParseCallback;

    /**
     * Callback for translate.
     */
    private ITranslateCallback entryTranslateCallback;

    /**
     * Callback for align.
     */
    private IAlignCallback entryAlignCallback;

    /**
     * Options for processing time.
     */
    private Map<String, String> processOptions;

    private String inEncodingLastParsedFile;

    private StringBuilder outbuf;

    /**
     * Plugin loader.
     */
    public static void loadPlugins() {
        Core.registerFilterClass(OmegatMarkdownFilter.class);
    }

    /**
     * Plugin unloader.
     */
    public static void unloadPlugins() {
    }

    /**
     * Creates a new instance.
     */
    public OmegatMarkdownFilter() {
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
     * Define fuzzy mark prefix for source which will be stored in TM.
     * It's 'fuzzy' by default, but each
     * filter can redefine it.
     *
     * @return fuzzy mark prefix
     */
    public String getFuzzyMark() {
        return "fuzzy";
    }

    /**
     * OmegaT calls this to see whether the filter has any options.
     * By default returns false, so filter
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
    public Map<String, String> changeOptions(final Dialog parent,
                                             final Map<String, String> config) {
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
     * Returns whether the file is supported by the filter,
     * given the file and possible file's encoding (
     * <code>null</code> encoding means autodetect).
     *
     * @param inFile Source file.
     * @param config optional configuration.
     * @param fc     Filter context.
     * @return Does the filter support the file.
     */
    public boolean isFileSupported(final File inFile, final Map<String, String> config,
                                   final FilterContext fc) {
        String inEncoding = fc.getInEncoding();
        try (BufferedReader reader = new BufferedReader(
                (inEncoding == null)
                        ? new InputStreamReader(new FileInputStream(inFile))
                        : new InputStreamReader(new FileInputStream(inFile), inEncoding))) {
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

    /**
     * Simple heading detector without parser.
     *
     * @param trimmed trimmed string.
     * @return true if it has MarkDown header, otherwise false.
     */
    private int getHeadingLevel(final String trimmed) {
        String[] headers = {"######", "#####", "####", "###", "##", "#"};
        return Arrays.stream(headers)
                .filter(trimmed::startsWith)
                .findFirst()
                .map(String::length)
                .orElse(0);
    }

    /**
     * Parse markdown file and process it.
     *
     * @param inFile   file to parse
     * @param config   filter's configuration options
     * @param fc       filters context.
     * @param callback callback for parsed data
     * @throws IOException when I/O error happened.
     */
    public final void parseFile(final File inFile, final Map<String, String> config,
                                final FilterContext fc, final IParseCallback callback)
            throws IOException {
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

    /**
     * Align markdown files.
     * <p>
     * Currently not supported.
     * </p>
     *
     * @param inFile   source file
     * @param outFile  translated file
     * @param config   filter's configuration options
     * @param fc filter context.
     * @param callback callback for store aligned data
     * @throws IOException when I/O error happened.
     */
    public final void alignFile(final File inFile, final File outFile,
                                final Map<String, String> config, final FilterContext fc,
                                final IAlignCallback callback) throws IOException {
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

    /**
     * Generate translated files.
     *
     * @param inFile   source file
     * @param outFile  output file
     * @param config   filter's configuration options
     * @param fc       filters context.
     * @param callback callback for get translation
     * @throws IOException when I/O error happened.
     */
    public final void translateFile(final File inFile, final File outFile,
                                    final Map<String, String> config, final FilterContext fc,
                                    final ITranslateCallback callback) throws IOException {
        entryParseCallback = null;
        entryTranslateCallback = callback;
        entryAlignCallback = null;
        processOptions = config;
        try {
            entryTranslateCallback.setPass(1);
            processFile(inFile, outFile, fc);
        } finally {
            entryTranslateCallback = null;
            processOptions = null;
        }
    }

    /**
     * Core of Markdown file processing.
     *
     * @param inFile  input file.
     * @param outFile output file.
     * @param fc      filter context.
     * @throws IOException throes when I/O error happened.
     */
    void processFile(final File inFile, final File outFile, final FilterContext fc)
            throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inFile))) {
            process(reader);
            reader.close();
            inEncodingLastParsedFile = "UTF-8";
            if (outFile != null) {
                String outEncoding = getOutputEncoding(fc);
                try (BufferedWriter outfile = new BufferedWriter(
                        (outEncoding == null)
                                ? new OutputStreamWriter(new FileOutputStream(outFile))
                                : new OutputStreamWriter(new FileOutputStream(outFile),
                                        outEncoding))) {
                    outfile.write(outbuf.toString());
                    outfile.flush();
                    outfile.close();
                }
            }
        }
    }

    /**
     * Process Markdown file from reader.
     *
     * @param reader reader specified to source markdown file.
     * @throws IOException throws when I/O error hapened.
     */
    void process(final BufferedReader reader) throws IOException {
        resetOutbuf();
        EntryHandler handler = new EntryHandler(this, IOUtils.toCharArray(reader));
        MarkdownSerializer serializer = new MarkdownSerializer(handler);
        PegDownProcessor processor = new PegDownProcessor();
        RootNode astRoot = processor.parseMarkdown(handler.getArticle());
        serializer.processNodes(astRoot);
        handler.finish();
    }

    /**
     * Process Markdown Strings.
     * <p>
     * This method is for Test purpose.
     * </p>
     *
     * @param testInput Markdown strings.
     */
    void process(final String testInput) {
        resetOutbuf();
        EntryHandler handler = new EntryHandler(this, testInput.toCharArray());
        MarkdownSerializer serializer = new MarkdownSerializer(handler);
        PegDownProcessor processor = new PegDownProcessor();
        RootNode astRoot = processor.parseMarkdown(handler.getArticle());
        serializer.processNodes(astRoot);
        handler.finish();
    }

    /**
     * Process entries and push it to OmegaT core.
     * @param entry entry string to add.
     * @return translation if TranslationCallback defined, otherwise original entry.
     */
    String processEntry(final String entry) {
        if (entryParseCallback != null) {
            entryParseCallback.addEntry(null, entry, null, false, null, null, this, null);
            return entry;
        } else {
            String translation = entryTranslateCallback.getTranslation(null, entry, null);
            return translation != null ? translation : entry;
        }
    }

    /**
     * Process entries and push it to OmegaT core.
     * @param entry entry string to add.
     * @return translation if TranslationCallback defined, otherwise original entry.
     */
    String processEntry(final String entry, final List<ProtectedPart> protectedParts) {
        if (entryParseCallback != null) {
            entryParseCallback.addEntry(null, entry, null, false, null, null, this, protectedParts);
            return entry;
        } else {
            String translation = entryTranslateCallback.getTranslation(null, entry, null);
            return translation != null ? translation : entry;
        }
    }

    /**
     * Return input file's encoding.
     * @return always UTF-8 with this plugin.
     */
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
     * @param fc filter context.
     * @return Encoding for output file.
     */
    public String getOutputEncoding(final FilterContext fc) {
        String encoding = fc.getOutEncoding();
        if (encoding == null && isTargetEncodingVariable()) {
            // Use input encoding if it's Unicode; otherwise default to UTF-8
            if (inEncodingLastParsedFile != null && inEncodingLastParsedFile
                    .toLowerCase().startsWith("utf-")) {
                encoding = inEncodingLastParsedFile;
            } else {
                encoding = "UTF-8";
            }
        }
        return encoding;
    }

    /**
     * Process entry and write translation to translated file.
     *
     * @param value entry string.
     */
    void writeTranslate(final String value, final List<ProtectedPart> protectedParts) {
        String translated = processEntry(value, protectedParts);
        outbuf.append(translated);
    }

    /**
     * Process entry and write translation to translated file.
     *
     * @param value entry string.
     * @param trans true when make translation for entry, otherwise write directory.
     */
    void writeTranslate(final String value, final boolean trans) {
        if (!value.isEmpty()) {
            if (trans) {
                String translated = processEntry(value);
                outbuf.append(translated);
            } else {
                outbuf.append(value);
            }
        }
    }

    /**
     * Reset buffer for Test.
     */
    void resetOutbuf() {
        outbuf = new StringBuilder();
    }

    /**
     * Get buffer contents for Test.
     */
    String getOutbuf() {
        return outbuf.toString();
    }

    /**
     * Append to buffer for test.
     */
    void appendOutbuf(final String text) {
        outbuf.append(text);
    }
}
