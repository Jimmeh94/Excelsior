package excelsior.game.cards.decks;

import excelsior.game.cards.Deck;
import excelsior.game.cards.cards.CardDummy;

import java.util.UUID;

public class DeckDummy extends Deck {

    public DeckDummy(UUID uuid) {
        super(uuid);

        for(int i = 0; i < 50; i++){
            addCard(new CardDummy(getOwner()));
        }
    }
}
