package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;

public class KillAuraA extends PacketCheck {
    private boolean sent;

    public KillAuraA(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 1)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity
                .EnumEntityUseAction.ATTACK) {
            if (!this.sent) {
                if (this.alert(AlertType.RELEASE, player, "", true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 5) {
                        this.ban(player);
                    }
                }
            } else {
                this.sent = false;
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
