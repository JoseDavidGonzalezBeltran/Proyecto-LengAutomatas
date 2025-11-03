import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    public String type; 
    public final String value; 
    public final List<ASTNode> children;

    // Constructor para nodos no terminales (producciones)
    public ASTNode(String type) {
        this.type = type; // Asignación inicial
        this.value = null;
        this.children = new ArrayList<>();
    }

    // Constructor para nodos terminales (tokens)
    public ASTNode(String type, String value) {
        this.type = type;
        this.value = value;
        this.children = new ArrayList<>();
    }

    // Método para desplegar el AST en pre-orden
    public void display(int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "<- " + type + (value != null ? " : " + value : ""));
        for (ASTNode child : children) {
            child.display(depth + 1);
        }
    }
}