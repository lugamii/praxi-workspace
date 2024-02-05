package com.minexd.fairfight.check.impl.step;

import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.util.update.PositionUpdate;
import org.bukkit.entity.Player;
import com.minexd.fairfight.check.checks.PositionCheck;

public class StepA extends PositionCheck {

	public StepA(PlayerData playerData) {
		super(playerData, "Step (Check 1)");
	}

	@Override
	public void handleCheck(final Player player, final PositionUpdate update) {
		double height = 0.9;
		double difference = update.getTo().getY() - update.getFrom().getY();

		if (difference > height) {
			this.alert(AlertType.EXPERIMENTAL, player, "", true);
		}
	}

}
