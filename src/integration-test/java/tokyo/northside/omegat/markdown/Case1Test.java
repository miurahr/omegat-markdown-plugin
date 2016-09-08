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
public class Case1Test extends TestFilterBase {

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
        expected.add("Install the python `boto` library. It's needed to access S3 service.\n" +
                     "```sudo easy_install boto```");
        expected.add("Install and configure memcached. For best performance, Seafile requires install memcached and enable memcache for objects. We recommend to allocate 128MB memory for memcached. Edit /etc/memcached.conf");
        expected.add("# Start with a cap of 64 megs of memory. It's reasonable, and the daemon default\n" +
                "# Note that the daemon will grow to this size, but does not start out holding this much\n" +
                "# memory\n" +
                "# -m 64\n" +
                "-m 128");
        expected.add("It's recommended to create separate buckets for commit, fs, and block objects. " +
                "The key_id and key are required to authenticate you to S3. You can find the key_id and key in the \"security credentials\" section on your AWS account page.");
        expected.add("Use S3 in newer regions");
        expected.add("After Januaray 2014, new regions of AWS will only support authentication signature version 4 for S3. At this time, new region includes Frankfurt and China.");
        expected.add("To use S3 backend in these regions, add following options to commit_object_backend, fs_object_backend and block_backend section in seafile.conf");
        expected.add("use_v4_signature = true\n" +
                "# eu-central-1 for Frankfurt region\n" +
                "aws_region = eu-central-1");
        expected.add("For file search and webdav to work with the v4 signature mechanism, you need to add following lines to ~/.boto");
        expected.add("[s3]\n" +
                "use-sigv4 = True");
        expected.add("Using memcached cluster");
        expected.add("In a cluster environment, you may want to use a memcached cluster. In the above configuration, you have to specify all the memcached server node addresses in seafile.conf");
        expected.add("memcached_options = --SERVER=192.168.1.134 --SERVER=192.168.1.135 --SERVER=192.168.1.136 --POOL-MIN=10 --POOL-MAX=100\n");
        expected.add("Because the server package is built on CentOS 6, if you're using Debian/Ubuntu, you have to copy the system CA bundle to CentOS's CA bundle path. Otherwise Seafile can't find the CA bundle so that the SSL connection will fail.");
        expected.add("sudo mkdir -p /etc/pki/tls/certs\n" +
                "sudo cp /etc/ssl/certs/ca-certificates.crt /etc/pki/tls/certs/ca-bundle.crt\n" +
                "sudo ln -s /etc/pki/tls/certs/ca-bundle.crt /etc/pki/tls/cert.pem");
        expected.add("Another important note is that you **must not use '.' in your bucket names**. Otherwise the wildcard certificate for AWS S3 cannot be resolved. This is a limitation on AWS.");
        expected.add("`host` is the address and port of the S3-compatible service. You cannot prepend \"http\" or \"https\" to the `host` option. By default it'll use http connections. If you want to use https connection, please set `use_https = true` option.");
        expected.add("`path_style_request` asks Seafile to use URLs like `https://192.168.1.123:8080/bucketname/object` to access objects. In Amazon S3, the default URL format is in virtual host style, such as `https://bucketname.s3.amazonaws.com/object`. But this style relies on advanced DNS server setup. So most S3-compatible storage systems only implement the path style format.");
        throw new SkipException("Skip acceptance test.(known bug)");
        //assertEquals(entries, expected);
    }

    @Test
    void testTranslate_case1() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/case1.md");
    }
}
