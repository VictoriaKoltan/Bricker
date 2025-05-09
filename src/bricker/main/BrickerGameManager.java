package bricker.main;

import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.BrickStrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.Heart;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Paddle;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickerGameManager extends GameManager {

    private static final int MAX_LIVES = 3;
    private int numOfLives = 3;
    private static final float BALL_SPEED = 250;
    private static final int BRICK_HEIGHT = 15;
    private static final int SPACING = 5;
    private final static int BRICKS_PER_ROW = 8;
    private final static int BRICK_ROWS = 7;
    private final int bricksPerRow;
    private final int brickRows;
    private Ball ball;
    private final int HEART_SIZE = 20;
    private List<GameObject> lifeHearts = new ArrayList<>();
    private TextRenderable livesTextRenderable;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener inputListener;
    private Counter bricksCounter = new Counter(0);
    private static final int HUNDRED_PERCENT = 100;
    private static final Random RAND_PERCENTAGE = new Random();
    private static final int ACTIVATION_PERCENTAGE = 50;


    /*** constructor with brick parameters from user**/
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int bricksPerRow, int brickRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.brickRows = brickRows;
    }

    /*** constructor with default brick parameters**/
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = BRICKS_PER_ROW;
        this.brickRows = BRICK_ROWS;
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.inputListener = inputListener;
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();

        //creating background
        createBackground(imageReader);

        //creating bricks
        createBricks(imageReader);

        //creating ball
        createBall(imageReader, soundReader);

        //create paddle
        createPaddle(imageReader);

        //creating walls
        createWalls(windowDimensions);

        //create hearts
        createHearts(windowDimensions, imageReader);

    }

    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", true);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", true);
        float totalSpacing = (bricksPerRow + 1) * SPACING;
        float brickWidth = (windowDimensions.x() - totalSpacing) / bricksPerRow;

        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < bricksPerRow; col++) {
                float x = SPACING + col * (brickWidth + SPACING);
                float y = 50 + row * (BRICK_HEIGHT + SPACING);
                bricksCounter.increment();
                Brick brick = new Brick(new Vector2(x, y),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage);
                gameObjects().addGameObject(brick);
                brick.setStrategy(strategyDecision());
            }
        }
    }
    /***
     * Bricks Strategy
     */
    private CollisionStrategy strategyDecision(){
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory();
        CollisionStrategy strategy = null;
        if (RAND_PERCENTAGE.nextInt(HUNDRED_PERCENT) < ACTIVATION_PERCENTAGE) {
            strategy = brickStrategyFactory.buildStrategy("first" ,gameObjects(), bricksCounter);
        }
//       else if

        return strategy;
    }

    private void createBall(ImageReader imageReader,SoundReader soundReader){
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        this.ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
        ball.setCenter(windowDimensions.mult(0.5f));
        this.gameObjects().addGameObject(ball);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;

        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setTag("ball");
    }

    private void createPaddle(ImageReader imageReader){
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject userPaddle = new Paddle(Vector2.ZERO, new Vector2(100, 15),
                paddleImage, inputListener, windowDimensions);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 30));
        gameObjects().addGameObject(userPaddle);
    }

    private void createWalls(Vector2 windowDimensions){
        float wallThickness = 15;

        //creating the ceiling
        GameObject ceiling = new GameObject
                (Vector2.ZERO, new Vector2(windowDimensions.x(), wallThickness), null);
        this.gameObjects().addGameObject(ceiling);

        //creating the left wall
        GameObject leftWall = new GameObject
                (Vector2.ZERO, new Vector2(wallThickness, windowDimensions.y()), null);
        this.gameObjects().addGameObject(leftWall);

        //creating the right wall
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - wallThickness, 0),
                        new Vector2(wallThickness, windowDimensions.y()), null);
        this.gameObjects().addGameObject(rightWall);

    }

    private void createHearts(Vector2 windowDimensions, ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        float totalWidth = MAX_LIVES * HEART_SIZE + (MAX_LIVES - 1) * SPACING;

        float startX = (windowDimensions.x() - totalWidth) / 2;
        float startY = 10;

        for (int i = 0; i < numOfLives; i++) {
            Vector2 topLeft = new Vector2(startX + i * (HEART_SIZE + SPACING), startY);
            GameObject heart = new GameObject(topLeft, new Vector2(HEART_SIZE, HEART_SIZE), heartImage);
            gameObjects().addGameObject(heart, Layer.UI);
            lifeHearts.add(heart);//הוספת לבבות חדשים
        }
        //numerical representation of lives
        createNumeric(startX, startY);
    }

//    TODO צריך להעביר למחלקה נפרדת???
    private void createNumeric(float startX, float startY){
        float textX = startX + numOfLives * (HEART_SIZE + SPACING) + 10;
        livesTextRenderable = new TextRenderable(String.valueOf(numOfLives));
        livesTextRenderable.setColor(Color.GREEN);
        GameObject livesTextObject = new GameObject(
                new Vector2(textX, startY),
                new Vector2(20, 20),
                livesTextRenderable
        );
        gameObjects().addGameObject(livesTextObject, Layer.UI);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (inputListener.isKeyPressed(KeyEvent.VK_W)) {
            handleWin();
            return;
        }
        checkForGameEnd();
    }
    //immediate win
    private void handleWin() {
        String prompt = "You win! Play again?";
        if (windowController.openYesNoDialog(prompt)) {
            numOfLives = MAX_LIVES;
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    //the game status
    private void checkForGameEnd(){
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if(bricksCounter.value() == 0){
            prompt = "You win!";
        }
        if(ballHeight > windowDimensions.y()){
            numOfLives--;
            if (!lifeHearts.isEmpty()) {
                GameObject lastHeart = lifeHearts.removeLast();
                gameObjects().removeGameObject(lastHeart, Layer.UI);
            }
            if (livesTextRenderable != null) {
                livesTextRenderable.setString(String.valueOf(numOfLives));
                //TODO אולי להעביר למחלקה
                if (numOfLives >= MAX_LIVES)
                    livesTextRenderable.setColor(Color.GREEN);
                else if (numOfLives == 2)
                    livesTextRenderable.setColor(Color.YELLOW);
                else
                    livesTextRenderable.setColor(Color.RED);
            }

            if(numOfLives > 0){
                ball.setCenter(windowDimensions.mult(0.5f));
            }
            else
                prompt = "You lose!";
        }
        if(!prompt.isEmpty()){
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt)) {
                numOfLives = MAX_LIVES;
                windowController.resetGame();
            }
            else
                windowController.closeWindow();

        }
    }

    public static void main(String[] args) {
        BrickerGameManager manager;

        if (args.length == 2) {
            manager = new BrickerGameManager("Bricker Game", new Vector2(700, 500),
                    Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } else {
            manager = new BrickerGameManager("Bricker Game", new Vector2(700, 500));
        }

        manager.run();
    }

}
