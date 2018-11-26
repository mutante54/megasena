/**
 * 
 */
package  br.com.myapp.core.vo;

/**
 * @author JEFFERSON
 * 
 */
public class Jogo {

	private Long concurso; // n�mero do concurso
	private String data; // data

	// dezenas do jogo : s�o 6, armazenas em ordem crescente (ou nao).
	private Dezena dez1;
	private Dezena dez2;
	private Dezena dez3;
	private Dezena dez4;
	private Dezena dez5;
	private Dezena dez6;

	public Jogo() {
	}

	public Jogo(Long concurso) {
		this.concurso = concurso;
	}

	public Jogo(Long concurso, String data, Dezena dez1, Dezena dez2, Dezena dez3, Dezena dez4, Dezena dez5, Dezena dez6) {
		this.concurso = concurso;
		this.data = data;
		this.dez1 = dez1;
		this.dez2 = dez2;
		this.dez3 = dez3;
		this.dez4 = dez4;
		this.dez5 = dez5;
		this.dez6 = dez6;
	}

	public Long getConcurso() {
		return concurso;
	}

	public void setConcurso(Long concurso) {
		this.concurso = concurso;
	}

	public Dezena getDez1() {
		return dez1;
	}

	public void setDez1(Dezena dez1) {
		this.dez1 = dez1;
	}

	public Dezena getDez2() {
		return dez2;
	}

	public void setDez2(Dezena dez2) {
		this.dez2 = dez2;
	}

	public Dezena getDez3() {
		return dez3;
	}

	public void setDez3(Dezena dez3) {
		this.dez3 = dez3;
	}

	public Dezena getDez4() {
		return dez4;
	}

	public void setDez4(Dezena dez4) {
		this.dez4 = dez4;
	}

	public Dezena getDez5() {
		return dez5;
	}

	public void setDez5(Dezena dez5) {
		this.dez5 = dez5;
	}

	public Dezena getDez6() {
		return dez6;
	}

	public void setDez6(Dezena dez6) {
		this.dez6 = dez6;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concurso == null) ? 0 : concurso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogo other = (Jogo) obj;
		if (concurso == null) {
			if (other.concurso != null)
				return false;
		} else if (!concurso.equals(other.concurso))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.concurso + " | " + this.dez1 + "-" + this.dez2 + "-" + this.dez3 + "-" + this.dez4 + "-" + this.dez5 + "-" + this.dez6 + " | " + this.data;
	}

}
