package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

import org.bukkit.entity.Player;

public class KillAuraI extends PacketCheck {

    private boolean sent;

    public KillAuraI(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 9)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig) packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
            if (this.sent) {
                this.alert(AlertType.EXPERIMENTAL, player, "", false);
            }
        } else if (packet instanceof PacketPlayInArmAnimation) {
            this.sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }

}
