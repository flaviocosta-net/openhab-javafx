package org.openhab.ui.javafx.model.rendering;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.openhab.ui.javafx.model.AbstractComponent;
import org.openhab.ui.javafx.model.Component;
import org.openhab.ui.javafx.model.ComponentType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * Creates a Component instance according to its type, and processes
 * subcomponents recursively if needed.
 * 
 * @author Flavio Costa
 */
public class ComponentDeserializer implements JsonDeserializer<Component<?>> {

	/**
	 * Name of the type member in JSON.
	 */
	private static final String TYPE_MEMBER = "type";

	@Override
	public Component<?> deserialize(JsonElement currentElement, Type classType, JsonDeserializationContext context)
			throws JsonParseException {

		JsonObject json = currentElement.getAsJsonObject();

		// determine the component type
		ComponentType componentType = context.deserialize(json.get(TYPE_MEMBER), ComponentType.class);
		AbstractComponent<?> component = getComponentInstance(componentType);

		try {

			for (Field field : combineFields(component.getClass())) {

				JsonElement element = json.get(field.getName());

				if (element != null) {

					Object value;
					if (element.isJsonPrimitive() || element.isJsonObject()) {
						// primitive or object type, just deserialize it recursively
						value = context.deserialize(element, getFieldClass(component, field));
					} else if (element.isJsonArray()) {
						// array type, each element needs to be deserialized separately
						value = StreamSupport.stream(element.getAsJsonArray().spliterator(), false)
								.map(c -> context.deserialize(c, Component.class)).collect(Collectors.toList());
					} else {
						throw new JsonParseException("Unsupported Json element type: " + element.getClass());
					}

					setFieldValue(component, field, value);
				}
			}

		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new JsonParseException(e);
		}

		return component;
	}

	/**
	 * If a field type is defined as a generic one, go up to the generic superclass
	 * to determine the actual type argument. It is assumed here that the type of
	 * generic fields corresponds to the first and only the type argument of the
	 * generic superclass.
	 * 
	 * @param component
	 *            Component instance being deserialized.
	 * @param field
	 *            Field reference.
	 * @return Inferred field type.
	 */
	private Type getFieldClass(Component<?> component, Field field) {

		if (field.getGenericType() instanceof TypeVariable) {
			// field has a generic type definition, find the actual type on the superclass
			ParameterizedType superClassType = (ParameterizedType) component.getClass().getGenericSuperclass();
			Type[] superClassArgs = superClassType.getActualTypeArguments();
			assert superClassArgs.length == 1;
			return superClassArgs[0];
		}

		// just return the type for the field
		return field.getType();
	}

	/**
	 * Retrieves the fields for the provided Class and all its superclasses.
	 * 
	 * @param objectClass
	 *            Class reference.
	 * @return All Fields going up in the class hierarchy.
	 */
	private List<Field> combineFields(Class<?> objectClass) {

		List<Field> combinedFields = new ArrayList<>(Arrays.asList(objectClass.getDeclaredFields()));
		Class<?> superClass = objectClass.getSuperclass();
		if (superClass != Object.class) {
			// recursive call to superclass
			combinedFields.addAll(combineFields(superClass));
		}
		return combinedFields;
	}

	/**
	 * Sets the value of a field regardless of its visibility.
	 * 
	 * @param component
	 *            Component instance.
	 * @param field
	 *            Field reference.
	 * @param value
	 *            New value to set.
	 * @throws IllegalAccessException
	 *             If the field could not be accessed.
	 */
	private void setFieldValue(AbstractComponent<?> component, Field field, Object value)
			throws IllegalAccessException {
		field.setAccessible(true); // access it even if private
		field.set(component, value);
	}

	/**
	 * Creates a component concrete instance based on the defined type.
	 * 
	 * @param componentType
	 *            Type for the component.
	 * @return Created object.
	 */
	private AbstractComponent<?> getComponentInstance(ComponentType componentType) {

		try {
			// calls the no-args constructor for the implementing class
			return (AbstractComponent<?>) componentType.getImplementingClass().getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new JsonParseException(e);
		}
	}
}
