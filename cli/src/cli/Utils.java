package cli;

import java.io.File;
import java.io.FileInputStream;

/**
 * Author: LAs;
 * Data: 2015/4/3.
 * Usage: Store utils function
 */

public class Utils {
    /*调试用, 以及对付java的啰嗦*/
    public static boolean debug = false;
    public static void dbg(String m){
        if (debug){
            System.out.println("[DBGMSG] " + m);
        }
    }
    public static void err(String m) {
        System.err.println("[ERROR] " + m);
    }

    public static void hint(String m){
        System.out.println("[HINT] " + m);
    }
    /* 以UTF-8编码从文本文件中读出字串 */
    public static String getTextFileContent(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        byte[] fileContent = new byte[(int) file.length()];
        int actualLen = fis.read(fileContent);

        Utils.dbg("Read content form: " + file.getName()
                + "\n\t except size: " + file.length()
                + "\n\t actual size: " + actualLen);

        fis.close();
        if (actualLen != file.length()){
            Utils.err("Except file length is " + file.length() + " but actual file length is " + actualLen);
            throw new Exception();
        }
        return new String(fileContent,"UTF-8");
    }

    /* 不知道放哪里好的帮助 */
    public static void showHelp(){
        System.out.println("Usage: highlight [OPTION]...[FILE]...");
        System.out.println("highlight your code with HTML tags.");
        System.out.println("\t-h display the help and exit");
        System.out.println("\t-l add the line number in the code");
        System.out.println("\t-e enable enhancement");
        System.out.println("\t-d print debug message");
        System.out.println("\t-o [FILENAME] specifies output file");
        System.out.println("\t-s [FILENAME] specifies source file");
        System.out.println("\t-t [LANGUAGE] specifies language");
        System.out.println("\t-c [THEME] specifies color theme");
        System.out.println("Example:");
        System.out.println("\thighlight -s java.java -o java.html");
        System.out.println("Project Location: <https://github.com/LastAvenger/Java-Highlighter>");
        System.out.print("Develop by LastAvengers, quweik and IMonday");
    }
}