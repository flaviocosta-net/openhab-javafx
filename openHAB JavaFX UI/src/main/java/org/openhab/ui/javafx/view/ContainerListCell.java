package org.openhab.ui.javafx.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

import org.openhab.ui.javafx.model.atom.Atom;
import org.openhab.ui.javafx.model.atom.IconAtom;
import org.openhab.ui.javafx.model.atom.MappingsAtom;
import org.openhab.ui.javafx.model.container.Container;
import org.openhab.ui.javafx.model.container.Frame;
import org.openhab.ui.javafx.model.container.Widget;
import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;
import org.openhab.ui.javafx.view.iconset.MaterialIcon;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * Implementation of a custom ListCell to display containers in a sitemap.
 * 
 * TODO This is the messiest of all classes implemented now, it still needs to
 * be reorganized in order to: 1) implement the missing sitemap types; 2)
 * implement support for component layouts and inline styles.
 * 
 * @author Flavio Costa
 */
public class ContainerListCell extends ListCell<Container<?, ?>> {

	/**
	 * Used to loads icons for the sitemap.
	 */
	private SchemeHandler<Optional<InputStream>> iconLoader = SchemeHandler.of(SchemeType.ICON);

	/**
	 * Provides a Cell Factory callback to use this class with list views.
	 * 
	 * @return New Callback instance.
	 */
	public static Callback<ListView<Container<?, ?>>, ListCell<Container<?, ?>>> getCellFactory() {
		return new Callback<ListView<Container<?, ?>>, ListCell<Container<?, ?>>>() {

			@Override
			public ListCell<Container<?, ?>> call(ListView<Container<?, ?>> listView) {
				// just calls the default constructor
				return new ContainerListCell();
			}
		};
	}

	/**
	 * Default constructor.
	 */
	private ContainerListCell() {
		super();
	}

	@Override
	protected void updateItem(Container<?, ?> item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			// clean removed items from the list
			setGraphic(null);
			getStyleClass().clear();

		} else {
			Node graphic;

			switch (item.getType()) {
			case FRAME:
				graphic = renderFrame((Frame) item);
				break;
			case WIDGET:
				graphic = renderWidget((Widget) item);
				break;
			default:
				// unexpected situation (bug)
				throw new IllegalArgumentException("Could not determine graphic for " + item);
			}

			// set the style class for the container
			setGraphic(graphic);
			getStyleClass().add("container-" + item.getType().toString().toLowerCase());
		}
	}

	/**
	 * Gets the graphic node for a Frame.
	 * 
	 * @param frame
	 *            Frame container.
	 * @return Resulting node.
	 */
	private Node renderFrame(Frame frame) {
		return new Label(frame.getData());
	}

	/**
	 * Gets the graphic node for a Widget.
	 * 
	 * @param group
	 *            Widget container.
	 * @return Resulting node.
	 */
	private Node renderWidget(Widget widget) {
		BorderPane widgetPane = new BorderPane();
		HBox widgetBox = new HBox(5);
		widgetBox.setAlignment(Pos.CENTER_LEFT);
		widgetPane.setLeft(widgetBox);

		// for each atom in the widget
		for (Atom<?> a : widget.getComponents()) {

			URI uri = renderAtom(widgetPane, widgetBox, a);
			if (uri != null) {
				// there is a URI defined in an atom, so we make this widget clickable
				getStyleClass().add("clickable");
			}
		}

		return widgetPane;
	}

	/**
	 * Given an Icon Atom, retrieves an InputStream for the icon contents.
	 * 
	 * @param atom
	 *            Atom that must be of IconAtom type.
	 * @return Contents for the icon, or null if not found.
	 */
	public InputStream getIconFromAtom(IconAtom atom) {
		return iconLoader.handle(atom.getData()).get();
	}

	/**
	 * Adds the graphic node for an Atom.
	 * 
	 * @param widgetPane
	 *            Pane representing the whole control.
	 * @param widgetBox
	 *            Box to include the icon and text.
	 * @param atom
	 *            Atom being added to the control pane.
	 * @return URI, in case there is one defined for the atom, or null if there is
	 *         none.
	 */
	private URI renderAtom(BorderPane widgetPane, HBox widgetBox, Atom<?> atom) {

		// TODO the code in this method is super ugly, but it should be improved when
		// support for the layout options is introduced

		URI uri = null;
		Node atomNode = null;
		boolean alignRight = false;

		switch (atom.getType()) {
		case ICON:
			try (InputStream is = getIconFromAtom((IconAtom) atom)) {
				atomNode = new ImageView(new Image(is, 24, 24, true, true));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case TEXT:
			alignRight = true;
		case LABEL:
			atomNode = new Label(atom.getData().toString());
			break;
		case SELECTION:
			JFXComboBox<Object> comboBox = new JFXComboBox<Object>();
			MappingsAtom.Data selectionData = ((MappingsAtom) atom).getData();
			Map<?, ?> mappings = selectionData.getMappings();
			comboBox.getItems().setAll(mappings.values());
			comboBox.setValue(mappings.get(selectionData.getSelected()));
			atomNode = comboBox;
			alignRight = true;
			break;
		case SLIDER:
			JFXSlider slider = new JFXSlider();
			slider.setValue((Double) atom.getData());
			atomNode = slider;
			alignRight = true;
			break;
		case SWITCH:
			JFXToggleButton toggle = new JFXToggleButton();
			MappingsAtom.Data switchData = ((MappingsAtom) atom).getData();
			toggle.setSelected("ON".equals(switchData.getSelected()));
			atomNode = toggle;
			alignRight = true;
			break;
		case GROUP:
			atomNode = new MaterialIcon("navigate-next");
			uri = URI.create(atom.getData().toString());
			alignRight = true;
			break;
		default:
			throw new UnsupportedOperationException("Atom type not supported: " + atom.getType());
		}

		atomNode.getStyleClass().add("atom-" + atom.getType().toString().toLowerCase());

		if (alignRight) {
			widgetPane.setRight(atomNode);
			BorderPane.setAlignment(atomNode, Pos.CENTER);
		} else {
			widgetBox.getChildren().add(atomNode);
		}

		return uri;
	}

}
