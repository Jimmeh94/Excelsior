package excelsior.events;

import ecore.ECore;
import ecore.services.hotbar.Hotbar;
import excelsior.Excelsior;
import excelsior.game.cards.CardBase;
import excelsior.game.chatchannels.ChatChannelKeys;
import excelsior.game.hotbars.HotbarArenaAdd;
import excelsior.game.hotbars.Hotbars;
import excelsior.game.hotbars.duel.HotbarActiveTurn;
import excelsior.game.hotbars.duel.HotbarCardDescription;
import excelsior.game.hotbars.duel.HotbarHand;
import excelsior.game.hotbars.duel.HotbarWaitingTurn;
import excelsior.game.match.field.Cell;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import excelsior.game.user.UserPlayer;
import excelsior.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);

        ECore.INSTANCE.getUsers().add(new UserPlayer(event.getPlayer()));
        ECore.INSTANCE.getChannels().findChannel(ChatChannelKeys.GlobalChannel).get().add(event.getPlayer().getUniqueId());
        Hotbars.HOTBAR_DEFAULT.setHotbar(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        event.setQuitMessage(null);

        Excelsior.INSTANCE.getArenaManager().checkPlayer(event.getPlayer());

        ECore.INSTANCE.playerQuit(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        event.setCancelled(true);

        UserPlayer userPlayer = PlayerUtils.getUserPlayer(event.getPlayer()).get();
        int index = event.getPlayer().getInventory().getHeldItemSlot();
        Hotbar current = userPlayer.getCurrentHotbar();

        if(userPlayer.getPlayerMode() == UserPlayer.PlayerMode.NORMAL){

        } else {
            //If hand is empty and right click, have gamemode handle empty right hand click
            if((event.getPlayer().getInventory().getItemInMainHand() == null
                    || event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
                    && userPlayer.getPlayerMode() != UserPlayer.PlayerMode.ARENA_MOVING_CARD){
                Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(event.getPlayer()).get().handlePlayerRightEmptyClick();
                return;
            }

            if(userPlayer.getPlayerMode() == UserPlayer.PlayerMode.ARENA_ADD){
                if(current instanceof HotbarArenaAdd){
                    current.handle(index, event.getPlayer(), event.getAction());
                }
            } else if(userPlayer.getPlayerMode() == UserPlayer.PlayerMode.ARENA_MOVING_CARD){

                if(event.getPlayer().getInventory().getItemInMainHand() == null ||
                        event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR){
                    //If aiming at appropriate cell for the card to move to, move card
                    CombatantProfilePlayer cpp = PlayerUtils.getCombatProfilePlayer(userPlayer.getUUID()).get();
                    Cell aim = cpp.getCurrentAim();
                    CardBase card = cpp.getCurrentlyMovingCard();

                    if(aim != null){
                        if(card != null && card.getMovement().isAvailableSpace(aim)){
                            Cell old = card.getCurrentCell();
                            old.setAvailable(true);
                            aim.placeCard(card);
                            Vector direction = old.getCenter().subtract(aim.getCenter()).normalize();
                            //Vector center = aim.getCenter();
                            //card.removeArmorStand();
                            //card.spawn3DRepresentationServer(new Location(Bukkit.getWorld(aim.getWorld()), center.getX(), center.getY(), center.getZ()));
                            card.moveArmorStand(direction);

                            Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(event.getPlayer());
                            PlayerUtils.getUserPlayer(event.getPlayer().getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
                            PlayerUtils.getCombatProfilePlayer(event.getPlayer().getUniqueId()).get().setCurrentlyMovingCard(null);
                        }
                    }
                } else {
                    //else let the hotbar handle the action
                    current.handle(index, event.getPlayer(), event.getAction());
                }

            } else if(Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(event.getPlayer()).isPresent() && Hotbars.isArenaDuelHotbar(current)){
                current.handle(index, event.getPlayer(), event.getAction());
            }
        }


    }
}
