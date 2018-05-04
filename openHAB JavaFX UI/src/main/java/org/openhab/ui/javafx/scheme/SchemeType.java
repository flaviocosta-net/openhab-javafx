package org.openhab.ui.javafx.scheme;

import java.util.Arrays;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Defines which URI schemes belong to which type, so they can be handled in a
 * similar way (i.e. typically by the same {@link SchemeHandler}. Each scheme
 * can only be included in one SchemeType - this is not actively enforced by
 * this implementation, but failure to do so may lead to unpredictable results.
 * 
 * @author Flavio Costa
 */
public enum SchemeType {

	// no schemes should repeat on this list!
	SITEMAP("sitemap", "navigation", "item", "thing"), SNACKBAR("snackbar"), ICON("icon"), WEB("http",
			"https"), UNSUPPORTED("mailto"), UNKNOWN;

	/**
	 * Schemes in the scheme type.
	 */
	private final String[] schemes;

	/**
	 * Creates a new SchemeType.
	 * 
	 * @param schemes
	 *            Schemes included in this type.
	 */
	private SchemeType(String... schemes) {
		this.schemes = schemes;
	}

	/**
	 * Verifies if this SchemeType includes a certain scheme.
	 * 
	 * @param scheme
	 *            Scheme to be verified.
	 * @return Whether the scheme is listed under this type.
	 */
	public boolean includes(String scheme) {
		if (schemes == null) {
			return false;
		}

		return Arrays.stream(schemes).anyMatch(s -> s.equals(scheme));
	}

	/**
	 * Parse a scheme and returns the matching SchemeType.
	 * 
	 * @param scheme
	 *            Scheme for which the type should be determined.
	 * @return Matching SchemeType, or UNKNOWN if the scheme does not match any
	 *         known scheme.
	 */
	public static @NonNull SchemeType parse(String scheme) {

		return Stream.of(SchemeType.values()).filter(t -> t.includes(scheme)).findFirst().orElse(UNKNOWN);
	}
}
