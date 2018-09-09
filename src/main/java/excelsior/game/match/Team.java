package excelsior.game.match;

import ecore.ECore;
import ecore.services.messages.ServiceMessager;
import ecore.services.messages.messagers.TitleMessager;
import excelsior.game.match.profiles.CombatantProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Team {

    private List<CombatantProfile> combatants;

    public Team() {
        combatants = new CopyOnWriteArrayList<>();
    }

    public Team(CombatantProfile... combatants){
        this.combatants = new CopyOnWriteArrayList<>();
        this.combatants.addAll(Arrays.asList(combatants));
    }

    public void addCombatant(CombatantProfile profile){
        combatants.add(profile);
    }

    public void broadcastStartTurnMessage() {
        for(CombatantProfile c: combatants){
            if(c.isPlayer()){
                ECore.INSTANCE.getMessager().sendMessage(Bukkit.getPlayer(c.getUUID()), ChatColor.YELLOW + "It's your turn", Optional.of(ServiceMessager.Prefix.DUEL));
                ECore.INSTANCE.getMessager().getTitleMessager().send(Bukkit.getPlayer(c.getUUID()), TitleMessager.Type.SUBTITLE,
                        ChatColor.GRAY + "It's your turn", 1, 2, 1);
            }
        }
    }


    public void broadcastEndTurnMessage(String s) {
        for(CombatantProfile c: combatants){
            if(c.isPlayer()){
                ECore.INSTANCE.getMessager().sendMessage(Bukkit.getPlayer(c.getUUID()), s, Optional.of(ServiceMessager.Prefix.DUEL));
                ECore.INSTANCE.getMessager().getTitleMessager().send(Bukkit.getPlayer(c.getUUID()), TitleMessager.Type.SUBTITLE,
                        s, 1, 2, 1);
            }
        }
    }

    public List<CombatantProfile> getCombatants() {
        return combatants;
    }
}