// Archivo: Parser.java

import java.util.ArrayList;
import java.util.List;

// Importa todas las clases de nodos del AST
import AST.*;

public class Parser {
    private final Scanner scanner;
    private Token currentToken;

    public Parser(Scanner scanner) {
        this.scanner = scanner;
        this.currentToken = scanner.getNextToken(); // Carga el primer token
    }

    // --- Métodos de Control ---

    /**
     * Consume el token actual si es del tipo esperado.
     * Si no, lanza un error de sintaxis.
     */
    private Token match(Token.Type expectedType) {
        if (currentToken.getType() == expectedType) {
            Token consumedToken = currentToken;
            currentToken = scanner.getNextToken();
            
            // Detenerse si el scanner encontró un error léxico
            if (currentToken.getType() == Token.Type.ERROR) {
                error("Error Léxico encontrado: " + currentToken.getLexeme());
            }
            return consumedToken;
        }
        error("Error de Sintaxis: Se esperaba " + expectedType + 
              " pero se encontró " + currentToken.getType() + 
              " ('" + currentToken.getLexeme() + "')");
        return null; // Nunca se alcanza
    }

    /**
     * Reporta un error de sintaxis y detiene la compilación.
     */
    private void error(String message) {
        System.err.println(message);
        // Lanzamos una excepción para parar la compilación (como pide el proyecto)
        throw new RuntimeException(message);
    }

    // --- Método de Arranque ---

    /**
     * Inicia el análisis sintáctico.
     * @return El nodo raíz del AST (ProgramNode).
     */
    public ProgramNode parse() {
        try {
            ProgramNode program = P(); // Inicia en el símbolo P
            match(Token.Type.EOF);     // Verifica el fin de archivo
            System.out.println("Análisis Sintáctico Exitoso.");
            return program;
        } catch (RuntimeException e) {
            System.err.println("Compilación fallida: " + e.getMessage());
            return null; // Devuelve null si falló
        }
    }

    // --- Métodos del Parser (Descenso Recursivo) ---

    /** P -> D S */
    private ProgramNode P() {
        List<DeclarationNode> declarations = D();
        Statement statement = S();
        return new ProgramNode(declarations, statement);
    }

    /**
     * D -> (int | float) id ; D
     * D -> ε (cadena nula)
     */
    private List<DeclarationNode> D() {
        List<DeclarationNode> declarations = new ArrayList<>();
        
        // D -> ε (cadena nula). Miramos si el token actual NO es int o float.
        if (currentToken.getType() != Token.Type.INT && 
            currentToken.getType() != Token.Type.FLOAT) {
            return declarations; // Devuelve lista vacía
        }

        // D -> (int | float) id ; D
        Token type = currentToken;
        if (type.getType() == Token.Type.INT) {
            match(Token.Type.INT);
        } else {
            match(Token.Type.FLOAT);
        }
        
        Token id = match(Token.Type.ID);
        match(Token.Type.SEMICOLON);
        
        declarations.add(new DeclarationNode(type, id));
        
        // Llamada recursiva para el resto de D
        declarations.addAll(D());
        
        return declarations;
    }

    /**
     * S -> if E then S else S
     * S -> while E do S
     * S -> { S L }
     * S -> input E
     * S -> output E
     */
    private Statement S() {
        switch (currentToken.getType()) {
            case IF:
                match(Token.Type.IF);
                Expression ifCond = E();
                match(Token.Type.THEN);
                Statement thenBranch = S();
                match(Token.Type.ELSE);
                Statement elseBranch = S();
                return new IfNode(ifCond, thenBranch, elseBranch);

            case WHILE:
                match(Token.Type.WHILE);
                Expression whileCond = E();
                match(Token.Type.DO);
                Statement doBranch = S();
                return new WhileNode(whileCond, doBranch);

            case LKEY: // {
                match(Token.Type.LKEY);
                Statement firstStatement = S();
                List<Statement> followingStatements = L(); // L consumirá el RKEY
                return new BlockNode(firstStatement, followingStatements);

            case INPUT:
                match(Token.Type.INPUT);
                Expression inputExpr = E();
                return new InputNode(inputExpr);

            case OUTPUT:
                match(Token.Type.OUTPUT);
                Expression outputExpr = E();
                return new OutputNode(outputExpr);

            default:
                error("Se esperaba una sentencia (if, while, {, input, output) " +
                      "pero se encontró " + currentToken.getType());
                return null; // Nunca se alcanza
        }
    }

    /**
     * L -> ; S L
     * L -> } (Esta regla consume el '}')
     */
    private List<Statement> L() {
        List<Statement> statements = new ArrayList<>();
        
        if (currentToken.getType() == Token.Type.SEMICOLON) {
            // L -> ; S L
            match(Token.Type.SEMICOLON);
            statements.add(S());
            statements.addAll(L()); // Llamada recursiva
            
        } else if (currentToken.getType() == Token.Type.RKEY) {
            // L -> } (Caso base, consume '}' y devuelve lista vacía)
            match(Token.Type.RKEY);
            
        } else {
            error("Se esperaba ';' o '}' dentro del bloque, pero se encontró " + 
                  currentToken.getType());
        }
        
        return statements;
    }

    /**
     * E -> num == num
     * E -> id == id
     * E -> num
     * E -> id
     */
    private Expression E() {
        Expression left;
        Token leftToken = currentToken;

        // Reconocer el primer operando (num o id)
        if (leftToken.getType() == Token.Type.NUM) {
            match(Token.Type.NUM);
            left = new NumNode(leftToken);
        } else if (leftToken.getType() == Token.Type.ID) {
            match(Token.Type.ID);
            left = new IdNode(leftToken);
        } else {
            error("Se esperaba una expresión (NUM o ID) pero se encontró " + 
                  currentToken.getType());
            return null; // Nunca se alcanza
        }

        // Verificar si es una comparación (num == num o id == id)
        if (currentToken.getType() == Token.Type.EQ) {
            Token op = match(Token.Type.EQ);
            
            Expression right;
            Token rightToken = currentToken;

            // La gramática es estricta: num == num y id == id
            if (rightToken.getType() == Token.Type.NUM && left instanceof NumNode) {
                match(Token.Type.NUM);
                right = new NumNode(rightToken);
            } else if (rightToken.getType() == Token.Type.ID && left instanceof IdNode) {
                match(Token.Type.ID);
                right = new IdNode(rightToken);
            } else {
                error("Error de tipo en la comparación: Solo se permite num == num o id == id.");
                return null; // Nunca se alcanza
            }
            
            return new ComparisonNode(left, op, right);
        }

        // Si no fue comparación, es E -> num o E -> id
        return left;
    }
}