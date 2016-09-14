package tokyo.northside.omegat.markdown;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test senario for reference usage.
 * Created by miurahr on 16/09/08.
 */
public class ReferenceTest extends TestFilterBase {

    @Test
    void testProcess_reference() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        List<String> entries = parse(mdf, "/source/reference.md");
        List<String> expected = new ArrayList<>();
        expected.add("Reference test");
        expected.add("please first read [S3 bucket naming rules][1].\n" +
                "Note especially not to use **UPPERCASE** letters\n" +
                "in bucket names (don't use camel style names, such as MyCommitOjbects).");
        expected.add("Run and Test");
        expected.add("Now you can start command by `./cmd.sh start`.");
        expected.add("http://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html \"the bucket naming rules\"");
        assertEquals(entries, expected);
    }

    @Test
    void testTranslate_reference() throws Exception {
        OmegatMarkdownFilter mdf = new OmegatMarkdownFilter();
        translateText(mdf, "/source/reference.md");
    }
}
