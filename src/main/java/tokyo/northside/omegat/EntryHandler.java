package tokyo.northside.omegat;

/**
 * Created by miurahr on 16/08/30.
 */
public class EntryHandler {

    protected OmegatMarkdownFilter filter;

    public EntryHandler() {
    }

    public EntryHandler(OmegatMarkdownFilter filter) {
        this.filter = filter;
    }

    void writeTranslate(final String text, final int start, final int end) {
        filter.writeTranslate(text, start, end);
    }
}
