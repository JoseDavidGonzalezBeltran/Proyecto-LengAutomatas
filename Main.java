public class Main {
    public static void main(String[] args) {
        String codigoFuente = 
            "output a;";
        // 1. Crear Scanner
        Scanner scanner = new Scanner(codigoFuente);
        
        // 2. Crear Parser
        Parser parser = new Parser(scanner);
        
        // 3. Analizar y obtener el AST
        ASTNode ast = parser.parse();
        
        // 4. Desplegar el Árbol Sintáctico (si no hay errores)
        if (ast != null) {
            System.out.println("\n--- ÁRBOL SINTÁCTICO (AST) ---\n");
            ast.display(0);
        }
    }
}