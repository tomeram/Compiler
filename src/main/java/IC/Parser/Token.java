/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  java_cup.runtime.Symbol
 */
package IC.Parser;

import java_cup.runtime.Symbol;

public class Token
extends Symbol {
    public String tagString;
    public int tag;
    public int column;
    public int line;

    public int getTag() {
        return this.tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getTagString() {
        return this.tagString;
    }

    public void setTagString(String tag) {
        this.tagString = tag;
    }

    public int getColumn() {
        return this.column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Token(int sym2, int line, int column, String tagString, String value) {
        super(sym2, (Object)value);
        this.line = line + 1;
        this.column = column + 1;
        this.tag = sym2;
        this.tagString = tagString;
        this.left = line + 1;
        this.right = column + 1;
    }
}

