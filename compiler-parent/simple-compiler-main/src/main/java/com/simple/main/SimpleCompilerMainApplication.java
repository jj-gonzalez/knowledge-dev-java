package com.simple.main;

import com.simple.ccr.Token;
import com.simple.clx.Lexer;
import com.simple.clx.impl.LexerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class SimpleCompilerMainApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SimpleCompilerMainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SimpleCompilerMainApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        String fileSource = args[0];
        String source = Files.readString(Paths.get(fileSource));
        Lexer lexer  = new LexerImpl();
        List<Token> tokens = lexer.tokenize(source);
        printTokenTable(tokens);
    }
    public static void printTokenTable(List<Token> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            log.warn("La lista de tokens está vacía.");
            return;
        }

        String[] headers = {"type", "lexeme", "line", "column"};
        int[] valWidths = new int[4];

        // 1. Calcular anchos máximos
        for (Token t : tokens) {
            valWidths[0] = Math.max(valWidths[0], String.valueOf(t.getType()).length());
            valWidths[1] = Math.max(valWidths[1], String.valueOf(t.getLexeme()).length());
            valWidths[2] = Math.max(valWidths[2], String.valueOf(t.getLine()).length());
            valWidths[3] = Math.max(valWidths[3], String.valueOf(t.getColumn()).length());
        }

        // 2. Definir el formato de la fila (String.format usa %s para rellenar)
        // El formato asegura que cada columna tenga un ancho fijo
        String rowTemplate = "| %s = %-" + valWidths[0] + "s " +
                "| %s = '%-" + valWidths[1] + "s' " +
                "| %s = %-" + valWidths[2] + "s " +
                "| %s = %-" + valWidths[3] + "s |";

        // 3. Calcular el largo del divisor para que coincida con el texto
        int rowLength = String.format(rowTemplate,
                headers[0], "", headers[1], "", headers[2], "", headers[3], "").length();
        String divider = "-".repeat(rowLength);

        log.info("\n\n***************************************Token Table************************************");
        // 4. Imprimir usando el Logger
        log.info(divider);
        for (Token t : tokens) {
            String formattedRow = String.format(rowTemplate,
                    headers[0], t.getType(),
                    headers[1], t.getLexeme(),
                    headers[2], t.getLine(),
                    headers[3], t.getColumn()
            );
            log.info(formattedRow);
            log.info(divider);
        }
    }

}
