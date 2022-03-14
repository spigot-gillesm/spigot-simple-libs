package com.github.spigot_gillesm.epicworlds_utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChunkScanner {

	private static final int CHUNK_SIZE_X = 16;
	private static final int CHUNK_SIZE_Z = 16;

	private final Chunk chunk;

	private final Predicate<Material> predicate;

	private final int maxHeight;
	private final int minHeight;

	private final List<Coordinate> blocksFound = new ArrayList<>();

	public ChunkScanner(final Chunk chunk, final Predicate<Material> predicate) {
		this.predicate = predicate;
		this.chunk = chunk;
		this.maxHeight = chunk.getWorld().getMaxHeight();
		this.minHeight = chunk.getWorld().getMinHeight();
	}

	/**
	 * Scan the chunk to retrieve all the targeted block coordinates.
	 *
	 * @param consumer the consumer to run on each retrieved blockdata
	 */
	public void scan(final Consumer<Block> consumer) {
		final var chunkSnapshot = chunk.getChunkSnapshot();
		EpicworldsUtils.getInstance().getServer().getScheduler().runTaskAsynchronously(EpicworldsUtils.getInstance(),
				() -> {
					final List<Coordinate> threadBlockFound = new ArrayList<>();
					for(var x = 0; x < CHUNK_SIZE_X; x++) {
						for(var z = 0; z < CHUNK_SIZE_Z; z++) {
							for(var y = minHeight; y < maxHeight; y++) {
								if(predicate.test(chunkSnapshot.getBlockType(x, y, z))) {
									threadBlockFound.add(new Coordinate(x, y, z));
								}
							}
						}
					}
					blocksFound.addAll(threadBlockFound);
					Bukkit.getServer().getScheduler().runTask(EpicworldsUtils.getInstance(),
							() -> getFoundBlocks().forEach(consumer));
				});
	}

	public List<Block> getFoundBlocks() {
		final List<Block> blockData = new ArrayList<>();
		blocksFound.forEach(coordinate -> blockData.add(coordinate.getBlockDataFromWorld(chunk)));

		return blockData;
	}

	private static class Coordinate {

		private final int x;
		private final int y;
		private final int z;

		public Coordinate(final int x, final int y, final int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Block getBlockDataFromWorld(final Chunk chunk) {
			return chunk.getBlock(x, y, z);
		}

	}

}
