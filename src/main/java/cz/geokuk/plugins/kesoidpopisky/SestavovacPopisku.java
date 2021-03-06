/**
 *
 */
package cz.geokuk.plugins.kesoidpopisky;

import java.util.*;

import cz.geokuk.plugins.kesoid.*;
import cz.geokuk.plugins.kesoid.data.EKesoidKind;

/**
 * Třída, která sestavuje popisky. Její stvoření může trvat, ale její provádění nikoli
 *
 * @author Martin Veverka
 *
 *
 *
 *         {wpt} - waypoint {typ1} - označení typu (jedno písmeno) {velikost} - označení velikosti {velikost1} - označení velikosti (jedno písmeno) {obtiznost} - označení obtížnosti {obtiznost1} - označení obtížnosti (jedno písmeno) {teren} - označení terenu {teren1} - označení terenu (jedno
 *         písmeno) {autor} - autor kešky {nazev} - název kešky {zalozeno} - datum založení {nbsp} - mezera (neprovádí se na ní zalomení), jen popisek {br} - odřádkování, jen popisek
 */
public class SestavovacPopisku {

	private static class Context {
		private final Wpt wpt;

		private boolean kesoidTypeResolved;

		public Kesoid kesoid;
		private Kes kes;

		Context(final Wpt wpt) {
			this.wpt = wpt;
		}

		/**
		 * @return
		 */
		public Kesoid getKesoid() {
			resolveKesoidType();
			return kesoid;
		}

		/**
		 * @return
		 */
		public boolean isKes() {
			resolveKesoidType();
			return kes != null;
		}

		/**
		 *
		 */
		private void resolveKesoidType() {
			if (kesoidTypeResolved) {
				return;
			}
			kesoid = wpt.getKesoid();
			if (kesoid instanceof Kes) {
				kes = (Kes) kesoid;
			}
			kesoidTypeResolved = true;
		}

	}

	private static interface Nahrazovac {
		void pridej(StringBuilder sb, Context ctx);

	}

	/**
	 * Oddělovač řádků. Lze ho najít
	 *
	 * @author Martin Veverka
	 *
	 */
	private static class NahrBr implements Nahrazovac {

		@Override
		public void pridej(final StringBuilder sb, final Context ctx) {}

	}

	private static class NahrKonstantni implements Nahrazovac {

		private final String konstatna;

		/**
		 *
		 */
		public NahrKonstantni(final String konstatna) {
			this.konstatna = konstatna;
		}

		@Override
		public void pridej(final StringBuilder sb, final Context ctx) {
			sb.append(konstatna);
		}

	}

	private static Map<String, Nahrazovac> sNahrazovace = new TreeMap<>();

	private static final NahrBr NAHRBR = new NahrBr();

	static {
		def("{wpt}", (sb, ctx) -> sb.append(ctx.wpt.getName()));

		def("{typ1}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getOneLetterType());
			}
		});

		def("{velikost}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getSize());
			}
		});

		def("{velikost1}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getOneLetterSize());
			}
		});

		def("{obtiznost}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getDifficulty());
			}
		});

		def("{obtiznost1}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getOneLetterDifficulty());
			}
		});

		def("{teren}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getTerrain());
			}
		});

		def("{teren1}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getOneLetterTerrain());
			}
		});

		def("{autor}", (sb, ctx) -> sb.append(ctx.getKesoid().getAuthor()));

		def("{nazev}", (sb, ctx) -> sb.append(ctx.wpt.getNazev()));

		def("{zalozeno}", (sb, ctx) -> sb.append(ctx.getKesoid().getHidden()));
		def("{nbsp}", new NahrKonstantni(" "));

		def("{br}", NAHRBR);

		def("{puvodnipotvora}", (sb, ctx) -> sb.append(computeByvalyPopisek(ctx.wpt)));

		// a nové, které nejsou na geocaching.cz

		def("{info}", (sb, ctx) -> {
			if (ctx.isKes()) {
				sb.append(ctx.kes.getInfo());
			}
		});

	}

	private final Nahrazovac[] nahrazky;

	private final int pocetRadku;

	public static String computeByvalyPopisek(final Wpt wpt) {
		final Kesoid kesoid = wpt.getKesoid();
		final String nazev = kesoid.getKesoidKind() == EKesoidKind.CGP ? kesoid.getIdentifier() : kesoid.getNazev();
		return nazev;

	}

	public static String getNahrazovaceDisplay() {
		return sNahrazovace.keySet().toString();

	}

	/**
	 * @param string
	 * @param xXX2
	 */
	private static void def(final String key, final Nahrazovac nahrazovac) {
		sNahrazovace.put(key, nahrazovac);
	}

	/**
	 *
	 */
	public SestavovacPopisku(final String pattern) {
		final List<Nahrazovac> nahrazovace = new ArrayList<>();
		vytvorNahrazovace(nahrazovace, pattern);
		int n = 1;
		for (final Nahrazovac nahr : nahrazovace) {
			if (nahr == NAHRBR) {
				n++;
			}
		}
		pocetRadku = n;
		nahrazky = nahrazovace.toArray(new Nahrazovac[nahrazovace.size()]);
	}

	public String[] sestavPopisek(final Wpt wpt) {
		int n = 0;
		final Context ctx = new Context(wpt);
		final String[] popisky = new String[pocetRadku];
		final StringBuilder sb = new StringBuilder();
		for (final Nahrazovac nahr : nahrazky) {
			nahr.pridej(sb, ctx);
			if (nahr == NAHRBR) {
				popisky[n] = sb.toString();
				n++;
				sb.setLength(0);
			}
		}
		popisky[n] = sb.toString();
		return popisky;
	}

	private void vytvorNahrazovace(final List<Nahrazovac> nahrazovace, final String vzorek) {
		if (vzorek.length() == 0) {
			return;
		}
		for (final Map.Entry<String, Nahrazovac> entry : sNahrazovace.entrySet()) {
			final int delka = entry.getKey().length();
			final int poz = vzorek.indexOf(entry.getKey());
			if (poz >= 0) { // našli jsme některý
				vytvorNahrazovace(nahrazovace, vzorek.substring(0, poz));
				nahrazovace.add(entry.getValue());
				vytvorNahrazovace(nahrazovace, vzorek.substring(poz + delka));
				return; // jednu jsme našli a vyřešili, jiné se najdou jinde
			}
		}
		nahrazovace.add(new NahrKonstantni(vzorek));
	}
}