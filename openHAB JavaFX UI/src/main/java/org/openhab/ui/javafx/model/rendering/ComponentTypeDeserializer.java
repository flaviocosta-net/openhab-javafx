package org.openhab.ui.javafx.model.rendering;

import java.lang.reflect.Type;

import org.openhab.ui.javafx.model.ComponentType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Creates a ComponentType instance based on the input string.
 * 
 * @author Flavio Costa
 */
public class ComponentTypeDeserializer implements JsonDeserializer<ComponentType> {

	@Override
	public ComponentType deserialize(JsonElement currentElement, Type classType, JsonDeserializationContext context)
			throws JsonParseException {

		// converts types to uppercase to match the Java enum naming
		return ComponentType.valueOf(currentElement.getAsString().toUpperCase());
	}
}
