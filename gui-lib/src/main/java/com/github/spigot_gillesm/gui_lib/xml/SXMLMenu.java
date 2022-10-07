package com.github.spigot_gillesm.gui_lib.xml;

/*public class SXMLMenu extends SimpleMenu {

	private final Scene scene;

	protected SXMLMenu(@NotNull final SXMLLoader sxmlLoader) throws ParsingException {
		this.scene = new Scene(sxmlLoader);

		init();
	}

	protected void init() throws ParsingException {
		scene.load(getClass());
		setTitle(scene.getTitle());
	}

	@Override
	protected ItemStack getSlotItem(int slot) {
		for(final var entrySet : scene.getButtons().entrySet()) {
			final int actualCoordinates = entrySet.getKey().getX() * 9 + entrySet.getKey().getY();

			if(slot == actualCoordinates) {
				return entrySet.getValue().getIcon();
			}
		}

		return null;
	}

}*/
