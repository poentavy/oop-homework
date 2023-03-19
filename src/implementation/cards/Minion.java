package implementation.cards;

import fileio.CardInput;
import implementation.Player;


public class Minion extends Card {
    public enum PreferredRow {
        FRONT,
        BACK
    }

    private PreferredRow preferredRow;
    private boolean frozen = false;
    private boolean attacked = false;

    public Minion(final CardInput cardInput, final Player owner) {
        super(cardInput, owner);
        findPreferredRow();
    }

    public final PreferredRow getPreferredRow() {
        return preferredRow;
    }

    /**
     * Freeze
     */
    public final void freeze() {
        frozen = true;
    }

    /**
     * Unfreeze
     */
    public final void unfreeze() {
        frozen = false;
    }

    /**
     * Is Frozen
     * @return true if minion is frozen
     */
    public final boolean isFrozen() {
        return frozen;
    }

    /**
     * Attack
     */
    public final void attack() {
        attacked = true;
    }

    /**
     * Reset attack
     */
    public final void resetAttack() {
        attacked = false;
    }

    /**
     * Has attacked
     * @return true if attacked this turn
     */
    public final boolean hasAttacked() {
        return attacked;
    }

    /**
     * Is tank
     * @return true if minion is tank
     */
    public final boolean isTank() {
        return switch (cardInput.getName()) {
            case "Goliath", "Warden" -> true;
            default -> false;
        };
    }

    private void findPreferredRow() {
        switch (cardInput.getName()) {
            case "The Ripper", "Miraj", "Goliath", "Warden" -> {
                preferredRow = PreferredRow.FRONT;
            }
            default -> {
                preferredRow = PreferredRow.BACK;
            }
        }
    }
}
