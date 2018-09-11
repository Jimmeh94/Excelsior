package excelsior.game.cards.decks;

import excelsior.game.cards.Deck;
import excelsior.game.cards.cards.CardDummy;
import excelsior.game.cards.cards.CardDummyThree;
import excelsior.game.cards.cards.CardDummyTwo;

import java.util.UUID;

public class DeckDummy extends Deck {

    public DeckDummy(UUID uuid) {
        super(uuid);

        for(int i = 0; i < 20; i++){
            addCard(new CardDummy(getOwner()));
        }
        for(int i = 0; i < 20; i++){
            addCard(new CardDummyTwo(getOwner()));
        }
        for(int i = 0; i < 10; i++){
            addCard(new CardDummyThree(getOwner()));
        }
    }
}
