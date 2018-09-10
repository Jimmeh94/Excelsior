package excelsior.game.match.gamemodes;

import ecore.services.TimeFormatter;
import ecore.services.messages.ServiceMessager;
import excelsior.game.hotbars.Hotbars;
import excelsior.game.hotbars.duel.HotbarHand;
import excelsior.game.match.Arena;
import excelsior.game.match.Team;
import excelsior.game.match.field.Cell;
import excelsior.game.match.field.Grid;
import excelsior.game.match.profiles.CombatantProfile;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import excelsior.game.user.UserPlayer;
import excelsior.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Gamemode {

    protected List<Team> teams;
    private int timeLimit; //in seconds
    private TurnManager turnManager;
    protected String world;
    protected Arena arena;
    protected Stage gameStage = Stage.PRE_GAME;
    private int preGameTimeLimit = 5;

    public Gamemode(int timeLimit, int timeLimitForEachTurn, String world){
        teams = new ArrayList<>();

        this.timeLimit = timeLimit;
        turnManager = new TurnManager(timeLimitForEachTurn);
        this.world = world;
    }

    protected abstract void tick();
    protected abstract void endingGame();
    protected abstract void startingGame();
    public abstract String getName();

    /**
     * ID of the gamemode.
     * Duel = 0
     * Not sure if this will ever be used or not
     * @return
     */
    public abstract int getID();

    public void start(Vector start){
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    PlayerUtils.getUserPlayer(Bukkit.getPlayer(p.getUUID())).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL);
                    Bukkit.getPlayer(p.getUUID()).teleport(new Location(Bukkit.getWorld(world), start.getX(), start.getY(), start.getZ()));
                }
            }
        }
        arena.broadcastMessage(ChatColor.YELLOW + "Countdown will begin in 5 seconds", Optional.of(ServiceMessager.Prefix.DUEL));
        startingGame();
    }

    public void endGame(){
        arena.getGrid().resetCells();
        endingGame();
        arena = null;
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    PlayerUtils.getUserPlayer(Bukkit.getPlayer(p.getUUID())).get().setPlayerMode(UserPlayer.PlayerMode.NORMAL);
                    //Teleport out
                    //Bukkit.getPlayer(p.getUUID()).teleport(new Location(Bukkit.getWorld(world), start.getX(), start.getY(), start.getZ()));
                }
            }
        }
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void baseTick(){

        if(gameStage == Stage.PRE_GAME){
            //give 5 seconds for orientation
            //Shuffle decks
            //shift stage to countdown
            preGameTimeLimit--;
            if(preGameTimeLimit == 1){
                for(Team team: teams){
                    for(CombatantProfile c: team.getCombatants()){
                        c.getDeck().shuffleCards();
                        c.drawHand();
                        if(c.isPlayer()){
                            UserPlayer userPlayer = PlayerUtils.getUserPlayer(Bukkit.getPlayer(c.getUUID())).get();
                            userPlayer.setCurrentHotbar(new HotbarHand((CombatantProfilePlayer) c));
                            userPlayer.getCurrentHotbar().setHotbar(userPlayer.getPlayer());
                        }
                    }
                }

                arena.broadcastMessage(ChatColor.YELLOW + "All decks have been shuffled and your hands have been drawn!",
                        Optional.of(ServiceMessager.Prefix.DUEL));

            } else if(preGameTimeLimit == 0){
                gameStage = Stage.COUNTDOWN;
                preGameTimeLimit = 10;
            }

            if(preGameTimeLimit > 0) {
                return;
            }

        } else if(gameStage == Stage.COUNTDOWN){
            ChatColor color;
            if(preGameTimeLimit > 6){
                color = ChatColor.GREEN;
            } else if(preGameTimeLimit <= 6 && preGameTimeLimit > 3){
                color = ChatColor.YELLOW;
            } else {
                color = ChatColor.RED;
            }
            arena.broadcastMessage(color + "The " + getName() + " begins in " + preGameTimeLimit + "s", Optional.of(ServiceMessager.Prefix.DUEL));
            preGameTimeLimit--;

            if(preGameTimeLimit == 0){
                gameStage = Stage.IN_GAME;
                for(Team team: teams){
                    for(CombatantProfile c: team.getCombatants()){
                        Hotbars.HOTBAR_WAITING_TURN.setHotbar(Bukkit.getPlayer(c.getUUID()));
                    }
                }
            }

            return;
        }

        timeLimit--;
        if(timeLimit == 0){
            endGame();
        } else {
            for(Team team: teams){
                if(team.isEmptyOfPlayers()){
                    //TODO if not a PlayerVsAI game or if players don't want them to be repalced by bots
                    //TODO game should end and the other team should win
                } else {
                    for(CombatantProfile c: team.getCombatants()){
                        if(c.isPlayer()){
                            UserPlayer user = PlayerUtils.getUserPlayer(Bukkit.getPlayer(c.getUUID())).get();
                            user.updateScoreboard();
                        }
                    }
                }
            }

            if(turnManager.needToStartNextTurn()){
                turnManager.startNextTurn(teams);
            }
        }
        tick();
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public void updatePlayersAim(Grid grid) {
        if(gameStage != Stage.IN_GAME){
            return;
        }

        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    CombatantProfilePlayer cpp = (CombatantProfilePlayer)p;
                    Player player = Bukkit.getPlayer(p.getUUID());
                    BlockIterator it = new BlockIterator(player.getEyeLocation(), 0, 100);
                    while(it.hasNext()){
                        Block block = it.next();
                        if(block.getType() != Material.AIR){
                            if(grid.isCell(block.getLocation().toVector())){
                                Cell cell = grid.getCell(block.getLocation().toVector()).get();
                                if(cpp.getCurrentAim() != null && cpp.getCurrentAim() == cell){
                                    continue;
                                }
                                if(cpp.getCurrentAim() != null) {
                                    cpp.getCurrentAim().clearAimForPlayer(player);
                                }
                                cpp.setCurrentAim(cell);
                                cpp.getCurrentAim().drawAimForPlayer(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isPlayerCombatant(Player player) {
        for(Team team: teams){
            if(team.isPlayerCombatant(player)){
                return true;
            }
        }
        return false;
    }

    public void playerQuit(Player player) {
        for(Team team: teams){
            if(team.isPlayerCombatant(player)){
                team.playerQuit(player);
            }
        }
        //TODO do something here. Give option to other players to continue, continue with bot, or leave
    }

    public String getTimeLeftFormatted() {
        return TimeFormatter.getFormattedTime(timeLimit);
    }

    public boolean isPlayersTurn(Player owner) {
        return turnManager.isPlayersTurn(owner);
    }

    public String getTimeLeftInCurrentTurnFormatted() {
        return turnManager.getTimeLeftInCurrentTurn();
    }

    public List<Team> getTeams() {
        return teams;
    }


    protected class TurnManager {

        private Team currentTurn;
        private int timeLimitForEachTurn;
        private int elapsedTime;

        public TurnManager(int timeLimitForEachTurn) {
            this.timeLimitForEachTurn = timeLimitForEachTurn;
        }

        public void startNextTurn(List<Team> teams){
            if(currentTurn == null){
                currentTurn = teams.get(0);
            } else {
                int index = teams.indexOf(currentTurn);
                if(index == teams.size() - 1){
                    currentTurn = teams.get(0);
                } else {
                    currentTurn = teams.get(index + 1);
                }
            }
            elapsedTime = 0;

            currentTurn.broadcastStartTurnMessage();

            for(Team team: teams){
                if(team == currentTurn){
                    for(CombatantProfile c: team.getCombatants()){
                        if(c.isPlayer()){
                            Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(Bukkit.getPlayer(c.getUUID()));
                        }
                    }
                } else {
                    for(CombatantProfile c: team.getCombatants()){
                        if(c.isPlayer()){
                            Hotbars.HOTBAR_WAITING_TURN.setHotbar(Bukkit.getPlayer(c.getUUID()));
                        }
                    }
                    team.broadcastEndTurnMessage(ChatColor.GRAY + "Other team's turn now");
                }
            }
        }

        public boolean needToStartNextTurn(){
            elapsedTime++;
            if(elapsedTime >= timeLimitForEachTurn || currentTurn == null){
                return true;
            }
            return false;
        }

        public boolean isPlayersTurn(Player owner) {
            for(Team team: teams){
                if(team.isPlayerCombatant(owner) && team == currentTurn){
                    return true;
                }
            }
            return false;
        }

        public String getTimeLeftInCurrentTurn() {
            return TimeFormatter.getFormattedTime(timeLimitForEachTurn - elapsedTime);
        }
    }

    protected enum Stage {
        PRE_GAME,
        COUNTDOWN,
        IN_GAME,
        POST_GAME
    }


}
