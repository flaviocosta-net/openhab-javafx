package org.openhab.ui.javafx.navigation;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;

import javafx.application.HostServices;

/**
 * Provides support to handle URI actions for {@link SchemeType} WEB.
 * 
 * @author Flavio Costa
 */
public class WebSchemeHandler implements SchemeHandler<Void> {

	/**
	 * Used to open web pages on the default browser.
	 */
	private HostServices hostServices;

	/**
	 * Constructor receiving the HostServices.
	 * 
	 * @param hostServices
	 *            HostServices instance from the Application.
	 */
	public WebSchemeHandler(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	@Override
	public Set<SchemeType> getAcceptedTypes() {
		return Collections.singleton(SchemeType.WEB);
	}

	@Override
	public Void handle(URI uri) throws IllegalArgumentException {
		validate(uri);
		hostServices.showDocument(uri.toString());
		return null;
	}
}
