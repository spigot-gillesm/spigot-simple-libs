package com.github.spigot_gillesm.player_lib;

import com.github.spigot_gillesm.file_utils.FileUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerData {

	@Getter(AccessLevel.PACKAGE)
	private final UUID uuid;

	private final Map<String, Boolean> values = new HashMap<>();

	PlayerData(final UUID uuid) {
		this.uuid = uuid;
	}

	private Optional<Player> getPlayer() {
		return Optional.ofNullable(Bukkit.getServer().getPlayer(uuid));
	}

	/**
	 * Set the tag's current value to the given object.
	 *
	 * @param tag the string representing the tag
	 * @param object the object
	 * @param saveOnQuit whether to save the value when the player quits the server
	 */
	public void setRawValue(@NotNull final String tag, final Object object, final boolean saveOnQuit) {
		getPlayer().ifPresent(player -> {
			player.setMetadata(tag, new FixedMetadataValue(PlayerLib.plugin, object));
			values.put(tag, saveOnQuit);
		});
	}

	/**
	 * Set the tag's current value to the given object.
	 *
	 * @param tag the string representing the tag
	 * @param object the object
	 */
	public void setRawValue(@NotNull final String tag, final Object object) {
		getPlayer().ifPresent(player -> {
			player.setMetadata(tag, new FixedMetadataValue(PlayerLib.plugin, object));
			values.put(tag, false);
		});
	}

	/**
	 * Set the tag's current value to the given object.
	 *
	 * @param tag the player tag
	 * @param object the object
	 */
	public void setTagValue(@NotNull final PlayerTag tag, final Object object) {
		setRawValue(tag.toString(), object, tag.saveOnQuit());
	}

	/**
	 * Get the tag's current value.
	 *
	 * @param tag the string representing the tag
	 * @return the tag's value
	 */
	@Nullable
	public Object getRawValue(final String tag) {
		return getPlayer()
				.map(player -> player.hasMetadata(tag) ? player.getMetadata(tag).get(0).value() : null)
				.orElse(null);
	}

	/**
	 * Get the tag's current value.
	 *
	 * @param tag the player tag
	 * @return the tag's value
	 */
	@Nullable
	public Object getTagValue(final PlayerTag tag) {
		return getRawValue(tag.toString());
	}

	/**
	 * Get a map of the player's current tags and their value.
	 *
	 * @return a map of the player's tag and their value
	 */
	public Map<String, Object> getMap() {
		final Map<String, Object> tagValues = new HashMap<>();
		getPlayer().ifPresent(player -> values.forEach((tag, bool) -> tagValues.put(tag, getRawValue(tag))));

		return tagValues;
	}

	private boolean saveOnQuit(final String tag) {
		return values.containsKey(tag);
	}

	/**
	 * Get the tag's current collection of values.
	 *
	 * @param tag the string representing the tag
	 * @param <T> the collection's type
	 * @return the tag's collection of values
	 * @throws IllegalArgumentException if the current value is not a collection of type T
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getRawList(final String tag) {
		try {
			return getRawValue(tag) != null ? (Collection<T>) getRawValue(tag) : new ArrayList<>();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(tag + " value is not a collection of the given type");
		}
	}

	/**
	 * Get the tag's current collection of values.
	 *
	 * @param tag the string representing the tag
	 * @param <T> the collection's type
	 * @param clazz the type's class
	 * @return the tag's collection of values
	 * @throws IllegalArgumentException if the current value is not a collection of type T
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getRawList(final String tag, Class<T> clazz) {
		try {
			return getRawValue(tag) != null ? (Collection<T>) getRawValue(tag) : new ArrayList<>();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException(tag + " value is not a collection of the given type");
		}
	}

	/**
	 * Get the tag's current collection of values.
	 *
	 * @param tag the player tag
	 * @param <T> the collection's type
	 * @return the tag's collection of values
	 * @throws IllegalArgumentException if the current value is not a collection of type T
	 */
	public <T> Collection<T> getTagList(final PlayerTag tag) {
		return getRawList(tag.toString());
	}

	/**
	 * Get the tag's current collection of values.
	 *
	 * @param tag the player tag
	 * @param <T> the collection's type
	 * @param clazz the type's class
	 * @return the tag's collection of values
	 * @throws IllegalArgumentException if the current value is not a collection of type T
	 */
	public <T> Collection<T> getTagList(final PlayerTag tag, Class<T> clazz) {
		return getRawList(tag.toString(), clazz);
	}

	/**
	 * Add the given object to the tag's current collection of values.
	 *
	 * @param tag the string representing the tag
	 * @param object the object to add
	 * @param saveOnQuit whether to save the value when the player quits the server
	 * @throws IllegalArgumentException if the current value is not a collection of type T
	 */
	public void addRawValue(final String tag, final Object object, final boolean saveOnQuit) {
		final var value = getRawList(tag);
		value.add(object);
		setRawValue(tag, value, saveOnQuit);
	}

	/**
	 * Add the given object to the tag's current collection of values.
	 *
	 * @param tag the player tag
	 * @param object the object to add
	 * @throws IllegalArgumentException if the current value is not a collection of type T
	 */
	public void addTagValue(final PlayerTag tag, final Object object) {
		addRawValue(tag.toString(), object, tag.saveOnQuit());
	}

	/**
	 * Remove the tag from the player if it exists.
	 *
	 * @param tag the player tag
	 */
	public void removeValue(final PlayerTag tag) {
		removeRawValue(tag.toString());
	}

	/**
	 * Remove the tag from the player if it exists.
	 *
	 * @param tag the string representing the tag
	 */
	public void removeRawValue(final String tag) {
		getPlayer().ifPresent(player -> player.removeMetadata(tag, PlayerLib.plugin));
	}

	/**
	 * Write to the player's data file all the stored tags and their value.
	 */
	public void writeToFile() {
		final var file = FileUtils.getResource("player_data/" + uuid.toString() + ".yml");
		final var conf = FileUtils.clearContent(file);

		getMap().forEach((tag, value) -> {
			if(saveOnQuit(tag) && value != null) {
				conf.set("tags." + tag, value.toString());
			}
		});
		getPlayer().ifPresent(p -> conf.set("name", p.getName()));
		FileUtils.saveConfiguration(file, conf);
	}

	/**
	 * Load from the player's data file all the stored tags and their value.
	 */
	public void loadFromFile() {
		final var conf = FileUtils.getConfiguration("player_data/" + uuid.toString() + ".yml");
		if(conf.contains("tags")) {
			conf.getConfigurationSection("tags").getValues(false).forEach(this::setRawValue);
		}
	}

}
