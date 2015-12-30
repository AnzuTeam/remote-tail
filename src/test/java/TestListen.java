import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by nanashi07 on 15/12/30.
 */
public class TestListen implements Runnable {
    final String account = "eservice";
    final String password = "eservice";
    final String host = "10.64.33.95";
    final int port = 22;

    public static void main(String[] args) throws IOException, JSchException {
        TestListen listen = new TestListen();
        listen.connect();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String type = scanner.nextLine();
            if ("exit".equalsIgnoreCase(type)) {
//                listen.stop = true;
                listen.session.disconnect();
                System.out.println("session disconnected");
                break;
            }
        }
        System.out.println("application closed");
    }

    Session session;
    boolean stop = false;
    boolean changed = false;

    @Override
    public void run() {
        try {
            tail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void connect() throws IOException, JSchException {
        JSch.setConfig("StrictHostKeyChecking", "no");

        JSch jsch = new JSch();
        session = jsch.getSession(account, host, port);
        session.setPassword(password);
//        session.setOutputStream(System.out);
        session.connect();
        System.out.println("session connected");
    }

    void tail() throws JSchException, IOException {
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
                changed = true;
//                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
//                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ee) {
            }
        }
        channel.disconnect();
    }

}
