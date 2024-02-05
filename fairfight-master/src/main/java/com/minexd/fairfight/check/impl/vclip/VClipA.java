package com.minexd.fairfight.check.impl.vclip;

import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.nucleus.BlockUtil;
import com.minexd.fairfight.util.update.PositionUpdate;
import com.minexd.fairfight.check.checks.PositionCheck;
import com.minexd.fairfight.player.PlayerData;

import org.bukkit.entity.Player;

public class VClipA extends PositionCheck {

    public VClipA(PlayerData playerData) {
        super(playerData, "V-Clip (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        double difference = update.getTo().getY() - update.getFrom().getY();
        if (difference >= 2.0 && !BlockUtil.isBlockFaceAir(player)) {
            player.teleport(update.getFrom());
            this.alert(AlertType.RELEASE, player, "", true);
        }
    }

}
