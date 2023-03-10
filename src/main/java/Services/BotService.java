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
    private boolean burner = false;
    private boolean tele = false;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
        this.tick = false;
    }

    public int GetOppositeDirection(GameObject gameObject2) {
        return (getHeadingBetween(gameObject2)+180)%360;
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

    private void searchFoods() {
        List<GameObject> foodList;
        foodList = gameState.getGameObjects()
                .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                .collect(Collectors.toList());
        List<GameObject> superFood;
        superFood = gameState.getGameObjects()
                .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                .collect(Collectors.toList());
        if (superFood.size() > 0 && foodList.size() > 0) {
            if (getDistanceBetween(bot, superFood.get(0)) < getDistanceBetween(bot, foodList.get(0)) * 2) {
                playerAction.heading = getHeadingBetween(superFood.get(0));
                System.out.println("ngejar superfood");
            } else {
                playerAction.heading = getHeadingBetween(foodList.get(0));
                System.out.println("ngejar food biasa");
            }
        } else {
            if (superFood.size() > 0) {
                playerAction.heading = getHeadingBetween(superFood.get(0));
                System.out.println("ngejar superfood");
            } else if (foodList.size() > 0) {
                playerAction.heading = getHeadingBetween(foodList.get(0));
                System.out.println("ngejar food biasa");
            } else {
                playerAction.action = PlayerActions.FORWARD;
            }
        }
    }

    private void checkDefence() {
        List<GameObject> allPlayersBigger = gameState.getPlayerGameObjects()
                .stream().filter(item -> item.getId() != bot.getId())
                .filter(item -> item.getSize() >= bot.getSize())
                .sorted(Comparator
                        .comparing(item -> getDistanceBetween(bot, item)))
                .collect(Collectors.toList());
        if (!allPlayersBigger.isEmpty()) {
            if (getDistanceBetween(allPlayersBigger.get(0), bot) <= 3 * allPlayersBigger.get(0).getSize()) {
                System.out.println("Dikejar");

                // beda ukuran dikit
                if (allPlayersBigger.get(0).getSize() <= 1.3 * bot.getSize() && getHeadingBetween(allPlayersBigger.get(0))>75) {
                    System.out.println("Bisa dilawan brow");
                
                    if (this.tick) {
                        playerAction.action = PlayerActions.FORWARD;
                        playerAction.heading = getHeadingBetween(allPlayersBigger.get(0));
                        this.tick = false;
                    } else {
                        playerAction.heading = getHeadingBetween(allPlayersBigger.get(0));
                        if (bot.getSize() >= 40) {
                            playerAction.action = PlayerActions.FIRETORPEDOES;
                        } else {
                            playerAction.action = PlayerActions.FORWARD;
                        }
                        this.tick = true;
                    }
                } else {
                    // (getHeadingBetween(allPlayersBigger.get(0)) < 50+allPlayersBigger.get(0).getSize()) 
                    System.out.println(getHeadingBetween(allPlayersBigger.get(0)));
                    System.out.println("Lari gan");
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = (GetOppositeDirection(allPlayersBigger.get(0))+30);
                    if (!burner && bot.getSize() > 20) {
                        playerAction.action = PlayerActions.STARTAFTERBURNER;
                        burner = true;
                        System.out.println("nyala gas PAS LARI");
                    } else if(burner) {
                        /* Matiin gas */
                        playerAction.action = PlayerActions.STOPAFTERBURNER;
                        burner = false;
                        System.out.println("end burner");
                    }
                    if(this.tick){
                            playerAction.heading = GetOppositeDirection(allPlayersBigger.get(0));
                            this.tick = false;
                    }else{
                        if(bot.getSize()>=40){
                                playerAction.heading = getHeadingBetween(allPlayersBigger.get(0));
                                playerAction.action = PlayerActions.FIRETORPEDOES;
                        }else{
                                playerAction.heading = GetOppositeDirection(allPlayersBigger.get(0));
                        }
                        this.tick = true;
                    }
                }
            }
        }
    }

    private void checkEnemy(List<GameObject> targets) {
        /* Kondisi dimana ada target yang bisa dikejar. Target disort */
        targets.sort(Comparator.comparing(player -> getDistanceBetween(bot, player)));
        /* Bot diarahkan ke lawan */
        playerAction.heading = getHeadingBetween(targets.get(0));
        /* Bagian selang seling */
        if (this.tick) {
            if (bot.getSize() > 30) {
                // nyalain burner klo jauh dri target
                if (getDistanceBetween(bot, targets.get(0)) > 200
                        && targets.get(0).getSize() < bot.getSize() * 1.25) {
                    if (!burner) {
                        playerAction.action = PlayerActions.STARTAFTERBURNER;
                        burner = true;
                        System.out.println("nyala gas");
                    } else if (bot.getSize() < 70) {
                        playerAction.action = PlayerActions.STOPAFTERBURNER;
                        burner = false;
                        System.out.println("mati gas");
                    }
                    System.out.println("far from target");
                }

                else if (getDistanceBetween(bot, targets.get(0)) < bot.getSize() / 2 + 75 && targets.get(0).getSize() < bot.getSize() * 1.25)
                // deket sm target
                {
                    if (burner) {
                        playerAction.action = PlayerActions.STOPAFTERBURNER;
                        burner = false;
                        System.out.println("mati lg gas");
                    }
                    playerAction.heading = getHeadingBetween(targets.get(0));
                    if (bot.getSize() > 40) {
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                    }
                } else {
                    playerAction.heading = getHeadingBetween(targets.get(0));
                    if (bot.getSize() > 40) {
                        playerAction.action = PlayerActions.FIRETORPEDOES;
                    }
                    System.out.println("deket bgt");
                
                }
            }
            this.tick = false;
        } else {
            playerAction.heading = getHeadingBetween(targets.get(0));
            this.tick = true;
            if (bot.getSize() > 40) {
                playerAction.action = PlayerActions.FIRETORPEDOES;
            }
        }
    }
    
    private void checkTorpedoes(List<GameObject> torpedoes) {
        if (torpedoes.size() > 0) {
            System.out.print("jarak bot ke torpedo: ");
            System.out.println(getDistanceBetween(bot, torpedoes.get(0)));
            if (getDistanceBetween(bot, torpedoes.get(0)) < 80 + bot.getSize() / 2) {
                playerAction.action = PlayerActions.FORWARD;
                // if (bot.getSize() > 50) {
                //     playerAction.action = PlayerActions.ACTIVATESHIELD;
                // }
                System.out.print("heading torpedo ");
                System.out.println(torpedoes.get(0).currentHeading);
                System.out.print("heading bot ");
                System.out.println(bot.currentHeading);
                playerAction.heading = (GetOppositeDirection(torpedoes.get(0)) + 60) % 360;
            }
        }
    }
    
    private void checkGasCloud(List<GameObject> gasCloud) {
        if (!gasCloud.isEmpty()) {
            System.out.print("jarak bot ke gas: ");
            System.out.println(getDistanceBetween(bot, gasCloud.get(0)));
            if (getDistanceBetween(bot, gasCloud.get(0)) <= 50 + 1.25 * bot.getSize()) {
                System.out.println("hindar gascloud");
                System.out.println(playerAction.heading);
                playerAction.heading = (GetOppositeDirection(gasCloud.get(0)) + 45) % 360;
            }
        }
    }
    
    private void checkBorderofMap() {
        /* Fungsi untuk memastikan supaya player ga keluar dari map */
        if (getDistanceFromCenter() + (bot.getSize() * 2.5) > gameState.getWorld().getRadius()) {
            playerAction.action = PlayerActions.FORWARD;
            System.out.println("To close to edge");
            playerAction.heading = getHeadingCenter()+30;
        }
    }


    public void computeNextPlayerAction(PlayerAction playerAction) {
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = new Random().nextInt(360);

        if (!gameState.getGameObjects().isEmpty() && playerStateList.size() >= 1) {
            System.out.println("");
            /*Himpunan kandidat musuh */
            List<GameObject> allEnemies = gameState.getPlayerGameObjects()
                    .stream()
                    .filter(player -> player.getId() != bot.getId())
                    .sorted(Comparator
                            .comparing(player -> getDistanceBetween(bot, player)))
                    .collect(Collectors.toList());
            /*Himpunan kandidat musuh yang lebih kecil */
            List<GameObject> targets = gameState.getPlayerGameObjects()
                    .stream()
                    .filter(player -> player.getId() != bot.getId())
                    .filter(player -> player.getSize() < bot.getSize())

                    .collect(Collectors.toList());
            /*Himpunan kandidat meriam yang lebih kecil */
            List<GameObject> torpedoes;
                    torpedoes = gameState.getGameObjects()
                            .stream()
                            .filter(item_torpedo -> item_torpedo.getGameObjectType() == ObjectTypes.TORPEDOSALVO)
                            .filter(item_torpedo -> item_torpedo.currentHeading != bot.currentHeading)
                            .filter(item_torpedo -> getDistanceBetween(bot, item_torpedo)<200)
                            .sorted(Comparator
                                    .comparing(item_torpedo -> getDistanceBetween(bot, item_torpedo)))
                            .collect(Collectors.toList());
            /*Himpunan kandidat gas cloud */
            List<GameObject> gasCloud;
                            gasCloud = gameState.getGameObjects()
                                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD)
                                    .sorted(Comparator
                                            .comparing(item -> getDistanceBetween(bot, item)))
                                    .collect(Collectors.toList());

            if (targets.size() == 0) {
                searchFoods();
            } else {
                checkEnemy(targets);
            }
 
            /* Fungsi untuk menghindari gas cloud */
            checkGasCloud(gasCloud);
            
            //Fungsi untuk menghindari torpedo
            checkTorpedoes(torpedoes);

            // Fungsi untuk memastikan apakah bot perlu menghindari dari bot lawan atau tidak
            checkDefence();

            /* Matiin gas */
            if (bot.getSize() <= 70 && burner) {
                playerAction.action = PlayerActions.STOPAFTERBURNER;
                burner = false;
                System.out.println("end burner");
            }
            /*Fungsi untuk mengecek apakah player dekat dengan ujung map */
            checkBorderofMap();
            System.out.println("");
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

    public List<List<Integer>> getPlayerList() {
        return this.playerStateList;
    }

    public List<List<Integer>> getObjectList() {
        return this.objectStateList;
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream()
                .filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    private double getDistanceFromCenter() {
        var triangleX = Math.abs(bot.getPosition().x - gameState.world.centerPoint.x);
        var triangleY = Math.abs(bot.getPosition().y - gameState.world.centerPoint.y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
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

    private int getHeadingCenter() {
        Position centerPoint = gameState.world.centerPoint;
        var direction = toDegrees(Math.atan2(centerPoint.y - bot.getPosition().y,
                centerPoint.x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }

}