package com.minexd.fairfight.check.impl.velocity;

import com.minexd.fairfight.check.checks.PositionCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.MathUtil;
import com.minexd.fairfight.util.update.PositionUpdate;

import org.bukkit.entity.Player;

public class VelocityA extends PositionCheck {

    public VelocityA(PlayerData playerData) {
        super(playerData, "Velocity (Check 1)");
    }

    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int) this.getVl();

        if (this.playerData.getVelocityY() > 0.0 && !this.playerData.isUnderBlock() && !this.playerData
                .isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this
                .playerData.isInWeb() && !this.playerData.isWasInWeb() && System.currentTimeMillis() - this
                .playerData.getLastDelayedMovePacket() > 220L && System.currentTimeMillis() - this.playerData
                .getLastMovePacket().getTimestamp() < 110L) {
            final int threshold = 10 + MathUtil.pingFormula(this.playerData.getPing()) * 2;

            if (++vl >= threshold) {
                if (this.alert(AlertType.RELEASE, player, "VL " + vl + ".", true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);

                    if (!this.playerData.isBanning() && violations > Math.max(this.playerData.getPing() / 10L, 15L)) {
                        this.ban(player);
                    }
                }

                this.playerData.setVelocityY(0.0);

                vl = 0;
            }
        } else {
            vl = 0;
        }

        this.setVl(vl);
    }

}
