/**
 * 
 */
package  br.com.myapp.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import  br.com.myapp.core.vo.Dezena;
import  br.com.myapp.core.vo.Jogo;

/**
 * @author JEFFERSON
 * 
 */
public class Engine {

	private List<Jogo> jogos; // lista de jogos que foram mapeados
	private List<Jogo> jogoSorteados; // lista de jogos que foram sorteados
	private List<Dezena> dezenasSorteadas;
	private int[] arrayOcorrencias; // ocorrencias
	private int[] arrayRepeticoes; // repeticoes
	private Boolean[] arrayAusencias; // ausencias (sim ou n�o)

	private int qtdCiclosPadraoRepeticoes = 3; // quantidade de ciclos
	private int qtdJogosPadraoRepeticoes = 2;
	// para considerar uma repeti��o
	public int qtdJogosPadraoAusencias = 10; // quantidade de jogos para que uma dezena seja considerada ausente
	// padr�o do arquivo de configura��o
	public String valorBufferJogos;

	private boolean isOrdemCrescente;
	private boolean isOrdemCrescenteResultado;

	public Engine() {
	}
	/*
	 * GETTERS AND SETTERS
	 */

	public List<Jogo> getJogos() {
		return jogos;
	}
	public List<Dezena> getDezenasSorteadas() {
		return dezenasSorteadas;
	}
	public List<Jogo> getJogoSorteados() {
		return jogoSorteados;
	}

	// M�TOD DE NEG�CIO ////////////////////////////
	// execu��o geral do programa
	// retorna 1 caso a opera��o de leitura tenha sido realizada com sucesso
	public void executar(String padraoRepeticao, int padraoAusencia, boolean isOrdemCrescenteIn, boolean isOrdemCrescenteResult) throws Exception {
		String[] padRep = padraoRepeticao.trim().split("\\/");

		qtdJogosPadraoRepeticoes = Integer.parseInt(padRep[0]);
		qtdCiclosPadraoRepeticoes = Integer.parseInt(padRep[1]);

		qtdJogosPadraoAusencias = padraoAusencia;

		isOrdemCrescente = isOrdemCrescenteIn;
		isOrdemCrescenteResultado = isOrdemCrescenteResult;

		try {

			if (lerArquivoCSV()) {
				// gera as dezenas (dezenas)
				gerarJogos();
				// sorteia os jogos
				sortearJogos();
			} else {
				throw new Exception("Erro inesperado ao processar arquivo de dados");
			}
		} catch (FileNotFoundException er) { // caso de base de dados n�o
			throw er;
		}

	}
	// recebe como par�metro o endere�o do arquivo a ser carregado
	// se n�o receber o endere�o dinamicamente, usa o endere�o padr�o
	// se ler o arq., retorna true, sen�o, retorna false.
	@SuppressWarnings("unused")
	public boolean lerArquivo() throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {

		// REALIZA A LEITURA DA BASE DE DADOS (via GET http)
		ByteArrayOutputStream byteArrayOutput = FileUploader.downloadFile(isOrdemCrescente);

		// tamanho do arquivo em bytes
		int bytesInputFileSize = byteArrayOutput.size();

		BufferedReader bufferArquivo = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(byteArrayOutput.toByteArray())));

		Jogo jogo; // objeto VO de jogo

		jogos = new ArrayList<Jogo>();

		String valor = "";

		String[] arrayColunaTr = new String[8]; // 8 colunas de uma determinada linha

		// BufferedInputStream with byte array reads (fonte: http://nadeausoftware.com/articles/2008/02/java_tip_how_read_files_quickly#BufferedInputStreamwithbytearrayreads)
		char[] byteArray = new char[bytesInputFileSize];
		long checkSum = 0L;
		int nRead;
		while ((nRead = bufferArquivo.read(byteArray, 0, bytesInputFileSize)) != -1)
			for (int i = 0; i < nRead; i++)
				checkSum += byteArray[i];

		valor = String.valueOf(byteArray);

		// pr�ximo passo: tabela de dados � separada do contexto
		// 1 - separar a tabela (<table>) e cabe�alho
		String auxTable = valor.substring(valor.indexOf("<table") + 6, valor.indexOf("</table>")); // isola a tabela
		auxTable = auxTable.substring(auxTable.indexOf("</tr>") + 5, auxTable.lastIndexOf("</tr>") + 5); // do primeiro <tr> encontrado (cabe�alho) at� o ultimo (ultima linha da tabela)
		// 2 - separar, linha a linha, e ir confeccionando as colunas, at� mapear todos os jogos.
		String linhaTable = ""; // armazena toda a linha da table
		String colunaTable = ""; // armazena toda a coluna de determinada linha
		int i = 0; // indice do loop de linha
		int j = 0; // indice do loop de coluna
		int indiceTr1 = 0; // inicio do indice de tr
		int indiceTr2 = 0; // fim do indice de tr
		int indiceTd1 = 0; // inicio do indice de td, filho de tr
		int indiceTd2 = 0; // fim do indice de td, filho de tr

		while (auxTable.length() > 0 && auxTable.indexOf("<tr") != -1) // verifica se texto ainda possui conteudo v�lido
		{
			jogo = new Jogo(); // instancia novo jogo
			int indiceColunaTr = 0; // indice da coluna atual desta linha

			// existem as linhas com cores de fundo diferenciadas. A inten��o � diferenci�-las e assim obter os resultados esperados
			// Para tanto, o valor inicial do texto � retirado, para avaliar qual o tipo de tag
			// N�o haver� mais de 1 ocorr�ncia do bgcolor. Se houver, deve ser considerada.
			String aux = auxTable.substring(auxTable.indexOf("<tr"), 20); // a partir da tag <tr>

			if (aux.indexOf("<tr bgcolor=") != -1) // encontrou
			{
				indiceTr1 = auxTable.indexOf("<tr bgcolor=") + 20; // inicio da linha
			} else // nada encontrado
			{
				indiceTr1 = auxTable.indexOf("<tr>") + 4; // pega apenas o inicio da linha
			}

			indiceTr2 = auxTable.indexOf("</tr>"); // fim da linha

			linhaTable = auxTable.substring(indiceTr1, indiceTr2); // pega todo o conteudo da linha

			i = indiceTr2 + 5; // inicia no pr�ximo <tr>

			auxTable = auxTable.substring(i, auxTable.length()); // descarta linha

			while (linhaTable.length() > 0 && linhaTable.indexOf("<td>") != -1) // verifica se texto ainda possui conteudo v�lido
			{
				indiceTd1 = linhaTable.indexOf("<td>") + 4; // inicio do conteudo da tag
				indiceTd2 = linhaTable.indexOf("</td>"); // fim do conteudo da tag

				colunaTable = linhaTable.substring(indiceTd1, indiceTd2); // conteudo

				j = indiceTd2 + 5; // inicia da pr�xima tag <td>

				linhaTable = linhaTable.substring(j, linhaTable.length()); // descarta coluna
				arrayColunaTr[indiceColunaTr] = colunaTable;

				indiceColunaTr++; // atualiza indice para pr�xima coluna

				// se todos os valores ja foram mapeados, n�o precisa continuar
				if (indiceColunaTr >= 8) {
					linhaTable = "";
				} // limpa

			}
			jogo.setConcurso(Long.valueOf(arrayColunaTr[0]));
			jogo.setData(arrayColunaTr[1]);
			jogo.setDez1(new Dezena(Integer.parseInt(arrayColunaTr[2])));
			jogo.setDez2(new Dezena(Integer.parseInt(arrayColunaTr[3])));
			jogo.setDez3(new Dezena(Integer.parseInt(arrayColunaTr[4])));
			jogo.setDez4(new Dezena(Integer.parseInt(arrayColunaTr[5])));
			jogo.setDez5(new Dezena(Integer.parseInt(arrayColunaTr[6])));
			jogo.setDez6(new Dezena(Integer.parseInt(arrayColunaTr[7])));

			jogos.add(jogo);

		}

		// define pr�ximo concurso (concurso atual + 1)
		// int numProxConcurso = Integer.parseInt(jogos.get(jogos.size() - 1).getConcurso()) + 1;

		// aplicar regras
		calcularOcorrenciasRepeticoes();
		calcularAusencias();

		return true;
	}

	private boolean lerArquivoCSV() throws IOException {

		// REALIZA A LEITURA DA BASE DE DADOS (via GET http)
		//InputStream is = FileUploader.downloadFileCSV(isOrdemCrescente);
		InputStream is = FileUploader.loadLocalFile();

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = "";
		String cvsSplitBy = ";";

		Jogo jogo;

		jogos = new ArrayList<Jogo>();

		try {

			while ((line = br.readLine()) != null) {

				jogo = new Jogo(); // instancia novo jogo

				// split no separador (;)
				String[] jogoStrArray = line.split(cvsSplitBy);

				jogo.setConcurso(Long.valueOf(jogoStrArray[0]));
				jogo.setData(jogoStrArray[1]);
				jogo.setDez1(new Dezena(Integer.parseInt(jogoStrArray[2])));
				jogo.setDez2(new Dezena(Integer.parseInt(jogoStrArray[3])));
				jogo.setDez3(new Dezena(Integer.parseInt(jogoStrArray[4])));
				jogo.setDez4(new Dezena(Integer.parseInt(jogoStrArray[5])));
				jogo.setDez5(new Dezena(Integer.parseInt(jogoStrArray[6])));
				jogo.setDez6(new Dezena(Integer.parseInt(jogoStrArray[7])));

				this.jogos.add(jogo);
			}

			// aplicar regras
			calcularOcorrenciasRepeticoes();
			calcularAusencias();

			return true;

		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					return false;
				}
			}
		}
	}

	public void calcularOcorrenciasRepeticoes() {
		arrayOcorrencias = new int[61]; // 1 a 60 (0 = -1)
		arrayRepeticoes = new int[61]; // 1 a 60 (0 = -1)

		arrayOcorrencias[0] = -1;
		arrayRepeticoes[0] = -1;

		// zera as arrays
		for (int h = 1; h < 61; h++) {
			arrayOcorrencias[h] = 0;
			arrayRepeticoes[h] = 0;
		}

		int dezRepete; // repeti��es de dezena espec�fica
		int numJogos; // n�mero de jogos por dezena (cada ciclo possu� 3 jogos)

		// para cada dezena, de 1 a 60, calcular a quantidade total de ocorr�ncias e repeti��es.
		// de acordo com as regras do jogo, uma dezena s� ocorre uma vez por concurso
		// basta realizar loop e verificar concurso a concurso se a dezena ocorreu (e armazenar as inf. na respec. array)
		for (int i = 1; i <= 60; i++) // dezenas
		{
			numJogos = 0;
			dezRepete = 0;

			for (int j = 0; j < jogos.size(); j++) // jogos
			{
				// procura por repeti??es da dezena no concurso para tanto, o ciclo de jogos deve ser menor que 3 (inicia-se em 0)
				// ao entrar no loop, teste se o ciclo de jogos encerrou (|0-1-2|3-4-5|6-7-8|)
				if (numJogos >= qtdCiclosPadraoRepeticoes || j == jogos.size() - 1) { // se for >= 3, ciclo encerrou (estaria entrando no 4? ciclo) ou ciclo n?o encerrou mas arraylist sim

					if (dezRepete >= qtdJogosPadraoRepeticoes) // "n" ou mais ocorr�ncias em "n" ciclos de jogos
					{
						arrayRepeticoes[i]++; // dezena i teve 1 repeticao no ciclo atual de "n" jogos
					}

					dezRepete = 0; // ao terminar o ciclo, tudo incia-se do 0
					numJogos = 0; // zera o numero de jogos
				}

				// procura por ocorr?ncia da dezena no concurso
				if (i == jogos.get(j).getDez1().getNumero() || i == jogos.get(j).getDez2().getNumero() || i == jogos.get(j).getDez3().getNumero() || i == jogos.get(j).getDez4().getNumero() || i == jogos.get(j).getDez5().getNumero() || i == jogos.get(j).getDez6().getNumero()) {
					arrayOcorrencias[i]++; // i = dezena
					dezRepete++; // dezena (i) ocorreu (2 ou 3 ocorr?ncias em 3 jogos indica 1 repeticao)
				}

				numJogos++; // incrementa (mais um jogo do ciclo)
			}

		}
	}

	public void calcularAusencias() {
		arrayAusencias = new Boolean[61]; // 1 a 60 (0 = -1)
		arrayAusencias[0] = null;

		Boolean dezAusente;
		// para uma dezena ser considerada ausente, basta que a mesma n�o tenha ocorrido nos �ltimos 10 concursos
		for (int i = 1; i <= 60; i++) // dezenas
		{
			dezAusente = true; // dezena inicia como ausente

			for (int j = jogos.size() - qtdJogosPadraoAusencias; j < jogos.size(); j++) // volta 10 concursos at� o ultimo
			{
				if (i == jogos.get(j).getDez1().getNumero() || i == jogos.get(j).getDez2().getNumero() || i == jogos.get(j).getDez3().getNumero() || i == jogos.get(j).getDez4().getNumero() || i == jogos.get(j).getDez5().getNumero() || i == jogos.get(j).getDez6().getNumero()) {
					dezAusente = false;
				}
			}

			if (dezAusente) {
				arrayAusencias[i] = true;
			} // EST� ausente: dezena n�o ocorreu nos �ltimos dez jogos
			else {
				arrayAusencias[i] = false;
			} // N�O esta ausente: dezena ocorreu nos �ltimos dez jogos
		}
	}

	// recebe como par�metro as 3 arrays de dados, sendo que a de ocorrencias e
	// de repeti��es estejam ordenadas [as dezenas, e n�o os valores]
	@SuppressWarnings("unused")
	public void gerarJogos() {

		List<Dezena> ocorrencias = new ArrayList<Dezena>();
		List<Dezena> repeticoes = new ArrayList<Dezena>();
		dezenasSorteadas = new ArrayList<Dezena>();
		// m�ximo de dezenas � 60
		int numDezenas = 1; // numero de dezenas selecionadas
		ocorrencias = ordenarVetor(arrayOcorrencias, new DezenaQtdPadraoComparator(), false); // ordena as dezenas em ordem decrescente, por ocorrencia
		repeticoes = ordenarVetor(arrayRepeticoes, new DezenaQtdPadraoComparator(), false); // ordena as dezenas em ordem decrescente, por repeticao

		// /////////////////////////////////////////////////////////////////////////////////////////////////////
		// Percorre a array de ocorr�ncias
		for (int indice = 0; indice < ocorrencias.size(); indice++) {
			if (indice <= 30) {
				// DEZENAS QUE MAIS OCORREM E QUE EST�O AUSENTES
				if (arrayAusencias[ocorrencias.get(indice).getNumero()] == true) {
					dezenasSorteadas.add(ocorrencias.get(indice));
					numDezenas++;
				} else // DEZENAS QUE MAIS OCORREM E QUE N�O EST�O AUSENTES
				{
					// VERIFICAR SE A DEZENA ESTA ENTRE AS 20 QUE MAIS REPETEM
					// PERCORRE A ARRAY DE REPETI�OES
					for (int j = 1; j <= 20; j++) {
						if (ocorrencias.get(indice).getNumero() == repeticoes.get(j).getNumero()) // dezena repete
						{
							dezenasSorteadas.add(ocorrencias.get(indice));
							numDezenas++;
						}
					}
				}
			} else // dezenas intermediarias (somente as que estao entre as 10 q
			// mais repetem)
			if (indice > 30 && indice <= 50) {
				// NESTE CASO A DEZENA DEVE ESTAR AUSENTE
				if (arrayAusencias[ocorrencias.get(indice).getNumero()] == true) {
					dezenasSorteadas.add(ocorrencias.get(indice));
					numDezenas++;
				} else // N�O-AUSENTES -> (NESTE CASO, COMO A MESMA N�O OCORRE COM FREQU�NCIA, DEVE TER ALTA TAXA DE REPETI��O)
				{
					// VERIFICAR SE A DEZENA ESTA ENTRE AS 10 QUE MAIS REPETEM. PERCORRE A ARRAY DE REPETI�OES
					for (int j = 1; j <= 10; j++) {
						if (ocorrencias.get(indice).getNumero() == repeticoes.get(j).getNumero()) // dezena
						// repete
						{
							dezenasSorteadas.add(ocorrencias.get(indice));
							numDezenas++;
						}
					}
				}
			} else // 10 dezenas q menos ocorrem
			if (indice > 50) {
				// neste caso, ignora-se as repeti��es e pega-se somente as
				// ausentes
				if (arrayAusencias[ocorrencias.get(indice).getNumero()] == true) // se (na array de ausencias, no indice que corresponde � dezena mais colocad em
				// ocorr�ncias, seu valor for true)
				{
					dezenasSorteadas.add(ocorrencias.get(indice));
					numDezenas++;
				}
			}
		}

		// ordenando a lista de numeros gerados (crescente)
		Collections.sort(dezenasSorteadas, new DezenaNumeroComparator());
		// se necess�rio, inverter a lista para obter ordem decrescente
		if (!isOrdemCrescenteResultado) {
			Collections.reverse(dezenasSorteadas);
		}

	}
	// m�todo que sorteia 3 jogos, a partir das dezenas selecionadas
	public void sortearJogos() {

		// TODO sobrecarregar m�todo equals na classe Dezena

		int quantidadeJogos = dezenasSorteadas.size() / 6; // quantidade m�xima de jogos a serem gerados

		Random random = new Random();
		jogoSorteados = new ArrayList<Jogo>();
		List<Dezena> dezenasInseridasNoJogo;
		Jogo jogo;

		for (int i = 1; i <= quantidadeJogos; i++) {

			int qtdDezenasInseridasNoJogo = 0;
			dezenasInseridasNoJogo = new ArrayList<Dezena>();
			Dezena dezenaSorteada = null;

			while (qtdDezenasInseridasNoJogo < 6) {
				dezenaSorteada = dezenasSorteadas.get(random.nextInt(dezenasSorteadas.size() - 1));
				if (!dezenasInseridasNoJogo.contains(dezenaSorteada)) {
					dezenasInseridasNoJogo.add(dezenaSorteada);
					qtdDezenasInseridasNoJogo++;
				}
			}

			if (isOrdemCrescenteResultado) {
				Collections.sort(dezenasInseridasNoJogo, new DezenaNumeroComparator());
			}

			jogo = new Jogo(null, null, dezenasInseridasNoJogo.get(0), dezenasInseridasNoJogo.get(1), dezenasInseridasNoJogo.get(2), dezenasInseridasNoJogo.get(3), dezenasInseridasNoJogo.get(4), dezenasInseridasNoJogo.get(5));
			jogoSorteados.add(jogo);
		}
	}

	// m?todo que mapeia um determinado vetor, ordenando-o conforme as configura??es.
	// Passa-se um vetor como par?metro (com os numeros [int] das dezenas) e ele retorna uma lista com as dezenas ordenadas.
	// O padrao de ordenacaoo tb � passado como parametro, indicando se a lista dever� ser ordenado crescente ou decrescente
	public List<Dezena> ordenarVetor(int[] vetor, Comparator<Dezena> comparator, boolean isOrdemCrescente) {

		List<Dezena> dezenasOrdenadas = new ArrayList<Dezena>();

		for (int i = 1; i < vetor.length; i++) {
			dezenasOrdenadas.add(new Dezena(i, vetor[i])); // indice � o n�mero da dezena / valor � qtdNoPadrao
		}

		Collections.sort(dezenasOrdenadas, comparator); // ordenar pela qtd de items em um determinado padr�o, em ordem crescente

		if (!isOrdemCrescente) {
			Collections.reverse(dezenasOrdenadas);
		}

		return dezenasOrdenadas;
	}

	public List<Dezena> getDezenasRepeticoes() {
		List<Dezena> dezenas = new ArrayList<Dezena>();
		for (int i = 0; i < arrayRepeticoes.length; i++) {
			dezenas.add(new Dezena(i, arrayRepeticoes[i]));
		}

		return dezenas;
	}

	public List<Dezena> getDezenasOcorrencias() {
		List<Dezena> dezenas = new ArrayList<Dezena>();
		for (int i = 0; i < arrayOcorrencias.length; i++) {
			dezenas.add(new Dezena(i, arrayOcorrencias[i]));
		}

		return dezenas;
	}

	public Boolean[] getArrayAusencias() {
		return arrayAusencias;
	}

	public void setArrayAusencias(Boolean[] arrayAusencias) {
		this.arrayAusencias = arrayAusencias;
	}
}