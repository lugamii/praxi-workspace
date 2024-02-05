package com.minexd.fairfight.check.impl.aimassist;

import com.minexd.fairfight.check.checks.RotationCheck;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.MathUtil;
import org.bukkit.entity.Player;
import com.minexd.fairfight.FairFight;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.util.update.RotationUpdate;

public class AimAssistE extends RotationCheck {

	private float lastPitchRate;
	private float lastYawRate;

	public AimAssistE(PlayerData playerData) {
		super(playerData, "Aim (Check 5)");
	}

	@Override
	public void handleCheck(Player player, RotationUpdate update) {
		if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
			return;
		}

		float diffPitch = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
		float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());

		float diffYawPitch = Math.abs(diffYaw - diffPitch);

		float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
		float diffYawRate = Math.abs(this.lastYawRate - diffYaw);

		float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
		float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);

		if (diffYaw > 0.05f && diffPitch > 0.05 && (diffPitchRate > 1.0 || diffYawRate > 1.0) &&
		    (diffPitchRatePitch > 1.0f || diffYawRateYaw > 1.0f) && diffYawPitch < 0.009f && diffYawPitch > 0.001f) {
			this.alert(AlertType.EXPERIMENTAL, player,
					String.format("DYP %.3f. DP %.3f. DY %.3f. DPR %.3f. DYR %.3f. DPRP %.3f. DYRY %.3f.",
							diffYawPitch, diffYaw, diffPitch, diffPitchRate, diffYawRate,
							diffPitchRatePitch, diffYawRateYaw), false);
		}

		this.lastYawRate = diffYaw;
		this.lastPitchRate = diffPitch;
	}

}
