// Archivo: AST.java

import java.util.List;

/**
 * Contiene todas las clases de nodos para el Árbol Sintáctico Abstracto (AST).
 */
public class AST {

    // --- Interfaces Base ---
    public interface Node {
        String toString(); // Para desplegar el árbol
    }

    public interface Statement extends Node {}
    public interface Expression extends Node {}

    // --- Nodos Concretos ---

    /** P -> D S */
    public static class ProgramNode implements Node {
        final List<DeclarationNode> declarations;
        final Statement statement;

        public ProgramNode(List<DeclarationNode> declarations, Statement statement) {
            this.declarations = declarations;
            this.statement = statement;
        }
        
        @Override public String toString() {
            StringBuilder sb = new StringBuilder("Program:\n");
            for (DeclarationNode d : declarations) {
                sb.append("  ").append(d).append("\n");
            }
            sb.append("  ").append(statement);
            return sb.toString();
        }
    }

    /** D -> (int | float) id ; */
    public static class DeclarationNode implements Node {
        final Token type;
        final Token id;

        public DeclarationNode(Token type, Token id) {
            this.type = type;
            this.id = id;
        }
        @Override public String toString() {
            return "Declare(" + type.getLexeme() + " " + id.getLexeme() + ")";
        }
    }

    /** S -> if E then S else S */
    public static class IfNode implements Statement {
        final Expression condition;
        final Statement thenBranch;
        final Statement elseBranch;

        public IfNode(Expression condition, Statement thenBranch, Statement elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }
        @Override public String toString() {
            return "If(Cond: " + condition + ", Then: " + thenBranch + ", Else: " + elseBranch + ")";
        }
    }

    /** S -> while E do S */
    public static class WhileNode implements Statement {
        final Expression condition;
        final Statement doBranch;

        public WhileNode(Expression condition, Statement doBranch) {
            this.condition = condition;
            this.doBranch = doBranch;
        }
        @Override public String toString() {
            return "While(Cond: " + condition + ", Do: " + doBranch + ")";
        }
    }
    
    /** S -> { S L } (L es la lista de ; S) */
    public static class BlockNode implements Statement {
        final Statement firstStatement;
        final List<Statement> followingStatements; // De L

        public BlockNode(Statement firstStatement, List<Statement> followingStatements) {
            this.firstStatement = firstStatement;
            this.followingStatements = followingStatements;
        }
        @Override public String toString() {
            StringBuilder sb = new StringBuilder("Block {\n");
            sb.append("    ").append(firstStatement).append("\n");
            for(Statement s : followingStatements) {
                sb.append("    ").append(s).append("\n");
            }
            sb.append("  }");
            return sb.toString();
        }
    }

    /** S -> input E */
    public static class InputNode implements Statement {
        final Expression expression; // La gramática dice E, usualmente sería un ID

        public InputNode(Expression expression) {
            this.expression = expression;
        }
        @Override public String toString() {
            return "Input(" + expression + ")";
        }
    }

    /** S -> output E */
    public static class OutputNode implements Statement {
        final Expression expression;

        public OutputNode(Expression expression) {
            this.expression = expression;
        }
        @Override public String toString() {
            return "Output(" + expression + ")";
        }
    }

    /** E -> num == num | id == id */
    public static class ComparisonNode implements Expression {
        final Expression left;
        final Token operator;
        final Expression right;

        public ComparisonNode(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
        @Override public String toString() {
            return "Compare(" + left + " == " + right + ")";
        }
    }

    /** E -> id */
    public static class IdNode implements Expression {
        final Token token;
        public IdNode(Token token) { this.token = token; }
        @Override public String toString() {
            return "Id(" + token.getLexeme() + ")";
        }
    }

    /** E -> num */
    public static class NumNode implements Expression {
        final Token token;
        public NumNode(Token token) { this.token = token; }
        @Override public String toString() {
            return "Num(" + token.getLexeme() + ")";
        }
    }
}