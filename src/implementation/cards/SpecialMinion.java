package implementation.cards;

import fileio.CardInput;
import implementation.Player;


public class SpecialMinion extends Minion {
    public enum Ability {
        WeakKnees,
        Skyjack,
        Shapeshift,
        GodsPlan
    }

    private Ability ability;

    public SpecialMinion(final CardInput cardInput, final Player owner) {
        super(cardInput, owner);
        findAbility();
    }

    public final Ability getAbility() {
        return ability;
    }

    private void findAbility() {
        switch (cardInput.getName()) {
            case "The Ripper" -> {
                ability = Ability.WeakKnees;
            }
            case "Miraj" -> {
                ability = Ability.Skyjack;
            }
            case "The Cursed One" -> {
                ability = Ability.Shapeshift;
            }
            case "Disciple" -> {
                ability = Ability.GodsPlan;
            }
            default -> { }
        }
    }
}
