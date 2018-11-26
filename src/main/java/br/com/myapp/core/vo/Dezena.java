/**
 * 
 */
package br.com.myapp.core.vo;

/**
 * @author JEFFERSON
 * 
 */
public class Dezena {

	private int numero;

	private int qtdNoPadrao;
	private boolean isAusente;

	/**
	 * 
	 */
	public Dezena() {
		// TODO Auto-generated constructor stub
	}

	public Dezena(int numero) {
		this.numero = numero;
	}

	public Dezena(int numero, int qtdNoPadrao) {
		this.numero = numero;
		this.qtdNoPadrao = qtdNoPadrao;
	}

	public Dezena(int numero, int qtdNoPadrao, boolean isAusente) {
		this.numero = numero;
		this.qtdNoPadrao = qtdNoPadrao;
		this.isAusente = isAusente;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getQtdNoPadrao() {
		return qtdNoPadrao;
	}

	public void setQtdNoPadrao(int qtdOcorrencias) {
		this.qtdNoPadrao = qtdOcorrencias;
	}

	public boolean isAusente() {
		return isAusente;
	}

	public void setAusente(boolean isAusente) {
		this.isAusente = isAusente;
	}

	@Override
	public boolean equals(Object o) {
		Dezena dez = (Dezena) o;
		return this.numero == dez.getNumero();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Integer.toString(numero);
	}

}
