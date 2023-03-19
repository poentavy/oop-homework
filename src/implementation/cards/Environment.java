package implementation.cards;

import fileio.CardInput;
import implementation.Player;

public class Environment extends Card {
    public enum Effect {
        Firestorm,
        Winterfell,
        HeartHound
    }

    private Effect effect;

    public Environment(final CardInput cardInput, final Player owner) {
        super(cardInput, owner);
        findEffect();
    }

    public final Effect getEffect() {
        return effect;
    }

    private void findEffect() {
        switch (cardInput.getName()) {
            case "Firestorm" -> {
                effect = Effect.Firestorm;
            }
            case "Winterfell" -> {
                effect = Effect.Winterfell;
            }
            case "Heart Hound" -> {
                effect = Effect.HeartHound;
            }
            default -> { }
        }
    }
}
