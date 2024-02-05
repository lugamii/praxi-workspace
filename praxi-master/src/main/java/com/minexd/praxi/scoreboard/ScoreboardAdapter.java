package com.minexd.praxi.scoreboard;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.minexd.praxi.event.game.EventGame;
import com.minexd.praxi.profile.Profile;
import com.minexd.praxi.profile.ProfileState;
import com.minexd.praxi.queue.QueueProfile;
import com.minexd.zoot.util.CC;
import com.minexd.zoot.util.TimeUtil;
import java.util.ArrayList;
import java.util.List;
import com.minexd.praxi.Praxi;
import com.minexd.praxi.party.Party;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardAdapter implements BoardAdapter {

	private int inQueues;
	private int inFights;

	public ScoreboardAdapter() {
		new BukkitRunnable() {
			@Override
			public void run() {
				int inQueues = 0;
				int inFights = 0;

				for (Player player : Bukkit.getOnlinePlayers()) {
					Profile profile = Profile.getByUuid(player.getUniqueId());

					if (profile != null) {
						if (profile.getState() == ProfileState.QUEUEING) {
							inQueues++;
						} else if (profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.EVENT) {
							inFights++;
						}
					}
				}

				ScoreboardAdapter.this.inQueues = inQueues;
				ScoreboardAdapter.this.inFights = inFights;
			}
		}.runTaskTimerAsynchronously(Praxi.get(), 2L, 2L);
	}

	@Override
	public String getTitle(Player player) {
		return Praxi.get().getMainConfig().getString("SCOREBOARD.TITLE");
	}

	@Override
	public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> cooldowns) {
		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (!profile.getOptions().showScoreboard()) {
			return null;
		}

		List<String> lines = new ArrayList<>();

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
			lines.add("&cOnline: &f" + Bukkit.getOnlinePlayers().size());
			lines.add("&cIn Fights: &f" + inFights);
			lines.add("&cIn Queues: &f" + inQueues);

			if (EventGame.getActiveGame() == null && !EventGame.getCooldown().hasExpired()) {
				lines.add("&cEvent Cooldown: &f" + TimeUtil.millisToTimer(EventGame.getCooldown().getRemaining()));
			}
		}

		if (profile.getState() == ProfileState.LOBBY) {
			if (profile.getParty() != null) {
				lines.add("&cYour Party");

				int added = 0;
				Party party = profile.getParty();

				for (Player otherPlayer : party.getListOfPlayers()) {
					added++;

					lines.add(" &7" + (party.getLeader().equals(otherPlayer) ? "*" : "-") + " &r" +
					          otherPlayer.getName());

					if (added >= 4) {
						break;
					}
				}
			}
		} else if (profile.getState() == ProfileState.QUEUEING) {
			QueueProfile queueProfile = profile.getQueueProfile();

			lines.add(CC.SB_BAR);
			lines.add("&a&oSearching for a match...");
			lines.add(" ");
			lines.add("&c" + queueProfile.getQueue().getQueueName());
			lines.add("&cElapsed: &f" + TimeUtil.millisToTimer(queueProfile.getPassed()));

			if (queueProfile.getQueue().isRanked()) {
				lines.add("&cELO Range: &f" + queueProfile.getMinRange() + " -> " + queueProfile.getMaxRange());
			}
		} else if (profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.SPECTATING) {
			lines.addAll(profile.getMatch().getScoreboardLines(player));
		} else if (profile.getState() == ProfileState.EVENT) {
			lines.addAll(EventGame.getActiveGame().getGameLogic().getScoreboardEntries());
		}

		lines.add(0, CC.SB_BAR);
		lines.add(CC.SB_BAR);

		return lines;
	}

}
