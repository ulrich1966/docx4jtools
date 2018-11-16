package de.juli.docx4j.lisener;

import java.util.EventListener;

interface AdListener extends EventListener {
	void advertisement(AdEvent e);
}