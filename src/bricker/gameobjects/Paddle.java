package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions) {// יש יותר מידי פרמטרים צריך מקסימום 4
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);//deltaTime - how much time has passed from the previous frame
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        Vector2 velocity = movementDir.mult(MOVEMENT_SPEED);
        setVelocity(velocity);

        // calculates new location
        Vector2 newTopLeft = getTopLeftCorner().add(velocity.mult(deltaTime));
        float paddleWidth = getDimensions().x();
        float screenWidth = windowDimensions.x();/**הוספתי משתנה לבנאי, לבדוק אם מותר*/

        float newX = newTopLeft.x();
        if (newX < 0) {
            newX = 0;
        } else if (newX + paddleWidth > screenWidth) {
            newX = screenWidth - paddleWidth;
        }

        setTopLeftCorner(new Vector2(newX, newTopLeft.y()));
    }

    }



