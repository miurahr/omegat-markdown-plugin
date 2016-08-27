package tokyo.northside.omegat;

/**
 * Created by miurahr on 16/08/26.
 */
abstract class AbstractMarkdownFilter {
    protected StringBuilder outbuf;
    protected char[] articleBuf;

    String getChars(final int start, final int end) {
        char[] buf = new char[end - start];
        System.arraycopy(articleBuf, start, buf, 0, end - start);
        return String.valueOf(buf);
    }

    abstract void writeTranslate(final String text, final boolean trans);
    abstract void writeTranslate(final String text, final int start, final int end);

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
}
