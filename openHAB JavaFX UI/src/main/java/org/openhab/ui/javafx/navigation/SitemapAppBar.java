package org.openhab.ui.javafx.navigation;

import java.util.HashMap;
import java.util.Map;

import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;
import org.openhab.ui.javafx.view.iconset.MaterialIcon;

import com.jfoenix.controls.JFXHamburger;

import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * App Bar displayed on top of the window, above the sitemap list.
 * 
 * @author Flavio Costa
 */
public class SitemapAppBar extends BorderPane {

	/**
	 * Used to display snackbar messages.
	 */
	private final SchemeHandler<?> snackbarHandler = SchemeHandler.of(SchemeType.SNACKBAR);

	/**
	 * Used to control sitemap navigation from app bar icons.
	 */
	private final SchemeHandler<?> sitemapHandler = SchemeHandler.of(SchemeType.SITEMAP);

	/**
	 * Supported icon types in the app bar.
	 */
	public static enum IconId {
		HAMBURGER, NAV_UP, OVERFLOW_MENU
	}

	/**
	 * Label with the application title.
	 */
	private final Label label = new Label("[Title]");

	/**
	 * Icons in the app bar.
	 */
	private final Map<IconId, Pane> icons = new HashMap<>();

	/**
	 * Default constructor.
	 * 
	 */
	public SitemapAppBar() {

		// generate icons
		JFXHamburger hamburger = new JFXHamburger();
		icons.put(IconId.HAMBURGER, hamburger);
		MaterialIcon goUp = new MaterialIcon("arrow-back");
		goUp.getStyleClass().add("navigation-up");
		icons.put(IconId.NAV_UP, goUp);
		MaterialIcon menuIcon = new MaterialIcon("more-vert");
		menuIcon.getStyleClass().add("overflow-menu");
		icons.put(IconId.OVERFLOW_MENU, menuIcon);

		// style for the app bar itself
		getStyleClass().add("app-bar");

		// layout the nodes to the left
		HBox hbox = new HBox();
		hbox.getChildren().addAll(getIcon(IconId.HAMBURGER), getIcon(IconId.NAV_UP), label);
		setLeft(hbox);

		// and to the right
		setRight(menuIcon);

		// set actions
		menuIcon.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				snackbarHandler.handle("Menu not implemented yet");
			}
		});

		hamburger.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				snackbarHandler.handle("Drawer not implemented yet");
			}
		});

		goUp.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				sitemapHandler.handle("navigation:up");
			}
		});
	}

	/**
	 * Returns a title property for this app bar.
	 * 
	 * @return Property for a title displayed on the app bar.
	 */
	public StringProperty titleProperty() {
		return label.textProperty();
	}

	/**
	 * Returns a reference to one of the icons in the app bar.
	 * 
	 * @param icon
	 *            Icon Id.
	 * @return Icon instance.
	 */
	public Pane getIcon(IconId icon) {
		return icons.get(icon);
	}
}
