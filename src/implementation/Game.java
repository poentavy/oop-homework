package implementation;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.StartGameInput;
import fileio.CardInput;
import fileio.ActionsInput;
import fileio.Input;
import fileio.DecksInput;
import implementation.cards.Card;
import implementation.cards.Hero;
import implementation.cards.Minion;
import implementation.commands.Debug;
import implementation.commands.Gameplay;
import implementation.commands.Statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// Lazy singleton
public final class Game {
    private static Game instance = null;
    private Input inputData = null;
    private ArrayNode output = null;
    private ArrayList<ArrayList<Card>> p1Decks = null;
    private ArrayList<ArrayList<Card>> p2Decks = null;
    private Player player1 = null;
    private Player player2 = null;
    private static final int BOARD_SIZE = 4;
    private ArrayList[] board = new ArrayList[BOARD_SIZE];
    private int playerTurn;
    private int manaToAdd;
    private int turns;
    private int gamesPlayed;

    private Game() {
    }

    /**
     * Get instance
     * @return instance
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }

    /**
     * Initialize
     * @param id input data
     * @param out output node
     */
    public void init(final Input id, final ArrayNode out) {
        p1Decks = new ArrayList<>();
        p2Decks = new ArrayList<>();
        player1 = new Player();
        player2 = new Player();
        gamesPlayed = 1;

        this.inputData = id;
        this.output = out;
    }

    public Input getInputData() {
        return inputData;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Get player
     * @param playerIdx player index
     * @return player
     */
    public Player getPlayer(final int playerIdx) {
        if (playerIdx == 1) {
            return player1;
        } else if (playerIdx == 2) {
            return player2;
        } else {
            return null;
        }
    }

    public ArrayList[] getBoard() {
        return board;
    }

    /**
     * Advance turn
     */
    public void advanceTurn() {
        if (playerTurn == 2) {
            playerTurn = 1;
            Hero hero = (Hero) player1.getHero();
            hero.resetAttack();
            // Unfreeze all player 2's cards
            for (int i = 0; i < board.length; i++) {
                for (Object o : board[i]) {
                    Card card = (Card) o;
                    if (card.getOwner() == player2) {
                        Minion minion = (Minion) card;
                        minion.unfreeze();
                        minion.resetAttack();
                    }
                }
            }
        } else {
            playerTurn = 2;
            Hero hero = (Hero) player2.getHero();
            hero.resetAttack();
            // Unfreeze all player 1's cards
            for (int i = 0; i < board.length; i++) {
                for (Object o : board[i]) {
                    Card card = (Card) o;
                    if (card.getOwner() == player1) {
                        Minion minion = (Minion) card;
                        minion.unfreeze();
                        minion.resetAttack();
                    }
                }
            }
        }

        turns++;

        if (turns == 2) {
            advanceRound();
            turns = 0;
        }
    }

    private void advanceRound() {
        player1.takeCardFromDeck();
        player2.takeCardFromDeck();

        player1.setMana(player1.getMana() + manaToAdd);
        player2.setMana(player2.getMana() + manaToAdd);

        final int ten = 10;

        if (manaToAdd < ten) {
            manaToAdd++;
        }
    }

    /**
     * Damage card
     * @param x x coordinate
     * @param y y coordinate
     * @param damage damage value
     */
    public void damageCard(final int x, final int y, final int damage) {
        Card c = (Card) board[y].get(x);

        c.getCardInput().setHealth(c.getCardInput().getHealth() - damage);

        if (c.getCardInput().getHealth() <= 0) {
            board[y].remove(x);
        }
    }

    /**
     * Win game
     * @param playerIdx player who wins
     */
    public void winGame(final int playerIdx) {
        if (playerIdx == 1) {
            player1.win();
        } else {
            player2.win();
        }
    }

    /**
     * Run game
     */
    public void run() {
        for (GameInput game : inputData.getGames()) {
            turns = 0;
            manaToAdd = 2;
            player1.emptyHand();
            player2.emptyHand();
            player1.setMana(1);
            player2.setMana(1);

            // Clear board
            for (int i = 0; i < board.length; i++) {
                board[i] = new ArrayList<>();
            }

            DecksInput p1deckInfo = inputData.getPlayerOneDecks();
            DecksInput p2deckInfo = inputData.getPlayerTwoDecks();
            p1Decks = new ArrayList<>();
            p2Decks = new ArrayList<>();

            // Player 1 decks
            for (ArrayList<CardInput> deck : p1deckInfo.getDecks()) {
                ArrayList<Card> newDeck = new ArrayList<>();

                for (CardInput card : deck) {
                    newDeck.add(Card.createCard(card, player1));
                }

                p1Decks.add(newDeck);
            }

            // Player 2 decks
            for (ArrayList<CardInput> deck : p2deckInfo.getDecks()) {
                ArrayList<Card> newDeck = new ArrayList<>();

                for (CardInput card : deck) {
                    newDeck.add(Card.createCard(card, player2));
                }

                p2Decks.add(newDeck);
            }

            StartGameInput start = game.getStartGame();
            ArrayList<Card> p1Deck = new ArrayList<>(p1Decks.get(start.getPlayerOneDeckIdx()));
            ArrayList<Card> p2Deck = new ArrayList<>(p2Decks.get(start.getPlayerTwoDeckIdx()));

            Collections.shuffle(p1Deck, new Random(start.getShuffleSeed()));
            Collections.shuffle(p2Deck, new Random(start.getShuffleSeed()));

            player1.setDeck(p1Deck);
            player1.setHero(Card.createCard(start.getPlayerOneHero(), player1));
            player2.setDeck(p2Deck);
            player2.setHero(Card.createCard(start.getPlayerTwoHero(), player2));

            playerTurn = start.getStartingPlayer();

            player1.takeCardFromDeck();
            player2.takeCardFromDeck();

            for (ActionsInput action : game.getActions()) {
                executeAction(action);
            }

            gamesPlayed++;
        }
    }

    private void executeAction(final ActionsInput action) {
        switch (action.getCommand()) {
            case "getPlayerDeck" -> Debug.getPlayerDeck(action.getPlayerIdx());
            case "getCardsInHand" ->
                    Debug.getCardsInHand(action.getPlayerIdx());
            case "getCardsOnTable" -> Debug.getCardsOnTable();
            case "getPlayerTurn" -> Debug.getPlayerTurn();
            case "getPlayerHero" -> Debug.getPlayerHero(action.getPlayerIdx());
            case "getCardAtPosition" ->
                    Debug.getCardAtPosition(action.getX(), action.getY());
            case "getPlayerMana" -> Debug.getPlayerMana(action.getPlayerIdx());
            case "getEnvironmentCardsInHand" ->
                    Debug.getEnvironmentCardsInHand(action.getPlayerIdx());
            case "getFrozenCardsOnTable" -> Debug.getFrozenCardsOnTable();
            case "getTotalGamesPlayed" -> Statistics.getTotalGamesPlayed();
            case "getPlayerOneWins" -> Statistics.getPlayerOneWins();
            case "getPlayerTwoWins" -> Statistics.getPlayerTwoWins();
            case "endPlayerTurn" -> Gameplay.endPlayerTurn();
            case "placeCard" -> Gameplay.placeCard(action.getHandIdx());
            case "cardUsesAttack" ->
                    Gameplay.cardUsesAttack(action.getCardAttacker(), action.getCardAttacked());
            case "cardUsesAbility" ->
                    Gameplay.cardUsesAbility(action.getCardAttacker(), action.getCardAttacked());
            case "useAttackHero" ->
                    Gameplay.useAttackHero(action.getCardAttacker());
            case "useHeroAbility" ->
                    Gameplay.useHeroAbility(action.getAffectedRow());
            case "useEnvironmentCard" ->
                    Gameplay.useEnvironmentCard(action.getHandIdx(), action.getAffectedRow());
            default -> { }
        }
    }
}
