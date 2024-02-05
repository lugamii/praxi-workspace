package com.minexd.fairfight.check.impl.autoclicker;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.event.player.AlertType;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

import org.bukkit.entity.Player;

import com.minexd.fairfight.player.PlayerData;

public class AutoClickerA extends PacketCheck {

	private int swings;
	private int movements;

	public AutoClickerA(PlayerData playerData) {
		super(playerData, "Auto-Clicker (Check 1)");
	}

	@Override
	public void handleCheck(Player player, Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation
		    && !this.playerData.isDigging() && !this.playerData.isPlacing() && !this.playerData.isFakeDigging()
		    && (System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket()) > 220L
		    && (System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp()) < 110L) {
			++this.swings;
		} else if (packet instanceof PacketPlayInFlying && ++this.movements == 20) {
			if (this.swings > 20 &&
			    this.alert(AlertType.RELEASE, player, "C " + this.swings + ".", true)) {

				int violations = this.playerData.getViolations(this, 60000L);

				if (!this.playerData.isBanning() && violations > 3) {
					this.ban(player);
				}
			}

			this.playerData.setLastCps(this.swings);
			this.movements = this.swings = 0;
		}
	}

}
