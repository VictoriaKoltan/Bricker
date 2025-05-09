package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class BrickStrategyFactory {
    //המוטיבציה: חשבו כיצד לעצב את הקוד כדי לכלול את ההגבלה של 3 התנהגויות מיוחדות ללבנה. עצבו את הקוד כך שאם
    //תתבקשו להרחיב את התוכנה כך שיהיה ניתן ליצור לבנה עם יותר מ 3- התנהגויות, תוכלו לעשות את השינוי
    //במינימום שינויי קוד
//    לשנות את השמות בהתאם!!
    //להסביר בREADME למה בחרנו בפקטורי
    private static final String FIRST_STRATEGY = "first";
    private static final String SECOND_STRATEGY = "second";
    private static final String THIRD_STRATEGY = "third";

    public BrickStrategyFactory() {
    }

    public CollisionStrategy buildStrategy(String type, GameObjectCollection gameObjects, Counter brickCounter) {
        CollisionStrategy strategy;
        switch (type) {
            case FIRST_STRATEGY:
                strategy = new BasicCollisionStrategy(gameObjects, brickCounter);
                break;
            case SECOND_STRATEGY:
                strategy = new SecondStrategy();
                break;
            case THIRD_STRATEGY:
                strategy = new ThirdStrategy();
                break;
            default:
                strategy = null;
                break;
        }
        return strategy;
    }
}