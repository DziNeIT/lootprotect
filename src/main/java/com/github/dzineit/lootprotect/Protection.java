package com.github.dzineit.lootprotect;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public final class Protection {
	public final ItemStack stack;
	public final UUID owner;
	public final long start;
	public final long length;

	Protection(final ItemStack stack, final UUID owner, final long start,
			final long length) {
		this.stack = stack;
		this.owner = owner;
		this.start = start;
		this.length = length;
	}
}
