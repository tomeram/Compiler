package IC.Parser;

%%

%class Lexer
%public
%function next_token
%type Token
%line
%column
%cup
%scanerror LexicalError
%eofval{
	if (yystate() == YYINITIAL) newToken(sym.EOF,"EOF"); else throw new LexicalError("errorEOF"); 
%eofval}

ZERO = 0
NUM = [1-9]
INT = [0-9]
IDENT = [a-z]+[0-9a-zA-Z\_]*
CLASS = [A-Z]+[0-9a-zA-Z\_]*
InputCharacter = [^\r\n]
LineTerminator = \r|\n|\r\n
WHITE = {LineTerminator} | [ \t\f]


/* comments */
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?


%{
	StringBuffer string = new StringBuffer();
	int stringStart = 0;
	 
	private Token newToken(int sym, String tag) {
		return new Token(sym, yyline,yycolumn,tag,yytext());
}
%}

%state STRING

%%

<YYINITIAL> {
	"/*"~"*/" | {EndOfLineComment}	{ /* ignore */ }
	<<EOF>> { return newToken(sym.EOF,"EOF"); }
	
	\" { string.setLength(0); stringStart = yycolumn; yybegin(STRING); }
	"," { return newToken(sym.COL,","); }
	"[" { return newToken(sym.LSP,"["); }
	"]" { return newToken(sym.RSP,"]"); }
	"{" { return newToken(sym.LCP,"{"); }
	"}" { return newToken(sym.RCP,"}");  }
	"." { return newToken(sym.DOT,"."); }
	
	"-" { return newToken(sym.MINUS,"-"); }
	"!" { return newToken(sym.EXCLAM,"!"); }
	"*" { return newToken(sym.MULTI,"*"); }
	"/" { return newToken(sym.DIV,"/"); }
	"%" { return newToken(sym.MOD,"%"); }
	"+" { return newToken(sym.PLUS,"+"); }
	"<" { return newToken(sym.LT,"<"); }
	">" { return newToken(sym.GT,">"); }
	"<=" { return newToken(sym.LE,"<="); }
	">=" { return newToken(sym.GE,">="); }
	"==" { return newToken(sym.EQEQ,"=="); }
	"!=" { return newToken(sym.NE,"!="); }
	"&&" { return newToken(sym.AND,"&&"); }
	"||" { return newToken(sym.OR,"||"); }
	"=" { return newToken(sym.EQ,"="); }
	";" { return newToken(sym.SEMICOL,";"); }
	
	"null" { return newToken(sym.NULL,"null"); }
	"false" { return newToken(sym.FALSE,"false"); }
	"true" { return newToken(sym.TRUE,"true"); }
	"length" { return newToken(sym.LENGTH,"length"); }
	"new" { return newToken(sym.NEW,"new"); }
	"this" { return newToken(sym.THIS,"this"); }
	"continue" { return newToken(sym.CONTINUE,"continue"); }
	"break" { return newToken(sym.BREAK,"break"); }
	"while" { return newToken(sym.WHILE,"while"); }
	"return" { return newToken(sym.RETURN,"return"); }
	"string" { return newToken(sym.STRING,"string"); }
	"boolean" { return newToken(sym.BOOLEAN,"boolean"); }
	"int" { return newToken(sym.INT,"int"); }
	"void" { return newToken(sym.VOID,"void"); }
	"static" { return newToken(sym.STATIC,"static"); }
	"extends" { return newToken(sym.EXTENDS,"extends"); }
	"class" { return newToken(sym.CLASS,"class"); }
	"else" { return newToken(sym.ELSE,"else"); }
	"if" { return newToken(sym.IF,"if"); }
	
	{WHITE} { /* ignore */ }
	"(" { return newToken(sym.LP,"("); }
	")" { return newToken(sym.RP,")"); }
	
	
	{ZERO}+ { return newToken(sym.NUM,"INTEGER"); }
	{ZERO}{INT}+ { return newToken(sym.NUM,"INTEGER"); }
	{NUM}{INT}* { return newToken(sym.NUM,"INTEGER"); }
	
	{IDENT} { return newToken(sym.ID,"ID"); }
	{CLASS} { return newToken(sym.CLASS_ID,"CLASS_ID"); }
	
}

<STRING> {
	\" { yybegin(YYINITIAL); 
		return new Token(sym.STRING_LITERAL, yyline,stringStart,"STRING", string.toString()); }
	\\t                            { string.append("\\t"); }
	\\n                            { string.append("\\n");}
	\\\"                           { string.append("\\\""); }
	\\\\                           { string.append("\\\\"); }
	\\                             { throw new LexicalError("error"); }
	[ -~] { string.append(yytext()); }
}