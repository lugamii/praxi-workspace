package com.minexd.fairfight.client;

import com.google.common.collect.ImmutableMap;

import com.minexd.fairfight.FairFight;
import com.minexd.fairfight.player.PlayerData;
import com.minexd.fairfight.event.player.AlertType;
import com.minexd.fairfight.event.player.PlayerAlertEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class ClientManager {

    public static final Map<String, String> BLACKLISTED_MODS;

    static {
        BLACKLISTED_MODS = ImmutableMap.of("MouseTweaks", "Mouse Tweaks", "Particle Mod", "Particle Mod", "npcmod", "NPC Mod");
    }

    private Set<ClientType> clientTypes;

    public ClientManager() {
        this.clientTypes = new HashSet<>();
        this.clientTypes.add(new ModClientType("Ethylene", "ethylene", null));
        this.clientTypes.add(new ModClientType("Ghost Client (Generic)", "gc", null));
        this.clientTypes.add(new ModClientType("Merge Aimbot", "Aimbot", null));
        this.clientTypes.add(new ModClientType("Cracked Vape v2.49", "mergeclient", null));
        this.clientTypes.add(new ModClientType("Cracked Vape v2.50", "wigger", null));
        this.clientTypes.add(new ModClientType("OpenComputers", "OpenComputers", "1.0"));
        this.clientTypes.add(new ModClientType("Schematica Reach", "Schematica", "1.7.6.git"));
        this.clientTypes.add(new ModClientType("TimeChanger Misplace", "timechanger", "1.0 "));
        this.clientTypes.add(new ModClientType("TcpNoDelay Clients", "TcpNoDelayMod-2.0", "1.0"));
        this.clientTypes.add(new PayloadClientType("Cracked Vape v2.06", "LOLIMAHCKER", true));
        this.clientTypes.add(new PayloadClientType("Cracked Merge", "cock", true));
        this.clientTypes.add(new PayloadClientType("BspkrsCore Client 1", "customGuiOpenBspkrs", true));
        this.clientTypes.add(new PayloadClientType("BspkrsCore Client 2", "0SO1Lk2KASxzsd", true));
        this.clientTypes.add(new PayloadClientType("BspkrsCore Client 3", "mincraftpvphcker", true));
        this.clientTypes.add(new PayloadClientType("Cracked Incognito", "lmaohax", true));
        this.clientTypes.add(new PayloadClientType("Old TimeChanger Misplace", "MCnetHandler", true));
        this.clientTypes.add(new PayloadClientType("OCMC", "OCMC", false));
        this.clientTypes.add(new PayloadClientType("CheatBreaker", "CB-Client", false));
        this.clientTypes.add(new PayloadClientType("Cosmic Client", "CC", false));
        this.clientTypes.add(new PayloadClientType("Labymod", "LABYMOD", false));
    }

    public void onModList(PlayerData playerData, Player player, Map<String, String> mods) {
        ClientType type = null;

        typeCheck:
        for (Map.Entry<String, String> entry : mods.entrySet()) {
            for (ClientType clientType : this.clientTypes) {
                if (clientType instanceof ModClientType) {
                    final ModClientType modClientType = (ModClientType) clientType;

                    if (modClientType.getModId().equalsIgnoreCase(entry.getKey()) && modClientType.getModVersion().equalsIgnoreCase(entry.getValue())) {
                        type = modClientType;
                        break typeCheck;
                    }
                }
            }
        }

        if (type == null) {
            type = EnumClientType.FORGE;

            final StringJoiner blacklisted = new StringJoiner(", ");
            boolean kick = false;

            for (String modId : ClientManager.BLACKLISTED_MODS.keySet()) {
                if (mods.containsKey(modId)) {
                    blacklisted.add(ClientManager.BLACKLISTED_MODS.get(modId));
                    kick = true;
                }
            }

            if (kick) {
                player.kickPlayer(ChatColor.RED + "[FairFight] Blacklisted modification: " + blacklisted.toString());
            }
        }

        playerData.setClient(type);
        playerData.setForgeMods(mods);

        if (type.isHacked()) {
            playerData.setRandomBanRate(500.0);
            playerData.setRandomBanReason(type.getName());
            playerData.setRandomBan(true);

            FairFight.getInstance().getServer().getPluginManager().callEvent(new PlayerAlertEvent(AlertType.RELEASE, player, type.getName(), null));
        }
    }

}
