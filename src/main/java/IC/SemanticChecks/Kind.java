package IC.SemanticChecks;

public enum Kind {
    CLASS			("Class"),
    //	VAR 			("Variabel"),
    METHOD 			("Method"),
    FIELD 			("Field");

    private String string_val;

    Kind(String val) {
        this.string_val = val;
    }

    @Override
    public String toString() {
        return this.string_val;
    }
}
