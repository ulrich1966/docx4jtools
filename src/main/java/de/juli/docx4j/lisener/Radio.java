package de.juli.docx4j.lisener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.event.EventListenerList;

public class Radio {
	private EventListenerList listeners = new EventListenerList();

	private List<String> ads = Arrays.asList("Jetzt explodiert auch der Haarknoten", "Red Fish verleiht Flossen", "Bom Chia Wowo", "Wunder Whip. Iss milder.");

	public Radio() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				Collections.shuffle(ads);
				notifyAdvertisement(new AdEvent(this, ads.get(0)));
			}
		}, 0, 500);
	}

	public void addAdListener(AdListener listener) {
		listeners.add(AdListener.class, listener);
	}

	public void removeAdListener(AdListener listener) {
		listeners.remove(AdListener.class, listener);
	}

	protected synchronized void notifyAdvertisement(AdEvent event) {
		for (AdListener l : listeners.getListeners(AdListener.class))
			l.advertisement(event);
	}
}