package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

import java.util.Random;

public class BasicCollisionStrategy implements CollisionStrategy {
//    private static final String COLL_WITH_BRICK = "collision with brick detected";
    private GameObjectCollection gameObjects;
    private final Counter brickCounter;


    public BasicCollisionStrategy(GameObjectCollection gameObjects, Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }
/***
 * The bricks disappear 50 percent of the time (implementation in the gameManager)
 */
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
//        System.out.println(COLL_WITH_BRICK);
            brickCounter.decrement();
            gameObjects.removeGameObject(obj1, Layer.DEFAULT);
    }
}
