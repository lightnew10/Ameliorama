package fr.lightnew.ameliorama.tools;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class CustomScoreboardExtend {

    private final String title;
    private final Scoreboard scoreboard;
    private final Map<Integer, String> lines;
    private final Player player;
    private final Objective objective;

    public CustomScoreboardExtend(String title, Player player) {
        this.title = title;
        this.scoreboard = new Scoreboard();
        this.lines = new ConcurrentHashMap<>();
        this.player = player;
        this.objective = scoreboard.addObjective(title, ObjectiveCriteria.DUMMY, Component.literal(title), ObjectiveCriteria.RenderType.INTEGER);
        initializeObjective();
    }

    private void initializeObjective() {
        ClientboundSetObjectivePacket createObjectivePacket = new ClientboundSetObjectivePacket(objective, ClientboundSetObjectivePacket.METHOD_ADD);
        ((CraftPlayer) player).getHandle().connection.send(createObjectivePacket);

        ClientboundSetDisplayObjectivePacket displayPacket = new ClientboundSetDisplayObjectivePacket(1, objective);
        ((CraftPlayer) player).getHandle().connection.send(displayPacket);
    }

    public void removeScoreBoard() {
        ClientboundSetObjectivePacket createObjectivePacket = new ClientboundSetObjectivePacket(objective, ClientboundSetObjectivePacket.METHOD_REMOVE);
        ((CraftPlayer) player).getHandle().connection.send(createObjectivePacket);
    }

    public void setLine(int number, String line) {
        if (line == null)
            return;

        if (lines.containsKey(number))
            removeLine(number);


        if (lines.size() >= 10)
            throw new RuntimeException("You cannot set more than 10 lines");

        lines.put(number, line);

        ClientboundSetScorePacket changeScorePacket = new ClientboundSetScorePacket(
                ServerScoreboard.Method.CHANGE,
                title,
                line,
                number
        );
        ((CraftPlayer) player).getHandle().connection.send(changeScorePacket);
    }

    public void setLines(String... lines) {
        List<String> l = Arrays.asList(lines);
        Collections.reverse(l);
        for (int i = 0; i < l.size(); i++) {
            setLine(i, l.get(i));
        }
    }

    public void removeLine(int number) {
        String line = lines.remove(number);

        if (line == null)
            return;

        ClientboundSetScorePacket removeScorePacket = new ClientboundSetScorePacket(
                ServerScoreboard.Method.REMOVE,
                title,
                line,
                0
        );
        ((CraftPlayer) player).getHandle().connection.send(removeScorePacket);
    }
}

