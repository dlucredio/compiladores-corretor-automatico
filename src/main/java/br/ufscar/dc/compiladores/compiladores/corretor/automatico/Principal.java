/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package br.ufscar.dc.compiladores.compiladores.corretor.automatico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author daniellucredio
 */
public class Principal {

    static int numCTLexico = 0;
    static int numCTSintatico = 0;
    static int numCTSemantico = 0;
    static int numCTGerador = 0;
    static int numCTLexicoCorretos = 0;
    static int numCTSintaticoCorretos = 0;
    static int numCTSemanticoCorretos = 0;
    static int numCTGeradorCorretos = 0;

    public static void main(String[] args) throws Exception {
        if (args.length < 6) {
            System.out.println("Uso: java -jar compiladores-corretor-automatico.java "
                    + "<caminho para o compilador executavel> " + "<caminho para o compilador gcc> "
                    + "<caminho para uma pasta temporaria> "
                    + "<caminho para a pasta com os casos de teste> "
                    + "\"RAs dos alunos do grupo\" "
                    + "tipoTeste (lexico|sintatico|semantico|gerador|semanticogerador|tudo");
            System.exit(0);
        }
        String executavel = args[0];
        String compiladorGcc = args[1];
        String diretorioTemporario = args[2];
        String diretorioCasosDeTeste = args[3];
        String grupo = args[4];
        String tipoTeste = args[5];

        if (!tipoTeste.equals("lexico") && !tipoTeste.equals("sintatico")
                && !tipoTeste.equals("semantico") && !tipoTeste.equals("gerador") && !tipoTeste.equals("semanticogerador")
                && !tipoTeste.equals("tudo") && !tipoTeste.startsWith("gabarito")) {
            System.out.println(
                    "Na opcao tipoTeste, especifique: lexico ou sintatico ou semantico ou gerador ou tudo");
            System.exit(0);
        }

        File fDiretorioCasosDeTeste = new File(diretorioCasosDeTeste);
        if (!fDiretorioCasosDeTeste.isDirectory() || !fDiretorioCasosDeTeste.exists()) {
            System.out.println(
                    "Caminho " + diretorioCasosDeTeste + " nao existe ou nao e uma pasta!");
            System.exit(0);
        }

        File fPastaDeTrabalho = new File(diretorioTemporario, "saidaProduzida");
        if (fPastaDeTrabalho.exists() && fPastaDeTrabalho.isDirectory()) {
            FileUtils.deleteDirectory(fPastaDeTrabalho);

        }
        fPastaDeTrabalho.mkdirs();

        File fLexico = new File(fDiretorioCasosDeTeste, "1.casos_teste_lexico");
        File fSintatico = new File(fDiretorioCasosDeTeste, "2.casos_teste_sintatico");
        File fSemantico = new File(fDiretorioCasosDeTeste, "3.casos_teste_semantico");
        File fGerador = new File(fDiretorioCasosDeTeste, "4.casos_teste_gerador");
        File fLexicoEntrada = new File(fLexico, "entrada");
        File fLexicoSaida = new File(fLexico, "saida");
        File fSintaticoEntrada = new File(fSintatico, "entrada");
        File fSintaticoSaida = new File(fSintatico, "saida");
        File fSemanticoEntrada = new File(fSemantico, "entrada");
        File fSemanticoSaida = new File(fSemantico, "saida");
        File fGeradorEntrada = new File(fGerador, "1.entrada");
        File fGeradorEntradaExecucao = new File(fGerador, "3.entrada_execucao");
        File fGeradorSaida = new File(fGerador, "4.saida");

        if (!(fLexicoEntrada.exists() && fLexicoEntrada.isDirectory()
                || fLexicoSaida.exists() && fLexicoSaida.isDirectory()
                || fSintaticoEntrada.exists() && fSintaticoEntrada.isDirectory()
                || fSintaticoSaida.exists() && fSintaticoSaida.isDirectory()
                || fSemanticoEntrada.exists() && fSemanticoEntrada.isDirectory()
                || fSemanticoSaida.exists() && fSemanticoSaida.isDirectory()
                || fGeradorEntrada.exists() && fGeradorEntrada.isDirectory()
                || fGeradorEntradaExecucao.exists() && fGeradorEntradaExecucao.isDirectory()
                || fGeradorSaida.exists() && fGeradorSaida.isDirectory())) {
            System.out.println("Pasta de casos de testes corrompida. Verifique "
                    + "se as seguintes subpastas estao presentes:");
            System.out.println(fLexicoEntrada.getAbsolutePath());
            System.out.println(fLexicoSaida.getAbsolutePath());
            System.out.println(fSintaticoEntrada.getAbsolutePath());
            System.out.println(fSintaticoSaida.getAbsolutePath());
            System.out.println(fSemanticoEntrada.getAbsolutePath());
            System.out.println(fSemanticoSaida.getAbsolutePath());
            System.out.println(fGeradorEntrada.getAbsolutePath());
            System.out.println(fGeradorEntradaExecucao.getAbsolutePath());
            System.out.println(fGeradorSaida.getAbsolutePath());
            System.exit(0);
        }

        float notaCTLexico = 0;
        float notaCTSintatico = 0;
        float notaCTSemantico = 0;
        float notaCTGerador = 0;

        if (tipoTeste.equals("lexico") || tipoTeste.equals("gabarito-lexico")) {
            System.out.println("Corrigindo analisador léxico ...");
            analisaLexico(executavel, fLexicoEntrada, fLexicoSaida, fPastaDeTrabalho,
                    tipoTeste.equals("gabarito-lexico"));
            notaCTLexico = 10.0f * (((float) numCTLexicoCorretos) / ((float) numCTLexico));
        }
        if (tipoTeste.equals("tudo") || tipoTeste.equals("sintatico")
                || tipoTeste.equals("gabarito-sintatico")) {
            System.out.println("Corrigindo analisador sintatico ...");
            analisaSintatico(executavel, fSintaticoEntrada, fSintaticoSaida, fPastaDeTrabalho,
                    tipoTeste.equals("gabarito-sintatico"));
            notaCTSintatico = 10.0f * (((float) numCTSintaticoCorretos) / ((float) numCTSintatico));

        }
        if (tipoTeste.equals("tudo") || tipoTeste.equals("semantico") || tipoTeste.equals("semanticogerador")
                || tipoTeste.equals("gabarito-semantico")) {
            System.out.println("Corrigindo analisador semantico ...");
            analisaSemanticoComErros(executavel, fSemanticoEntrada, fSemanticoSaida,
                    fPastaDeTrabalho, tipoTeste.equals("gabarito-semantico"));
            notaCTSemantico = 10.0f * (((float) numCTSemanticoCorretos) / ((float) numCTSemantico));
        }
        if (tipoTeste.equals("tudo") || tipoTeste.equals("gerador") || tipoTeste.equals("semanticogerador")
                || tipoTeste.equals("gabarito-gerador")) {
            System.out.println("Corrigindo gerador de codigo ...");
            analisaGeradorDeCodigo(executavel, compiladorGcc, fGeradorEntrada,
                    fGeradorEntradaExecucao, fGeradorSaida, fPastaDeTrabalho,
                    tipoTeste.equals("gabarito-gerador"));
            notaCTGerador = 10.0f * (((float) numCTGeradorCorretos) / ((float) numCTGerador));
        }

        System.out.println("\n\n==================================");
        System.out.println("Nota do grupo \"" + grupo + "\":");

        System.out.println("CT Léxico = " + notaCTLexico + " (" + numCTLexicoCorretos + "/"
                + numCTLexico + ")");
        System.out.println("CT Sintático = " + notaCTSintatico + " (" + numCTSintaticoCorretos + "/"
                + numCTSintatico + ")");
        System.out.println("CT Semântico = " + notaCTSemantico + " (" + numCTSemanticoCorretos + "/"
                + numCTSemantico + ")");
        System.out.println("CT Gerador = " + notaCTGerador + " (" + numCTGeradorCorretos + "/"
                + numCTGerador + ")");
        System.out.println("==================================");
    }

    private static void analisaLexico(String executavel, File entrada, File saida,
            File pastaDeTrabalho, boolean gerarGabarito) throws IOException {
        File fSaida1 = new File(pastaDeTrabalho, "saidaLexico");
        fSaida1.mkdirs();
        File[] casosDeTeste = entrada.listFiles();
        numCTLexico = casosDeTeste.length;
        for (File ctEntrada : casosDeTeste) {
            System.out.println("   " + ctEntrada.getName());
            File fSaidaUsuario = new File(fSaida1, ctEntrada.getName());
            String cmd = executavel + " " + ctEntrada.getAbsolutePath() + " "
                    + fSaidaUsuario.getAbsolutePath();
            System.out.println("Executando: " + cmd);
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
                System.out.println("Execução finalizada");
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (gerarGabarito) {
            numCTLexicoCorretos = numCTLexico;
            System.out.println("Gerando gabarito");
            FileUtils.copyDirectory(fSaida1, saida);
            System.out.println("Gabarito gerado");
        } else {
            System.out.println("Verificando resultado");
            numCTLexicoCorretos = compararPastas(saida, fSaida1);
            System.out.println("Resultado verificado");
        }
    }

    private static void analisaSintatico(String executavel, File entrada, File saida,
            File pastaDeTrabalho, boolean gerarGabarito) throws IOException {
        File fSaida1 = new File(pastaDeTrabalho, "saidaSintatico");
        fSaida1.mkdirs();
        File[] casosDeTeste = entrada.listFiles();
        numCTSintatico = casosDeTeste.length;
        for (File ctEntrada : casosDeTeste) {
            System.out.println("   " + ctEntrada.getName());
            File fSaidaUsuario = new File(fSaida1, ctEntrada.getName());
            String cmd = executavel + " " + ctEntrada.getAbsolutePath() + " "
                    + fSaidaUsuario.getAbsolutePath();
            System.out.println("Executando: " + cmd);
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (gerarGabarito) {
            numCTSintaticoCorretos = numCTSintatico;
            FileUtils.copyDirectory(fSaida1, saida);
        } else {
            numCTSintaticoCorretos = compararPastas(saida, fSaida1);
        }
    }

    private static void analisaSemanticoComErros(String executavel, File entrada, File saida,
            File pastaDeTrabalho, boolean gerarGabarito) throws IOException {
        File fSaida1 = new File(pastaDeTrabalho, "saidaSemanticoComErros");
        fSaida1.mkdirs();
        File[] casosDeTeste = entrada.listFiles();
        numCTSemantico = casosDeTeste.length;
        for (File ctEntrada : casosDeTeste) {
            System.out.println("   " + ctEntrada.getName());

            File fSaidaUsuario = new File(fSaida1, ctEntrada.getName());
            String cmd = executavel + " " + ctEntrada.getAbsolutePath() + " "
                    + fSaidaUsuario.getAbsolutePath();
            System.out.println("Executando: " + cmd);
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (gerarGabarito) {
            numCTSemanticoCorretos = numCTSemantico;
            FileUtils.copyDirectory(fSaida1, saida);
        } else {
            numCTSemanticoCorretos = compararPastas(saida, fSaida1);
        }
    }

    private static void analisaGeradorDeCodigo(String executavel, String gcc, File entrada,
            File entradaExecucao, File saidaExecucaoCasosDeTeste, File pastaDeTrabalho,
            boolean gerarGabarito) throws IOException, InterruptedException {
        System.out.println("   Gerando codigo...");
        File fSaida1 = new File(pastaDeTrabalho, "saidaGeradorDeCodigo");
        File fSaida2 = new File(pastaDeTrabalho, "saidaExecucao");
        fSaida1.mkdirs();
        fSaida2.mkdirs();
        File[] casosDeTeste = entrada.listFiles();
        numCTGerador = casosDeTeste.length;
        for (File ctEntrada : casosDeTeste) {
            System.out.println("   " + ctEntrada.getName());

            File fSaidaUsuario = new File(fSaida1, ctEntrada.getName() + ".c");
            String cmd = executavel + " " + ctEntrada.getAbsolutePath() + " "
                    + fSaidaUsuario.getAbsolutePath();
            try {
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("   Compilando codigo gerado...");
        for (File ctEntrada : casosDeTeste) {
            File fGerado = new File(fSaida1, ctEntrada.getName() + ".c");
            File fCompilado = new File(fSaida1, ctEntrada.getName() + ".out");
            String cmd =
                    gcc + " " + fGerado.getAbsolutePath() + " -o " + fCompilado.getAbsolutePath();
            try {
                System.out.println(cmd);
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("   Testando codigo compilado...");
        for (File ctEntrada : casosDeTeste) {
            File fCompilado = new File(fSaida1, ctEntrada.getName() + ".out");
            File fEntradaExecucao = new File(entradaExecucao, ctEntrada.getName());
            File fSaidaExecucao = new File(fSaida2, ctEntrada.getName());

            String cmd = fCompilado.getAbsolutePath();

            try {
                Process p = Runtime.getRuntime().exec(cmd);
                InputStream is;
                FileInputStream fisEntradaExecucao;
                FileOutputStream fosSaidaExecucao;
                try (OutputStream os = p.getOutputStream()) {
                    is = p.getInputStream();
                    fisEntradaExecucao = new FileInputStream(fEntradaExecucao);
                    int b = fisEntradaExecucao.read();
                    while (b != -1) {
                        os.write(b);
                        b = fisEntradaExecucao.read();
                    }
                    os.flush();
                    fSaidaExecucao.createNewFile();
                    fosSaidaExecucao = new FileOutputStream(fSaidaExecucao);
                    b = is.read();
                    while (b != -1) {
                        fosSaidaExecucao.write(b);
                        b = is.read();
                    }
                    fosSaidaExecucao.flush();
                }
                fosSaidaExecucao.close();
                is.close();
                fisEntradaExecucao.close();

                p.waitFor();
            } catch (Exception e) {
                System.out.println("   Erro ao executar comando ["+cmd+"]: "+e.getMessage());
            }

        }

        if (gerarGabarito) {
            numCTGeradorCorretos = numCTGerador;
            FileUtils.copyDirectory(fSaida2, saidaExecucaoCasosDeTeste);
        } else {
            numCTGeradorCorretos = compararPastas(saidaExecucaoCasosDeTeste, fSaida2);
        }
    }

    private static int compararPastas(File pastaCasosTeste, File pastaAluno) throws IOException {
        int numAcertos = 0;
        File[] filesCasosTeste = pastaCasosTeste.listFiles();
        for (File fCasoTeste : filesCasosTeste) {
            if (!fCasoTeste.getName().endsWith("alt")) {
                File fAluno = new File(pastaAluno, fCasoTeste.getName());
                boolean igual = compararArquivos(fCasoTeste, fAluno);
                if (!igual) {
                    int alt = 1;
                    String altTxt = "." + alt + "alt";
                    File fCasoTesteAlt = new File(pastaCasosTeste, fCasoTeste.getName() + altTxt);
                    while (fCasoTesteAlt.exists()) {
                        igual = compararArquivos(fCasoTesteAlt, fAluno);
                        if (igual) {
                            break;
                        }
                        alt++;
                        altTxt = "." + alt + "alt";
                        fCasoTesteAlt = new File(pastaCasosTeste, fCasoTeste + altTxt);
                    }
                }
                if (!igual) {
                    File f = new File(fAluno.getParent(), "_erro_" + fAluno.getName());
                    fAluno.renameTo(f);
                } else {
                    File f = new File(fAluno.getParent(), "_ok_" + fAluno.getName());
                    fAluno.renameTo(f);
                    numAcertos++;
                }
            }
        }
        return numAcertos;
    }

    private static boolean compararArquivos(File fCasoTeste, File fAluno)
            throws FileNotFoundException, IOException {
        try (InputStream i1 = new FileInputStream(fCasoTeste)) {
            if(!fAluno.exists()) {
                return false;
            }
            try (InputStream i2 = new FileInputStream(fAluno)) {
                int char1 = -1;
                int char2 = -1;
                while ((char1 = i1.read()) != -1 & (char2 = i2.read()) != -1) {
                    if (char1 == '\r') {
                        char1 = i1.read();
                    }
                    if (char2 == '\r') {
                        char2 = i2.read();
                    }
                    if (char1 != char2) {
                        return false;
                    }
                }
                return !(char1 != -1 || char2 != -1);
            }
        }
    }
}
