/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool
          with fuzzy matching, translation memory, keyword search,
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2016 Hiroshi Miura
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
public final class MarkdownFilterUtils {

    /** Private constructor for util class. */
    private MarkdownFilterUtils() {
    }

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
