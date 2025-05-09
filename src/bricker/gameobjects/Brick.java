package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    private CollisionStrategy collisionStrategy;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
//        this.collisionStrategy = collisionStrategy;
    }
//הגדרנו מופע של בריק, להסביר למה הגדרנו פונקציה פומבית
    //הבעיה היתה ששלחנו את האובייקט לפני שיצרנו אותו
    public void setStrategy(CollisionStrategy strategy) {
        this.collisionStrategy = strategy;
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        super.shouldCollideWith(other);
        return other.getTag().equals("ball");
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(shouldCollideWith(other)){
            collisionStrategy.onCollision(this, other);
        }
    }


}
