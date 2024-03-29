package com.minexd.fairfight.check.impl.badpackets;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

import org.bukkit.entity.Player;

public class BadPacketsC extends PacketCheck {

    private boolean sent;

    public BadPacketsC(PlayerData playerData) {
        super(playerData, "Packets (Check 3)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction playerAction = ((PacketPlayInEntityAction) packet).b();

            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING || playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                if (this.sent) {
                    if (this.alert(AlertType.RELEASE, player, "", true)) {
                        final int violations = this.playerData.getViolations(this, 60000L);

                        if (!this.playerData.isBanning() && violations > 2) {
                            this.ban(player);
                        }
                    }
                } else {
                    this.sent = true;
                }
            }
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }

}
