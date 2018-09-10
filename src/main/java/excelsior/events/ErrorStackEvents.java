package excelsior.events;

import ecore.ECore;
import ecore.events.ServiceErrorStackCreatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ErrorStackEvents implements Listener {

    @EventHandler
    public void onError(ServiceErrorStackCreatedEvent event){
        ECore.INSTANCE.getMessager().sendErrorStack(event.getEntry());
    }

}
