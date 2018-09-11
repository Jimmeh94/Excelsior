package excelsior.events;

import excelsior.Excelsior;
import excelsior.events.custom.PlayerModeChangeEvent;
import excelsior.game.match.field.Cell;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import excelsior.game.user.UserPlayer;
import excelsior.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerModeEvents implements Listener {

    @EventHandler
    public void onChange(PlayerModeChangeEvent event){

        if(Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(event.getPlayer()).isPresent()){
            CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(event.getPlayer().getUniqueId()).get();

            if(event.getChangeTo() == UserPlayer.PlayerMode.ARENA_VIEWING_CARD_INFO || event.getChangeTo() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                if(cpp.getCurrentAim() != null){
                    cpp.getCurrentAim().clearAimForPlayer(event.getPlayer());
                }

                if(event.getChangeTo() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                    //highlight all available cells to move to as green
                    if(cpp.getCurrentAim() != null){
                        Excelsior.INSTANCE.getArenaManager().findArenaWithCombatant(event.getPlayer().getUniqueId()).get()
                                .getGrid().displayAvailableCellsToMoveTo(cpp.getCurrentAim().getOccupyingCard());
                    }
                }
            } else if(event.getChangeTo() == UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT){
                if(cpp.getCurrentAim() != null){
                    cpp.getCurrentAim().drawAimForPlayer(event.getPlayer());
                }
                if(event.getChangeFrom() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                    for(Cell cell: Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(event.getPlayer()).get().getGrid().getCells()){
                        if(!cell.isAvailable() && cell.getOccupyingCard().isOwner(event.getPlayer().getUniqueId())){
                            Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(event.getPlayer()).get()
                                    .getGrid().eraseAvailableCellsToMoveTo(cell.getOccupyingCard());
                        }
                    }
                }
            }
        }

    }

}
