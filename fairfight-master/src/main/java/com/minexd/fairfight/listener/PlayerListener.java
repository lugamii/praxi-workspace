package com.minexd.fairfight.listener;

import io.netty.buffer.Unpooled;
import java.text.MessageFormat;
import java.util.List;

import com.minexd.fairfight.FairFight;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.event.player.PlayerAlertEvent;
import com.minexd.fairfight.event.player.PlayerBanEvent;
import com.minexd.fairfight.mongo.FairFightLog;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.util.nucleus.util.TaskUtil;
import com.minexd.fairfight.util.zoot.util.CC;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		FairFight.getInstance().getPlayerDataManager().addPlayerData(event.getPlayer());

		if (event.getPlayer().hasPermission("fairfight.alerts")) {
			FairFight.getInstance().getReceivingAlerts().add(event.getPlayer().getUniqueId());
		}

		FairFight.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(FairFight.getInstance(), () -> {
			final PlayerConnection playerConnection = ((CraftPlayer) event.getPlayer()).getHandle().playerConnection;
			final PacketPlayOutCustomPayload packetPlayOutCustomPayload = new PacketPlayOutCustomPayload("REGISTER",
					new PacketDataSerializer(Unpooled.wrappedBuffer("CB-Client".getBytes()))
			);
			final PacketPlayOutCustomPayload packetPlayOutCustomPayload2 = new PacketPlayOutCustomPayload("REGISTER",
					new PacketDataSerializer(Unpooled.wrappedBuffer("CC".getBytes()))
			);

			playerConnection.sendPacket(packetPlayOutCustomPayload);
			playerConnection.sendPacket(packetPlayOutCustomPayload2);
		}, 10L);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		FairFight.getInstance().getReceivingAlerts().remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		final Player player = event.getPlayer();
		final PlayerData playerData = FairFight.getInstance().getPlayerDataManager().getPlayerData(player);

		if (playerData != null) {
			playerData.setSendingVape(true);
		}
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		final Player player = event.getPlayer();
		final PlayerData playerData = FairFight.getInstance().getPlayerDataManager().getPlayerData(player);

		if (playerData != null) {
			playerData.setInventoryOpen(false);
		}
	}

	@EventHandler
	public void onPlayerAlert(PlayerAlertEvent event) {
		if (!FairFight.getInstance().isAntiCheatEnabled()) {
			event.setCancelled(true);
			return;
		}

		final Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		final PlayerData playerData = FairFight.getInstance().getPlayerDataManager().getPlayerData(player);

		if (playerData == null) {
			return;
		}

		final String extra = event.getExtra() == null ? "" : " (" + event.getExtra() + ")";
		final String message = CC.translate(new MessageFormat(FairFight.getInstance().getConfig().getString("name") + " {0} &ehas flagged &b{1} &7(Ping: {2}){3}")
				.format(new Object[]{
				        player.getName(), event.getCheckName(), player.getPing(), extra
						// Profile.getProfiles().get(player.getUniqueId()).getColoredUsername(), event.getCheckName(), player.getPing(), extra
						// NucleusAPI.getColoredName(player), event.getCheckName(), player.getPing(), extra
				}));

		if (event.getAlertType() == AlertType.RELEASE) {
			for (Player onlinePlayer : FairFight.getInstance().getServer().getOnlinePlayers()) {
				if (FairFight.getInstance().canAlert(onlinePlayer)) {
					onlinePlayer.sendMessage(message);
				}
			}
		}

		FairFightLog.getQueue().add(new FairFightLog(
				player.getUniqueId(),
				event.getCheckName(),
				playerData.getClient().getName(),
				player.getPing(),
				MinecraftServer.getServer().tps1.getAverage(),
				System.currentTimeMillis()
		));
	}

	@EventHandler
	public void onPlayerBan(PlayerBanEvent event) {
		if (!FairFight.getInstance().isAntiCheatEnabled()) {
			event.setCancelled(true);
			return;
		}

		final Player player = event.getPlayer();

		if (player == null) {
			return;
		}

		final List<String> messages = FairFight.getInstance().getConfig().getStringList("ban-broadcast");

		for (String s : messages) {
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', s).replaceAll("%player%", player.getName()));
		}

		TaskUtil.run(() -> {
			FairFight.getInstance().getServer().dispatchCommand(
					FairFight.getInstance().getServer().getConsoleSender(),
					FairFight.getInstance().getConfig().getString("ban-command").replaceAll("%player%", player.getName())
			);
		});
	}

}
