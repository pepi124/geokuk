package cz.geokuk.plugins.mapy.kachle;

import java.util.EnumSet;

public class JKachlovnikDoRohu extends JKachlovnik {

	private static final long serialVersionUID = -7897332661428146095L;

	public JKachlovnikDoRohu() {
		super("Levý dolní roh - kachlovník", Priority.KACHLE);
	}

	@Override
	public void setKachloTypes(final KaSet aKachloSet) {
		super.setKachloTypes(new KaSet(EnumSet.of(EKaType.OPHOTO_M)));
	}

}
