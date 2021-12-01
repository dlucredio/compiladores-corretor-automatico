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

    static int numCT1 = 0;
    static int numCT2 = 0;
    static int numCT3 = 0;
    static int numCT4 = 0;
    static int numCT5 = 0;
    static int numCT1Corretos = 0;
    static int numCT2Corretos = 0;
    static int numCT3Corretos = 0;
    static int numCT4Corretos = 0;
    static int numCT5Corretos = 0;

    public static void main(String[] args) throws Exception {
        if (args.length < 6) {
            System.out.println("Uso: java -jar compiladores-corretor-automatico.java "
                    + "<caminho para o compilador executavel> " + "<caminho para o compilador gcc> "
                    + "<caminho para uma pasta temporaria> "
                    + "<caminho para a pasta com os casos de teste> "
                    + "\"RAs dos alunos do grupo\" "
                    + "\"tipoTeste (t1|t2|t3|t4|t5|gabarito, em qualquer combinação)\"");
            System.exit(0);
        }
        String executavel = args[0];
        String compiladorGcc = args[1];
        String diretorioTemporario = args[2];
        String diretorioCasosDeTeste = args[3];
        String grupo = args[4];
        String tipoTeste = args[5];

        boolean especificouTipoTeste = false;

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

        File fT1 = new File(fDiretorioCasosDeTeste, "1.casos_teste_t1");
        File fT2 = new File(fDiretorioCasosDeTeste, "2.casos_teste_t2");
        File fT3 = new File(fDiretorioCasosDeTeste, "3.casos_teste_t3");
        File fT4 = new File(fDiretorioCasosDeTeste, "4.casos_teste_t4");
        File fT5 = new File(fDiretorioCasosDeTeste, "5.casos_teste_t5");
        File fT1Entrada = new File(fT1, "entrada");
        File fT1Saida = new File(fT1, "saida");
        File fT2Entrada = new File(fT2, "entrada");
        File fT2Saida = new File(fT2, "saida");
        File fT3Entrada = new File(fT3, "entrada");
        File fT3Saida = new File(fT3, "saida");
        File fT4Entrada = new File(fT4, "entrada");
        File fT4Saida = new File(fT4, "saida");
        File fT5Entrada = new File(fT5, "1.entrada");
        File fT5EntradaExecucao = new File(fT5, "3.entrada_execucao");
        File fT5Saida = new File(fT5, "4.saida");

        if (!(fT1Entrada.exists() && fT1Entrada.isDirectory()
                && fT1Saida.exists() && fT1Saida.isDirectory()
                && fT2Entrada.exists() && fT2Entrada.isDirectory()
                && fT2Saida.exists() && fT2Saida.isDirectory()
                && fT3Entrada.exists() && fT3Entrada.isDirectory()
                && fT3Saida.exists() && fT3Saida.isDirectory()
                && fT4Entrada.exists() && fT4Entrada.isDirectory()
                && fT4Saida.exists() && fT4Saida.isDirectory()
                && fT5Entrada.exists() && fT5Entrada.isDirectory()
                && fT5EntradaExecucao.exists() && fT5EntradaExecucao.isDirectory()
                && fT5Saida.exists() && fT5Saida.isDirectory())) {
            System.out.println("Pasta de casos de testes corrompida. Verifique "
                    + "se as seguintes subpastas estao presentes:");
            System.out.println(fT1Entrada.getAbsolutePath());
            System.out.println(fT1Saida.getAbsolutePath());
            System.out.println(fT2Entrada.getAbsolutePath());
            System.out.println(fT2Saida.getAbsolutePath());
            System.out.println(fT3Entrada.getAbsolutePath());
            System.out.println(fT3Saida.getAbsolutePath());
            System.out.println(fT4Entrada.getAbsolutePath());
            System.out.println(fT4Saida.getAbsolutePath());
            System.out.println(fT5Entrada.getAbsolutePath());
            System.out.println(fT5EntradaExecucao.getAbsolutePath());
            System.out.println(fT5Saida.getAbsolutePath());
            System.exit(0);
        } else {
            numCT1 = fT1Entrada.listFiles().length;
            numCT2 = fT2Entrada.listFiles().length;
            numCT3 = fT3Entrada.listFiles().length;
            numCT4 = fT4Entrada.listFiles().length;
            numCT5 = fT5Entrada.listFiles().length;
        }

        float notaCT1 = 0;
        float notaCT2 = 0;
        float notaCT3 = 0;
        float notaCT4 = 0;
        float notaCT5 = 0;

        if (tipoTeste.contains("t1") || tipoTeste.contains("gabarito-t1")) {
            System.out.println("Corrigindo T1 ...");
            numCT1Corretos = analisaT1aT4("t1", executavel, fT1Entrada, fT1Saida, fPastaDeTrabalho,
                    tipoTeste.contains("gabarito-t1"));
            notaCT1 = 10.0f * (((float) numCT1Corretos) / ((float) numCT1));
            especificouTipoTeste = true;
        }
        if (tipoTeste.contains("t2") || tipoTeste.contains("gabarito-t2")) {
            System.out.println("Corrigindo T2 ...");
            numCT2Corretos = analisaT1aT4("t2", executavel, fT2Entrada, fT2Saida, fPastaDeTrabalho,
                    tipoTeste.contains("gabarito-t2"));
            notaCT2 = 10.0f * (((float) numCT2Corretos) / ((float) numCT2));
            especificouTipoTeste = true;
        }
        if (tipoTeste.contains("t3") || tipoTeste.contains("gabarito-t3")) {
            System.out.println("Corrigindo T3 ...");
            numCT3Corretos = analisaT1aT4("t3", executavel, fT3Entrada, fT3Saida, fPastaDeTrabalho,
                    tipoTeste.contains("gabarito-t3"));
            notaCT3 = 10.0f * (((float) numCT3Corretos) / ((float) numCT3));
            especificouTipoTeste = true;
        }
        if (tipoTeste.contains("t4") || tipoTeste.contains("gabarito-t4")) {
            System.out.println("Corrigindo T4 ...");
            numCT4Corretos = analisaT1aT4("t4", executavel, fT4Entrada, fT4Saida, fPastaDeTrabalho,
                    tipoTeste.contains("gabarito-t4"));
            notaCT4 = 10.0f * (((float) numCT4Corretos) / ((float) numCT4));
            especificouTipoTeste = true;
        }
        if (tipoTeste.contains("t5") || tipoTeste.contains("gabarito-t5")) {
            System.out.println("Corrigindo T5 ...");
            numCT5Corretos = analisaT5(executavel, compiladorGcc, fT5Entrada, fT5EntradaExecucao, fT5Saida, fPastaDeTrabalho,
                    tipoTeste.contains("gabarito-t5"));
            notaCT5 = 10.0f * (((float) numCT5Corretos) / ((float) numCT5));
            especificouTipoTeste = true;
        }

        if (!especificouTipoTeste) {
            System.out.println(
                    "Na opcao tipoTeste, especifique: \"t1 t2 t3\", ou \"t3 t4 t5\" por exemplo");
            System.exit(0);
        }

        System.out.println("\n\n==================================");
        System.out.println("Nota do grupo \"" + grupo + "\":");

        System.out.println("CT 1 = " + notaCT1 + " (" + numCT1Corretos + "/"
                + numCT1 + ")");
        System.out.println("CT 2 = " + notaCT2 + " (" + numCT2Corretos + "/"
                + numCT2 + ")");
        System.out.println("CT 3 = " + notaCT3 + " (" + numCT3Corretos + "/"
                + numCT3 + ")");
        System.out.println("CT 4 = " + notaCT4 + " (" + numCT4Corretos + "/"
                + numCT4 + ")");
        System.out.println("CT 5 = " + notaCT5 + " (" + numCT5Corretos + "/"
                + numCT5 + ")");

        System.out.println("==================================");
    }

    private static int analisaT1aT4(String tipoTeste, String executavel, File entrada, File saida,
            File pastaDeTrabalho, boolean gerarGabarito) throws IOException {
        File fSaida1 = new File(pastaDeTrabalho, "saida_"+tipoTeste);
        fSaida1.mkdirs();
        File[] casosDeTeste = entrada.listFiles();
        int numCT = casosDeTeste.length;
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

        int ret = 0;

        if (gerarGabarito) {
            ret = numCT;
            System.out.println("Gerando gabarito");
            FileUtils.copyDirectory(fSaida1, saida);
            System.out.println("Gabarito gerado");
        } else {
            System.out.println("Verificando resultado");
            ret = compararPastas(saida, fSaida1);
            System.out.println("Resultado verificado");
        }

        return ret;
    }

    private static int analisaT5(String executavel, String gcc, File entrada,
            File entradaExecucao, File saidaExecucaoCasosDeTeste, File pastaDeTrabalho,
            boolean gerarGabarito) throws IOException, InterruptedException {
        System.out.println("   Gerando codigo...");
        File fSaida1 = new File(pastaDeTrabalho, "saida_t5_GeradorDeCodigo");
        File fSaida2 = new File(pastaDeTrabalho, "saida_t5_Execucao");
        fSaida1.mkdirs();
        fSaida2.mkdirs();
        File[] casosDeTeste = entrada.listFiles();
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
            String cmd = gcc + " " + fGerado.getAbsolutePath() + " -o " + fCompilado.getAbsolutePath();
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
                System.out.println("   Erro ao executar comando [" + cmd + "]: " + e.getMessage());
            }

        }

        int ret = 0;
        if (gerarGabarito) {
            ret = numCT5;
            FileUtils.copyDirectory(fSaida2, saidaExecucaoCasosDeTeste);
        } else {
            ret = compararPastas(saidaExecucaoCasosDeTeste, fSaida2);
        }

        return ret;
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
            if (!fAluno.exists()) {
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
