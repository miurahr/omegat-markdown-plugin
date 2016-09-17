package tokyo.northside.omegat.markdown;

import org.testng.annotations.Test;


/**
 * Integration test class to read from resources and check with json entries.
 * @author Hiroshi Miura
 */
public class MarkdownIntegrationTest extends TestFilterBase {

    @Test
    public void integrationTest() throws Exception {
        test("/source/test-cases/autolink");
        test("/source/test-cases/backslash_escape");
        test("/source/test-cases/case1");
        testTranslate("/source/test-cases/fencedcode");
        testTranslate("/source/test-cases/reference");
        testTranslate("/source/test-cases/table");
        testTranslate("/source/test-cases/seafile_cli");
    }

}
