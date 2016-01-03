package addlog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Go {

    static String[] test = "I hope i'm not breaking some ettiquete but this answer is now the best answer (separate module which directly returns a compiled & loaded class without producing a .class file on disk). It's also almost the same with the solution i linked to: javablogging.com/dynamic-in-memory-compilation and the solution i wrote myself based on that link. – Teo Mar 8 '15 at 6:56 @erolagnab, thank you very much! This is very useful solution for me! Excellent! – DmitryTsechoev Mar 19 '15 at 20:03 @Teo That exactly what my answer did. It only writes to disk if you are debug mode so you can step into the code when debugging. By default it works entirely in memory. BTW It has been in production for years with many users so it is perhaps the best tested solution. – Peter Lawrey Jun 1 '15 at 20:29 @Peter yes but in my opinion it's more obvious in this answer with InMemoryJavaCompiler.compile(...) unlike the small logic that you are using (static cached compiler vs constructed compiler) which although not important still adds to the size of the answer. But i see you edited your answer anyway :) I'd like to investigate & actually run both codes so that simplicity isn't my only argument but right now it is :S I'll come back with a comment when i'm done. – Teo Jun 2 '15 at 16:43 @Peter: tried to run your answer by pasting your source code in my project and found you have some extra dependencies there (for loggers, annotations, etc.). This made me decide i don't want to test it anymore, i don't want to have to mavenize or add some dependencies to my project that aren't essential to my question. The current answer (which worked just by pasting) doesn't have such 'clutter' & was obvious enough at the time it was posted to convince me to accept it. I realize maybe this is a bit unfair to your answer (which i upvoted btw) since you obviously know what you're talking about – Teo Jun 2 '15 at 19:14".split("\\s");
    static boolean stop = false;

    public static void main(String[] args) {
        final String name = args.length > 0 && args[0] != null ? args[0] : "mylog.log";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    writeFile(name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.printf("Enter for exit:");
            String line = scanner.nextLine();
            if ("exit".equals(line)) {
                stop = true;
                return;
            }
        } while (true);

    }

    static void writeFile(String name) throws IOException, InterruptedException {
        BufferedWriter bw = Files.newBufferedWriter(Paths.get(name), Charset.defaultCharset(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);

        while (!stop) {
            long wait = new SecureRandom().nextInt(3000);
            Thread.sleep(wait);
            bw.write(String.format("%s%s%n", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), random()));
            bw.flush();
        }

        bw.close();
    }

    static String random() {
        int max = test.length;
        int size = new SecureRandom().nextInt(200);
        if (size == 0) size = 1;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(" ").append(test[new SecureRandom().nextInt(max)]);
        }
        return sb.toString();
    }


}
