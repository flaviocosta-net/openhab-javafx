package org.openhab.ui.javafx.navigation;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;
import org.openhab.ui.javafx.view.SitemapListView;

/**
 * Provides support to handle URI actions for {@link SchemeType} SITEMAP.
 * 
 * @author Flavio Costa
 */
public class SitemapSchemeHandler implements SchemeHandler<NavigationHistory> {

	/**
	 * Navigation history for the sitemap list view.
	 */
	private NavigationHistory navigation;

	/**
	 * Used to display snackbar messages.
	 */
	private final SchemeHandler<?> snackbarHandler = SchemeHandler.of(SchemeType.SNACKBAR);

	/**
	 * Creates a new instance with a list view.
	 * 
	 * @param listView
	 *            List associated with this SchemeHandler.
	 */
	public SitemapSchemeHandler(SitemapListView listView) {
		navigation = new NavigationHistory(listView);
	}

	@Override
	public Set<SchemeType> getAcceptedTypes() {
		return Collections.singleton(SchemeType.SITEMAP);
	}

	@Override
	public URI getURI() {
		return navigation.getCurrentURI();
	}

	@Override
	public NavigationHistory handle(URI uri) throws IllegalArgumentException {

		validate(uri);

		switch (uri.getScheme()) {
		case "sitemap":
			navigation.navigateTo(uri);
			break;
		case "navigation":
			if (uri.getSchemeSpecificPart().equals("up")) {
				navigation.back();
			} else {
				snackbarHandler.handle(String.format("Invalid URL %s", uri));
			}
			break;
		case "item":
		case "thing":
			snackbarHandler.handle(String.format("Implementation for \"%s\" scheme missing", uri.getScheme()));
			break;
		default:
			snackbarHandler.handle(String.format("Unsupported scheme on URI %s", uri));
		}

		return navigation;
	}
}
