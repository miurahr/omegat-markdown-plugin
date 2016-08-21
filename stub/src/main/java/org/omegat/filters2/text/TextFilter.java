/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2014 Didier Briel
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

package org.omegat.filters2.text;

import java.awt.Dialog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.omegat.filters2.AbstractFilter;
import org.omegat.filters2.FilterContext;
import org.omegat.filters2.Instance;

public class TextFilter extends AbstractFilter {
    public static final String ISO88592 = "ISO-8859-2";

    /**
     * Text filter should segmentOn text into paragraphs on line breaks.
     */
    public static final String SEGMENT_BREAKS = "BREAKS";
    /**
     * Default. Text filter should segmentOn text into paragraphs on empty lines.
     */
    public static final String SEGMENT_EMPTYLINES = "EMPTYLINES";
    /**
     * Text filter should not segmentOn text into paragraphs.
     */
    public static final String SEGMENT_NEVER = "NEVER";

    public static final String OPTION_SEGMENT_ON = "segmentOn";
    
    /**
     * Length at which a line break should occur in target documents
     */
    public static final String OPTION_LINE_LENGTH = "lineLength";
    
    /**
     * Maximum line length in target documents
     */
    public static final String OPTION_MAX_LINE_LENGTH = "maxLineLength";

    
    /**
     * Register plugin into OmegaT.
     */
    public static void loadPlugins() {
    }

    public static void unloadPlugins() {
    }

    @Override
    public String getFileFormatName() {
        return "";
    }

    @Override
    public Instance[] getDefaultInstances() {
        return new Instance[] { new Instance("*.txt")};
    }

    @Override
    public boolean isSourceEncodingVariable() {
        return true;
    }

    @Override
    public boolean isTargetEncodingVariable() {
        return true;
    }
    
    @Override
    protected boolean requirePrevNextFields() {
        return true;
    }

    @Override
    public void processFile(BufferedReader in, BufferedWriter out, FilterContext fc) throws IOException {
    }

    @Override
    public Map<String, String> changeOptions(Dialog parent, Map<String, String> config) {
        return null;
    }

    /**
     * Returns true to indicate that Text filter has options.
     * 
     * @return True, because Text filter has options.
     */
    @Override
    public boolean hasOptions() {
        return true;
    }
}
