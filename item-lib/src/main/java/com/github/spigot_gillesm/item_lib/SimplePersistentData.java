package com.github.spigot_gillesm.item_lib;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SimplePersistentData<T, Z> {

	@Getter(AccessLevel.PACKAGE)
	private final String key;

	@Getter(AccessLevel.PACKAGE)
	private final PersistentDataType<T, Z> persistentDataType;

	@Getter(AccessLevel.PACKAGE)
	private final Z value;

	SimplePersistentData(@NotNull String key, @NotNull PersistentDataType<T, Z> persistentDataType, @NotNull Z value) {
		this.key = key;
		this.persistentDataType = persistentDataType;
		this.value = value;
	}

}
