package at.fraubock.spendenverwaltung.gui.views;

import javax.swing.JPanel;

public abstract class InitializableView extends JPanel {
	private static final long serialVersionUID = 1L;

	public abstract void init();
}
