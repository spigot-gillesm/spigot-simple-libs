package com.github.spigot_gillesm.spell_lib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.CompletableFuture;

/**
 * <b>ReflectionUtils</b> - Reflection handler for NMS and CraftBukkit.<br>
 * Caches the packet related methods and is asynchronous.
 * <p>
 * This class does not handle null checks as most of the requests are from the
 * other utility classes that already handle null checks.
 * <p>
 * <a href="https://wiki.vg/Protocol">Clientbound Packets</a> are considered fake
 * updates to the client without changing the actual data. Since all the data is handled
 * by the server.
 *
 * @author Crypto Morin
 * @version 2.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketUtil {
	/**
	 * We use reflection mainly to avoid writing a new class for version barrier.
	 * The version barrier is for NMS that uses the Minecraft version as the main package name.
	 * <p>
	 * E.g. EntityPlayer in 1.15 is in the class {@code net.minecraft.server.v1_15_R1}
	 * but in 1.14 it's in {@code net.minecraft.server.v1_14_R1}
	 * In order to maintain cross-version compatibility we cannot import these classes.
	 */
	public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	public static final String CRAFTBUKKIT = "org.bukkit.craftbukkit." + VERSION + '.';
	public static final String NMS = "net.minecraft.server." + VERSION + '.';

	private static final MethodHandle PLAYER_CONNECTION;
	private static final MethodHandle GET_HANDLE;
	private static final MethodHandle SEND_PACKET;

	static {

		Class<?> entityPlayer = getNMSClass("EntityPlayer");
		Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");
		Class<?> playerConnection = getNMSClass("PlayerConnection");

		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle sendPacket = null;
		MethodHandle getHandle = null;
		MethodHandle connection = null;

		try {

			connection = lookup.findGetter(entityPlayer, "playerConnection", playerConnection);
			getHandle = lookup.findVirtual(craftPlayer, "getHandle", MethodType.methodType(entityPlayer));
			sendPacket = lookup.findVirtual(playerConnection, "sendPacket", MethodType.methodType(void.class, getNMSClass("Packet")));

		} catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException ex) {

			ex.printStackTrace();

		}

		PLAYER_CONNECTION = connection;
		SEND_PACKET = sendPacket;
		GET_HANDLE = getHandle;
	}

	/**
	 * Get a NMS (net.minecraft.server) class.
	 *
	 * @param name the name of the class.
	 * @return the NMS class or null if not found.
	 * @since 1.0.0
	 */
	@Nullable
	public static Class<?> getNMSClass(@NotNull String name) {
		try {
			return Class.forName(NMS + name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Sends a packet to the player asynchronously if they're online.
	 * Packets are thread-safe.
	 *
	 * @param player  the player to send the packet to.
	 * @param packets the packets to send.
	 * @return the async thread handling the packet.
	 * @see #sendPacketSync(Player, Object...)
	 * @since 1.0.0
	 */
	@NotNull
	public static CompletableFuture<Void> sendPacket(@NotNull Player player, @NotNull Object... packets) {
		return CompletableFuture.runAsync(() -> sendPacketSync(player, packets))
				.exceptionally(ex -> {
					ex.printStackTrace();
					return null;
				});
	}

	/**
	 * Sends a packet to the player synchronously if they're online.
	 *
	 * @param player  the player to send the packet to.
	 * @param packets the packets to send.
	 * @see #sendPacket(Player, Object...)
	 * @since 2.0.0
	 */
	public static void sendPacketSync(@NotNull Player player, @NotNull Object... packets) {
		try {
			Object handle = GET_HANDLE.invoke(player);
			Object connection = PLAYER_CONNECTION.invoke(handle);

			// Checking if the connection is not null is enough. There is no need to check if the player is online.
			if (connection != null) {
				for (Object packet : packets) SEND_PACKET.invoke(connection, packet);
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	/**
	 * Get a CraftBukkit (org.bukkit.craftbukkit) class.
	 *
	 * @param name the name of the class to load.
	 * @return the CraftBukkit class or null if not found.
	 * @since 1.0.0
	 */
	@Nullable
	public static Class<?> getCraftClass(@NotNull String name) {
		try {
			return Class.forName(CRAFTBUKKIT + name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
