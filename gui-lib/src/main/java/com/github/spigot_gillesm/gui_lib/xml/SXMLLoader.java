package com.github.spigot_gillesm.gui_lib.xml;

import com.github.spigot_gillesm.gui_lib.SimpleButton;
import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.gui_lib.stage.Scene;
import com.github.spigot_gillesm.item_lib.SimpleItem;
import org.bukkit.Material;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class SXMLLoader {

	final Document document;

	public SXMLLoader(final String path) throws ParsingException {
		final URL url = getClass().getResource(path);
		final var xmlReader = new SAXReader();

		try {
			xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		} catch (SAXException e) {
			throw new ParsingException("The XMLReader could not be created or the feature could not be changed.");
		}

		try {
			this.document = xmlReader.read(url);
		} catch (DocumentException e) {
			throw new ParsingException("An error occurred during parsing.");
		}
	}

	public SXMLLoader(final URL url) throws ParsingException {
		final var xmlReader = new SAXReader();

		try {
			xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		} catch (SAXException e) {
			throw new ParsingException("The XMLReader could not be created or the feature could not be changed.");
		}

		try {
			this.document = xmlReader.read(url);
		} catch (DocumentException e) {
			throw new ParsingException("An error occurred during parsing.");
		}
	}

	public int size() throws ParsingException {
		final Element root = document.getRootElement();
		final var idAttribute = root.attribute(SXMLField.SIZE.value);

		final var value = idAttribute != null ? idAttribute.getValue() : "3";

		try {
			final var size = Integer.parseInt(value);

			if(size < 0 || size > 6) {
				throw new ParsingException(document.getName() + " size must be an integer between 1 and 6 included.");
			}

			return size;

		} catch (final NumberFormatException exception) {
			throw new ParsingException(document.getName() + " size must be an integer.");
		}
	}

	public Optional<String> title() {
		final Element root = document.getRootElement();
		final var titleElement = root.element(SXMLField.TITLE.value);

		if(titleElement == null) {
			return Optional.empty();
		}

		return Optional.of(titleElement.getStringValue());
	}

	public Optional<String> titleColor() {
		final Element root = document.getRootElement();
		final var titleElement = root.element(SXMLField.TITLE.value);

		if(titleElement == null) {
			return Optional.empty();
		}
		final var hexAttribute = titleElement.attribute(SXMLField.COLOR.value);

		if(hexAttribute == null) {
			return Optional.empty();
		}

		return Optional.of(hexAttribute.getValue());
	}

	private Scene.Coordinates retrieveButtonCoordinates(final Element buttonElement) throws ParsingException {
		final var rawCoords = buttonElement.attribute(SXMLField.COORDINATES.value);

		if(rawCoords == null) {
			throw new ParsingException("All the " + document.getName() + " buttons must define their coordinates.");
		}
		final String[] data = rawCoords.getValue().split(";");

		if(data.length != 2) {
			throw new ParsingException("Wrong button coordinates format for " + document.getName() + ": " +
					"Coordinates must be in format 'line;column'.");
		}

		try {
			return new Scene.Coordinates(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
		} catch (final NumberFormatException exception) {
			throw new ParsingException("Wrong button coordinates format for " + document.getName() + ": values must be an integer.");
		}
	}

	private Scene.Coordinates configureButton(final SimpleButton button, final Element buttonElement) throws ParsingException {
		final var builder = SimpleItem.newBuilder()
				.material(Material.WOODEN_SWORD);
		final var materialElement = buttonElement.element(SXMLField.MATERIAL.value);

		if(materialElement != null) {
			try {
				builder.material(Material.valueOf(materialElement.getStringValue()));
			} catch (final IllegalArgumentException exception) {
				throw new ParsingException("Invalid material in " + document.getName() + " buttons: "
						+ materialElement.getStringValue() + ".");
			}
		}
		final var displayedNameElement = buttonElement.element(SXMLField.DISPLAYED_NAME.value);

		if(displayedNameElement != null) {
			builder.displayName(displayedNameElement.getStringValue());
		}

		button.setIcon(builder.build().make().getItemStack());

		return retrieveButtonCoordinates(buttonElement);
	}

	private Optional<Scene.Coordinates> configureButton(final SimpleButton button, final String id) throws ParsingException {
		final Element root = document.getRootElement();
		final var buttonsElement = root.element("buttons");

		if(buttonsElement == null) {
			return Optional.empty();
		}
		final var iterator = buttonsElement.elementIterator();

		while(iterator.hasNext()) {
			final var buttonElement = iterator.next();
			final var currentId = buttonElement.attribute(SXMLField.ID.value);

			if(currentId == null) {
				throw new ParsingException(document.getName() + " buttons must declare their id.");
			}
			if(id.equals(currentId.getValue())) {
				return Optional.of(configureButton(button, buttonElement));
			}
		}

		return Optional.empty();
	}

	public Map<String, Scene.Coordinates> configureButtons(final Class<? extends SimpleMenu> clazz) throws ParsingException, IllegalAccessException {
		final var fields = Arrays.stream(clazz.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(SXML.class))
				.collect(Collectors.toSet());
		final Map<String, Scene.Coordinates> coordinates = new HashMap<>();

		for(final var field : fields) {
			field.setAccessible(true);

			final var value = field.get(this);

			if(value instanceof SimpleButton) {
				configureButton((SimpleButton) value, field.getName()).ifPresent(c -> coordinates.put(field.getName(), c));
			}
		}

		return coordinates;
	}
	
	private enum SXMLField {
		
		ID("id"),
		SIZE("size"),
		COORDINATES("coord"),
		TITLE("title"),
		COLOR("color"),
		MATERIAL("material"),
		DISPLAYED_NAME("displayedName");

		private final String value;
		
		SXMLField(final String value) {
			this.value = value;
		}
		
	}

}
