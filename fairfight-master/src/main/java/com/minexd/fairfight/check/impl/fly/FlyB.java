package com.minexd.fairfight.check.impl.fly;

import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.update.PositionUpdate;
import org.bukkit.entity.Player;

import com.minexd.fairfight.check.checks.PositionCheck;

public class FlyB extends PositionCheck {

    public FlyB(PlayerData playerData) {
        super(playerData, "Flight (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();

        if (!this.playerData.isInLiquid() && !this.playerData.isOnGround()) {
            final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
            final double offsetY = update.getTo().getY() - update.getFrom().getY();

            if (offsetH > 0.0 && offsetY == 0.0) {
                if (++vl >= 10 && this.alert(AlertType.RELEASE, player, String.format("H %.2f. VL %s.", offsetH, vl), true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);

                    if (!this.playerData.isBanning() && violations > 8) {
                        this.ban(player);
                    }
                }
            } else {
                vl = 0;
            }
        } else {
            vl = 0;
        }

        this.setVl(vl);
    }

}
