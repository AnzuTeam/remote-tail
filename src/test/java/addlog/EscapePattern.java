package addlog;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by nanashi07 on 16/1/8.
 */
public class EscapePattern {

    @Test
    public void test() {
        String pattern;

        pattern = "1111[abc]+2222";
        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
        System.out.println(Arrays.toString("1111111bbb2222222".split(pattern)));

        pattern = "1111[ab\\[c]+2222";
        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
        System.out.println(Arrays.toString("1111111bbb2222222".split(pattern)));

        pattern = "1111[ab]c]+2222";
        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
        System.out.println(Arrays.toString("1111111bbb2222222".split(pattern)));

        pattern = "1111[ab\\[]c]+2222";
        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
        System.out.println(Arrays.toString("1111111bc]]]]2222222".split(pattern)));

        pattern = "1111[ab][c]+2222";
        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
        System.out.println(Arrays.toString("1111111bbb2222222".split(pattern)));

//        pattern = "";
//        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
//
//        pattern = "";
//        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
//
//        pattern = "";
//        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
//
//        pattern = "";
//        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
//
//        pattern = "";
//        System.out.printf("%s => %s%n", pattern, escapePattern(pattern));
    }

    CharSequence escapePattern(String pattern) {
        if (pattern == null) return null;

        // [:alnum:]	代表英文大小寫字元及數字，亦即 0-9, A-Z, a-z
        // [:alpha:]	代表任何英文大小寫字元，亦即 A-Z, a-z
        // [:blank:]	代表空白鍵與 [Tab] 按鍵兩者
        // [:cntrl:]	代表鍵盤上面的控制按鍵，亦即包括 CR, LF, Tab, Del.. 等等
        // [:digit:]	代表數字而已，亦即 0-9
        // [:graph:]	除了空白字元 (空白鍵與 [Tab] 按鍵) 外的其他所有按鍵
        // [:lower:]	代表小寫字元，亦即 a-z
        // [:print:]	代表任何可以被列印出來的字元
        // [:punct:]	代表標點符號 (punctuation symbol)，亦即：" ' ? ! ; : # $...
        // [:upper:]	代表大寫字元，亦即 A-Z
        // [:space:]	任何會產生空白的字元，包括空白鍵, [Tab], CR 等等
        // [:xdigit:]	代表 16 進位的數字類型，因此包括： 0-9, A-F, a-f 的數字與字元

        /** 轉換規則 **/
        // 1. { ==> \{
        // 2. } ==> \}
        // 3. " ==> \"
        // 4. [x]+ ==> [x]\+
        // 5. \d ==> [0-9]

        StringBuilder sb = new StringBuilder();
        char[] chars = pattern.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            // rule 1.
            if (c == '{') {
                sb.append('\\');
            }
            // rule 2.
            if (c == '}') {
                sb.append('\\');
            }
            // rule 3.
            if (c == '"') {
                sb.append('\\');
            }
            // rule 4.
            if (c == '+') {
                sb.append('\\');
            }
            // rule 5.
            if (c == 'd' && i > 0 && chars[i - 1] == '\\') {
                sb.deleteCharAt(sb.length() - 1).append("[0-9]");
                // 不加入原本的 'd'
                continue;
            }

            sb.append(c);
        }
        return sb;
    }


}
