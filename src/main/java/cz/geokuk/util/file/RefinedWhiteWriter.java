package cz.geokuk.util.file;

import java.io.*;

/**
 * Třída je dekorační třídou writeru. Upravuje do použitelné podoby bílé znaky. Znak tabulátoru '\t' nahradí mezerou. Mezery na konci řádku odstraní. Výskyty \r, \n, \r\n nahradí oddělovačem konce řádku daným property line.separator. Pokud poslední řádek nekončí oddělovačem řádku, přidá oddělovač
 * řádku. Výstup má tedy bud nulovou délku nebo končí znaky nového řádku. Příklad:
 *
 * <pre>
 * PrintWriter pwr = new PrintWriter(new RefinedWhiteWriter(new FileWriter("test_RefineWhiteWriter.txt")));
 * </pre>
 *
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author unascribed
 * @version 1.0
 */

public class RefinedWhiteWriter extends Writer {

	private static final String NL = System.getProperty("line.separator");

	private final Writer iWriter;
	private int iCitacMezer = 0;
	private boolean iLfNeukoncuje = false;
	private boolean iPotrebaUkoncitRadek = false;

	public static void main(final String[] args) throws IOException {
		final PrintWriter pwr = new PrintWriter(new RefinedWhiteWriter(new FileWriter("test_RefineWhiteWriter.txt")));
		// PrintWriter pwr = new PrintWriter(new FileWriter("test_RefineWhiteWriter.txt"));
		pwr.print("Nenimezera\r");
		pwr.print("                         jedna dva  tri   ctyri  a nic\n");
		pwr.println();
		pwr.println("                                             ");
		pwr.print("jedna dva  tri   ctyri  pet     \r\n");
		pwr.println("                                             ");
		pwr.println("                                             ");
		pwr.println("                                             ");
		pwr.print("a poslenacek\n");
		pwr.print("                                             ");
		pwr.close();
	}

	public RefinedWhiteWriter(final Writer aWriter) {
		iWriter = aWriter;
	}

	@Override
	public void close() throws IOException {
		if (iPotrebaUkoncitRadek) {
			ukonciRadek(); // ukonči poslední řádek, pokud nebyl ukončen explicitně
		}
		iWriter.flush();
		iWriter.close(); // a uzavři stream
	}

	@Override
	public void flush() throws IOException {
		iWriter.flush(); // jednoduchýž přenos
	}

	@Override
	public void write(final char[] aZnaky, final int aOd, final int aPocet) throws IOException {
		final int xxdo = aOd + aPocet;
		for (int i = aOd; i < xxdo; i++) { // všechny znaky po jednom zpracovat
			zpracujZnak(aZnaky[i]);
		}
	}

	private void ukonciRadek() throws IOException {
		iWriter.write(NL); // řádkový separátor napsat
		iCitacMezer = 0; // a mezery předtím načítané zahodit
		iPotrebaUkoncitRadek = false; // řádek ukončen, takže zaniká potřeba ho ukončovat
	}

	private void zpracujZnak(char c) throws IOException {
		if (c == '\t') {
			c = ' '; // tabulátory tvrdě nahradit za mezeru a teprve potom pokračovat
		}
		if (c == ' ') {
			iLfNeukoncuje = false;
			iCitacMezer++; // započítat ji, ale nikam neposílat
		} else if (c == '\r') {
			ukonciRadek(); // ukončí vžy, ale následné LF pak ne
			iLfNeukoncuje = true;
		} else if (c == '\n') {
			if (!iLfNeukoncuje) {
				ukonciRadek();
			}
			iLfNeukoncuje = false;
		} else // je to nebílý znak
		{
			iLfNeukoncuje = false;
			iPotrebaUkoncitRadek = true; // pokud byl vypsán nějaký znak je na konci streamu potřeba ukončit řádek
			for (int i = 0; i < iCitacMezer; i++) {
				iWriter.write(' '); // dozapisovat všechny mezery
			}
			iCitacMezer = 0;
			iWriter.write(c); // a zapsat ten přišlý znak
		}
	}

}