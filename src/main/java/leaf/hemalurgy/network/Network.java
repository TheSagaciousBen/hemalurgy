/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the New Tardis Mod team.
 * That mod taught me how to do proper syncing between server and client.
 * https://tardis-mod.com/books/home/page/links#bkmrk-source
 */

package leaf.hemalurgy.network;

import leaf.hemalurgy.Hemalurgy;
import leaf.hemalurgy.network.packets.SyncPlayerSpiritwebMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class Network
{
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Hemalurgy.MODID, Hemalurgy.MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);


	public static void init()
	{
		int id = 0;
		NETWORK_CHANNEL.registerMessage(id++, SyncPlayerSpiritwebMessage.class, SyncPlayerSpiritwebMessage::encode, SyncPlayerSpiritwebMessage::decode, SyncPlayerSpiritwebMessage::handle);
	}


	/**
	 * Sends a packet to the server.<br>
	 * Must be called Client side.
	 */
	public static void sendToServer(Object msg)
	{
		NETWORK_CHANNEL.sendToServer(msg);
	}

	/**
	 * Send a packet to a specific player.<br>
	 * Must be called Server side.
	 */
	public static void sendTo(Object msg, ServerPlayer player)
	{
		if (!(player instanceof FakePlayer))
		{
			NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
		}
	}

	public static void sendPacketToAll(Object packet)
	{
		NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
	}


	public static void sendToAllAround(Object mes, ResourceKey<Level> dim, BlockPos pos, int radius)
	{
		NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), radius, dim)), mes);
	}

	public static void sendToAllInWorld(Object mes, ServerLevel world)
	{
		NETWORK_CHANNEL.send(PacketDistributor.DIMENSION.with(world::dimension), mes);
	}

	public static void sendToTrackingTE(Object mes, BlockEntity te)
	{
		if (te != null && !te.getLevel().isClientSide)
		{
			NETWORK_CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> te.getLevel().getChunkAt(te.getBlockPos())), mes);
		}
	}
}
