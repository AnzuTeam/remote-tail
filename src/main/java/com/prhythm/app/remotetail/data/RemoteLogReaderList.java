package com.prhythm.app.remotetail.data;

import com.jcraft.jsch.ChannelExec;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Expirable;
import com.prhythm.core.generic.logging.Logs;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Log 內容
 * Created by nanashi07 on 15/12/30.
 */
public class RemoteLogReaderList extends RemoteSourceReaderList {

    /**
     * 檔案行數
     */
    transient Expirable<Integer> lineCount = new Expirable<Integer>(INTERVAL) {
        @Override
        protected Integer get() throws Exception {
            synchronized (server) {
                if (!server.isConnected()) server.connect();
            }
            ChannelExec exec = server.openChannel("exec");
            exec.setErrStream(System.err);
            // 指令 wc : 計算檔案行數
            String cmd = String.format("wc -l %s", path);
            Logs.trace("計算行數(%s)", cmd);
            exec.setCommand(cmd);
            InputStream in = exec.getInputStream();
            exec.connect();
            Scanner scanner = new Scanner(in);
            int size = scanner.nextInt();
            Logs.trace("%s 行數: %d", path, size);
            in.close();
            exec.disconnect();

            return size;
        }
    };

    public RemoteLogReaderList(Server server, LogPath logPath) {
        super(server, logPath);

        // 起始讀取作業
        new Thread(this).start();
    }

    @Override
    public int size() {
        return lineCount.value();
    }

    @Override
    public Line get(int index) {
        // 序號由 1 開始
        index = index + 1;

        if (path.hasLine(index)) {
            return new Line(index, path.atLine(index));
        } else {
            linesToRead.add(index);
            Line line = new Line(index, null);
            // 加入觀察者，以更新內容
            addObserver(line);
            return line;
        }
    }

    @Override
    public int indexOf(Object o) {
        Line line = (Line) o;
        return line.getIndex() - 1;
    }

}
