package org.openhab.ui.javafx.navigation;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.Deque;

import org.openhab.ui.javafx.model.container.Container;
import org.openhab.ui.javafx.model.container.NavigableContainer;
import org.openhab.ui.javafx.model.rendering.RenderingModelProvider;
import org.openhab.ui.javafx.view.SitemapListView;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The NavigationHistory is the return type for the {@link SitemapSchemeHandler}
 * 
 * TODO Needs to decide if and how to implement "back" vs. "up" navigation, as
 * the concept is not properly represented in the UI.
 * 
 * @author Flavio Costa
 */
public class NavigationHistory {

	/**
	 * Used to retrieve the rendering model for the provided URL.
	 */
	private RenderingModelProvider modelProvider;

	/**
	 * Stack with the URIs in the navigation history.
	 */
	private Deque<URI> history = new ArrayDeque<>();

	/**
	 * Currently level in the hierarchy.
	 */
	private IntegerProperty currentLevel = new SimpleIntegerProperty();

	/**
	 * Display title for the current sitemap or page.
	 */
	private StringProperty currentTitle = new SimpleStringProperty();

	/**
	 * List associated with this navigation history.
	 */
	private SitemapListView listView;

	/**
	 * Creates a new instance with a list view.
	 * 
	 * @param listView
	 *            List associated with this navigation history.
	 */
	protected NavigationHistory(SitemapListView listView) {
		modelProvider = RenderingModelProvider.get();
		this.listView = listView;
	}

	/**
	 * Property indicating the current level in the navigation hierarchy. The root
	 * sitemap level is 1, whenever the users go down one level this value is
	 * incremented and when the user goes back to the parent it is decremented.
	 * 
	 * @return Level property reference.
	 */
	public ReadOnlyIntegerProperty currentLevelProperty() {
		return currentLevel;
	}

	/**
	 * Property indicating the title of the current navigation view.
	 * 
	 * @return Title property reference.
	 */
	public ReadOnlyStringProperty currentTitleProperty() {
		return currentTitle;
	}

	/**
	 * Returns the URI currently being displayed on the list view.
	 * 
	 * @return Current/last URI.
	 */
	public URI getCurrentURI() {
		return this.history.peek();
	}

	/**
	 * Navigates to another container.
	 * 
	 * @param uri
	 *            URI of the container to be displayed.
	 */
	public void navigateTo(URI uri) {

		// rendering model for a sitemap or page
		NavigableContainer<?, Container<?, ?>> model = modelProvider.retrieve(uri);

		history.push(uri);
		listView.navigateTo(model);
		currentLevel.set(history.size());
		currentTitle.set(model.getData().getLabel());
	}

	/**
	 * Navigates back to the previous container.
	 */
	public void back() {
		// discard current item
		history.pop();
		// go back to the previous item
		navigateTo(history.pop());
	}
}
