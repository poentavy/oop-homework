package implementation.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;
import implementation.Game;
import implementation.Player;
import implementation.cards.Card;
import implementation.cards.Minion;
import implementation.cards.SpecialMinion;
import implementation.cards.Environment;
import implementation.cards.Hero;

import java.util.ArrayList;

public final class Gameplay {

    private Gameplay() {
    }

    /**
     * End player turn
     */
    public static void endPlayerTurn() {
        Game.getInstance().advanceTurn();
    }

    /**
     * Place card
     *
     * @param handIdx hand index
     */
    public static void placeCard(final int handIdx) {
        ArrayNode out = Game.getInstance().getOutput();
        Player p = Game.getInstance().getPlayer(Game.getInstance().getPlayerTurn());
        Card card = p.getCardFromHand(handIdx);
        ArrayList[] board = Game.getInstance().getBoard();

        if (card.getType() == Card.Type.Environment) {
            ObjectNode n = out.addObject();
            n.put("command", "placeCard");
            n.put("handIdx", handIdx);
            n.put("error", "Cannot place environment card on table.");
            return;
        }

        if (card.getCardInput().getMana() > p.getMana()) {
            ObjectNode n = out.addObject();
            n.put("command", "placeCard");
            n.put("handIdx", handIdx);
            n.put("error", "Not enough mana to place card on table.");
            return;
        }

        Minion minion = (Minion) card;
        final int three = 3;
        final int five = 5;

        if (minion.getPreferredRow() == Minion.PreferredRow.BACK) {
            if (Game.getInstance().getPlayerTurn() == 1) {
                if (board[three].size() == five) {
                    ObjectNode n = out.addObject();
                    n.put("command", "placeCard");
                    n.put("handIdx", handIdx);
                    n.put("error", "Cannot place card on table since row is full.");
                    return;
                } else {
                    board[three].add(p.removeCardFromHand(handIdx));
                }
            } else {
                if (board[0].size() == five) {
                    ObjectNode n = out.addObject();
                    n.put("command", "placeCard");
                    n.put("handIdx", handIdx);
                    n.put("error", "Cannot place card on table since row is full.");
                    return;
                } else {
                    board[0].add(p.removeCardFromHand(handIdx));
                }
            }
        } else {
            if (Game.getInstance().getPlayerTurn() == 1) {
                if (board[2].size() == five) {
                    ObjectNode n = out.addObject();
                    n.put("command", "placeCard");
                    n.put("handIdx", handIdx);
                    n.put("error", "Cannot place card on table since row is full.");
                    return;
                } else {
                    board[2].add(p.removeCardFromHand(handIdx));
                }
            } else {
                if (board[1].size() == five) {
                    ObjectNode n = out.addObject();
                    n.put("command", "placeCard");
                    n.put("handIdx", handIdx);
                    n.put("error", "Cannot place card on table since row is full.");
                    return;
                } else {
                    board[1].add(p.removeCardFromHand(handIdx));
                }
            }
        }

        p.setMana(p.getMana() - minion.getCardInput().getMana());
    }

    /**
     * Card uses attack
     *
     * @param attacker attacker coordinates
     * @param attacked attacked coordinates
     */
    public static void cardUsesAttack(final Coordinates attacker, final Coordinates attacked) {
        ArrayNode out = Game.getInstance().getOutput();
        Player p = Game.getInstance().getPlayer(Game.getInstance().getPlayerTurn());
        ArrayList[] board = Game.getInstance().getBoard();
        Minion atk = (Minion) board[attacker.getX()].get(attacker.getY());
        Minion victim = (Minion) board[attacked.getX()].get(attacked.getY());

        if (atk.isFrozen()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAttack");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacker card is frozen.");
            return;
        }

        if (atk.hasAttacked()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAttack");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacker card has already attacked this turn.");
            return;
        }

        if (atk.getOwner() == victim.getOwner()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAttack");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacked card does not belong to the enemy.");
            return;
        }

        boolean tankPresent = false;
        if (Game.getInstance().getPlayerTurn() == 1) {
            for (int y = 0; y < board.length / 2; y++) {
                for (Object o : board[y]) {
                    Minion m = (Minion) o;

                    if (m.isTank()) {
                        tankPresent = true;
                        break;
                    }
                }
            }
        } else {
            for (int y = 2; y < board.length; y++) {
                for (Object o : board[y]) {
                    Minion m = (Minion) o;

                    if (m.isTank()) {
                        tankPresent = true;
                        break;
                    }
                }
            }
        }

        if (tankPresent && !victim.isTank()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAttack");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacked card is not of type 'Tank'.");
            return;
        }

        atk.attack();
        Game.getInstance().damageCard(attacked.getY(), attacked.getX(),
                atk.getCardInput().getAttackDamage());
    }

    /**
     * Card uses ability
     *
     * @param attacker attacker coordinates
     * @param attacked attacked coordinates
     */
    public static void cardUsesAbility(final Coordinates attacker, final Coordinates attacked) {
        ArrayNode out = Game.getInstance().getOutput();
        Player p = Game.getInstance().getPlayer(Game.getInstance().getPlayerTurn());
        ArrayList[] board = Game.getInstance().getBoard();
        SpecialMinion atk = (SpecialMinion) board[attacker.getX()].get(attacker.getY());
        Minion victim = (Minion) board[attacked.getX()].get(attacked.getY());

        if (atk.isFrozen()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAbility");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacker card is frozen.");
            return;
        }

        if (atk.hasAttacked()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAbility");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacker card has already attacked this turn.");
            return;
        }

        if (atk.getAbility() == SpecialMinion.Ability.GodsPlan) {
            if (atk.getOwner() != victim.getOwner()) {
                ObjectNode n = out.addObject();
                n.put("command", "cardUsesAbility");
                ObjectNode a = n.putObject("cardAttacker");
                a.put("x", attacker.getX());
                a.put("y", attacker.getY());
                ObjectNode v = n.putObject("cardAttacked");
                v.put("x", attacked.getX());
                v.put("y", attacked.getY());
                n.put("error", "Attacked card does not belong to the current player.");
                return;
            }

            victim.getCardInput().setHealth(victim.getCardInput().getHealth() + 2);
            atk.attack();
            return;
        }

        if (atk.getOwner() == victim.getOwner()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAbility");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacked card does not belong to the enemy.");
            return;
        }

        boolean tankPresent = false;
        if (Game.getInstance().getPlayerTurn() == 1) {
            for (int y = 0; y < board.length / 2; y++) {
                for (Object o : board[y]) {
                    Minion m = (Minion) o;

                    if (m.isTank()) {
                        tankPresent = true;
                        break;
                    }
                }
            }
        } else {
            for (int y = 2; y < board.length; y++) {
                for (Object o : board[y]) {
                    Minion m = (Minion) o;

                    if (m.isTank()) {
                        tankPresent = true;
                        break;
                    }
                }
            }
        }

        if (tankPresent && !victim.isTank()) {
            ObjectNode n = out.addObject();
            n.put("command", "cardUsesAbility");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            ObjectNode v = n.putObject("cardAttacked");
            v.put("x", attacked.getX());
            v.put("y", attacked.getY());
            n.put("error", "Attacked card is not of type 'Tank'.");
            return;
        }

        switch (atk.getAbility()) {
            case WeakKnees -> {
                victim.getCardInput().setAttackDamage(victim.getCardInput().getAttackDamage() - 2);

                if (victim.getCardInput().getAttackDamage() < 0) {
                    victim.getCardInput().setAttackDamage(0);
                }
            }
            case Shapeshift -> {
                int hp = victim.getCardInput().getHealth();
                victim.getCardInput().setHealth(victim.getCardInput().getAttackDamage());
                victim.getCardInput().setAttackDamage(hp);

                if (victim.getCardInput().getHealth() <= 0) {
                    board[attacked.getX()].remove(attacked.getY());
                }
            }
            case Skyjack -> {
                int victimHp = victim.getCardInput().getHealth();
                victim.getCardInput().setHealth(atk.getCardInput().getHealth());
                atk.getCardInput().setHealth(victimHp);
            }
            default -> {
            }
        }

        atk.attack();
    }

    /**
     * Use attack hero
     * @param attacker attacker coordinates
     */
    public static void useAttackHero(final Coordinates attacker) {
        ArrayNode out = Game.getInstance().getOutput();
        Player p = Game.getInstance().getPlayer(Game.getInstance().getPlayerTurn());
        ArrayList[] board = Game.getInstance().getBoard();
        Minion atk = (Minion) board[attacker.getX()].get(attacker.getY());
        Hero victim = null;
        if (Game.getInstance().getPlayerTurn() == 1) {
            victim = (Hero) Game.getInstance().getPlayer(2).getHero();
        } else {
            victim = (Hero) Game.getInstance().getPlayer(1).getHero();
        }

        if (atk.isFrozen()) {
            ObjectNode n = out.addObject();
            n.put("command", "useAttackHero");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            n.put("error", "Attacker card is frozen.");
            return;
        }

        if (atk.hasAttacked()) {
            ObjectNode n = out.addObject();
            n.put("command", "useAttackHero");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            n.put("error", "Attacker card has already attacked this turn.");
            return;
        }

        boolean tankPresent = false;
        if (Game.getInstance().getPlayerTurn() == 1) {
            for (int y = 0; y < board.length / 2; y++) {
                for (Object o : board[y]) {
                    Minion m = (Minion) o;

                    if (m.isTank()) {
                        tankPresent = true;
                        break;
                    }
                }
            }
        } else {
            for (int y = 2; y < board.length; y++) {
                for (Object o : board[y]) {
                    Minion m = (Minion) o;

                    if (m.isTank()) {
                        tankPresent = true;
                        break;
                    }
                }
            }
        }

        if (tankPresent) {
            ObjectNode n = out.addObject();
            n.put("command", "useAttackHero");
            ObjectNode a = n.putObject("cardAttacker");
            a.put("x", attacker.getX());
            a.put("y", attacker.getY());
            n.put("error", "Attacked card is not of type 'Tank'.");
            return;
        }

        atk.attack();
        victim.getCardInput().setHealth(victim.getCardInput().getHealth()
                - atk.getCardInput().getAttackDamage());
        if (victim.getCardInput().getHealth() <= 0) {
            ObjectNode n = out.addObject();
            if (Game.getInstance().getPlayerTurn() == 1) {
                n.put("gameEnded", "Player one killed the enemy hero.");
                Game.getInstance().winGame(1);
            } else {
                n.put("gameEnded", "Player two killed the enemy hero.");
                Game.getInstance().winGame(2);
            }
        }
    }

    /**
     * Use hero ability
     * @param affectedRow row
     */
    public static void useHeroAbility(final int affectedRow) {
        ArrayNode out = Game.getInstance().getOutput();
        int turn = Game.getInstance().getPlayerTurn();
        Player p = Game.getInstance().getPlayer(turn);
        ArrayList[] board = Game.getInstance().getBoard();
        Hero hero = (Hero) p.getHero();

        if (p.getMana() < hero.getCardInput().getMana()) {
            ObjectNode n = out.addObject();
            n.put("command", "useHeroAbility");
            n.put("affectedRow", affectedRow);
            n.put("error", "Not enough mana to use hero's ability.");
            return;
        }

        if (hero.hasAttacked()) {
            ObjectNode n = out.addObject();
            n.put("command", "useHeroAbility");
            n.put("affectedRow", affectedRow);
            n.put("error", "Hero has already attacked this turn.");
            return;
        }

        final int three = 3;

        if (hero.getAbility() == Hero.Ability.SubZero
                || hero.getAbility() == Hero.Ability.LowBlow) {
            if ((turn == 2 && (affectedRow == 0 || affectedRow == 1))
                    || (turn == 1 && (affectedRow == 2 || affectedRow == three))) {
                ObjectNode n = out.addObject();
                n.put("command", "useHeroAbility");
                n.put("affectedRow", affectedRow);
                n.put("error", "Selected row does not belong to the enemy.");
                return;
            }
        } else {
            if ((turn == 2 && (affectedRow == 2 || affectedRow == three))
                    || (turn == 1 && (affectedRow == 0 || affectedRow == 1))) {
                ObjectNode n = out.addObject();
                n.put("command", "useHeroAbility");
                n.put("affectedRow", affectedRow);
                n.put("error", "Selected row does not belong to the current player.");
                return;
            }
        }

        switch (hero.getAbility()) {
            case SubZero -> {
                Minion maxAtk = null;
                int max = -1;
                for (Object o : board[affectedRow]) {
                    Minion card = (Minion) o;

                    if (card.getCardInput().getAttackDamage() > max) {
                        maxAtk = card;
                        max = card.getCardInput().getAttackDamage();
                    }
                }

                if (maxAtk != null) {
                    maxAtk.freeze();
                }
            }
            case LowBlow -> {
                Minion maxHp = null;
                int max = -1;
                int maxIdx = -1;
                for (int x = 0; x < board[affectedRow].size(); x++) {
                    Minion card = (Minion) board[affectedRow].get(x);

                    if (card.getCardInput().getHealth() > max) {
                        maxHp = card;
                        max = card.getCardInput().getHealth();
                        maxIdx = x;
                    }
                }

                if (maxHp != null) {
                    Game.getInstance().damageCard(maxIdx, affectedRow,
                            maxHp.getCardInput().getHealth());
                }
            }
            case EarthBorn -> {
                for (Object o : board[affectedRow]) {
                    Minion card = (Minion) o;

                    card.getCardInput().setHealth(card.getCardInput().getHealth() + 1);
                }
            }
            case BloodThirst -> {
                for (Object o : board[affectedRow]) {
                    Minion card = (Minion) o;

                    card.getCardInput().setAttackDamage(card.getCardInput().getAttackDamage() + 1);
                }
            }
            default -> { }
        }

        hero.attack();
        p.setMana(p.getMana() - hero.getCardInput().getMana());
    }

    /**
     * Use environment card
     * @param handIdx hand index
     * @param affectedRow row
     */
    public static void useEnvironmentCard(final int handIdx, final int affectedRow) {
        ArrayNode out = Game.getInstance().getOutput();
        int turn = Game.getInstance().getPlayerTurn();
        Player p = Game.getInstance().getPlayer(turn);
        Card card = p.getCardFromHand(handIdx);
        ArrayList[] board = Game.getInstance().getBoard();

        if (card.getType() != Card.Type.Environment) {
            ObjectNode n = out.addObject();
            n.put("command", "useEnvironmentCard");
            n.put("handIdx", handIdx);
            n.put("affectedRow", affectedRow);
            n.put("error", "Chosen card is not of type environment.");
            return;
        }

        final int three = 3;
        final int five = 5;

        if (card.getCardInput().getMana() > p.getMana()) {
            ObjectNode n = out.addObject();
            n.put("command", "useEnvironmentCard");
            n.put("handIdx", handIdx);
            n.put("affectedRow", affectedRow);
            n.put("error", "Not enough mana to use environment card.");
            return;
        }

        if ((turn == 2 && (affectedRow == 0 || affectedRow == 1))
                || (turn == 1 && (affectedRow == 2 || affectedRow == three))) {
            ObjectNode n = out.addObject();
            n.put("command", "useEnvironmentCard");
            n.put("handIdx", handIdx);
            n.put("affectedRow", affectedRow);
            n.put("error", "Chosen row does not belong to the enemy.");
            return;
        }

        Environment env = (Environment) card;

        switch (env.getEffect()) {
            case Firestorm -> {
                int size = board[affectedRow].size();

                for (int x = 0; x < size; x++) {
                    Game.getInstance().damageCard(x, affectedRow, 1);
                    // If a card has died
                    if (size > board[affectedRow].size()) {
                        size = board[affectedRow].size();
                        x--;
                    }
                }
            }
            case Winterfell -> {
                for (Object o : board[affectedRow]) {
                    Minion minion = (Minion) o;

                    minion.freeze();
                }
            }
            case HeartHound -> {
                boolean full = false;
                if (turn == 1) {
                    if ((affectedRow == 0 && board[three].size() == five)
                            || (affectedRow == 1 && board[2].size() == five)) {
                        full = true;
                    }
                } else {
                    if ((affectedRow == three && board[0].size() == five)
                            || (affectedRow == 2 && board[1].size() == five)) {
                        full = true;
                    }
                }

                if (full) {
                    ObjectNode n = out.addObject();
                    n.put("command", "useEnvironmentCard");
                    n.put("handIdx", handIdx);
                    n.put("affectedRow", affectedRow);
                    n.put("error", "Cannot steal enemy card since the player's row is full.");
                    return;
                }

                Card maxHealth = (Card) board[affectedRow].get(0);
                int idx = 0;
                for (int x = 1; x < board[affectedRow].size(); x++) {
                    Card c = (Card) board[affectedRow].get(x);

                    if (c.getCardInput().getHealth() > maxHealth.getCardInput().getHealth()) {
                        maxHealth = c;
                        idx = x;
                    }
                }

                board[affectedRow].remove(idx);

                switch (affectedRow) {
                    case 0 -> {
                        maxHealth.setOwner(Game.getInstance().getPlayer(1));
                        board[three].add(maxHealth);
                    }
                    case 1 -> {
                        maxHealth.setOwner(Game.getInstance().getPlayer(1));
                        board[2].add(maxHealth);
                    }
                    case 2 -> {
                        maxHealth.setOwner(Game.getInstance().getPlayer(2));
                        board[1].add(maxHealth);
                    }
                    case three -> {
                        maxHealth.setOwner(Game.getInstance().getPlayer(2));
                        board[0].add(maxHealth);
                    }
                    default -> { }
                }
            }
            default -> { }
        }

        p.setMana(p.getMana() - env.getCardInput().getMana());
        p.removeCardFromHand(handIdx);
    }


}
