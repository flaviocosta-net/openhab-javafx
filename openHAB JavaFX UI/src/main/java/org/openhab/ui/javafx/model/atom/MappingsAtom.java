package org.openhab.ui.javafx.model.atom;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openhab.ui.javafx.model.atom.MappingsAtom.Data;

/**
 * Mappings Atom model implementation. This class should be used by atoms whose
 * data is represented by {"key": "value"} mappings, one of which may be
 * selected. The entries must be ordered according to their sequence in the
 * rendering model.
 * 
 * @author Flavio Costa
 */
public class MappingsAtom extends AbstractAtom<Data> {

	/**
	 * Mapping data for the atom.
	 */
	public class Data {

		/**
		 * Mapping entries.
		 */
		private LinkedHashMap<String, String> mappings;

		/**
		 * Selected mapping key.
		 */
		private String selected;

		/**
		 * Returns the selected mapping key.
		 * 
		 * @return Selected key.
		 */
		public String getSelected() {
			return selected;
		}

		/**
		 * Returns the ordered mappings.
		 * 
		 * @return Selection mappings in the order they appear in the sitemap.
		 */
		public Map<String, String> getMappings() {
			return Collections.unmodifiableMap(mappings);
		}
	}
}
