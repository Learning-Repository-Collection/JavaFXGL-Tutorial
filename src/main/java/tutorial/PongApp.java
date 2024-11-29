package tutorial;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * class for PongApp
 */
public class PongApp extends GameApplication {

    private static final int PADDLE_WIDTH = 30;
    private static final int PADDLE_HEIGHT = 100;
    private static final int BALL_SIZE = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 5;

    // two paddles and a ball
    private Entity paddle1;
    private Entity paddle2;
    private Entity ball;

    /**
     * setting the window
     * @param gameSettings gameSettings
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setTitle("Pong");
    }

    /**
     * launching the window
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * handling user input
     */
    @Override
    protected void initInput() {

        // using Key W to handle paddle one
        getInput().addAction(new UserAction("Up 1") {
            @Override
            protected void onAction() {
                paddle1.translateY(-PADDLE_SPEED);
            }
        }, KeyCode.W);

        // using Key S to handle paddle one
        getInput().addAction(new UserAction("Down 1") {
            @Override
            protected void onAction() {
                paddle2.translateY(PADDLE_SPEED);
            }
        }, KeyCode.S);

        // using Key UP to handle paddle two
        getInput().addAction(new UserAction("Up 2") {
            @Override
            protected void onAction() {
                paddle2.translateY(-PADDLE_SPEED);
            }
        }, KeyCode.UP);

        // using Key DOWN to handle paddle two
        getInput().addAction(new UserAction("Down 2") {
            @Override
            protected void onAction() {
                paddle2.translateY(PADDLE_SPEED);
            }
        }, KeyCode.DOWN);

    }

    /**
     * initializes the game variables
     * @param vars variables
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        // creating a game variable
        vars.put("score1", 0);
        vars.put("score2", 0);
    }

    /**
     * initializing the game
     */
    @Override
    protected void initGame() {

        // places the paddle on the left side and vertically centered on the screen
        paddle1 = spawnBat(0, (double) getAppHeight() / 2 - (double) PADDLE_HEIGHT / 2);

        // places the second paddle at the right edge and vertically centered on the screen
        paddle2 = spawnBat(getAppWidth() - PADDLE_WIDTH,
                (double) getAppHeight() / 2 - (double) PADDLE_HEIGHT / 2);

        // places ball on the screen
        ball = spawnBall((double) getAppWidth() / 2 - (double) BALL_SIZE / 2,
                (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
    }

    /**
     * creating the paddle bat
     * @param x position x
     * @param y position y
     * @return entity
     */
    private Entity spawnBat(double x, double y) {
        return entityBuilder()
                .at(x, y)
                // meant for collision detection
                .viewWithBBox(new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT))
                .buildAndAttach();
    }

    /**
     * creating the ball
     * @param x position x
     * @param y position y
     * @return entity
     */
    private Entity spawnBall(double x, double y) {
        return entityBuilder()
                .at(x, y)
                .viewWithBBox(new Rectangle(BALL_SIZE, BALL_SIZE))
                .with("velocity", new Point2D(BALL_SPEED, BALL_SPEED))
                .buildAndAttach();
    }

    /**
     * method for initializing the user interface
     */
    @Override
    protected void initUI() {

        // displaying the scores of player 1 and player 2
        Text textScore1 = getUIFactoryService().newText("", Color.BLACK, 22);
        Text textScore2 = getUIFactoryService().newText("", Color.BLACK, 22);

        // positioning of the scores of player 1 and player 2
        textScore1.setTranslateX(10);
        textScore1.setTranslateY(50);

        textScore2.setTranslateX(getAppWidth() - 30);
        textScore2.setTranslateY(50);

        // binding the text to the game variables
        textScore1.textProperty().bind(getWorldProperties().intProperty("score1").asString());
        textScore2.textProperty().bind(getWorldProperties().intProperty("score2").asString());

        // text is added to the UI
        getGameScene().addUINodes(textScore1, textScore2);
    }

    /**
     * the game update loop
     * @param tpf time per frame
     */
    @Override
    protected void onUpdate(double tpf) {

        // ball translation and velocity
        Point2D velocity = ball.getObject("velocity");
        ball.translate(velocity);

        // ensuring that the ball touches the paddle
        if (ball.getX() == paddle1.getRightX()
                && ball.getY() < paddle1.getBottomY()
                && ball.getBottomY() > paddle1.getY()) {
            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }

        // ensuring that the ball touches the other paddle
        if (ball.getRightX() == paddle2.getX()
                && ball.getY() < paddle2.getBottomY()
                && ball.getBottomY() > paddle2.getY()) {
            ball.setProperty("velocity", new Point2D(-velocity.getX(), velocity.getY()));
        }

        // when the ball exits the left side
        if (ball.getX() <= 0) {
            getWorldProperties().increment("score2", +1);
            resetBall();
        }

        // when the ball exits the right side
        if (ball.getRightX() >= getAppWidth()) {
            getWorldProperties().increment("score1", +1);
            resetBall();
        }

        // handling collisions
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

        if (ball.getBottomY() >= getAppHeight()) {
            ball.setY(getAppHeight() - BALL_SIZE);
            ball.setProperty("velocity", new Point2D(velocity.getX(), -velocity.getY()));
        }

    }

    /**
     * repositioning the ball
     */
    private void resetBall() {
        ball.setPosition((double) getAppWidth() / 2 - (double) BALL_SIZE / 2,
                (double) getAppHeight() / 2 - (double) BALL_SIZE / 2);
        ball.setProperty("velocity", new Point2D(BALL_SPEED, BALL_SPEED));
    }


}
