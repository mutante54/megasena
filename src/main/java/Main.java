import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.myapp.core.Engine;
import br.com.myapp.core.vo.Dezena;
import br.com.myapp.core.vo.Jogo;

public class Main {

	private static Engine engine;

	private static StringBuilder sbReport = new StringBuilder();

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		engine = new Engine();

		try {
			//engine.executar("2/3", 10, true, true);
			// ajuste realizado em 04/06/2018
			engine.executar("2/2", 5, true, true);

			System.out.println("Histórico (10 últimos): ");
			sbReport.append("Histórico (10 últimos): ");
			for (int i = engine.getJogos().size() - 1; i >= engine.getJogos().size() - 10; i--) {
				System.out.println(engine.getJogos().get(i));
				sbReport.append("\n");
				sbReport.append(engine.getJogos().get(i));
			}

			System.out.println("\n --------------------------- ");
			sbReport.append("\n --------------------------- ");
			System.out.println("Dezenas que mais OCORREM: ");
			sbReport.append("\n Dezenas que mais OCORREM: \n");
			for (Dezena dezena : engine.getDezenasOcorrencias()) {

				if (dezena.getNumero() == 0) {
					continue;
				}

				System.out.print(dezena.getNumero() + "[" + dezena.getQtdNoPadrao() + "]" + " ");
				sbReport.append(dezena.getNumero() + "[" + dezena.getQtdNoPadrao() + "]" + " ");

				if (dezena.getNumero() != 0 && dezena.getNumero() % 6 == 0) {
					System.out.println("\n");
					sbReport.append("\n");
				}
			}

			System.out.println("\n --------------------------- ");
			sbReport.append("\n --------------------------- ");
			System.out.println("Dezenas que mais REPETEM: ");
			sbReport.append("\n Dezenas que mais REPETEM: \n");
			for (Dezena dezena : engine.getDezenasRepeticoes()) {

				if (dezena.getNumero() == 0) {
					continue;
				}

				System.out.print(dezena.getNumero() + "[" + dezena.getQtdNoPadrao() + "]" + " ");
				sbReport.append(dezena.getNumero() + "[" + dezena.getQtdNoPadrao() + "]" + " ");

				if (dezena.getNumero() != 0 && dezena.getNumero() % 6 == 0) {
					System.out.println("\n");
					sbReport.append("\n");
				}
			}

			System.out.println("\n --------------------------- ");
			sbReport.append("\n --------------------------- ");
			System.out.println("Dezenas AUSENTES: ");
			sbReport.append("\n Dezenas AUSENTES: \n");
			for (int i = 0; i < engine.getArrayAusencias().length; i++) {

				if (i == 0) {
					continue;
				}

				System.out.print(i + "[" + engine.getArrayAusencias()[i] + "]" + " ");
				sbReport.append(i + "[" + engine.getArrayAusencias()[i] + "]" + " ");

				if (i != 0 && i % 6 == 0) {
					System.out.println("\n");
					sbReport.append("\n");
				}
			}

			System.out.println("\n --------------------------- ");
			sbReport.append("\n --------------------------- ");
			System.out.println("Dezenas sugeridas: ");
			sbReport.append("\n Dezenas sugeridas: \n");
			for (Dezena dezena : engine.getDezenasSorteadas()) {
				System.out.print(dezena.getNumero() + "-");
				sbReport.append(dezena.getNumero() + "-");
			}

			System.out.println("\n --------------------------- ");
			sbReport.append("\n --------------------------- ");
			System.out.println("Jogos sugeridos: ");
			sbReport.append("\n Jogos sugeridos: \n");
			for (Jogo jogo : engine.getJogoSorteados()) {

				System.out.print(jogo.getDez1() + "-" + jogo.getDez2() + "-" + jogo.getDez3() + "-" + jogo.getDez4() + "-" + jogo.getDez5() + "-" + jogo.getDez6());
				sbReport.append(jogo.getDez1() + "-" + jogo.getDez2() + "-" + jogo.getDez3() + "-" + jogo.getDez4() + "-" + jogo.getDez5() + "-" + jogo.getDez6());

				System.out.println("\n");
				sbReport.append("\n");

			}

			generateReport();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void generateReport() throws FileNotFoundException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		PrintWriter pw = new PrintWriter(new File("C:/Users/G0055135/Desktop/my things/mega/relatorio_" + sdf.format(new Date()) + ".txt"));
		pw.write(sbReport.toString());
		pw.close();
		System.out.println("done!");
	}

}
