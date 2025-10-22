public class Token {
    
    public enum Type {
        IF, ELSE, ID,
        INT, FLOAT, INPUT, OUTPUT, SEMICOLON,
        LKEY, RKEY, LP, RP, EQ, 
        EOF, ERROR
}
    private Type type;
    private String lexeme;
    
    public Token(Type type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                '}';
    }
}