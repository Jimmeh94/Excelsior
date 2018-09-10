package excelsior.game.match.profiles;

import excelsior.game.cards.CardBase;
import excelsior.game.cards.Deck;
import excelsior.game.hotbars.duel.HotbarHand;
import excelsior.game.match.cards.Hand;

import java.util.UUID;

public abstract class CombatantProfile {

    private UUID owner;
    private Hand hand;
    private Deck deck;

    private double health, energy;

    public CombatantProfile(UUID owner, Deck deck) {
        this.owner = owner;
        this.deck = deck.clone();

        health = 800;
        energy = 100;

        hand = new Hand(owner);
    }

    public void drawHand(){
        //draw 4 cards
        for(int i = 0; i < 4; i++){
            drawCard();
        }
    }

    public void drawCard(){
        hand.addCard(deck.getNextCard(true));
    }

    public Deck getDeck() {
        return deck;
    }

    public double getHealth() {
        return health;
    }

    public double getEnergy() {
        return energy;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isPlayer(){
        return this instanceof CombatantProfilePlayer;
    }

    public UUID getUUID() {
        return owner;
    }
}
