package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;

public class KillAuraH extends PacketCheck {

    private boolean sent;

    public KillAuraH(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 8)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig) packet).c();

            if ((digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK || digType ==
                    PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) && this.sent && this.alert(AlertType
                    .RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);

                if (!this.playerData.isBanning() && violations > 2) {
                    this.ban(player);
                }
            }
        } else if (packet instanceof PacketPlayInUseEntity) {
            this.sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }

}
