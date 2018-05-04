package org.openhab.ui.javafx.view.iconset;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * An interactive, clickable control that is rendered as a Material Design icon.
 * 
 * @author Flavio Costa
 */
public class MaterialIcon extends StackPane {

	/**
	 * Creates an icon with the specified icon category.
	 * 
	 * @param category
	 *            Icon category.
	 */
	public MaterialIcon(String category) {

		SchemeHandler<Optional<InputStream>> iconLoader = SchemeHandler.of(SchemeType.ICON);

		Node child;

		try (InputStream is = iconLoader.handle("icon:material/" + category).get()) {
			child = new ImageView(new Image(is, 24, 24, true, true));
		} catch (IOException e) {
			// TODO should display a default icon
			e.printStackTrace();
			child = new Label("[icon]");
		}

		this.getChildren().add(child);
	}
}
