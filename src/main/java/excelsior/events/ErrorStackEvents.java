package excelsior.events;

import ecore.ECore;
import ecore.events.ServiceErrorStackCreatedEvent;
import ecore.services.messages.ServiceMessager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ErrorStackEvents implements Listener {

    @EventHandler
    public void onError(ServiceErrorStackCreatedEvent event){
        ServiceMessager.sendErrorStack(event.getEntry());
    }

}
