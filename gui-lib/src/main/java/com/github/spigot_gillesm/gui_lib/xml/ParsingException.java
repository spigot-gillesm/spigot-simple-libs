package com.github.spigot_gillesm.gui_lib.xml;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class ParsingException extends Exception {

	@Getter
	@Nullable
	private final String message;

	public ParsingException() {
		this.message = null;
	}

	public ParsingException(final @Nullable String message) {
		this.message = message;
	}

}
