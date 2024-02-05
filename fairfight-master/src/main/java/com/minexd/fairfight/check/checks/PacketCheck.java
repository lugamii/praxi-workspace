package com.minexd.fairfight.check.checks;

import net.minecraft.server.v1_8_R3.Packet;
import com.minexd.fairfight.check.AbstractCheck;
import com.minexd.fairfight.player.PlayerData;

public abstract class PacketCheck extends AbstractCheck<Packet> {

    public PacketCheck(PlayerData playerData, final String name) {
        super(playerData, Packet.class, name);
    }

}
