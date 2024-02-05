package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;

public class KillAuraQ extends PacketCheck {

    private boolean sentAttack;
    private boolean sentInteract;

    public KillAuraQ(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 17)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            if (this.sentAttack && !this.sentInteract && this.alert(AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);

                if (!this.playerData.isBanning() && violations > 2) {
                    this.ban(player);
                }
            }
        } else if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity.EnumEntityUseAction action = ((PacketPlayInUseEntity) packet).a();

            if (action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.sentAttack = true;
            } else if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                this.sentInteract = true;
            }
        } else if (packet instanceof PacketPlayInFlying) {
            final boolean b = false;
            this.sentInteract = b;
            this.sentAttack = b;
        }
    }

}
