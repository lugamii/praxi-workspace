package com.minexd.fairfight.check.impl.fly;

import com.minexd.fairfight.check.checks.PositionCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.update.PositionUpdate;
import org.bukkit.entity.Player;

public class FlyD extends PositionCheck {

    public FlyD(PlayerData playerData) {
        super(playerData, "Flight (Check 4)");
    }

    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double offsetY = update.getTo().getY() - update.getFrom().getY();
        if (this.playerData.getVelocityY() == 0.0 && this.playerData.isWasOnGround() && !this.playerData.isUnderBlock
                () && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData
                .isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() && !this.playerData
                .isOnStairs() && offsetY > 0.0 && offsetY < 0.41999998688697815 && update.getFrom().getY() % 1.0 ==
                0.0) {
            this.alert(AlertType.EXPERIMENTAL, player, String.format("O %.2f.", offsetY), false);
        }
    }

}
