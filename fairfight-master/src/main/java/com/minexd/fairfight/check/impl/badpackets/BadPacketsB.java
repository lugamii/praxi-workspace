package com.minexd.fairfight.check.impl.badpackets;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

import org.bukkit.entity.Player;

public class BadPacketsB extends PacketCheck {

    public BadPacketsB(PlayerData playerData) {
        super(playerData, "Packets (Check 2)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying && Math.abs(((PacketPlayInFlying) packet).e()) > 90.0f && this.alert
                (AlertType.RELEASE, player, "", false) && !this.playerData.isBanning() && !this.playerData
                .isRandomBan()) {
            this.randomBan(player, 200.0);
        }
    }

}
