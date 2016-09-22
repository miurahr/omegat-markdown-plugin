/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2008 Alex Buloichik
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package tokyo.northside.omegat.markdown;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.omegat.core.Core;
import org.omegat.core.data.EntryKey;
import org.omegat.core.data.ProjectProperties;
import org.omegat.core.data.ProtectedPart;
import org.omegat.core.data.RealProject;
import org.omegat.filters2.AbstractFilter;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.IAlignCallback;
import org.omegat.filters2.IFilter;
import org.omegat.filters2.IParseCallback;
import org.omegat.filters2.ITranslateCallback;
import org.omegat.filters2.master.FilterMaster;
import org.omegat.tokenizer.DefaultTokenizer;
import org.omegat.util.Language;

import com.alibaba.fastjson.JSON;

import static org.testng.Assert.*;


/**
 * Base class for test filter parsing.
 * 
 * @author Alex Buloichik <alex73mail@gmail.com>
 * @author Hiroshi Miura
 */
abstract class TestFilterBase  {
    protected FilterContext context = new FilterContext(new Language("en"), new Language("be"), false)
            .setTargetTokenizerClass(DefaultTokenizer.class);

    protected File outFile;

    protected void setUp() throws Exception {
        Core.initializeConsole(Collections.emptyMap());
        Core.setFilterMaster(new FilterMaster(FilterMaster.createDefaultFiltersConfig()));
        Core.setProject(new TestProject(new ProjectPropertiesTest()));
    }

    protected List<String> parse(IFilter filter, String resource) throws Exception {
        return parse(filter, resource, Collections.emptyMap());
    }

    protected List<String> parse(IFilter filter, String resource, Map<String, String> options)
            throws Exception {
        final List<String> result = new ArrayList<>();

        filter.parseFile(new File(this.getClass().getResource(resource).getFile()), options, context, new IParseCallback() {
            public void addEntry(String id, String source, String translation, boolean isFuzzy,
                    String comment, IFilter filter) {
                addEntry(id, source, translation, isFuzzy, comment, null, filter, null);
            }

            public void addEntry(String id, String source, String translation, boolean isFuzzy, String comment,
                                 String path, IFilter filter, List<ProtectedPart> protectedParts) {
                String[] props = comment == null ? null : new String[] { "comment", comment };
                addEntryWithProperties(id, source, translation, isFuzzy, props, path, filter, protectedParts);
            }

            public void addEntryWithProperties(String id, String source, String translation,
                                               boolean isFuzzy, String[] props, String path,
                                               IFilter filter, List<ProtectedPart> protectedParts) {
                if (!source.isEmpty()) {
                    result.add(source);
                }
            }

            public void linkPrevNextSegments() {
            }
        });

        return result;
    }

    protected void parse2(final AbstractFilter filter, final String filename,
            final Map<String, String> result, final Map<String, String> legacyTMX) throws Exception {

        filter.parseFile(new File(filename), Collections.emptyMap(), context, new IParseCallback() {
            public void addEntry(String id, String source, String translation, boolean isFuzzy,
                    String comment, IFilter filter) {
                addEntry(id, source, translation, isFuzzy, comment, null, filter, null);
            }

            public void addEntry(String id, String source, String translation, boolean isFuzzy, String comment,
                    String path, IFilter filter, List<ProtectedPart> protectedParts) {
                String[] props = comment == null ? null : new String[] { "comment", comment };
                addEntryWithProperties(id, source, translation, isFuzzy, props, path, filter, protectedParts);
            }

            @Override
            public void addEntryWithProperties(String id, String source, String translation,
                    boolean isFuzzy, String[] props, String path,
                    IFilter filter, List<ProtectedPart> protectedParts) {
                String segTranslation = isFuzzy ? null : translation;
                result.put(source, segTranslation);
                if (translation != null) {
                    // Add systematically the TU as a legacy TMX
                    String tmxSource = isFuzzy ? "[" + filter.getFuzzyMark() + "] " + source : source;
                    addFileTMXEntry(tmxSource, translation);
                }
            }

            public void addFileTMXEntry(String source, String translation) {
                legacyTMX.put(source, translation);
            }

            public void linkPrevNextSegments() {
            }
        });
    }

    protected void translate(IFilter filter, String resource) throws Exception {
        translate(filter, resource, Collections.emptyMap());
    }
    
    protected void translate(IFilter filter, String resource, Map<String, String> config) throws Exception {
        outFile = File.createTempFile("output", ".md");
        outFile.deleteOnExit();
        filter.translateFile(new File(this.getClass().getResource(resource).getFile()), outFile, config, context,
                new ITranslateCallback() {
                    public String getTranslation(String id, String source, String path) {
                        return source;
                    }

                    public String getTranslation(String id, String source) {
                        return source;
                    }

                    public void linkPrevNextSegments() {
                    }

                    public void setPass(int pass) {
                    }
                });
    }

    protected void align(IFilter filter, String in, String out, IAlignCallback callback) throws Exception {
        File inFile = new File("test/data/filters/" + in);
        File outFile = new File("test/data/filters/" + out);
        filter.alignFile(inFile, outFile, Collections.emptyMap(), context, callback);
    }

    protected void translateText(IFilter filter, String filename) throws Exception {
        translateText(filter, filename, Collections.emptyMap());
    }

    protected void translateText(IFilter filter, String resource, Map<String, String> config) throws Exception {
        translate(filter, resource, config);
        boolean result = FileUtils.contentEquals(new File(this.getClass().getResource(resource)
                .getFile()), outFile);
        if (!result) {
            List<String> origin = IOUtils.readLines(FileUtils.openInputStream(new File(this.getClass().getResource(resource)
                .getFile())));
            List<String> lines = IOUtils.readLines(FileUtils.openInputStream(outFile));
            if (origin.size() != lines.size()) {
                fail("Differ a number of lines. Original:" + origin.size() + ", outFile: " + lines.size());
            }
            assertEquals(lines, origin, resource);
        }
    }

    /**
     * ProjectProperties successor for create project without directory.
     */
    protected static class ProjectPropertiesTest extends ProjectProperties {
        ProjectPropertiesTest() {
            super();
            setTargetTokenizer(DefaultTokenizer.class);
        }
    }

    /**
     * RealProject successor for load file testing only.
     */
    protected class TestProject extends RealProject {

        public TestProject(ProjectProperties props) {
            super(props);
        }

        public FileInfo loadSourceFiles(IFilter filter, String file, Map<String, String> filterOptions) throws Exception {
            Core.setProject(this);

            Set<String> existSource = new HashSet<String>();
            Set<EntryKey> existKeys = new HashSet<EntryKey>();

            LoadFilesCallback loadFilesCallback = new LoadFilesCallback(existSource, existKeys);

            FileInfo fi = new FileInfo();
            fi.filePath = file;

            loadFilesCallback.setCurrentFile(fi);

            filter.parseFile(new File(file), filterOptions, context, loadFilesCallback);

            loadFilesCallback.fileFinished();

            return fi;
        }
    }

    protected List<AlignedEntry> al;
    protected int alCount;

    protected void checkAlignStart(TestAlignCallback calback) {
        this.al = calback.entries;
        alCount = 0;
    }

    protected void checkAlignEnd() {
        assertEquals(alCount, al.size());
    }

    protected void checkAlign(String id, String source, String translation, String path) {
        AlignedEntry en = al.get(alCount);
        assertEquals(id, en.id);
        assertEquals(source, en.source);
        assertEquals(translation, en.translation);
        assertEquals(path, en.path);
        alCount++;
    }

    protected void checkAlignById(String id, String source, String translation, String path) {
        for(AlignedEntry en:al) {
            if (id.equals(en.id)) {
                assertEquals(source, en.source);
                assertEquals(translation, en.translation);
                assertEquals(path, en.path);
                alCount++;
                return;
            }
        }
        fail();
    }

    protected static class TestAlignCallback implements IAlignCallback {
        public List<AlignedEntry> entries = new ArrayList<AlignedEntry>();

        public void addTranslation(String id, String source, String translation, boolean isFuzzy,
                String path, IFilter filter) {
            AlignedEntry en = new AlignedEntry();
            en.id = id;
            en.source = source;
            en.translation = translation;
            en.path = path;
            entries.add(en);
        }
    }

    protected static class AlignedEntry {
        public String id;
        public String source;
        public String translation;
        public String path;
    }

    protected void test(final String testcase) throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, testcase + ".md");
        List<String> entries = parse(mdf, testcase + ".md");
        try (BufferedReader reader = MarkdownFilterUtils.getBufferedReader(new
                        File(this.getClass().getResource(testcase + ".json").getFile()),
                "UTF-8")) {
            String jsonString = IOUtils.toString(reader);
            ArrayList expected = JSON.parseObject(jsonString, ArrayList.class);
            assertEquals(entries, expected, testcase);
        }
    }

    protected void testTranslate(final String testcase) throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, testcase + ".md");
    }

}
