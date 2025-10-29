// Archivo: Scanner.java

import java.util.HashMap;
import java.util.Map;

public class Scanner {
    private final String source;
    private int position = 0;
    private int line = 1;

    // Tabla de Palabras Clave
    private static final Map<String, Token.Type> keywords = new HashMap<>();

    static {
        keywords.put("if", Token.Type.IF);
        keywords.put("else", Token.Type.ELSE);
        keywords.put("then", Token.Type.THEN);
        keywords.put("while", Token.Type.WHILE);
        keywords.put("do", Token.Type.DO);
        keywords.put("int", Token.Type.INT);
        keywords.put("float", Token.Type.FLOAT);
        keywords.put("input", Token.Type.INPUT);
        keywords.put("output", Token.Type.OUTPUT);
    }

    public Scanner(String source) {
        this.source = source + "\n"; // Facilita el manejo del EOF
    }

    // --- Métodos Auxiliares de Lectura ---

    private boolean isAtEnd() {
        return position >= source.length();
    }

    private char advance() {
        return source.charAt(position++);
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(position);
    }

    private char peekNext() {
        if (position + 1 >= source.length()) return '\0';
        return source.charAt(position + 1);
    }

    private void skipWhitespaceAndComments() {
        while (!isAtEnd()) {
            char c = peek();
            switch (c) {
                case ' ':
                case '\r':
                case '\t':
                    advance();
                    break;
                case '\n':
                    line++;
                    advance();
                    break;
                case '/':
                    if (peekNext() == '/') { // Comentario de línea
                        while (peek() != '\n' && !isAtEnd()) {
                            advance();
                        }
                    } else {
                        return; // No es comentario, es otro token (división, no soportada)
                    }
                    break;
                default:
                    return; // Carácter significativo
            }
        }
    }

    // --- Métodos de Reconocimiento de Tokens ---

    /**
     * Reconoce: id -> letra (letra | digito)+
     * IMPORTANTE: La gramática exige que un ID tenga 2 o más caracteres.
     */
    private Token handleIdentifier(char firstChar) {
        StringBuilder lexemeBuilder = new StringBuilder();
        lexemeBuilder.append(firstChar);

        // La regla (letra | digito)+ exige al menos UN carácter más.
        if (!Character.isLetterOrDigit(peek())) {
            String errorMsg = "ID mal formado '" + firstChar + "' en línea " + line + ". Debe tener al menos 2 caracteres.";
            System.err.println(errorMsg);
            return new Token(Token.Type.ERROR, errorMsg);
        }

        // Consumir (letra | digito)+
        while (Character.isLetterOrDigit(peek())) {
            lexemeBuilder.append(advance());
        }

        String lexeme = lexemeBuilder.toString();
        Token.Type type = keywords.get(lexeme); // Es palabra clave?
        
        if (type == null) {
            type = Token.Type.ID; // No, es un ID
        }
        
        return new Token(type, lexeme);
    }

    /**
     * Reconoce: num -> digito+
     * IMPORTANTE: La gramática solo permite enteros, no flotantes.
     */
    private Token handleNumber(char firstChar) {
        StringBuilder lexemeBuilder = new StringBuilder();
        lexemeBuilder.append(firstChar);

        while (Character.isDigit(peek())) {
            lexemeBuilder.append(advance());
        }

        // Si después del número hay una letra (ej. "123abc")
        if (Character.isLetter(peek())) {
            String errorMsg = "Número mal formado '" + lexemeBuilder.toString() + "' en línea " + line;
            System.err.println(errorMsg);
            return new Token(Token.Type.ERROR, errorMsg);
        }

        return new Token(Token.Type.NUM, lexemeBuilder.toString());
    }

    // --- Método Principal ---

    public Token getNextToken() {
        skipWhitespaceAndComments();

        if (isAtEnd()) {
            return new Token(Token.Type.EOF, "");
        }

        char c = advance();
        String lexeme = String.valueOf(c);

        // 1. Operadores y Puntuación
        switch (c) {
            case ';': return new Token(Token.Type.SEMICOLON, lexeme);
            case '{': return new Token(Token.Type.LKEY, lexeme);
            case '}': return new Token(Token.Type.RKEY, lexeme);
            case '=':
                if (peek() == '=') {
                    advance(); // Consumir el segundo '='
                    return new Token(Token.Type.EQ, "==");
                } else {
                    // La gramática no soporta asignación '=', solo '=='
                    String errorMsg = "Símbolo '=' inesperado. ¿Quisiste decir '=='? Línea " + line;
                    System.err.println(errorMsg);
                    return new Token(Token.Type.ERROR, errorMsg);
                }
            
            // La gramática no incluye '(', ')', '+', '-', etc.
        }

        // 2. Identificadores y Palabras Clave
        if (Character.isLetter(c)) {
            return handleIdentifier(c);
        }
        
        // 3. Números
        if (Character.isDigit(c)) {
            return handleNumber(c);
        }

        // 4. Carácter no reconocido
        String errorMsg = "Carácter inesperado '" + c + "' en línea " + line;
        System.err.println(errorMsg);
        return new Token(Token.Type.ERROR, errorMsg);
    }
}
