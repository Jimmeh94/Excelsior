package excelsior.runnables;

import ecore.runnables.AbstractTimer;
import excelsior.Excelsior;

public class TimerArena extends AbstractTimer {

    public TimerArena() {
        super(20L * 1L);
    }

    @Override
    protected void runTask() {
        Excelsior.INSTANCE.getArenaManager().tick();
    }
}
