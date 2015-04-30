package de.unibox.client.events;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import de.unibox.core.network.object.CommunicatorMessage;

/**
 * The Class CommunicationEvent.
 */
public class CommunicationEvent extends Event {

	/** The Constant INCOMING_MESSAGE. */
	public static final EventType<CommunicationEvent> INCOMING_MESSAGE = new EventType<CommunicationEvent>(
			Event.ANY, "INCOMING_MESSAGE");

	/** The root target. */
	private static Node rootTarget;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -494434504043997970L;

	/**
	 * Gets the root target.
	 *
	 * @return the root target
	 */
	public static Node getRootTarget() {
		return CommunicationEvent.rootTarget;
	}

	/**
	 * Promote.
	 *
	 * @param thisMessage
	 *            the this message
	 */
	public static void promote(final CommunicatorMessage thisMessage) {
		final CommunicationEvent event = new CommunicationEvent(
				CommunicationEvent.INCOMING_MESSAGE);
		// invoke Platfrom.runLater to fire event on javafx main thread!
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				event.setMessageObject(thisMessage);
				CommunicationEvent.rootTarget.fireEvent(event);
			}
		});
	}

	/**
	 * Sets the target.
	 *
	 * @param thisTarget
	 *            the new target
	 */
	public static void setTarget(final Node thisTarget) {
		CommunicationEvent.rootTarget = thisTarget;
	}

	/** The message object. */
	private CommunicatorMessage messageObject;

	/**
	 * Instantiates a new communication event.
	 *
	 * @param arg0
	 *            the arg0
	 */
	public CommunicationEvent(final EventType<? extends Event> arg0) {
		super(arg0);
	}

	/**
	 * Instantiates a new communication event.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 * @param arg2
	 *            the arg2
	 */
	private CommunicationEvent(final Object arg0, final EventTarget arg1,
			final EventType<? extends Event> arg2) {
		super(arg0, arg1, arg2);
	}

	/**
	 * Gets the message object.
	 *
	 * @return the message object
	 */
	public CommunicatorMessage getMessageObject() {
		return this.messageObject;
	}

	/**
	 * Sets the message object.
	 *
	 * @param thisMessage
	 *            the new message object
	 */
	public void setMessageObject(final CommunicatorMessage thisMessage) {
		this.messageObject = thisMessage;
	}

}
