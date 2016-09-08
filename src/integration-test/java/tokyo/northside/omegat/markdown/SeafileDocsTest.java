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

import org.testng.SkipException;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Integrated test with live files.
 * Created by miurahr on 16/09/08.
 */
public class SeafileDocsTest extends TestFilterBase {

    @Test
    void testProcess_fencedcode() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/fencedcode.md");
        List<String> expected = new ArrayList<>();
        expected.add("Fenced code test");
        expected.add("Samples from seafile project docs.");
        expected.add("Modify Seafile.conf");
        expected.add("Edit `seafile.conf`, add the following lines:");
        expected.add("[commit_object_backend]\n" +
                "name = s3\n" +
                "# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.\n" +
                "bucket = my-commit-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n\n" +
                "[fs_object_backend]\n" +
                "name = s3\n" +
                "# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.\n" +
                "bucket = my-fs-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n\n" +
                "[block_backend]\n" +
                "name = s3\n" +
                "# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.\n" +
                "bucket = my-block-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n");
        expected.add("Use HTTPS connections to S3");
        expected.add("Since Pro 5.0.4, you can use HTTPS connections to S3. Add the following options to seafile.conf:");
        expected.add("[commit_object_backend]\n" +
                "name = s3\n" +
                "......\n" +
                "use_https = true\n" +
                "\n" +
                "[fs_object_backend]\n" +
                "name = s3\n" +
                "......\n" +
                "use_https = true\n" +
                "\n" +
                "[block_backend]\n" +
                "name = s3\n" +
                "......\n" +
                "use_https = true\n");
        expected.add("Use S3-compatible Object Storage");
        expected.add("Many object storage systems are now compatible with the S3 API, such as OpenStack Swift and " +
                "Ceph's RADOS Gateway. You can use these S3-compatible storage systems as backend for Seafile." +
                " Here is an example config:");
        expected.add("[commit_object_backend]\n" +
                "name = s3\n" +
                "bucket = my-commit-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "host = 192.168.1.123:8080\n" +
                "path_style_request = true\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n" +
                "\n" +
                "[fs_object_backend]\n" +
                "name = s3\n" +
                "bucket = my-fs-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "host = 192.168.1.123:8080\n" +
                "path_style_request = true\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n" +
                "\n" +
                "[block_backend]\n" +
                "name = s3\n" +
                "bucket = my-block-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "host = 192.168.1.123:8080\n" +
                "path_style_request = true\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n");
        assertEquals(entries, expected);
    }

    @Test
    void testTranslate_fencedcode() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/fencedcode.md");
    }

}
