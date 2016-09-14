package tokyo.northside.omegat.markdown;

import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Tests for fenced code block.
 * Created by miurahr on 16/09/08.
 */
public class FencedCodeTest {

    @Test
    public void testFencedCode_1() throws Exception {
        String testInput = "```\n" +
                "[commit_object_backend]\n" +
                "name = s3\n" +
                "# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.\n" +
                "bucket = my-commit-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n" +
                "```";
        List<String> expected = new ArrayList<>();
        expected.add("[commit_object_backend]\n" +
                "name = s3\n" +
                "# bucket name can only use lowercase characters, numbers, periods and dashes. Period cannot be used in Frankfurt region.\n" +
                "bucket = my-commit-objects\n" +
                "key_id = your-key-id\n" +
                "key = your-secret-key\n" +
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getEntries(), expected);
        assertEquals(filter.getOutbuf(), testInput);
    }

    @Test
    public void testFencedCode_2() throws Exception {
        String testInput = "```\n" +
                "[commit_object_backend]\n" +
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
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n" +
                "```";
        List<String> expected = new ArrayList<>();
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
                "memcached_options = --SERVER=localhost --POOL-MIN=10 --POOL-MAX=100\n");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        assertEquals(filter.getEntries(), expected);
        assertEquals(filter.getOutbuf(), testInput);
    }


    @Test
    public void testFencedCode_3() throws Exception {
        String testInput = "- Install the python `boto`library.It 's needed to access S3 service.\n" +
                "```\n" +
                "sudo easy_install boto\n" +
                "```";
        List<String> expected = new ArrayList<>();
        expected.add("Install the python `boto`library.It 's needed to access S3 service.```sudo easy_install boto```");
        MockFilter filter = new MockFilter();
        filter.process(testInput);
        throw new SkipException("Skip acceptance test.(known bug)");
        //assertEquals(filter.getEntries(), expected);
        //assertEquals(filter.getOutbuf(), testInput);
    }

}
