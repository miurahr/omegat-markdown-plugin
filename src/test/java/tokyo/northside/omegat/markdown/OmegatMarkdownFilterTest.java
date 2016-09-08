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

import static org.testng.Assert.*;

import org.omegat.filters2.FilterContext;
import org.testng.annotations.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Test for OmegatMarkdownFilter plugin for omegat.
 * Created by miurahr on 16/08/23.
 */
class OmegatMarkdownFilterTest extends TestFilterBase {
    @Test
    void testGetFileFormatName() throws Exception {
        String expected = "Markdown Filter";
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertEquals(mdf.getFileFormatName(), expected);
    }

    @Test
    void testGetHint() throws Exception {
        String expected = "Note: Filter to translate Markdown files.";
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertEquals(mdf.getHint(), expected);
    }

    @Test
    void testIsSourceEncodingVariable() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertFalse(mdf.isSourceEncodingVariable());
    }

    @Test
    void testIsTargetEncodingVariable() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        assertFalse(mdf.isTargetEncodingVariable());
    }

    @Test
    void testIsFileSupported_true() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        File target = new File(this.getClass().getResource("/source/case0.md").getFile());
        FilterContext fc = new FilterContext();
        assertTrue(mdf.isFileSupported(target, null, fc));
    }

    @Test
    void testIsFileSupported_false() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        File target = new File(this.getClass().getResource("/source/nomarkdown.txt").getFile());
        FilterContext fc = new FilterContext();
        assertFalse(mdf.isFileSupported(target, null, fc));
    }

    @Test
    void testProcess_case1() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/case1.md");
        List<String> expected = new ArrayList<>();
        expected.add("Setup With Amazon S3");
        expected.add("**Note**: Since Seafile Server 5.0.0, all config files are moved to the central **conf** folder. [Read More](../deploy/new_directory_layout_5_0_0.md).");
        expected.add("Prepare");
        expected.add("To setup Seafile Professional Server with Amazon S3:");
        expected.add("Setup the basic Seafile Professional Server following the guide on [Download and setup Seafile Professional Server](download_and_setup_seafile_professional_server.md)" );
        expected.add("Install the python `boto` library. It's needed to access S3 service.");
        expected.add("Install and configure memcached. For best performance, Seafile requires install memcached and enable memcache for objects. We recommend to allocate 128MB memory for memcached. Edit /etc/memcached.conf");
        expected.add("Modify Seafile.conf");
        expected.add("Edit `seafile.conf`, add the following lines:");
        expected.add("It's recommended to create separate buckets for commit, fs, and block objects.\n" +
                "The key_id and key are required to authenticate you to S3. You can find the key_id and key in the \"security credentials\" section on your AWS account page.");
        expected.add("When creating your buckets on S3, please first read [S3 bucket naming rules][1]. Note especially not to use **UPPERCASE** letters in bucket names (don't use camel style names, such as MyCommitOjbects).");
        expected.add("Use S3 in newer regions");
        expected.add("After Januaray 2014, new regions of AWS will only support authentication signature version 4 for S3. At this time, new region includes Frankfurt and China.");
        expected.add("To use S3 backend in these regions, add following options to commit_object_backend, fs_object_backend and block_backend section in seafile.conf");
        expected.add("For file search and webdav to work with the v4 signature mechanism, you need to add following lines to ~/.bot");
        expected.add("Using memcached cluster");
        expected.add("In a cluster environment, you may want to use a memcached cluster. In the above configuration, you have to specify all the memcached server node addresses in seafile.conf");
        expected.add("Use HTTPS connections to S3");
        expected.add("Since Pro 5.0.4, you can use HTTPS connections to S3. Add the following options to seafile.conf:");
        expected.add("Because the server package is built on CentOS 6, if you're using Debian/Ubuntu, you have to copy the system CA bundle to CentOS's CA bundle path. Otherwise Seafile can't find the CA bundle so that the SSL connection will fail.");
        expected.add("Another important note is that you **must not use '.' in your bucket names**. Otherwise the wildcard certificate for AWS S3 cannot be resolved. This is a limitation on AWS.");
        expected.add("Use S3-compatible Object Storage");
        expected.add("`host` is the address and port of the S3-compatible service. You cannot prepend \"http\" or \"https\" to the `host` option. By default it'll use http connections. If you want to use https connection, please set `use_https = true` option.");
        expected.add("`path_style_request` asks Seafile to use URLs like `https://192.168.1.123:8080/bucketname/object` to access objects. In Amazon S3, the default URL format is in virtual host style, such as `https://bucketname.s3.amazonaws.com/object`. But this style relies on advanced DNS server setup. So most S3-compatible storage systems only implement the path style format.");
        expected.add("Run and Test");
        expected.add("Now you can start Seafile by `./seafile.sh start` and `./seahub.sh start` and visit the website.");
        assertEquals(entries, expected);
    }

    @Test
    void testTranslate_case1() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/case1.md");
    }

    @Test
    void testProcess_table() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/table.md");
        List<String> expected = new ArrayList<>();
        expected.add("Tables");
        expected.add("First Header");
        expected.add("Second Header");
        expected.add("Content Cell");
        expected.add("Content Cell");
        expected.add("Content Cell");
        expected.add("Content Cell");
        expected.add("Command");
        expected.add("Description");
        expected.add("git status");
        expected.add("List all new or modified files");
        expected.add("git diff");
        expected.add("Show file differences that haven't been staged");
        expected.add("Inline formatting");
        expected.add("Command");
        expected.add("Description");
        expected.add("`git status`");
        expected.add("List all *new or modified* files");
        expected.add("`git diff`");
        expected.add("Show file differences that **haven't been** staged");
        expected.add("Alignment");
        expected.add("Left-aligned");
        expected.add("Center-aligned");
        expected.add("Right-aligned");
        expected.add("git status");
        expected.add("git status");
        expected.add("git status");
        expected.add("git diff");
        expected.add("git diff");
        expected.add("git diff");
        assertEquals(entries, expected);
    }

    @Test
    void testTranslate_table() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/table.md");
    }
}
