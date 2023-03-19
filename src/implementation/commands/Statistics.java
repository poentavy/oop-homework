package implementation.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import implementation.Game;

public final class Statistics {

    private Statistics() { }

    /**
     * Get total games played
     */
    public static void getTotalGamesPlayed() {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();

        n.put("command", "getTotalGamesPlayed");
        n.put("output", Game.getInstance().getGamesPlayed());
    }

    /**
     * Get player one wins
     */
    public static void getPlayerOneWins() {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();

        n.put("command", "getPlayerOneWins");
        n.put("output", Game.getInstance().getPlayer(1).getWins());
    }

    /**
     * Get player two wins
     */
    public static void getPlayerTwoWins() {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();

        n.put("command", "getPlayerTwoWins");
        n.put("output", Game.getInstance().getPlayer(2).getWins());
    }
}
