package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;

public class KillAuraP extends PacketCheck {

    private boolean sent;

    public KillAuraP(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 16)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand) packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            if (this.sent && this.alert(AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);

                if (!this.playerData.isBanning() && violations > 2) {
                    this.ban(player);
                }
            }
        } else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity) packet).a() ==
                PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            this.sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }

}
