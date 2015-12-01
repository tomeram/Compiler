/*
 * Decompiled with CFR 0_110.
 */
package IC.Parser;

import java.io.PrintStream;

public class LexicalError
extends Exception {
    public LexicalError(String message) {
        LexicalError.PrintTokenError(message);
    }

    public static void PrintTokenError(String errMsg) {
        System.err.println("Error!    " + errMsg);
    }
}

