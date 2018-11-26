/**
 * 
 */
package br.com.myapp.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

/**
 * @author JEFFERSON
 * 
 */
public class FileUploader {

	/**
	 * 
	 */
	public FileUploader() {
		// TODO Auto-generated constructor stub
	}

	public static ByteArrayOutputStream downloadFile(boolean isOrdemCrescente) throws FileNotFoundException {
		String url = "";

		if (isOrdemCrescente) {
			// url = "http://www1.caixa.gov.br/loterias/_arquivos/loterias/D_mgsasc.zip";
			url = "https://drive.google.com/uc?export=download&id=0BxQW9zgAgRp8NkY1cC1RSThFWTQ";
		} else {
			url = "http://www1.caixa.gov.br/loterias/_arquivos/loterias/D_megase.zip";
		}

		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);

			InputStream inputStreamFileZipped = response.getEntity().getContent();

			return unZip(inputStreamFileZipped);

		} catch (Exception e) {
			throw new FileNotFoundException("N�o foi poss�vel processar o arquivo de dados. Causa: " + e.getMessage());
		}

	}

	public static InputStream downloadFileCSV(boolean isOrdemCrescente) throws FileNotFoundException {
		String url = "";

		if (isOrdemCrescente) {
			url = "https://www.dropbox.com/s/2p1wfdjyk66pj7q/d_megasc.csv?dl=1";
		} else {
			throw new IllegalArgumentException("Arquivo n�o disponivel");
		}

		try {
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpGet httpGet = new HttpGet(url);
			HttpResponse response = httpClient.execute(httpGet);

			return response.getEntity().getContent();

		} catch (Exception e) {
			throw new FileNotFoundException("N�o foi poss�vel processar o arquivo de dados. Causa: " + e.getMessage());
		}

	}

	public static InputStream loadLocalFile() throws FileNotFoundException {
		File file = new File("C:/Users/G0055135/Desktop/my things/mega/d_megasc.csv");
		return new FileInputStream(file);
	}

	public static ByteArrayOutputStream unZip(InputStream inputStreamZipFile) throws Exception {

		byte[] buffer = new byte[1024];

		try {

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(inputStreamZipFile);
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			ByteArrayOutputStream byteArrayOutput = null;

			while (ze != null) {

				String fileName = ze.getName();

				if (fileName.equalsIgnoreCase("d_megasc.htm") || fileName.equalsIgnoreCase("d_megase.htm") || fileName.equalsIgnoreCase("D_MEGA.HTM")) {
					byteArrayOutput = new ByteArrayOutputStream();

					int len;
					while ((len = zis.read(buffer)) > 0) {
						byteArrayOutput.write(buffer, 0, len);
					}

					byteArrayOutput.close();

					break; // unico arquivo
				}

				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			return byteArrayOutput;

		} catch (IOException ex) {
			throw new Exception("Erro ao descompactar arquivo de dados", ex);
		}
	}
}
