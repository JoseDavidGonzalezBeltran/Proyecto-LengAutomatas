// Archivo: Main.java

import AST.ProgramNode;

public class Main {
    public static void main(String[] args) {

        // --- PRUEBA 1: PROGRAMA VÁLIDO ---
        String sourceCodeOK = 
            "// Declaraciones\n" +
            "int varInt;\n" +
            "float varFloat;\n" +
            "\n" +
            "// Sentencias\n" +
            "{\n" +
            "  input varInt;\n" +
            "  if varInt == 100\n" +
            "  then\n" +
            "    output 200;\n" +
            "  else\n" +
            "    output 300\n" +
            "}\n";

        System.out.println("--- Probando Código Válido ---");
        Scanner scannerOK = new Scanner(sourceCodeOK);
        Parser parserOK = new Parser(scannerOK);
        ProgramNode astOK = parserOK.parse();

        // Si el AST no es null, se desplegará
        if (astOK != null) {
            System.out.println("\n--- ÁRBOL SINTÁCTICO (AST) ---");
            System.out.println(astOK.toString());
        }

        System.out.println("\n====================================\n");

        // --- PRUEBA 2: PROGRAMA CON ERROR SINTÁCTICO ---
        // (Falta 'then' y el ID 'x' es muy corto)
        String sourceCodeError = 
            "int x; // Error Léxico: ID 'x' muy corto\n" +
            "if 10 == 10 \n" + // Error Sintáctico: Falta 'then'
            "  output 100 \n" +
            "else \n" +
            "  output 200 \n";
            
        System.out.println("--- Probando Código con Error ---");
        Scanner scannerError = new Scanner(sourceCodeError);
        Parser parserError = new Parser(scannerError);
        // El parser detectará el error y parará la compilación.
        parserError.parse();
    }
}