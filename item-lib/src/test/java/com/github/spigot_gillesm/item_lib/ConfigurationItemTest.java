package com.github.spigot_gillesm.item_lib;

class ConfigurationItemTest {

	/*@Test
	void given_valid_input_then_matching_configuration_item_is_returned() throws IOException {
		final var classLoader = getClass().getClassLoader();
		final var file = new File(classLoader.getResource("valid_item.yml").getFile());

		final ItemFactory itemFactory = mock(ItemFactory.class);
		final Server server = mock(Server.class);
		final ItemMeta itemMeta = mock(ItemMeta.class);
		final MockedStatic<Bukkit> mockBukkit = Mockito.mockStatic(Bukkit.class);
		final MockedStatic<Server> mockServer = Mockito.mockStatic(Server.class);

		mockBukkit.when(Bukkit::getServer).thenReturn(server);
		mockBukkit.when(() -> Bukkit.getItemFactory()).thenReturn(itemFactory);
		mockServer.when(server::getItemFactory).thenReturn(itemFactory);
		when(itemFactory.getItemMeta(Material.WOODEN_SWORD)).thenReturn(itemMeta);

		final SimpleItem sourceItem = ConfigurationItem.getSimpleItemFromFile(file);
		final var targetItem = SimpleItem.newBuilder()
				.material(Material.WOODEN_SWORD)
				.displayName("&aTraining Sword")
				.damage(32)
				.lore("", "&fThis is a training sword!")
				.setAttackDamage(3, EquipmentSlot.HAND)
				.setAttackSpeed(1.6, EquipmentSlot.HAND)
				.addItemFlags(ItemFlag.HIDE_ENCHANTS)
				.build()
				.make();
		Assertions.assertEquals(sourceItem.getItemStack(), targetItem.getItemStack());

		mockBukkit.close();
		mockServer.close();
	}*/

	/*@Test
	void given_valid_input_then_matching_configuration_item_potion_is_returned() throws IOException {
		final var classLoader = getClass().getClassLoader();
		final var file = new File(classLoader.getResource("valid_potion.yml").getFile());

		final ConfigurationItem item = ConfigurationItem.getBuilderFromFile(file);
		Assertions.assertEquals(Material.POTION, item.getMaterial());
		Assertions.assertEquals("&dEpic Potion", item.getDisplayName());
		Assertions.assertEquals(PotionType.MUNDANE, item.getPotionType());

		final var healEffect = new ConfigurationItem.PotionEffectData();
		healEffect.setPotionEffectType("HEAL");
		healEffect.setAmplifier(2);
		final var strengthEffect = new ConfigurationItem.PotionEffectData();
		strengthEffect.setPotionEffectType("INCREASE_DAMAGE");
		strengthEffect.setAmplifier(1);
		strengthEffect.setDuration(20);
		final var potionData = new HashSet<>(Set.of(healEffect, strengthEffect));

		Assertions.assertEquals(potionData, item.getPotionEffectsData());
	}*/

}
