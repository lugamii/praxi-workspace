package com.minexd.fairfight.check.impl.vclip;

import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.nucleus.BlockUtil;
import com.minexd.fairfight.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.minexd.fairfight.check.checks.PositionCheck;

public class VClipB extends PositionCheck {

    public VClipB(PlayerData playerData) {
        super(playerData, "V-Clip (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {

        final double difference = update.getTo().getY() - update.getFrom().getY();

        if (difference >= 2.0 && BlockUtil.isSlab(player)) {
            player.teleport(update.getFrom());
            this.alert(AlertType.RELEASE, player, "", true);
        }
    }
}
