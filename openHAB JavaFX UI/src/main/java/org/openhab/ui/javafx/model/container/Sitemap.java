package org.openhab.ui.javafx.model.container;

import org.openhab.ui.javafx.model.container.Sitemap.Data;

/**
 * Sitemap model implementation. The sitemap container holds the containers
 * displayed in the root view of a sitemap.
 * 
 * @author Flavio Costa
 */
public class Sitemap extends AbstractNavigableContainer<Data, Container<?, ?>> {

	/**
	 * Stores data for the Sitemap.
	 */
	public class Data extends AbstractNavigableContainer.Data {

		/**
		 * Sitemap Id.
		 */
		private String id;

		/**
		 * TODO Implementation to be reviewed.
		 */
		// private String lang;

		/**
		 * Returns the id.
		 * 
		 * @return Sitemap Id.
		 */
		public String getId() {
			return id;
		}
	}
}
