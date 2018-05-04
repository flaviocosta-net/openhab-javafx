package org.openhab.ui.javafx.view;

import java.net.URI;

import org.openhab.ui.javafx.model.ComponentType;
import org.openhab.ui.javafx.model.container.Container;
import org.openhab.ui.javafx.model.container.Widget;
import org.openhab.ui.javafx.navigation.NavigationHistory;
import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Triggers needed actions when a new item is selected from the
 * {@link SitemapListView}.
 * 
 * @author Flavio Costa
 */
public class SitemapClickListener implements ChangeListener<Container<?, ?>> {

	@Override
	public void changed(ObservableValue<? extends Container<?, ?>> observable, Container<?, ?> oldValue,
			Container<?, ?> newValue) {

		if (newValue == null || newValue.getType() != ComponentType.WIDGET) {
			// only widgets can execute actions when selected
			return;
		}

		URI action = ((Widget) newValue).getData();

		// only clickable widgets will contain an URI
		if (action != null) {
			SchemeHandler<NavigationHistory> sitemapHandler = SchemeHandler.of(SchemeType.SITEMAP);
			URI widgetURI = sitemapHandler.getURI().resolve(action);

			// lookup for a handler matching the scheme of the given URI
			SchemeHandler<?> handler = SchemeHandler.of(SchemeType.parse(widgetURI.getScheme()));

			// runLater ensures that the ListView will only be changed after this listener
			// finished running, thus preventing concurrency exceptions
			Platform.runLater(() -> {
				handler.handle(widgetURI);
			});
		}
	}
}