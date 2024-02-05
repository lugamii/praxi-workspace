package com.minexd.fairfight.check.checks;

import com.minexd.fairfight.check.AbstractCheck;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.util.update.RotationUpdate;

public abstract class RotationCheck extends AbstractCheck<RotationUpdate> {

    public RotationCheck(PlayerData playerData, String name) {
        super(playerData, RotationUpdate.class, name);
    }

}
