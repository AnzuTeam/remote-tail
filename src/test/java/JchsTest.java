import com.jcraft.jsch.*;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nanashi07 on 15/12/30.
 */
public class JchsTest {

    final String account = "eservice";
    final String password = "eservice";
    final String host = "10.64.33.95";
    final int port = 22;

    @Test
    public void testConnection() throws JSchException, InterruptedException, IOException {
        JSch.setConfig("StrictHostKeyChecking", "no");

        JSch jsch = new JSch();
        Session session = jsch.getSession(account, host, port);
        session.setPassword(password);
//        session.setOutputStream(System.out);
        session.connect();

        Channel channel = session.openChannel("exec");
        channel.setInputStream(null);

        ChannelExec exec = (ChannelExec) channel;
        exec.setCommand("tail -f -n1000 ESFRONT/logs/FRServer1_console.log");

        InputStream in = channel.getInputStream();

        channel.connect();

        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
        channel.disconnect();
        session.disconnect();

    }

}
