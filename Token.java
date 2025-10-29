// Archivo: Token.java

public class Token {

    // Enum con todos los símbolos terminales de la gramática
    public enum Type {
        // Palabras clave (Keywords)
        IF, ELSE, THEN, WHILE, DO,
        INT, FLOAT, INPUT, OUTPUT,

        // Literales y Identificadores
        ID,     // id -> letra(letra|digito)+
        NUM,    // num -> digito+

        // Puntuación y Operadores
        SEMICOLON,  // ;
        LKEY,       // {
        RKEY,       // }
        EQ,         // ==

        // Control
        EOF,        // Fin de archivo
        ERROR       // Token de error
    }

    private final Type type;
    private final String lexeme;
    
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