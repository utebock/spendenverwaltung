package at.fraubock.spendenverwaltung.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.plaf.metal.MetalBorders;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
import at.fraubock.spendenverwaltung.gui.views.HistoryView;
import at.fraubock.spendenverwaltung.gui.views.ViewActionFactory;
import at.fraubock.spendenverwaltung.interfaces.domain.Action;
import at.fraubock.spendenverwaltung.interfaces.exceptions.ServiceException;
import at.fraubock.spendenverwaltung.interfaces.service.IActionService;

/**
 * class for polling actions. polls for new actions every few seconds and
 * presents them to the user via a notification panel (disregarding the current
 * view)
 * 
 * @NOTE after a specified amount of errors, the polling will shut down itself.
 * 
 * @author philipp muhoray
 * 
 */
public class ActionPolling extends Thread {

	private static final Logger log = Logger.getLogger(ActionPolling.class);

	/* constants */
	public static final int REMOVE_ACTION_VIEW_IN_MILLI = 5000;
	private final int POLLING_INTERVALL_IN_MILLI = 3000;
	private final int MAX_POLLING_ERRORS = 20;
	private final int NEW_ACTIONS_AMOUNT = 5;

	private IActionService actionService;
	private Date lastPolling;
	private int consecutivePollingErrors;
	private NotificationPanel notificationPanel;
	private List<Action> lastActions = new ArrayList<Action>();

	public ActionPolling(ViewDisplayer viewDisplayer,
			IActionService actionService, ViewActionFactory viewFactory) {
		this.actionService = actionService;
		this.lastPolling = new Date();
		this.consecutivePollingErrors = 0;
		this.notificationPanel = new NotificationPanel(viewDisplayer,
				viewFactory);
	}

	@Override
	public void run() {
		boolean serviceError;

		List<Action> actions;
		while (true) {
			try {
				actions = actionService.pollForActionSince(lastPolling,
						NEW_ACTIONS_AMOUNT);
				serviceError = false;
				lastPolling = new Date();

				/*
				 * note that there can be a time glitch when actions are
				 * inserted while making a polling request. they might be
				 * inserted in the same second as 'lastPolling' will be set,
				 * therefore they might appear in the next polling as well. to
				 * circumvent this, remove all actions that were present in the
				 * last polling
				 */
				for (Action a : lastActions) {
					if (actions.contains(a)) {
						actions.remove(a);
					}
				}
				lastActions = actions;

				if (!actions.isEmpty()) {
					notificationPanel.present(actions);
					continue;
				}

			} catch (ServiceException e) {
				serviceError = true;
				if (pollingError(e)) {
					return;
				}
			}

			try {
				sleep(POLLING_INTERVALL_IN_MILLI);
			} catch (InterruptedException e) {
				if (pollingError(e)) {
					return;
				}
				continue;
			}
			if (!serviceError)
				consecutivePollingErrors = 0;
		}
	}

	private boolean pollingError(Exception e) {
		consecutivePollingErrors++;
		log.error(consecutivePollingErrors + ". Error for actions: "
				+ e.getLocalizedMessage());
		if (consecutivePollingErrors == MAX_POLLING_ERRORS) {
			log.info("Consecutive polling errors exceeded max limit of "
					+ MAX_POLLING_ERRORS + ". Shut down polling thread");
			return true;
		}
		return false;
	}

	/**
	 * panel that renders a list of actions. will fade in to the given layered
	 * panel and fade out automatically. when clicking it, the history view will
	 * be shown with the actions in the table.
	 * 
	 * @author philipp muhoray
	 * 
	 */
	public class NotificationPanel extends JPanel implements MouseListener {
		private static final long serialVersionUID = 3957378274808663996L;

		private JLayeredPane rootLayeredPane;
		private ViewDisplayer viewDisplayer;
		private ViewActionFactory viewFactory;
		private List<Action> actions;

		public NotificationPanel(ViewDisplayer viewDisplayer,
				ViewActionFactory viewFactory) {
			this.rootLayeredPane = viewDisplayer.getLayeredPane();
			this.viewDisplayer = viewDisplayer;
			this.viewFactory = viewFactory;

			setLayout(new MigLayout());
			rootLayeredPane.add(this, new Integer(50));
			setBorder(BorderFactory.createTitledBorder(
					MetalBorders.getTextFieldBorder(), "Aktuelle Aktionen"));
			this.addMouseListener(this);
		}

		public void present(List<Action> actions) {
			this.actions = actions;
			int size = actions.size();

			for (Action a : actions) {
				add(new ActionPanel(a), "wrap,growx");
			}
			setVisible(true);

			for (int i = 0; i < 100 + size * 50; i++) {
				setBounds(rootLayeredPane.getWidth()-320, 770 - i, 300, 30 + size * 50);
				rootLayeredPane.repaint();
				rootLayeredPane.revalidate();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// nothing
				}
			}

			// hide after a while
			try {
				sleep(ActionPolling.REMOVE_ACTION_VIEW_IN_MILLI);
			} catch (InterruptedException e) {
				// nothing
			}

			fadeOut();

		}

		private void fadeOut() {
			if (!isVisible())
				return; // already invisible

			int size = this.getComponents().length;
			for (int i = 0; i < 100 + size * 50; i++) {
				setBounds(rootLayeredPane.getWidth()-320, (770 - (100 + size * 50)) + i, 300,
						30 + size * 50);
				rootLayeredPane.repaint();
				rootLayeredPane.revalidate();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					// nothing
				}
			}

			setVisible(false);

			for (Component comp : this.getComponents()) {
				this.remove(comp);
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			HistoryView view = (HistoryView) viewFactory
					.getViewForAction(viewFactory.getHistoryViewAction());
			view.init();
			view.showActions(actions);
			viewDisplayer.changeView(view);
		}
	}

	/**
	 * panel rendering a single action
	 * 
	 * @author philipp muhoray
	 * 
	 */
	private class ActionPanel extends JPanel {
		private static final long serialVersionUID = 2033492844139154802L;

		public ActionPanel(Action a) {
			setLayout(new MigLayout());
			JPanel textPanel = new JPanel();
			textPanel.add(new JLabel(a.getActor()));
			textPanel.add(new JLabel(" hat " + a.getEntity()));
			textPanel.add(new JLabel(a.getType().toString()));
			add(textPanel, "wrap");
			JSeparator sep = new JSeparator();
			add(sep, "growx");
		}

	}

}
