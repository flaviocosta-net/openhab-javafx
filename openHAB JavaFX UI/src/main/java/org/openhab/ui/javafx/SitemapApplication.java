package org.openhab.ui.javafx;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.openhab.ui.javafx.navigation.NavigationHistory;
import org.openhab.ui.javafx.navigation.SitemapAppBar;
import org.openhab.ui.javafx.navigation.SitemapSchemeHandler;
import org.openhab.ui.javafx.navigation.WebSchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;
import org.openhab.ui.javafx.view.SitemapListView;
import org.openhab.ui.javafx.view.iconset.IconSchemeHandler;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFX implementation of an openHAB sitemap client - main application window.
 * 
 * This application is not compatible with legacy sitemaps REST APIs, it expects
 * to be serviced with the rendering model in the new format.
 * 
 * For now, the following argument should be provided on the command line for
 * test purposes: --load=sitemap://demo
 * 
 * @author Flavio Costa
 */
public class SitemapApplication extends Application {

	/**
	 * Title for the Stage.
	 */
	private static final String APP_TITLE = "openHAB sitemap";

	/**
	 * Used for logging activities of this class.
	 */
	private static final Logger logger = Logger.getLogger(SitemapApplication.class.getPackage().getName());

	/**
	 * Standard main method to launch the GUI.
	 * 
	 * @param args
	 *            Arguments - passed to launch method.
	 * @throws SecurityException
	 *             Security exception while reading logging configuration.
	 * @throws IOException
	 *             IO exception while reading logging configuration.
	 */
	public static void main(String[] args) throws SecurityException, IOException {

		// setup logging
		InputStream configFile = SitemapApplication.class.getResourceAsStream("config/logging.properties");
		LogManager.getLogManager().readConfiguration(configFile);

		// launch the application
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		logger.info("JavaFX sitemap client start");

		// create required objects for the scheme handlers
		BorderPane borderPane = new BorderPane();
		SitemapListView listView = new SitemapListView();

		// register scheme handler instances
		SchemeHandler.register(new IconSchemeHandler());
		SchemeHandler.register(new WebSchemeHandler(getHostServices()));
		SchemeHandler.register(new SnackbarHandler(borderPane));
		SchemeHandler.register(new SitemapSchemeHandler(listView));

		// general app layout
		stage.setTitle(APP_TITLE);
		stage.setWidth(400);
		stage.setHeight(600);
		SchemeHandler<Optional<InputStream>> iconHandler = SchemeHandler.of(SchemeType.ICON);
		stage.getIcons().add(new Image(iconHandler.handle("icon:app/openhab").get()));
		Scene scene = new Scene(borderPane);
		scene.getStylesheets().add(SitemapApplication.class.getResource("config/default.css").toExternalForm());
		SitemapAppBar appBar = new SitemapAppBar();
		borderPane.setTop(appBar);
		borderPane.setCenter(listView);

		String sitemapURI = getParameters().getNamed().get("load");
		if (sitemapURI == null) {
			// warning as the application will not really behave properly
			logger.warning("No sitemap provided as an argument");
		} else {
			// load the sitemap
			SchemeHandler<NavigationHistory> sitemapHandler = SchemeHandler.of(SchemeType.SITEMAP);
			NavigationHistory nav = sitemapHandler.handle(sitemapURI);

			// property binding
			appBar.titleProperty().bind(nav.currentTitleProperty());
			Pane hamburger = appBar.getIcon(SitemapAppBar.IconId.HAMBURGER);
			hamburger.visibleProperty().bind(nav.currentLevelProperty().lessThanOrEqualTo(1));
			hamburger.managedProperty().bind(hamburger.visibleProperty());
			Pane goUp = appBar.getIcon(SitemapAppBar.IconId.NAV_UP);
			goUp.visibleProperty().bind(nav.currentLevelProperty().greaterThan(1));
			goUp.managedProperty().bind(goUp.visibleProperty());
		}

		// ready to be displayed
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		logger.info("JavaFX sitemap client stop");
	}
}
