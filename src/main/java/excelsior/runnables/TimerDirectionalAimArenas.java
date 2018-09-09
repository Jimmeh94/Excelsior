package excelsior.runnables;

import ecore.runnables.AbstractTimer;
import excelsior.Excelsior;

public class TimerDirectionalAimArenas extends AbstractTimer {

    public TimerDirectionalAimArenas() {
        super(2L);
    }

    @Override
    protected void runTask() {
        Excelsior.INSTANCE.getArenaManager().updatePlayersAim();
    }
}
