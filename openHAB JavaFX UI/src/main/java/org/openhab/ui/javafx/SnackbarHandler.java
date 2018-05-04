package org.openhab.ui.javafx;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import javafx.scene.layout.Pane;

/**
 * Provides snackbar functionality across the application.
 * 
 * @author Flavio Costa
 */
public class SnackbarHandler implements SchemeHandler<SnackbarEvent> {

	/**
	 * Snackbar instance.
	 */
	private final JFXSnackbar bar;

	/**
	 * Constructor for a given Pane where the snackbar should be displayed.
	 * 
	 * @param pane
	 *            Reference pane used to position the snackbar.
	 */
	public SnackbarHandler(Pane pane) {
		bar = new JFXSnackbar(pane);

		// make the snackbar as wide as the containing pane
		((Pane) pane.lookup(".jfx-snackbar-content")).minWidthProperty().bind(pane.widthProperty());
	}

	@Override
	public Set<SchemeType> getAcceptedTypes() {
		return Collections.singleton(SchemeType.SNACKBAR);
	}

	@Override
	public URI parseInput(String input) {
		try {
			// this implementation prevents "Illegal character in path" exceptions
			// when the input message contains space characters
			return new URI("snackbar", input, null);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@Override
	public SnackbarEvent handle(URI uri) throws IllegalArgumentException {
		validate(uri);

		// displays a message on the snackbar
		SnackbarEvent event = new SnackbarEvent(uri.getSchemeSpecificPart());
		bar.enqueue(event);
		return event;
	}
}