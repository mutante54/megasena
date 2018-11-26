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
public class DezenaQtdPadraoComparator implements Comparator<Dezena> {

	public int compare(Dezena dez1, Dezena dez2) {
		if (dez1.getQtdNoPadrao() == dez2.getQtdNoPadrao()) {
			return 0;
		} else if (dez1.getQtdNoPadrao() < dez2.getQtdNoPadrao()) {
			return -1;
		} else {
			return 1;
		}
	}
}
