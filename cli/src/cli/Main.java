package cli;

import java.io.*;
import java.util.Scanner;

/**
 * Author: LAs;
 * Data: 2015/3/10.
 * Usage: Program entry
 */

public class Main {
    public static void main(String args[]){
        /* 计时开始 */
        long start = System.currentTimeMillis();
        /* 获取并解析命令行参数 */
        DispatchParam dispatchParam = new DispatchParam(args);
        dispatchParam.dispatch();

        if (dispatchParam.isbHelp()) {
            Utils.showHelp();
            return;
        }

        /* 读取指定的配置, 语言文件的配置文件在etc/language.lang 高亮方案的配置文件在etc/colorTheme.css */
        String language = dispatchParam.getLanguage();
        String colorTheme = dispatchParam.getColorTheme();
        /* 默认配置 */
        if (language == null){
            language = dispatchParam.getSourceFile().substring(
                    dispatchParam.getSourceFile().lastIndexOf('.') + 1,
                    dispatchParam.getSourceFile().length()
            );
        }
        if (colorTheme == null){
            colorTheme = "default";
        }

        Configure configure = new Configure(
                language,
                colorTheme,
                dispatchParam.isbLineNumber(),
                dispatchParam.isEnhance()
        );

        /* 储存整个目标文件 */
        String srcStr = "";

        /* read source file */
        try {
            File srcFile = new File(dispatchParam.getSourceFile());
            srcStr = Utils.getTextFileContent(srcFile);
        }
        catch (Exception e){
            Utils.err("Read: " + dispatchParam.getSourceFile() + " fault.");
            if (Utils.debug) e.printStackTrace();
            System.exit(-1);
        }

        /* 代码交由 Lexer 高亮, 预期返回带有html标签的文本 */
        Utils.hint("Processing...");

        Lexer lexer = new Lexer(srcStr, configure.getElementsVector());
        srcStr = lexer.htmlen();

        if (dispatchParam.isbLineNumber()){
            String tempStr = srcStr;
            Scanner scanner = new Scanner(tempStr);
            srcStr = "";
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                srcStr += "<span class=line>" + line + "</span>\n";
            }
        }

        /* create output file */
        try{
            File outFile = new File(dispatchParam.getOutputFile());
            boolean isFileCreated = outFile.createNewFile();
            assert isFileCreated;

            FileOutputStream fos = new FileOutputStream(outFile);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));

            bw.write("<!DOCTYPE html><html><head><meta charset=\"utf-8\"></head>");
            bw.write(configure.getStyle());
            bw.write("<body><pre><code>");
            bw.write(srcStr);
            bw.write("</code></pre></body></html>");
            bw.flush();
            bw.close();
        }
        catch (Exception e){
            Utils.err("Write: " + dispatchParam.getOutputFile() + " fault.");
            if (Utils.debug) e.printStackTrace();
            System.exit(-1);
        }
        long end = System.currentTimeMillis();
        Utils.hint("Time used: " + Long.toString(end - start) + "ms.");
        Utils.hint("Done.");
    }
}
