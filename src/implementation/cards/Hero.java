package implementation.cards;

import fileio.CardInput;
import implementation.Player;


public class Hero extends Card {
    public enum Ability {
        SubZero,
        LowBlow,
        EarthBorn,
        BloodThirst
    }

    private Ability ability;
    private boolean attacked = false;

    public Hero(final CardInput cardInput, final Player owner) {
        super(cardInput, owner);
        findAbility();
    }

    public final Ability getAbility() {
        return ability;
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

    private void findAbility() {
        switch (cardInput.getName()) {
            case "Lord Royce" -> {
                ability = Ability.SubZero;
            }
            case "Empress Thorina" -> {
                ability = Ability.LowBlow;
            }
            case "King Mudface" -> {
                ability = Ability.EarthBorn;
            }
            case "General Kocioraw" -> {
                ability = Ability.BloodThirst;
            }
            default -> { }
        }
    }
}
