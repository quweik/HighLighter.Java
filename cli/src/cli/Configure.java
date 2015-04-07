package cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.MatchResult;

/**
 * Author: LAs
 * Data: 2015/3/25.
 * Usage: Read configure form configure file
 */
/*
* Language Config file format *.lang
* line comment
* blk Comment left
* blk Comment right
* macro
* keywords
*
* Color Theme Config file format .css
*/
class Element {
    String pattern;
    String type;
    int start;
    int end;
    /* 为了在Runtime 新建实例, 要保留一个无参构造函数 */
    public Element(){}
    public Element(String type) {
        this.type = type;
    }
}

public class Configure {
    /* 为了在Runtime时枚举, 必须是全局变量 */
    private Element lineComment = new Element("lineComment");
    private Element blkComment = new Element("blkComment");
    private Element string = new Element("string");
    private Element macro = new Element("macro");
    private Element character = new Element("character");
    private Element keywords = new Element("keywords");
    private Element number = new Element("number");
    private Element function = new Element("function");

    private Vector<Element> elementsVector = new Vector<Element>();

    /* 储存样式 */
    private String style = "";

    public Configure(String language, String colorTheme, boolean isLineNumber, boolean isEnhance){
        /* read language pattern form *.lang file */
        /*================================================
        /* 对于每个lang文件, 你可以自由定义/覆盖变量, 但是我提供了如下的内置变量
        *  lineComment 行注释
        *  blkCommentL blkCommentR 块注释的左边 和 右边
        *  macro 宏 显然不是所有的语言都有宏, 比如java
        *  String 提供了默认实现, 以""为标志
        *  character 提供了默认实现 以''为标志, 但有些语言也用''代替"" , 比如Pascal
        *  keywords 语言的关键字
        *  (?)type 或许还需要一些内置类型?
        *  number 提供了默认实现 数字
        *  function 提供了默认实现, 匹配诸如 func() 的字串, 虽然不是所有的函数调用都需要括号, 比如致力于消灭括号的Haskell
        * **可能**会让你拥有覆盖以上变量的方法
        ================================================*/
        Utils.hint("Reading configure: " + language + "  " + colorTheme);

        try {
            File file = new File("etc/" + language + ".lang");
            if (!file.exists()){
                Utils.err("Invalid configure file: " + file.getName());
                throw new Exception();
            }

            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            Scanner scanner = new Scanner(br);

            /* 变量名=值 */
            String pattern = "(\\b\\w+\\b)=(\\S+)";
            String pattern2 = "\\bcolor\\b=(\\b\\w+\\b)";

            /* 从配置文件中匹配, 遍历所有elem对象, 赋值*/
            while (scanner.hasNext(pattern)){
                scanner.next(pattern);
                MatchResult mr = scanner.match();
                Utils.dbg("conf var name: " + mr.group(1) + "\tvalue: " + mr.group(2));

                Class<?> cls = Class.forName("cli.Element");
                Object ins;
                boolean isNew = false;
                try {
                    ins = this.getClass().getDeclaredField(mr.group(1)).get(this);
                }
                catch (NoSuchFieldException e){
                    isNew = true;
                    Utils.err("NB: Not error, this exception is used create a non-existent object.");
                    ins = cls.newInstance();
                    //if (Utils.debug) e.printStackTrace();
                }
                Field field = cls.getDeclaredField("pattern");
                field.setAccessible(true);
                field.set(ins, mr.group(2));

                if (scanner.hasNext(pattern2)) {
                    scanner.next(pattern2);
                    mr = scanner.match();
                    Utils.dbg("color: " + mr.group(1));
                    field = cls.getDeclaredField("type");
                    field.setAccessible(true);
                    field.set(ins, mr.group(1));
                }
                /* 这个不是内置变量, 不需要修改, 直接加入 */
                if (isNew) elementsVector.add((Element)ins);
            }

            /* 由配置文件决定的实现, 未指定则置空 */
            if (lineComment.pattern != null) lineComment.pattern += ".*";
            else lineComment.pattern = "//.*";

            if (blkComment.pattern != null) blkComment.pattern = blkComment.pattern.replaceFirst("\\|", "(.|\n|\r)*?");
            else blkComment.pattern = "/\\*(.|\n|\r)*?\\*/";

            if (macro.pattern != null) macro.pattern += ".*";
            else macro.pattern = "#.*";

            if(keywords.pattern != null) keywords.pattern = "\\b(" + keywords.pattern  + ")\\b";
            else keywords.pattern = "\\b233\\b";

            /* 默认实现 */
            number.pattern = "\\b\\d+\\b";
            function.pattern = "\\b\\w+\\b(?=\\()";
            string.pattern = "\".*?[^\\\\](\\\\\\\\)*\"";
            character.pattern = "'.?'";

            Class<?> obj = Class.forName("cli.Configure");
            Field[] fields = obj.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == Class.forName("cli.Element")) {
                    elementsVector.add((Element) field.get(this));
                }
            }

            for (Element i:elementsVector){
                Utils.dbg("regexp: " + i.pattern + "\ttype: " + i.type);
            }
        }
        catch (Exception e) {
            Utils.err("Read " + language + ".lang fault.");
            if (Utils.debug) e.printStackTrace();
            System.exit(-1);
        }

        /* read color message form *.css file */
        try {
            File file = new File("etc/" + colorTheme + ".css");

            if (!file.exists()){
                Utils.err("Invalid configure file: " + file.getName());
                throw new Exception();
            }
            style += Utils.getTextFileContent(file);
        }
        catch (Exception e) {
            Utils.err("Read " + colorTheme + ".css fault.");
            if (Utils.debug) e.printStackTrace();
            System.exit(-1);
        }

        if (isLineNumber) {
            try {
                File file = new File("etc/x/lineNumber.css");
                style += Utils.getTextFileContent(file);
            }
            catch (Exception e){
                Utils.err("Can not add line number.");
                if (Utils.debug) e.printStackTrace();
            }
        }
        if (isEnhance){
            try {
                File file = new File("etc/x/"+ colorTheme +"_x.css");
                if (!file.exists()) file = new File("etc/x/default_x.css");
                style += Utils.getTextFileContent(file);
            }
            catch (Exception e){
                Utils.err("Can not add enhancements.");
                if (Utils.debug) e.printStackTrace();
            }
        }
    }

    /* 枚举类 这么麻烦的? */
    public static boolean isReserveType(String type){
        String[] reserveType =  {"lineComment","blkComment","string","macro","character","keywords","number","function"};
        for (String str:reserveType){
            if (str.equals(type)) return true;
        }
        return false;
    }
    /* **************** getter ***************/
    public String getStyle() {
        return "<style>" + style + "</style>";
    }

    public Vector<Element> getElementsVector() {
        return elementsVector;
    }
}
