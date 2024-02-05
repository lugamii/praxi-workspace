package com.minexd.fairfight.check.impl.inventory;

import com.minexd.fairfight.check.checks.PacketCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.entity.Player;

public class InventoryG extends PacketCheck {

    private boolean sent;
    private boolean vehicle;

    public InventoryG(PlayerData playerData) {
        super(playerData, "Inventory (Check 7)");
    }

    @Override
    public void handleCheck(Player player, Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.sent) {
                this.alert(AlertType.EXPERIMENTAL, player, "", true);
            }
            final boolean b = false;
            this.vehicle = b;
            this.sent = b;
        } else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand) packet).a() ==
                PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            if (this.playerData.isSprinting() && !this.vehicle) {
                this.sent = true;
            }
        } else if (packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction) packet).b() ==
                PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
            this.sent = false;
        } else if (packet instanceof PacketPlayInSteerVehicle) {
            this.vehicle = true;
        }
    }

}
