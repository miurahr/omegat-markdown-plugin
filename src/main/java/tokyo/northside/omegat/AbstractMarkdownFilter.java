package tokyo.northside.omegat;

/**
 * Created by miurahr on 16/08/26.
 */
abstract class AbstractMarkdownFilter {
    protected StringBuilder outbuf;
    protected char[] articleBuf;
    protected boolean tableMode = false;

    abstract void writeTranslate(final String text, final boolean trans);
    abstract void writeTranslate(final String text, final int start, final int end);
    abstract void flushToEof();

    /**
     * Reset buffer for Test.
     */
    void resetOutbuf() {
        outbuf = new StringBuilder();
    }

    /**
     * Get buffer contents for Test.
     */
    String getOutbuf() {
        return outbuf.toString();
    }


    void startTable() {
        tableMode = true;
    }

    void endTable() {
        tableMode = false;
    }

}
