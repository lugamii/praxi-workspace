package com.minexd.fairfight.check.checks;

import com.minexd.fairfight.check.AbstractCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.util.update.PositionUpdate;

public abstract class PositionCheck extends AbstractCheck<PositionUpdate> {

    public PositionCheck(PlayerData playerData, String name) {
        super(playerData, PositionUpdate.class, name);
    }

}
