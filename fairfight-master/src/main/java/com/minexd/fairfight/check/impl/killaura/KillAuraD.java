package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;

public class KillAuraD extends PacketCheck {

    public KillAuraD(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 4)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity
                .EnumEntityUseAction.ATTACK && this.playerData.isPlacing() && this.alert(AlertType.RELEASE, player,
                "", true)) {
            final int violations = this.playerData.getViolations(this, 60000L);
            if (!this.playerData.isBanning() && violations > 2) {
                this.ban(player);
            }
        }
    }

}
