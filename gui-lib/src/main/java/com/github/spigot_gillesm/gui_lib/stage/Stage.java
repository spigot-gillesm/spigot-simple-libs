package com.github.spigot_gillesm.gui_lib.stage;

import lombok.Setter;

public class Stage {

	@Setter
	private Scene scene;

	public Stage() { }

	public Stage(final Scene scene) {
		this.scene = scene;
	}

}
