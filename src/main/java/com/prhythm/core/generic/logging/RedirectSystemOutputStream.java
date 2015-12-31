package com.prhythm.core.generic.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 轉換系統輸出至 {@link ILogger}
 * Created by nanashi07 on 15/12/10.
 */

public class RedirectSystemOutputStream extends PrintStream {

    public RedirectSystemOutputStream(Level level) {
        super(new RedirectStream(level));
    }

}

class RedirectStream extends OutputStream {

    Level level;

    final ByteArrayOutputStream bufferedContent;

    public RedirectStream(Level level) {
        this.level = level;
        bufferedContent = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) throws IOException {
        switch (b) {
            case '\n':
                Logs.log(level, new String(bufferedContent.toByteArray()));
                bufferedContent.reset();
                break;
            default:
                synchronized (bufferedContent) {
                    bufferedContent.write(b);
                }
                break;
        }
    }

}