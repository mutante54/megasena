/**
 * 
 */
package br.com.myapp.core;

import java.util.Comparator;

import br.com.myapp.core.vo.Dezena;

/**
 * @author JEFFERSON
 * 
 */
public class DezenaNumeroComparator implements Comparator<Dezena> {

	public int compare(Dezena dez1, Dezena dez2) {
		if (dez1.getNumero() < dez2.getNumero()) {
			return -1;
		} else {
			return 1;
		}
	}
}
