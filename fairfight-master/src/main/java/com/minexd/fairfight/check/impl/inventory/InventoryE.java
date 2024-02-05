package com.minexd.fairfight.check.impl.inventory;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;

import org.bukkit.entity.Player;

public class InventoryE extends PacketCheck {

    private boolean sent;

    public InventoryE(PlayerData playerData) {
        super(playerData, "Inventory (Check 5)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInWindowClick) {
            if (this.sent) {
                this.alert(AlertType.EXPERIMENTAL, player, "", true);
            }
        } else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand) packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            this.sent = true;
        } else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }

}
