/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package cz.geokuk.plugins.geocoding;

/*
 * TableDemo.java requires no other files.
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import cz.geokuk.plugins.kesoid.Ikonizer;

/**
 * TableDemo is just like SimpleTableDemo, except that it uses a custom TableModel.
 */
public class JAdrTable extends JPanel {
	class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = -1777521413836209700L;

		private List<Nalezenec> adressList = new ArrayList<>();

		private final String[] columnNames = { "Přesnost", "Vzdálenost", "Azimut", "Adresa" };

		/**
		 * @return the keslist
		 */
		public List<Nalezenec> getAdressList() {
			return adressList;
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for each cell. If we didn't implement this method, then the last column would contain text ("true"/"false"), rather than a check box.
		 */
		@Override
		public Class<?> getColumnClass(final int c) {
			return getValueAt(0, c).getClass();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(final int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return adressList.size();
		}

		@Override
		public Object getValueAt(final int row, final int col) {
			final Nalezenec nalezenec = adressList.get(row);

			Object s = null;
			switch (col) {
			case 0:
				s = nalezenec.locationType;
				break;
			case 1:
				s = Math.round(nalezenec.getVzdalenost() / 100) / 10.0;
				break;
			case 2:
				s = Ikonizer.findSmerIcon(nalezenec.getAzimut());
				break;
			case 3:
				s = nalezenec.adresa;
				break;
			}
			return s;
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		@Override
		public boolean isCellEditable(final int row, final int col) {
			return false;
		}

		/**
		 * @param aKeslist
		 *            the keslist to set
		 */
		public void setAdressList(final List<Nalezenec> aKeslist) {
			adressList = aKeslist;
			fireTableStructureChanged();
		}

	}

	private static final long serialVersionUID = 7687619215661046034L;

	private final MyTableModel tableModel;

	private final JTable table;

	public JAdrTable() {
		super(new GridLayout(1, 0));

		tableModel = new MyTableModel();

		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		// Create the scroll pane and add the table to it.
		final JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to this panel.
		add(scrollPane);
		nastavVlastnostiSLoupcu();
	}

	public void addListSelectionListener(final ListSelectionListener listener) {
		table.getSelectionModel().addListSelectionListener(listener);
	}

	public Nalezenec getCurrent() {
		final int selectedRow = table.getSelectedRow();
		if (selectedRow < 0 || selectedRow >= tableModel.getAdressList().size()) {
			return null;
		}
		final Nalezenec nalezenec = tableModel.getAdressList().get(selectedRow);
		return nalezenec;
	}

	/**
	 * @return the keslist
	 */
	public List<Nalezenec> getKeslist() {
		return tableModel.getAdressList();
	}

	/**
	 * @param aAdresList
	 *            the keslist to set
	 */
	public void setNalezenci(final List<Nalezenec> aAdresList) {
		// Nalezenec current = getCurrent();
		tableModel.setAdressList(aAdresList);
		nastavVlastnostiSLoupcu();
		// if (current != null) {
		// int index = 0;
		// for (Nalezenec nalezenec : aKeslist) {
		// if (current.getKes() == nalezenec.getKes()) {
		// table.getSelectionModel().setSelectionInterval(index, index);
		// return;
		// }
		// index ++;
		// }
		// }
		if (aAdresList.size() > 0) {
			table.getSelectionModel().setSelectionInterval(0, 0);
		} else {
			table.getSelectionModel().setSelectionInterval(-1, -1);
		}

	}

	private void nastavVlastnostiSLoupcu() {
		TableColumn column;
		column = table.getColumnModel().getColumn(0);
		column.setMaxWidth(225);
		column.setPreferredWidth(150);
		column.setResizable(false);

		column = table.getColumnModel().getColumn(1);
		column.setMaxWidth(150);
		column.setPreferredWidth(150);
		column.setResizable(false);

		column = table.getColumnModel().getColumn(2);
		column.setMaxWidth(25);

		column = table.getColumnModel().getColumn(3);
		column.setPreferredWidth(700);
		column.setResizable(true);

	}

}
