package implementation.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import implementation.Game;
import implementation.Player;
import implementation.cards.Card;
import implementation.cards.Minion;

import java.util.ArrayList;

public final class Debug {

    private Debug() { }

    /**
     * Get cards in hand
     * @param playerIdx player index
     */
    public static void getCardsInHand(final int playerIdx) {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        Player p = Game.getInstance().getPlayer(playerIdx);

        n.put("command", "getCardsInHand");
        n.put("playerIdx", playerIdx);
        ArrayNode hand = n.putArray("output");

        for (Card card : p.getHand()) {
            ObjectNode c = hand.addObject();
            switch (card.getType()) {
                case Minion, SpecialMinion -> {
                    c.put("mana", card.getCardInput().getMana());
                    c.put("attackDamage", card.getCardInput().getAttackDamage());
                    c.put("health", card.getCardInput().getHealth());
                    c.put("description", card.getCardInput().getDescription());
                    ArrayNode colors = c.putArray("colors");
                    for (String color : card.getCardInput().getColors()) {
                        colors.add(color);
                    }
                    c.put("name", card.getCardInput().getName());
                }
                case Environment -> {
                    c.put("mana", card.getCardInput().getMana());
                    c.put("description", card.getCardInput().getDescription());
                    ArrayNode colors = c.putArray("colors");
                    for (String color : card.getCardInput().getColors()) {
                        colors.add(color);
                    }
                    c.put("name", card.getCardInput().getName());
                }
                default -> { }
            }
        }
    }

    /**
     * Get player deck
     * @param playerIdx player index
     */
    public static void getPlayerDeck(final int playerIdx) {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        Player p = Game.getInstance().getPlayer(playerIdx);

        n.put("command", "getPlayerDeck");
        n.put("playerIdx", playerIdx);
        ArrayNode deck = n.putArray("output");

        for (Card card : p.getDeck()) {
            ObjectNode c = deck.addObject();
            switch (card.getType()) {
                case Minion, SpecialMinion -> {
                    c.put("mana", card.getCardInput().getMana());
                    c.put("attackDamage", card.getCardInput().getAttackDamage());
                    c.put("health", card.getCardInput().getHealth());
                    c.put("description", card.getCardInput().getDescription());
                    ArrayNode colors = c.putArray("colors");
                    for (String color : card.getCardInput().getColors()) {
                        colors.add(color);
                    }
                    c.put("name", card.getCardInput().getName());
                }
                case Environment -> {
                    c.put("mana", card.getCardInput().getMana());
                    c.put("description", card.getCardInput().getDescription());
                    ArrayNode colors = c.putArray("colors");
                    for (String color : card.getCardInput().getColors()) {
                        colors.add(color);
                    }
                    c.put("name", card.getCardInput().getName());
                }
                default -> { }
            }
        }
    }

    /**
     * Get cards on table
     */
    public static void getCardsOnTable() {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        ArrayList[] board = Game.getInstance().getBoard();

        n.put("command", "getCardsOnTable");
        ArrayNode deck = n.putArray("output");

        for (int y = 0; y < board.length; y++) {
            ArrayNode r = deck.addArray();
            for (Object obj : board[y]) {
                Card card = (Card) obj;
                ObjectNode c = r.addObject();

                c.put("mana", card.getCardInput().getMana());
                c.put("attackDamage", card.getCardInput().getAttackDamage());
                c.put("health", card.getCardInput().getHealth());
                c.put("description", card.getCardInput().getDescription());
                ArrayNode colors = c.putArray("colors");
                for (String color : card.getCardInput().getColors()) {
                    colors.add(color);
                }
                c.put("name", card.getCardInput().getName());
            }
        }
    }

    /**
     * Get player turn
     */
    public static void getPlayerTurn() {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();

        n.put("command", "getPlayerTurn");
        n.put("output", Game.getInstance().getPlayerTurn());
    }

    /**
     * Get player hero
     * @param playerIdx player index
     */
    public static void getPlayerHero(final int playerIdx) {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        Player p = Game.getInstance().getPlayer(playerIdx);
        Card card = p.getHero();

        n.put("command", "getPlayerHero");
        n.put("playerIdx", playerIdx);
        ObjectNode hero = n.putObject("output");

        hero.put("mana", card.getCardInput().getMana());
        hero.put("description", card.getCardInput().getDescription());
        ArrayNode colors = hero.putArray("colors");
        for (String color : card.getCardInput().getColors()) {
            colors.add(color);
        }
        hero.put("name", card.getCardInput().getName());
        hero.put("health", card.getCardInput().getHealth());
    }

    /**
     * Get card at position
     * @param x x position
     * @param y y position
     */
    public static void getCardAtPosition(final int x, final int y) {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        ArrayList[] board = Game.getInstance().getBoard();

        if (y >= board[x].size()) {
            n.put("command", "getCardAtPosition");
            n.put("x", x);
            n.put("y", y);
            n.put("output", "No card available at that position.");
            return;
        }

        Card card = (Card) board[x].get(y);

        n.put("command", "getCardAtPosition");
        n.put("x", x);
        n.put("y", y);
        ObjectNode c = n.putObject("output");

        c.put("mana", card.getCardInput().getMana());
        c.put("attackDamage", card.getCardInput().getAttackDamage());
        c.put("health", card.getCardInput().getHealth());
        c.put("description", card.getCardInput().getDescription());
        ArrayNode colors = c.putArray("colors");
        for (String color : card.getCardInput().getColors()) {
            colors.add(color);
        }
        c.put("name", card.getCardInput().getName());
    }

    /**
     * Get player mana
     * @param playerIdx player index
     */
    public static void getPlayerMana(final int playerIdx) {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        Player p = Game.getInstance().getPlayer(playerIdx);

        n.put("command", "getPlayerMana");
        n.put("playerIdx", playerIdx);
        n.put("output", p.getMana());
    }

    /**
     * Get environment cards in hand
     * @param playerIdx player index
     */
    public static void getEnvironmentCardsInHand(final int playerIdx) {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        Player p = Game.getInstance().getPlayer(playerIdx);

        n.put("command", "getEnvironmentCardsInHand");
        n.put("playerIdx", playerIdx);
        ArrayNode hand = n.putArray("output");

        for (Card card : p.getHand()) {
            if (card.getType() == Card.Type.Environment) {
                ObjectNode c = hand.addObject();
                c.put("mana", card.getCardInput().getMana());
                c.put("description", card.getCardInput().getDescription());
                ArrayNode colors = c.putArray("colors");
                for (String color : card.getCardInput().getColors()) {
                    colors.add(color);
                }
                c.put("name", card.getCardInput().getName());
            }
        }
    }

    /**
     * Get frozen cards on table
     */
    public static void getFrozenCardsOnTable() {
        ArrayNode out = Game.getInstance().getOutput();
        ObjectNode n = out.addObject();
        ArrayList[] board = Game.getInstance().getBoard();

        n.put("command", "getFrozenCardsOnTable");
        ArrayNode deck = n.putArray("output");

        for (int y = 0; y < board.length; y++) {
            ArrayNode r = null;
            for (Object obj : board[y]) {
                Minion card = (Minion) obj;

                if (!card.isFrozen()) {
                    continue;
                } else if (r == null) {
                    r = deck.addArray();
                }

                ObjectNode c = r.addObject();

                c.put("mana", card.getCardInput().getMana());
                c.put("attackDamage", card.getCardInput().getAttackDamage());
                c.put("health", card.getCardInput().getHealth());
                c.put("description", card.getCardInput().getDescription());
                ArrayNode colors = c.putArray("colors");
                for (String color : card.getCardInput().getColors()) {
                    colors.add(color);
                }
                c.put("name", card.getCardInput().getName());
            }
        }
    }
}
