package excelsior.commands;

import excelsior.game.match.field.GridNormal;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(label.equalsIgnoreCase("test")){
            if(args.length == 1 && args[0].equalsIgnoreCase("grid")){
                Location location = ((Player)sender).getLocation();
                new GridNormal(location.toVector(), location.getWorld().getName());
            }
        }

        return true;
    }
}
