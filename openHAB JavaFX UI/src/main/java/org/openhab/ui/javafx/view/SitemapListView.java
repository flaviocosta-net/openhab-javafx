package org.openhab.ui.javafx.view;

import org.openhab.ui.javafx.model.Component;
import org.openhab.ui.javafx.model.ComponentType;
import org.openhab.ui.javafx.model.container.Container;
import org.openhab.ui.javafx.model.container.NavigableContainer;

import com.jfoenix.controls.JFXListView;

/**
 * Sitemap view that displays the components as list items.
 * 
 * @author Flavio Costa
 */
public class SitemapListView extends JFXListView<Container<?, ?>> {

	/**
	 * Default constructor.
	 */
	public SitemapListView() {

		// customized cell factory
		setCellFactory(ContainerListCell.getCellFactory());

		// act when widgets are clicked
		getSelectionModel().selectedItemProperty().addListener(new SitemapClickListener());
	}

	/**
	 * Updates the current view with the components in a container.
	 * 
	 * @param navigableContainer
	 *            Navigable container to be displayed.
	 */
	public void navigateTo(NavigableContainer<?, Container<?, ?>> navigableContainer) {
		getItems().clear();

		for (Container<?, ?> container : navigableContainer.getComponents()) {
			getItems().add(container);

			if (container.getType() == ComponentType.WIDGET) {
				// widgets contain atoms, which are not rendered
				// as separate lines but inside the widget itself
				continue;
			}

			for (Component<?> component : container.getComponents()) {
				// type cast should work because of the if / continue above
				getItems().add((Container<?, ?>) component);
			}
		}
	}
}
