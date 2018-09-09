package excelsior.runnables;

import excelsior.Excelsior;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class AbstractTimer implements Runnable {

    protected BukkitTask task;
    private int intervalsPassed = 0, cancelAt = -1;
    private long interval, delay;

    protected abstract void runTask();

    public AbstractTimer(long interval){
        this(interval, 0L);
    }

    public AbstractTimer(long interval, long delay){
        this(interval, delay, -1);
    }

    public AbstractTimer(long interval, long delay, int cancelAt){
        this.interval = interval;
        this.delay = delay;
        this.cancelAt = cancelAt;
    }

    public void start(){
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(Excelsior.INSTANCE, this, delay, interval);
    }

    public void stop(){
        if (task == null)
            return;
        try {
            task.cancel();
            task = null;
        } catch (IllegalStateException exc) {
        }
    }

    @Override
    public void run() {
        intervalsPassed++;
        if(cancelAt != -1 && intervalsPassed >= cancelAt / interval){
            stop();
        }
        runTask();
    }
}
