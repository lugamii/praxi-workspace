package com.minexd.fairfight.check.impl.aimassist;

import com.minexd.fairfight.check.checks.RotationCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.MathUtil;
import com.minexd.fairfight.util.update.RotationUpdate;

import org.bukkit.entity.Player;

public class AimAssistC extends RotationCheck {

    public AimAssistC(PlayerData playerData) {
        super(playerData, "Aim (Check 3)");
    }

    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }

        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        double vl = this.getVl();

        if (update.getFrom().getPitch() == update.getTo().getPitch() && diffYaw >= 3.0f && update.getFrom().getPitch() != 90.0f && update.getTo().getPitch() != 90.0f) {
            if ((vl += 0.9) >= 6.3) {
                this.alert(AlertType.EXPERIMENTAL, player, String.format("Y %.1f. VL %.1f.", diffYaw, vl), false);
            }
        } else {
            vl -= 1.6;
        }

        this.setVl(vl);
    }

}
