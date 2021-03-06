package cz.geokuk.plugins.cesty.akce.soubor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import cz.geokuk.framework.Dlg;
import cz.geokuk.plugins.cesty.CestyChangedEvent;
import cz.geokuk.plugins.cesty.data.Doc;

public class UlozKopiiAction extends SouboeCestaAction0 {

	private static final long serialVersionUID = 1L;
	private Doc doc;

	public UlozKopiiAction() {
		super("Uložit kopii cest (gpx) ...");
		putValue(SHORT_DESCRIPTION, "Uloží zadaný výlet jako kopii do jiného souboru GPX, dále se pak bude pracovat na původním souboru");
		putValue(MNEMONIC_KEY, KeyEvent.VK_K);
		// putValue(SMALL_ICON, ImageLoader.seekResIcon("x16/vylet/vyletAno.png"));
	}

	@Override
	public void actionPerformed(final ActionEvent e) {

		final JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new GpxFilter());
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setSelectedFile(cestyModel.getImplicitniVyletSaveCopyNovyFile());
		final int result = fc.showDialog(Dlg.parentFrame(), "Uložit kopii");
		if (result == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = doplnGpxPriponuProUkladani(fc.getSelectedFile());
			if (selectedFile.exists()) { // dtaz na přepsání
				if (!Dlg.prepsatSoubor(selectedFile)) {
					return;
				}
			}
			cestyModel.uloz(selectedFile, doc, false);
			System.out.println("Uložena cesta do: " + doc.getFile());
		}
	}

	public void onEvent(final CestyChangedEvent event) {
		doc = event.getDoc();
		setEnabled(doc != null && (!doc.isEmpty() || doc.isChanged()));
	}

}
