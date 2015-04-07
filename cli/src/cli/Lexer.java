package cli;

import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: IMonday;
 * Data: 2015/3/15.
 * Usage: Add HTML tags to given string
 * Detail: 这个模块获取
 */

public class Lexer {
    private String srcStr;
    private Vector<Element> elementVector;
    private String outStr = "";
    private int i = 0;

    public Lexer(String srcStr, Vector<Element> elementVector) {
        this.srcStr = srcStr;
        this.elementVector = elementVector;
    }

    public String htmlen() {
        srcStr = srcStr.replaceAll("<", "&lt");
        srcStr = srcStr.replaceAll(">", "&gt");
        i = 0;
        int mi = -1, en=0;
        for (Element e : elementVector) {
            Pattern pattern = Pattern.compile(e.pattern);
            Matcher matcher = pattern.matcher(srcStr);
            if (matcher.find()) {
                e.start = matcher.start();
                e.end = matcher.end();
                if (mi == -1 || mi > e.start) {
                    mi = e.start;
                    en = e.end;
                }
            } else {
                e.start = -1;
                e.end = -1;
            }
        }
        int pre = 0;
        while (true) {
            if (mi == -1) break;
            if (pre < mi)//无需高亮关键字加入到目标串(pre,mi-1)
            {
                outStr += srcStr.substring(pre, mi);
            }
            pre = en;
            for (Element e : elementVector) {
                if (e.start != mi) continue;
                Pattern pattern = Pattern.compile(e.pattern);
                Matcher matcher = pattern.matcher(srcStr);
                boolean kk = matcher.find(e.start);
                Scanner scanner = new Scanner(matcher.group(0));
                while (scanner.hasNextLine()) {
               /* 内置变量和自定义变量有不同的标记方法 */
                    if (Configure.isReserveType(e.type)) {
                        outStr += "<span class=" + e.type + ">"
                                + scanner.nextLine() + "</span>";
                    } else {
                        outStr += "<font color=" + e.type + ">"
                                + scanner.nextLine() + "</font>";
                    }
                    if (scanner.hasNextLine()) outStr += "\n";
                }
                break;
            }//添加高亮关键字部分到目标串中(mi,en-1)
            mi = -1;
            for (Element e : elementVector) {
                if (e.start == -1) continue;
                if (e.start < pre) {
                    Pattern pattern = Pattern.compile(e.pattern);
                    Matcher matcher = pattern.matcher(srcStr);
                    if (matcher.find(pre)) {
                        e.start = matcher.start();
                        e.end = matcher.end();
                    } else {
                        e.start = -1;
                        e.end = -1;
                    }
                }
                if (e.start == -1) continue;
                if (mi == -1 || mi > e.start) {
                    mi = e.start;
                    en = e.end;
                }
            }
        }
        if (pre != srcStr.length()) outStr += srcStr.substring(pre);
/*        while (srcStr.length() > i){

            boolean bMacth = false;
            for (Element e:elementVector) {
                if (matching(e)) {
                    bMacth = true;
                    break;
                }
            }
            if (bMacth) continue;
            if (!matchingFail()) {
                Utils.err("All purpose matching fail, program exit.");
                System.exit(-2);
            }
        }*/
        Utils.dbg("src:" + srcStr);
        return outStr;
    }
}
/*

    private boolean matching(Element elem){
        Pattern pattern = Pattern.compile(elem.pattern);
        Matcher matcher = pattern.matcher(srcStr);
        if (matcher.find(i)){
            System.out.println("pattern:" + elem.pattern + " i:"+i+"start:"+matcher.start() + "end "+matcher.end());
            if (matcher.start() != i) return false;
            Scanner scanner = new Scanner(matcher.group(0));
            while (scanner.hasNextLine()){
               *//*
*/
/* 内置变量和自定义变量有不同的标记方法 *//*
*/
/*
                if (Configure.isReserveType(elem.type)){
                    outStr += "<span class=" + elem.type + ">"
                            + scanner.nextLine() + "</span>";
                }
                else {
                    outStr += "<font color=" + elem.type + ">"
                            + scanner.nextLine() + "</font>";
                }
                if (scanner.hasNextLine()) outStr += "\n";
            }
            //srcStr = srcStr.substring(matcher.end(), srcStr.length());

            i = matcher.end();
            System.out.println(elem.type + " " + i);
            Utils.dbg("Capture:" + matcher.group(0) + "|\t type:" + elem.type);
            return  true;
        }
        return false;
    }

    private boolean matchingFail() { // 自动简化赛高
        return allPurposePattern("\\s") || allPurposePattern("\\b\\w+\\b") || (allPurposePattern("."));
    }
    private boolean allPurposePattern(String app){
        Pattern pattern = Pattern.compile(app);
        Matcher matcher = pattern.matcher(srcStr);
        if (matcher.find(i)){
            System.out.println("pattern:" + app + " i:"+i+"start:"+matcher.start() + "end "+matcher.end());
            if (matcher.start() != i) return false;
            outStr +=  matcher.group(0);
            //Utils.dbg("APP Capture:" + matcher.group(0) + "|\ttype:" + app);
            srcStr = srcStr.substring(matcher.end(), srcStr.length());
            i = matcher.end();
            return true;
        }
        //Utils.dbg("All purpose pattern: " + app + "matching fail.");
        return false;
    }
}
*/

/*package cli;

import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

*//**
 * Author: IMonday;
 * Data: 2015/3/15.
 * Usage: Add HTML tags to given string
 * Detail: 这个模块获取
 *//*

public class Lexer {
    private String srcStr;
    private Vector<Element> elementVector;
    private String outStr = "";

    public Lexer(String srcStr, Vector<Element> elementVector){
        this.srcStr = srcStr;
        this.elementVector = elementVector;
    }

    public String htmlen(){
        srcStr = srcStr.replaceAll("<","&lt");
        srcStr = srcStr.replaceAll(">","&gt");
        while (!srcStr.equals("")){

            boolean bMacth = false;
            for (Element e:elementVector) {
                if (matching(e)) {
                    bMacth = true;
                    break;
                }
            }
            if (bMacth) continue;
            if (!matchingFail()) {
                Utils.err("All purpose matching fail, program exit.");
                System.exit(-2);
            }
        }
        Utils.dbg("src:" + srcStr);
        return outStr;
    }

    private boolean matching(Element elem){
        Pattern pattern = Pattern.compile('^' + elem.pattern);
        Matcher matcher = pattern.matcher(srcStr);
        if (matcher.find()){
            Scanner scanner = new Scanner(matcher.group(0));
            while (scanner.hasNextLine()){
               *//* 内置变量和自定义变量有不同的标记方法 *//*
                if (Configure.isReserveType(elem.type)){
                    outStr += "<span class=" + elem.type + ">"
                            + scanner.nextLine() + "</span>";
                }
                else {
                    outStr += "<font color=" + elem.type + ">"
                            + scanner.nextLine() + "</font>";
                }
                if (scanner.hasNextLine()) outStr += "\n";
            }
            srcStr = srcStr.substring(matcher.end(), srcStr.length());

            Utils.dbg("Capture:" + matcher.group(0) + "|\t type:" + elem.type);
        }
        return matcher.find();
    }

    private boolean matchingFail() { // 自动简化赛高
        return allPurposePattern("^\\s") || allPurposePattern("^\\b\\w+\\b") || (allPurposePattern("^."));
    }
    private boolean allPurposePattern(String app){
        Pattern pattern = Pattern.compile(app);
        Matcher matcher = pattern.matcher(srcStr);
        if (matcher.find()){
            outStr +=  matcher.group(0);
            //Utils.dbg("APP Capture:" + matcher.group(0) + "|\ttype:" + app);
            srcStr = srcStr.substring(matcher.end(), srcStr.length());
            return true;
        }
        //Utils.dbg("All purpose pattern: " + app + "matching fail.");
        return false;
    }
}*/
