package com.minexd.fairfight.check.impl.killaura;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;

public class KillAuraT extends PacketCheck {

    private boolean sent;

    public KillAuraT(PlayerData playerData) {
        super(playerData, "Kill-Aura (Check 20)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            if (!this.sent) {
                this.sent = true;
            }
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }

}
