package at.fraubock.spendenverwaltung.gui.views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.ComponentBuilder;
import at.fraubock.spendenverwaltung.gui.components.HistorySearchPanel;
import at.fraubock.spendenverwaltung.gui.components.HistoryTableModel;
import at.fraubock.spendenverwaltung.gui.components.PageNavigator;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;
import at.fraubock.spendenverwaltung.util.ActionSearchVO;
import at.fraubock.spendenverwaltung.util.Pager;

public class HistoryView extends InitializableView {
	private static final long serialVersionUID = 4106122970891943776L;
	private static final Logger log = Logger.getLogger(HistoryView.class);

	private final int MAX_TABLE_SIZE = 20;

	private ViewActionFactory viewActionFactory;
	private ComponentBuilder builder;
	private JToolBar toolbar;
	private JPanel panel;
	private JTable historyTable;
	private JScrollPane scrollPane;
	private HistoryTableModel historyModel;
	private IActionService actionService;
	private PageNavigator navigator;

	public HistoryView(ViewActionFactory viewActionFactory,
			IActionService actionService) {
		this.setLayout(new MigLayout());
		this.viewActionFactory = viewActionFactory;
		this.actionService = actionService;
	}

	@Override
	public void init() {

		builder = new ComponentBuilder();
		panel = builder.createPanel(1200, 620);
		this.add(panel);

		toolbar = builder.createToolbar();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);

		JButton backButton = new JButton();
		javax.swing.Action getBack = viewActionFactory.getMainMenuViewAction();
		getBack.putValue(javax.swing.Action.SMALL_ICON, new ImageIcon(
				getClass().getResource("/images/backButton.jpg")));
		backButton.setAction(getBack);

		toolbar.add(backButton);

		panel.add(toolbar, "wrap,gapbottom 20");

		JLabel headline = builder.createLabel("Historie aller Aktionen");
		headline.setFont(new Font("Headline", Font.PLAIN, 14));
		panel.add(headline, "wrap, gapbottom 20");
		
		panel.add(new HistorySearchPanel(this), "wrap");

		historyTable = new JTable(historyModel = new HistoryTableModel());
		historyTable.setFillsViewportHeight(true);
		historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		historyTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()==2) {
					Action a = historyModel.getActionRow(historyTable
							.getSelectedRow());
					
					JOptionPane.showMessageDialog(HistoryView.this,
							a.getPayload(), "Details",
							JOptionPane.INFORMATION_MESSAGE);
					
				}
				
			}
		});

		historyTable.getColumnModel().getColumn(0).setMinWidth(200);
		historyTable.getColumnModel().getColumn(0).setMaxWidth(200);

		historyTable.getColumnModel().getColumn(1).setMinWidth(150);
		historyTable.getColumnModel().getColumn(1).setMaxWidth(150);

		historyTable.getColumnModel().getColumn(2).setMinWidth(210);
		historyTable.getColumnModel().getColumn(2).setMaxWidth(210);

		historyTable.getColumnModel().getColumn(3).setMinWidth(150);
		historyTable.getColumnModel().getColumn(3).setMaxWidth(150);

		scrollPane = new JScrollPane(historyTable);
		scrollPane.setPreferredSize(new Dimension(1200,
				MAX_TABLE_SIZE * 18 - 17));
		panel.add(scrollPane, "wrap");
		
		panel.add(navigator = new PageNavigator(historyModel), "growx");
		
		applyExtendedSearch(new ActionSearchVO());
	}

	public void applyExtendedSearch(ActionSearchVO searchVO) {
		Pager<Action> pager;
		try {
			pager = actionService.searchActions(searchVO,
					MAX_TABLE_SIZE);
			historyModel.setPager(pager);
			historyModel.refreshPage();
			navigator.modelRefreshed();
		} catch (ServiceException e) {
			JOptionPane.showMessageDialog(this,
					"Ein unerwarteter Fehler ist aufgetreten.", "Error",
					JOptionPane.ERROR_MESSAGE);
			log.error("Error when loading all actions as pager or refreshing page model: "
					+ e.getMessage());
		}
	}
}