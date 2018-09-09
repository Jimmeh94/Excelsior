package excelsior.commands;

import excelsior.Excelsior;
import excelsior.game.match.Arena;
import excelsior.game.match.Team;
import excelsior.game.match.field.Grid;
import excelsior.game.match.field.GridNormal;
import excelsior.game.match.gamemodes.GamemodeDuel;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if(label.equalsIgnoreCase("test")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("grid")){

                    Location location = player.getLocation();
                    new GridNormal(location.toVector(), location.getWorld().getName(), 11, 11, 3, 3, true);

                } else if(args[0].equalsIgnoreCase("arena")){
                    Location location = player.getLocation();

                    GamemodeDuel duel = new GamemodeDuel(location.getWorld().getName());
                    duel.addTeam(new Team(new CombatantProfilePlayer(player.getUniqueId())));
                    duel.addTeam(new Team(new CombatantProfilePlayer(player.getUniqueId())));

                    Grid grid = new GridNormal(location.toVector(), location.getWorld().getName(), 11, 11, 3, 3, true);

                    Excelsior.INSTANCE.getArenaManager().add(new Arena(duel, grid, player.getLocation().getWorld().getName()));
                }
            }
        }

        return true;
    }
}
