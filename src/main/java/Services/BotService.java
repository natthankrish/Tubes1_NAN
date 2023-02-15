package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class BotService {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;
    private List<List<Integer>> playerStateList;
    private List<List<Integer>> objectStateList;
    private boolean tick;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
        this.tick = false;
    }


    public GameObject getBot() {
        return this.bot;
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public void computeNextPlayerAction(PlayerAction playerAction) {
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = new Random().nextInt(360);

        /* Kalau game objects tidak kosong (masih ada item di game) */
        if (!gameState.getGameObjects().isEmpty() && playerStateList.size() >= 1) {
            /* Data semua player yang lebih kecil dari bot */
            List<GameObject> targets = gameState.getPlayerGameObjects()
                    .stream()
                    .filter(player -> player.getId() != bot.getId())
                    .filter(player -> player.getSize() < bot.getSize())
                    .collect(Collectors.toList());

            /* Kalau misalnya gaada target, incer makanan */
            if (targets.size() == 0) {
                List<GameObject> foodList;
                foodList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
                playerAction.heading = getHeadingBetween(foodList.get(0));
            } else {
                /* Kalau misalnya ada target, sort berdasarkan distance, arahin ke player terdekat  */
                targets.sort(Comparator.comparing(player -> getDistanceBetween(bot, player)));
                playerAction.heading = getHeadingBetween(targets.get(0));
                /* Tembak torpedoes selang seling sama gerakan maju */
                if (this.tick) {
                    if(bot.getSize()>=15){
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                    }
                    this.tick = false;
                    playerAction.heading = getHeadingBetween(targets.get(0));
                } else {
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(targets.get(0));
                    List<GameObject> gasCloud;
                    gasCloud = gameState.getGameObjects()
                        .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GAS_CLOUD)
                        .sorted(Comparator
                                .comparing(item -> getDistanceBetween(bot, item)))
                        .collect(Collectors.toList());
                    if (getDistanceBetween(bot, gasCloud.get(0)) < gasCloud.get(0).getSize()/2) {
                        if(bot.getSize() >= 15){
                            playerAction.action = PlayerActions.FIRETORPEDOES;
                        }else{
                            playerAction.action = PlayerActions.FORWARD;
                        }
                        
                    } else {
                        this.tick = true;
                        playerAction.heading = getHeadingBetween(targets.get(0));
                    }
                }
            } 
            
            List <GameObject> allPlayersBigger = gameState.getPlayerGameObjects()
                                                    .stream().filter(item->item.getId() != bot.getId())
                                                    .filter(item->item.getSize() >= bot.getSize())
                                                    .sorted(Comparator
                                                                .comparing(item -> getDistanceBetween(bot, item)))
                                                    .collect(Collectors.toList());
            if(!allPlayersBigger.isEmpty()){
                if(getDistanceBetween(allPlayersBigger.get(0), bot) <= 3 * allPlayersBigger.get(0).getSize()){
                    System.out.println("Dikejar");
                    System.out.println(allPlayersBigger.get(0).currentHeading);
                    if(allPlayersBigger.get(0).getSize() <= 1.2*bot.getSize()){
                        System.out.println("Bisa dilawan brow");
                        playerAction.heading = getHeadingBetween(allPlayersBigger.get(0));
                        this.tick = false;
                        if(this.tick){
                            playerAction.action = PlayerActions.FORWARD;
                            this.tick = false;
                        }else{
                            if(bot.getSize()>=15){
                                playerAction.action = PlayerActions.FIRETORPEDOES;
                            }else{
                                playerAction.action = PlayerActions.FORWARD;
                            }
                            this.tick = true;
                        }
                    }else{
                        System.out.println("Lari gan");
                        playerAction.heading = allPlayersBigger.get(0).currentHeading + allPlayersBigger.get(0).getSize()/2;
                    }
                }
            }
            if(getDistanceFromCenter() + (bot.getSize() * 1.5) > gameState.getWorld().getRadius()){
                System.out.println("To close to edge");
                playerAction.heading = getHeadingCenter();   
            }
            // .sorted(Comparator.comparing(player -> getDistanceBetween(bot, player))).collect(Collectors.toList());
            // System.out.println(getHeadingBetween(enemies.get(0)));
        }

        this.playerAction = playerAction;
    }

    public GameState getGameState() {
        return this.gameState;
    }


    public void setGameState(GameState gameState, List<List<Integer>> player, List<List<Integer>> object) {
        this.gameState = gameState;
        this.playerStateList = player;
        this.objectStateList = object;
        updateSelfState();
    }

    public List<List<Integer>> getPlayerList () {
        return this.playerStateList;
    }

    public List<List<Integer>> getObjectList () {
        return this.objectStateList;
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    private double getDistanceFromCenter(){
        var triangleX = Math.abs(bot.getPosition().x - gameState.world.centerPoint.x);
        var triangleY = Math.abs(bot.getPosition().y - gameState.world.centerPoint.y);
        return Math.sqrt(triangleX * triangleX + triangleY*triangleY);
    }

    private double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    private int getHeadingBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int getHeadingCenter(){
        Position centerPoint = gameState.world.centerPoint;
        var direction = toDegrees(Math.atan2(centerPoint.y- bot.getPosition().y,
                            centerPoint.x - bot.getPosition().x));
        return (direction + 360)%360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }


}
