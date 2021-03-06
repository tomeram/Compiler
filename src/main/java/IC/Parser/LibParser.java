
//----------------------------------------------------
// The following code was generated by CUP v0.11b 20141202 (SVN rev 60)
//----------------------------------------------------

package IC.Parser;

import java.util.LinkedList;
import java.util.List;
import java_cup.runtime.Symbol;
import IC.AST.*;
import IC.DataTypes;
import java_cup.runtime.XMLElement;

/** CUP v0.11b 20141202 (SVN rev 60) generated parser.
 */
@SuppressWarnings({"rawtypes"})
public class LibParser extends java_cup.runtime.lr_parser {

    public final Class getSymbolContainer() {
        return sym.class;
    }

    /** Default constructor. */
    public LibParser() {super();}

    /** Constructor which sets the default scanner. */
    public LibParser(java_cup.runtime.Scanner s) {super(s);}

    /** Constructor which sets the default scanner. */
    public LibParser(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

    /** Production table. */
    protected static final short _production_table[][] =
            unpackFromStrings(new String[] {
                    "\000\020\000\002\002\003\000\002\002\004\000\002\003" +
                            "\007\000\002\004\004\000\002\004\002\000\002\005\011" +
                            "\000\002\005\011\000\002\006\003\000\002\006\003\000" +
                            "\002\006\003\000\002\006\003\000\002\006\005\000\002" +
                            "\007\003\000\002\007\005\000\002\007\002\000\002\010" +
                            "\004" });

    /** Access to production table. */
    public short[][] production_table() {return _production_table;}

    /** Parse-action table. */
    protected static final short[][] _action_table =
            unpackFromStrings(new String[] {
                    "\000\043\000\004\006\006\001\002\000\004\002\045\001" +
                            "\002\000\004\002\001\001\002\000\004\022\007\001\002" +
                            "\000\004\015\010\001\002\000\006\007\011\016\ufffd\001" +
                            "\002\000\014\010\021\011\016\020\023\021\022\022\017" +
                            "\001\002\000\006\007\011\016\ufffd\001\002\000\004\016" +
                            "\014\001\002\000\004\002\uffff\001\002\000\004\016\ufffe" +
                            "\001\002\000\006\013\ufff8\023\ufff8\001\002\000\006\013" +
                            "\ufff7\023\ufff7\001\002\000\006\013\036\023\040\001\002" +
                            "\000\004\023\024\001\002\000\006\013\ufff9\023\ufff9\001" +
                            "\002\000\006\013\ufffa\023\ufffa\001\002\000\004\004\025" +
                            "\001\002\000\016\005\ufff3\011\016\012\ufff3\020\023\021" +
                            "\022\022\017\001\002\000\006\013\036\023\035\001\002" +
                            "\000\006\005\ufff5\012\ufff5\001\002\000\006\005\032\012" +
                            "\031\001\002\000\012\011\016\020\023\021\022\022\017" +
                            "\001\002\000\004\017\033\001\002\000\006\007\ufffb\016" +
                            "\ufffb\001\002\000\006\005\ufff4\012\ufff4\001\002\000\006" +
                            "\005\ufff2\012\ufff2\001\002\000\004\014\037\001\002\000" +
                            "\006\013\ufff6\023\ufff6\001\002\000\004\004\041\001\002" +
                            "\000\016\005\ufff3\011\016\012\ufff3\020\023\021\022\022" +
                            "\017\001\002\000\006\005\043\012\031\001\002\000\004" +
                            "\017\044\001\002\000\006\007\ufffc\016\ufffc\001\002\000" +
                            "\004\002\000\001\002" });

    /** Access to parse-action table. */
    public short[][] action_table() {return _action_table;}

    /** <code>reduce_goto</code> table. */
    protected static final short[][] _reduce_table =
            unpackFromStrings(new String[] {
                    "\000\043\000\006\002\003\003\004\001\001\000\002\001" +
                            "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                            "\000\006\004\012\005\011\001\001\000\004\006\017\001" +
                            "\001\000\006\004\014\005\011\001\001\000\002\001\001" +
                            "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                            "\002\001\001\000\002\001\001\000\002\001\001\000\002" +
                            "\001\001\000\002\001\001\000\002\001\001\000\010\006" +
                            "\025\007\027\010\026\001\001\000\002\001\001\000\002" +
                            "\001\001\000\002\001\001\000\006\006\025\010\033\001" +
                            "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
                            "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                            "\002\001\001\000\010\006\025\007\041\010\026\001\001" +
                            "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
                            "\002\001\001" });

    /** Access to <code>reduce_goto</code> table. */
    public short[][] reduce_table() {return _reduce_table;}

    /** Instance of action encapsulation class. */
    protected CUP$LibParser$actions action_obj;

    /** Action encapsulation object initializer. */
    protected void init_actions()
    {
        action_obj = new CUP$LibParser$actions(this);
    }

    /** Invoke a user supplied parse action. */
    public java_cup.runtime.Symbol do_action(
            int                        act_num,
            java_cup.runtime.lr_parser parser,
            java.util.Stack            stack,
            int                        top)
            throws java.lang.Exception
    {
    /* call code in generated class */
        return action_obj.CUP$LibParser$do_action(act_num, parser, stack, top);
    }

    /** Indicates start state. */
    public int start_state() {return 0;}
    /** Indicates start production. */
    public int start_production() {return 1;}

    /** <code>EOF</code> Symbol index. */
    public int EOF_sym() {return 0;}

    /** <code>error</code> Symbol index. */
    public int error_sym() {return 1;}



    public void report_fatal_error(String message, Object info) throws SyntaxError {
        throw new SyntaxError();
    }

    public void syntax_error(Symbol cur_token) {
        int i = 0;
        expected_token_ids();
        expected_token_ids();
        expected_token_ids();
        List<Integer> lst = expected_token_ids();

        Token k = (Token) cur_token;
        System.err.print(k.left + ":" + k.right + " : syntax error; found \"" + k.getTagString() + "\", expected: ");
        for (i = 0; i < lst.size(); i++)
            System.err.print(" '"+ symbl_name_from_id(lst.get(i)) + "'");
        System.err.println("");
    }


    /** Cup generated class to encapsulate user supplied action code.*/
    @SuppressWarnings({"rawtypes", "unchecked", "unused"})
    class CUP$LibParser$actions {
        private final LibParser parser;

        /** Constructor */
        CUP$LibParser$actions(LibParser parser) {
            this.parser = parser;
        }

        /** Method 0 with the actual generated action code for actions 0 to 300. */
        public final java_cup.runtime.Symbol CUP$LibParser$do_action_part00000000(
                int                        CUP$LibParser$act_num,
                java_cup.runtime.lr_parser CUP$LibParser$parser,
                java.util.Stack            CUP$LibParser$stack,
                int                        CUP$LibParser$top)
                throws java.lang.Exception
        {
      /* Symbol object for return from actions */
            java_cup.runtime.Symbol CUP$LibParser$result;

      /* select the action based on the action number */
            switch (CUP$LibParser$act_num)
            {
          /*. . . . . . . . . . . . . . . . . . . .*/
                case 0: // program ::= libic
                {
                    Program RESULT =null;
                    int plleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int plright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    ICClass pl = (ICClass)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    List tmp =   new LinkedList<ICClass>();

                    tmp.add(pl);

                    RESULT = new Program(tmp);

                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("program",0, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 1: // $START ::= program EOF
                {
                    Object RESULT =null;
                    int start_valleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
                    int start_valright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
                    Program start_val = (Program)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
                    RESULT = start_val;
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
          /* ACCEPT */
                CUP$LibParser$parser.done_parsing();
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 2: // libic ::= CLASS CLASS_ID LCP libmethod RCP
                {
                    ICClass RESULT =null;
                    int lleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).left;
                    int lright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).right;
                    String l = (String)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-3)).value;
                    int lmleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
                    int lmright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
                    List<Method> lm = (List<Method>)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;

                    if(!"Library".equals(l))
                        throw new SyntaxError();
                    else
                        RESULT = new ICClass(lleft, l, new LinkedList<Field>(), lm);

                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libic",1, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 3: // libmethod ::= libmethod_main libmethod
                {
                    List<Method> RESULT =null;
                    int mleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
                    int mright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
                    Method m = (Method)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
                    int mlistleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int mlistright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    List<Method> mlist = (List<Method>)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;

                    mlist.add(0,m);
                    RESULT = mlist;

                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libmethod",2, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 4: // libmethod ::=
                {
                    List<Method> RESULT =null;
                    RESULT = new LinkedList<Method>();
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libmethod",2, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 5: // libmethod_main ::= STATIC type ID LP formals RP SEMICOL
                {
                    Method RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).right;
                    Type i0 = (Type)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).value;
                    int i1left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).left;
                    int i1right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).right;
                    String i1 = (String)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).value;
                    int i2left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
                    int i2right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
                    List<Formal> i2 = (List<Formal>)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
                    RESULT = new LibraryMethod(i0, i1, i2);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libmethod_main",3, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-6)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 6: // libmethod_main ::= STATIC VOID ID LP formals RP SEMICOL
                {
                    Method RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).right;
                    Object i0 = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-5)).value;
                    int i1left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).left;
                    int i1right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).right;
                    String i1 = (String)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-4)).value;
                    int i2left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
                    int i2right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
                    List<Formal> i2 = (List<Formal>)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
                    Type voidtype = new PrimitiveType(i0left, DataTypes.VOID);
                    RESULT = new LibraryMethod(voidtype, i1, i2);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("libmethod_main",3, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-6)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 7: // type ::= INT
                {
                    Type RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    Object i0 = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    RESULT = new PrimitiveType(i0left, DataTypes.INT);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 8: // type ::= BOOLEAN
                {
                    Type RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    Object i0 = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    RESULT = new PrimitiveType(i0left, DataTypes.BOOLEAN);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 9: // type ::= STRING
                {
                    Type RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    Object i0 = (Object)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    RESULT = new PrimitiveType(i0left, DataTypes.STRING);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 10: // type ::= CLASS_ID
                {
                    Type RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    String i0 = (String)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    RESULT = new UserType(i0left, i0);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 11: // type ::= type LSP RSP
                {
                    Type RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
                    Type i0 = (Type)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;

                    i0.incrementDimension();
                    RESULT = i0;

                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("type",4, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 12: // formals ::= formals_main
                {
                    List<Formal> RESULT =null;
                    int frmlleft = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int frmlright = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    Formal frml = (Formal)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    List<Formal> frmllist = new LinkedList<Formal>();
                    frmllist.add(frml);
                    RESULT = frmllist;
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("formals",5, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 13: // formals ::= formals COL formals_main
                {
                    List<Formal> RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).right;
                    List<Formal> i0 = (List<Formal>)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)).value;
                    int i1left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int i1right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    Formal i1 = (Formal)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    i0.add(i1);
                    RESULT = i0;
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("formals",5, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-2)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 14: // formals ::=
                {
                    List<Formal> RESULT =null;
                    RESULT = new LinkedList<Formal>();
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("formals",5, ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
                case 15: // formals_main ::= type ID
                {
                    Formal RESULT =null;
                    int i0left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).left;
                    int i0right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).right;
                    Type i0 = (Type)((java_cup.runtime.Symbol) CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)).value;
                    int i1left = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).left;
                    int i1right = ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()).right;
                    String i1 = (String)((java_cup.runtime.Symbol) CUP$LibParser$stack.peek()).value;
                    RESULT = new Formal(i0, i1);
                    CUP$LibParser$result = parser.getSymbolFactory().newSymbol("formals_main",6, ((java_cup.runtime.Symbol)CUP$LibParser$stack.elementAt(CUP$LibParser$top-1)), ((java_cup.runtime.Symbol)CUP$LibParser$stack.peek()), RESULT);
                }
                return CUP$LibParser$result;

          /* . . . . . .*/
                default:
                    throw new Exception(
                            "Invalid action number "+CUP$LibParser$act_num+"found in internal parse table");

            }
        } /* end of method */

        /** Method splitting the generated action code into several parts. */
        public final java_cup.runtime.Symbol CUP$LibParser$do_action(
                int                        CUP$LibParser$act_num,
                java_cup.runtime.lr_parser CUP$LibParser$parser,
                java.util.Stack            CUP$LibParser$stack,
                int                        CUP$LibParser$top)
                throws java.lang.Exception
        {
            return CUP$LibParser$do_action_part00000000(
                    CUP$LibParser$act_num,
                    CUP$LibParser$parser,
                    CUP$LibParser$stack,
                    CUP$LibParser$top);
        }
    }

}
