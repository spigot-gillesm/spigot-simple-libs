package com.github.spigot_gillesm.gui_lib.stage;

import com.github.spigot_gillesm.gui_lib.SimpleMenu;
import com.github.spigot_gillesm.gui_lib.xml.ParsingException;
import com.github.spigot_gillesm.gui_lib.xml.SXMLLoader;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Scene {

	private final SXMLLoader sxmlLoader;

	@Getter
	private String title;

	@Getter
	private int size;

	@Getter
	private Map<String, Coordinates> coordinatesMap = new HashMap<>();

	public Scene(@NotNull final SXMLLoader sxmlLoader) {
		this.sxmlLoader = sxmlLoader;
	}

	public void load(final Class<? extends SimpleMenu> clazz) throws ParsingException {
		try {
			this.coordinatesMap = sxmlLoader.configureButtons(clazz);
		} catch (final IllegalAccessException exception) {
			throw new ParsingException("Error configuring " + getClass() + " buttons: Cannot access class fields.");
		}
		this.size = sxmlLoader.size();
		sxmlLoader.title().ifPresent(t -> this.title = t);
	}

	public static class Coordinates {

		@Getter
		private final int x;

		@Getter
		private final int y;

		public Coordinates(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

	}

}
