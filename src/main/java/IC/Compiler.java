/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  java_cup.runtime.Scanner
 *  java_cup.runtime.Symbol
 */
package IC;

import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.Lexer;
import IC.Parser.LibParser;
import IC.Parser.parser;
import IC.SemanticChecks.SymTableCreator;
import IC.SemanticChecks.SymTables.ProgramTable;
import IC.SemanticChecks.TypingRulesVisitor;
import IC.TypeTable.TypeTable;
import IC.lir.LirVisitor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

public class Compiler {
    public static void errorExit(String msg, int line) {
        System.err.println("semantic error at line " + line + ": " + msg);
        System.exit(0);
    }

    public static void main(String[] args) {
        FileReader txtFile;
        if (args.length > 5 || args.length < 1) {
            System.err.println("Error: Invalid number of arguments.");
            return;
        }
        String libPath = "";
        boolean printSymtab = false;
        boolean printAst = false;
        boolean printLir = false;
        int i = 1;
        while (i < args.length) {
            if (args[i].equals("-dump-symtab")) {
                printSymtab = true;
            } else if (args[i].equals("-print-ast")) {
                printAst = true;
            } else if (args[i].substring(0, 2).equals("-L")) {
                libPath = args[i].replaceFirst("-L", "");
            } else if (args[i].equals("-print-lir")) {
                printLir = true;
            } else {
                System.err.println("Error: \"" + args[i] + "\" is an invalid argument.");
                return;
            }
            ++i;
        }
        Program libProg = null;
        if (!libPath.equals("")) {
            FileReader libTxtFile;
            try {
                libTxtFile = new FileReader(libPath);
            }
            catch (FileNotFoundException e) {
                System.err.println("Error: \"" + libPath + "\" library file not found.");
                return;
            }
            Lexer libscanner = new Lexer(libTxtFile);
            LibParser libp = new LibParser(libscanner);
            try {
                libProg = (Program)libp.parse().value;
            }
            catch (Exception e) {
                System.exit(0);
            }
            System.out.println("Parsed " + libPath + " successfully!");
        }
        try {
            txtFile = new FileReader(args[0]);
        }
        catch (FileNotFoundException e) {
            System.err.println("Error: \"" + args[0] + "\" file not found.");
            return;
        }
        Lexer scanner = new Lexer(txtFile);
        parser p = new parser(scanner);
        Program prog = null;
        try {
            prog = (Program)p.parse().value;
        }
        catch (Exception e) {
            System.exit(0);
        }
        System.out.println("Parsed " + args[0] + " successfully!");
        ProgramTable progTable = null;
        if (!libPath.equals("")) {
            ProgramTable unifiedTable;
            SymTableCreator unified = new SymTableCreator(args[0]);
            progTable = unifiedTable = unified.createSymTables(libProg, prog);
        } else {
            SymTableCreator Ana = new SymTableCreator(args[0]);
            progTable = (ProgramTable)Ana.visit(prog);
        }
        TypingRulesVisitor checker = new TypingRulesVisitor();
        checker.visit(prog);
        if (printAst) {
            PrettyPrinter prt = new PrettyPrinter(args[0]);
            System.out.print(prt.visit(prog));
        }
        if (printSymtab) {
            System.out.println();
            progTable.print_table();
            TypeTable.print_TypeTable(args[0]);
        }
        LirVisitor lirVisit = new LirVisitor(progTable);
        lirVisit.visit(prog);
        if (printLir) {
            String lirPath = String.valueOf(args[0].substring(0, args[0].indexOf(".") + 1)) + "lir";
            FileWriter fw = null;
            try {
                fw = new FileWriter(new File(lirPath));
                lirVisit.printLirCode(fw);
                fw.close();
            }
            catch (IOException e) {
                System.out.println("Error writing to .lir file");
            }
        }
    }
}

