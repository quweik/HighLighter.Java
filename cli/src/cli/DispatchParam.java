package cli;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;
/**
 * Author: LAs;
 * Data: 2015/3/15.
 * Usage: receive and dispatch the command line parameters
 */
/*
*  -h help require argument? no
*  -l line number  argument? no
*  -f format code  argument? no
*  -e enhancement argument? no
*  -o destination file name argument? yes
*  -s source file name argument? yes
*  -t specifies language type argument? yes
*  -c color theme argument? yes
*  -d print debug message? no
**/

public class DispatchParam {
    private String args[];
    private boolean bLineNumber = false;
    private boolean bEnhance = false;
    private boolean bHelp = false;
    private String outputFile;
    private String sourceFile;
    private String language;
    private String colorTheme;
    private Options options;

    public boolean isEnhance() {
        return bEnhance;
    }

    public String getColorTheme() {
        return colorTheme;
    }

    public boolean isbLineNumber() {
        return bLineNumber;
    }

    public String getLanguage() {
        return language;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public boolean isbHelp() {
        return bHelp;
    }

    public DispatchParam(String args[]) {
        this.args = args;
        try {
            options = new Options();
            options.addOption("h", false, "help");
            options.addOption("l", false, "line number");
            options.addOption("e", false, "enhancement");
            options.addOption("d", false, "debug message");
            options.addOption("o", true, "output filename");
            options.addOption("s", true, "source filename");
            options.addOption("t", true, "language type");
            options.addOption("c", true, "color theme");
        }
        catch (Exception e) {
            System.err.print("[ERROR] Exception when add options.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void dispatch() {
        try{
            CommandLineParser parser = new GnuParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                bHelp = true;
                return;
            }
            /* -s and -o are required  */
            if (!cmd.hasOption("o") || !cmd.hasOption("s")) {
                System.err.println("[ERROR] Require parameters both -s and -o, use -h to show help.");
                throw new Exception();
            }else {
                outputFile = cmd.getOptionValue("o");
                sourceFile = cmd.getOptionValue("s");
            }

            if (cmd.hasOption("d")) { Utils.debug = true; }
            if (cmd.hasOption("l")) { bLineNumber = true; }
            if (cmd.hasOption("e")) { bEnhance = true; }
            if (cmd.hasOption("t")){ language = cmd.getOptionValue("t"); }
            if (cmd.hasOption("c")) { colorTheme = cmd.getOptionValue("c"); }
        }
        catch (ParseException e1) {
            System.err.println("[ERROR] Parse parameters fault.");
            if (Utils.debug) e1.printStackTrace();
            System.exit(-1);
        }
        catch (Exception e) {
            System.err.println("[ERROR] Unknown exception.");
            if (Utils.debug) e.printStackTrace();
            System.exit(-1);
        }
    }
}