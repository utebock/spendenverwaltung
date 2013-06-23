package at.fraubock.spendenverwaltung.gui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import at.fraubock.spendenverwaltung.gui.container.ViewDisplayer;
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
}
