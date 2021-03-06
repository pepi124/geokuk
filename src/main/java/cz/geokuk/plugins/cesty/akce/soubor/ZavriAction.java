package cz.geokuk.plugins.cesty.akce.soubor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import cz.geokuk.plugins.cesty.CestyChangedEvent;
import cz.geokuk.plugins.cesty.data.Doc;

public class ZavriAction extends SouboeCestaAction0 {

	private static final long serialVersionUID = 1L;
	private Doc doc;

	public ZavriAction() {
		super("Zavři cesty (gpx)");
		putValue(SHORT_DESCRIPTION, "Odstraní všechny cesty z obrazovky a uzavře otevřený soubor");
		putValue(MNEMONIC_KEY, KeyEvent.VK_Z);
		// putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
		// putValue(SMALL_ICON, ImageLoader.seekResIcon("x16/vylet/vyletAno.png"));
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (!super.ulozitSDotazem()) {
			return; // mělo se ukládat a řeklo se, že ne
		}
		cestyModel.zavri();
		// if (doc.getFile() == null) { // ještě nebyl určen soubor, musíme se zeptat
		// JFileChooser fc = new JFileChooser();
		// fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// fc.setSelectedFile(cestyModel.getImplicitniVyletNovyFile());
		// int result = fc.showDialog(Dlg.parentFrame(), "Uložit");
		// if (result == JFileChooser.APPROVE_OPTION) {
		// File selectedFile = fc.getSelectedFile();
		// if (selectedFile.exists()) { // dtaz na přepsání
		// if (! Dlg.prepsatSoubor(selectedFile)) return;
		// }
		// doc.setFile(selectedFile);
		// } else
		// return;
		// }
		// // TODO ukládat na pozadí a také mít jinde ukládací dialog
		// cestyModel.uloz(doc.getFile(), doc, true);
		// System.out.println("Uložena cesta do: " + doc.getFile());
	}

	public void onEvent(final CestyChangedEvent event) {
		doc = event.getDoc();
		setEnabled(doc != null && !doc.isEmpty());
	}

}
