package com.prhythm.app.remotetail.data;

import com.jcraft.jsch.ChannelExec;
import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Once;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.logging.Logs;
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Delimiters;
import com.prhythm.core.generic.util.Streams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 搜尋清單
 * Created by nanashi07 on 16/1/3.
 */
public class FilteredLogReaderList extends RemoteSourceReaderList {

    /**
     * 搜尋字串
     */
    String pattern;

    /**
     * 符合搜尋的結果行
     */
    final Once<List<Integer>> linesMatched = new Once<List<Integer>>() {
        @Override
        protected List<Integer> get() throws Exception {
            synchronized (server) {
                if (!server.isConnected()) server.connect();
            }
            ChannelExec exec = server.openChannel("exec");
            // 指令 wc : 計算檔案行數
            String cmd = String.format("grep -n %s %s | cut -d : -f1", Delimiters.with("\\\\").join(Delimiters.with("\\").split(pattern)), path);
            Logs.trace("取得符合行行號(%s)", cmd);
            exec.setCommand(cmd);
            InputStream in = exec.getInputStream();
            exec.connect();

            List<Integer> result = new ArrayList<>();
            List<Integer> values = Cube.from(Streams.toLines(in, Singleton.of(DataWrapper.class).getPreference().getCharset()))
                    .notNull()
                    .select((item, index) -> Integer.parseInt(item.trim()))
                    .toList();
            result.addAll(values);

            exec.disconnect();

            if (result.size() == 0) {
                App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.empty.search.result"), pattern);
            }

            return result;
        }
    };

    public FilteredLogReaderList(Server server, LogPath logPath, String pattern) {
        super(server, logPath);
        this.pattern = pattern;

        // 起始讀取作業
        new Thread(this).start();
    }

    @Override
    public int size() {
        return linesMatched.value().size();
    }

    @Override
    public Line get(int i) {
        // 取得實際資料行號
        int index = linesMatched.value().get(i);

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
        return linesMatched.value().indexOf(line.getIndex());
    }

}
