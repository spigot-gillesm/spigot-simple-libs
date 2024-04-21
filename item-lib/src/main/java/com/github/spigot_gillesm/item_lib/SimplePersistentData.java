package com.github.spigot_gillesm.item_lib;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.persistence.PersistentDataType;

public class SimplePersistentData<T, Z> {

	@Getter(AccessLevel.PACKAGE)
	private final String key;

	@Getter(AccessLevel.PACKAGE)
	private final PersistentDataType<T, Z> persistentDataType;

	@Getter(AccessLevel.PACKAGE)
	private final Z value;

	SimplePersistentData(final String key, final PersistentDataType<T, Z> persistentDataType, final Z value) {
		this.key = key;
		this.persistentDataType = persistentDataType;
		this.value = value;
	}

}
