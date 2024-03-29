package com.minexd.fairfight.check.impl.velocity;

import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.update.PositionUpdate;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import com.minexd.fairfight.check.checks.PositionCheck;

public class VelocityC extends PositionCheck {

	public VelocityC(PlayerData playerData) {
		super(playerData, "Velocity (Check 3)");
	}

	@Override
	public void handleCheck(final Player player, final PositionUpdate update) {
		double offsetY = update.getTo().getY() - update.getFrom().getY();
		double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(),
				update.getTo().getZ() - update.getFrom().getZ());

		double velocityH = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());

		EntityPlayer entityPlayer = ((CraftPlayer) update.getPlayer()).getHandle();
		if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() &&
		    !this.playerData.isUnderBlock() && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() &&
		    !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() &&
		    update.getFrom().getY() % 1.0 == 0.0 && offsetY > 0.0 && offsetY < 0.41999998688697815 &&
		    velocityH > 0.45 && !entityPlayer.world.c(entityPlayer.getBoundingBox().grow(1.0, 0.0, 1.0))) {
			double ratio = offsetH / velocityH;
			double vl = this.getVl();
			if (ratio < 0.62) {
				if ((vl += 1.1) >= 8.0 && this.alert(AlertType.RELEASE, player,
						String.format("P %s. VL %.2f.", Math.round(ratio * 100.0), vl), false) &&
				    !this.playerData.isBanning() && vl >= 20.0) {
					this.ban(player);
				}
			} else {
				vl -= 0.4;
			}
			this.setVl(vl);
		}
	}

}
