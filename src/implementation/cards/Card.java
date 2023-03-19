package implementation.cards;

import fileio.CardInput;
import implementation.Player;


public class Card {
    public enum Type {
        Minion,
        SpecialMinion,
        Environment,
        Hero
    }

    protected CardInput cardInput = new CardInput();
    private Type type;
    private Player owner;

    public Card(final CardInput cardInput, final Player owner) {
        this.cardInput.setMana(cardInput.getMana());
        this.cardInput.setAttackDamage(cardInput.getAttackDamage());
        this.cardInput.setHealth(cardInput.getHealth());
        this.cardInput.setDescription(cardInput.getDescription());
        this.cardInput.setColors(cardInput.getColors());
        this.cardInput.setName(cardInput.getName());

        this.owner = owner;
        findType();
    }

    public final CardInput getCardInput() {
        return cardInput;
    }

    public final Type getType() {
        return type;
    }

    public final Player getOwner() {
        return owner;
    }

    public final void setOwner(final Player owner) {
        this.owner = owner;
    }

    private void findType() {
        String name = cardInput.getName();

        switch (name) {
            case "Sentinel", "Berserker", "Goliath", "Warden" -> {
                type = Type.Minion;
            }
            case "Miraj", "The Ripper", "Disciple", "The Cursed One" -> {
                type = Type.SpecialMinion;
            }
            case "Firestorm", "Winterfell", "Heart Hound" -> {
                type = Type.Environment;
            }
            default -> {
                type = Type.Hero;
            }
        }

        if (type == Type.Hero) {
            final int heroHealth = 30;
            cardInput.setHealth(heroHealth);
        }
    }

    /**
     * Create new card
     * @param cardInput card input data
     * @param owner owner of card
     * @return newly created card
     */
    public static Card createCard(final CardInput cardInput, final Player owner) {
        return switch (cardInput.getName()) {
            case "Goliath", "Warden", "Sentinel", "Berserker" ->
                    new Minion(cardInput, owner);
            case "The Ripper", "Miraj", "The Cursed One", "Disciple" ->
                    new SpecialMinion(cardInput, owner);
            case "Firestorm", "Winterfell", "Heart Hound" ->
                    new Environment(cardInput, owner);
            default -> new Hero(cardInput, owner);
        };
    }
}
