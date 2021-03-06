package cz.geokuk.plugins.cesty.data;

import java.io.File;
import java.util.*;

import cz.geokuk.core.coordinates.Mou;
import cz.geokuk.core.coordinates.Mouable;
import cz.geokuk.plugins.cesty.data.Cesta.SearchResult;
import cz.geokuk.plugins.kesoid.Wpt;

/**
 * Dokument GPX se vším, co budeme podporovat
 *
 * @author Martin Veverka
 *
 */
public class Doc implements Iterable<Cesta> {

	/** Soubor ze kterého byla cesta načtena a do kterého se bude ukládat, null pokud ještě neuloženo */
	private File file;
	private boolean changed;

	private final List<Cesta> cesty = new ArrayList<>();

	/**
	 * Propojí dvě cesty do jiné nové cesty. Původní dvě cesty jsou odteď prázdné, ale další atributy jako jméo zůstanou netčeny.
	 *
	 * @param c1
	 * @param c2
	 * @return
	 */
	static Cesta propojCestyDoJine(final Cesta c1, final Cesta c2) {
		assert!c2.isEmpty();
		assert!c1.isEmpty();
		final Cesta cesta = Cesta.create();
		final Bod vaznyBod = c1.getCil();
		if (vaznyBod.getMou().equals(c2.getStart().getMou())) {
			final Usek prvniUsekDruheCesty = c2.getStart().getUvpred();
			if (prvniUsekDruheCesty != null) { // jen když není jednobodová
				vaznyBod.setUvpred(prvniUsekDruheCesty);
				prvniUsekDruheCesty.setBvzad(vaznyBod);
				cesta.setCil(c2.getCil());
			} else {
				cesta.setCil(c1.getCil());
			}
			cesta.setStart(c1.getStart());
		} else { // body nejsou na sobě, spojují se úsekem
			final Usek usek = cesta.createUsek();
			vaznyBod.setUvpred(usek);
			usek.setBvpred(c2.getStart());
			c2.getStart().setUvzad(usek);
			usek.setBvzad(vaznyBod);
			cesta.setStart(c1.getStart());
			cesta.setCil(c2.getCil());
		}
		c1.clear();
		c2.clear();
		cesta.napojBouskyNaTutoCestu();
		assert cesta.getStart() != null;
		assert cesta.getStart() != null;
		return cesta;
	}

	/**
	 * Vyhledá bod s přesně zadanými souřadnicemi
	 *
	 * @param mouable
	 * @return
	 */
	public Bod findBod(final Mouable mouable) {
		if (mouable == null) {
			return null;
		}
		if (mouable instanceof Bod) {
			return (Bod) mouable;
		}
		for (final Bod bod : getBody()) {
			if (mouable.getMou().equals(bod.getMou())) {
				return bod;
			}
		}
		return null;

	}

	public Cesta findNejblizsiCesta(final Mou mou) {
		final Bousek0 bousek = locateNejblizsiDoKvadratuVzdalenosi(mou, Long.MAX_VALUE, null, false);
		return bousek == null ? null : bousek.cesta;
	}

	/**
	 * Vrací seznam všech bodů cesty.
	 *
	 * @return
	 */
	public Iterable<Bod> getBody() {
		return new MultiIterable<Bod, Cesta>(cesty) {
			@Override
			protected Iterable<Bod> prepareIterable(final Cesta m) {
				return m.getBody();
			}
		};
	}

	public Iterable<Cesta> getCesty() {
		return cesty;
	}

	public File getFile() {
		return file;
	}

	public int getPocetCest() {
		return cesty.size();
	}

	public int getPocetJednobodovychCest() {
		int citac = 0;
		for (final Cesta cesta : getCesty()) {
			if (cesta.isJednobodova()) {
				citac++;
			}
		}
		return citac;
	}

	public int getPocetPrazdnychCest() {
		int citac = 0;
		for (final Cesta cesta : getCesty()) {
			if (cesta.isEmpty()) {
				citac++;
			}
		}
		return citac;
	}

	public int getPocetWaypointu() {
		int suma = 0;
		for (final Cesta cesta : cesty) {
			suma += cesta.getPocetWaypointu();
		}
		return suma;
	}

	public Cesta getPrvniCesta() {
		if (cesty.size() == 0) {
			return null;
		}
		return cesty.get(0);
	}

	public Iterable<Wpt> getWpts() {
		return new MultiIterable<Wpt, Cesta>(cesty) {
			@Override
			protected Iterable<Wpt> prepareIterable(final Cesta m) {
				return m.getWpts();
			}
		};
	}

	public boolean hasWpt(final Wpt wpt) {
		for (final Cesta cesta : cesty) {
			if (cesta.hasWpt(wpt)) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmpty() {
		return cesty.isEmpty();
	}

	public boolean isChanged() {
		return changed;
	}

	@Override
	public Iterator<Cesta> iterator() {
		return cesty.iterator();
	}

	public void kontrolaKonzistence() {
		boolean assertsEnabled = false;
		assert assertsEnabled = true;
		if (!assertsEnabled) {
			return;
		}

		for (final Cesta cesta : getCesty()) {
			kon(cesta.getDoc() == this);
			cesta.kontrolaKonzistence();
		}
	}

	public void kontrolaZeJeTady(final Cesta cesta) {
		kon(cesta.getDoc() == this);
		kon(cesty.indexOf(cesta) >= 0);
	}

	public Bousek0 locateNejblizsiDoKvadratuVzdalenosi(final Mou mou, final long kvadratMaximalniVzdalenosti, final Cesta preferovanaCesta, final boolean aDatPrednostBoduPredUsekem) {
		SearchResult srmin = new SearchResult();
		for (final Cesta cesta : cesty) {
			final SearchResult sr = cesta.locateNejblizsiDoKvadratuVzdalenosi(mou, kvadratMaximalniVzdalenosti, aDatPrednostBoduPredUsekem);
			if (sr.bousek != null) { // když je z preferované cesty není co řešit
				if (sr.bousek.getCesta() == preferovanaCesta) {
					return sr.bousek;
				}
			}
			if (aDatPrednostBoduPredUsekem && srmin.bousek instanceof Usek && sr.bousek instanceof Bod) {
				srmin = sr; // tentokrát přišel bod, tak mu dáme přednost
				continue;
			}
			if (aDatPrednostBoduPredUsekem && srmin.bousek instanceof Bod && sr.bousek instanceof Usek) {
				continue; // přišel úsek, přitom jsme měli už bod, nebudeme se tím zabývat
			}
			// je to buď bodbod nebo usek-usek nebo nezáleží na typu, budeme porovnávat veliksoti
			if (sr.kvadradVzdalenosti < srmin.kvadradVzdalenosti) {
				srmin = sr;
			}
		}
		return srmin.bousek;
	}

	public void resetChanged() {
		changed = false;
	}

	public void setFile(final File file) {
		this.file = file;
	}

	public void xadd(final Cesta cesta) {
		cesty.add(cesta);
		cesta.setDoc(this);
		setChanged();
	}

	void removex(final Cesta cesta) {
		cesty.remove(cesta);
		setChanged();
	}

	void setChanged() {
		changed = true;
	}

	private void kon(final boolean podm) {
		if (!podm) {
			throw new RuntimeException("Selhala kontrola konzistence cesty");
		}
	}

}
