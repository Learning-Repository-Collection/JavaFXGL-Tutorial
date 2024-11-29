package tutorial;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * class for BasicGameApp
 */
public class BasicGameApp extends GameApplication {

    private Entity player;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * setting up the window
     * @param gameSettings gameSettings
     */
    @Override
    protected void initSettings(GameSettings gameSettings) {
        gameSettings.setWidth(600);
        gameSettings.setHeight(600);
        gameSettings.setTitle("Basic Game App");
        gameSettings.setVersion("0.1");
    }

    /**
     * handling the entity
     */
    @Override
    protected void initGame() {
        player = FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300, 300)
                .viewWithBBox("brick.png")
                .with(new CollidableComponent(true))
                // .view(new Rectangle(25, 25, Color.BLUE))
                // .view("brick.png")
                .buildAndAttach();

        FXGL.entityBuilder()
                .type(EntityType.COIN)
                .at(500, 200)
                .viewWithBBox(new Circle(15, 15, 15, Color.YELLOW))
                .with(new CollidableComponent(true))
                .buildAndAttach();

    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.COIN) {

            // order of types is the same as passed into the constructor
            @Override
            protected void onCollisionBegin(Entity player, Entity coin) {
                coin.removeFromWorld();
            }
        });
    }

//    @Override
//    protected void initInput() {
//        Input input = FXGL.getInput();
//
//        input.addAction(new UserAction("Move Right") {
//            @Override
//            protected void onAction() {
//                player.translateX(5);
//            }
//        }, KeyCode.D);
//    }

    /**
     * handling the user input & movement
     */
    @Override
    protected void initInput() {

        // use Key D to move 5 steps right
        FXGL.onKey(KeyCode.D, () ->{
            player.translateX(5);
            inc("pixelsMoved", +5);
        });

        // use Key A to move 5 steps left
        FXGL.onKey(KeyCode.A, () ->{
            player.translateX(-5);
            inc("pixelsMoved", -5);

        });

        // use Key W to move 5 steps up
        FXGL.onKey(KeyCode.W, () ->{
            player.translateY(-5);
            inc("pixelsMoved", +5);

        });

        // use Key S to move 5 steps down
        FXGL.onKey(KeyCode.S, () ->{
            player.translateY(5);
            inc("pixelsMoved", +5);
        });

        // use Key F to play a sound
//        FXGL.onKeyDown(KeyCode.F, () -> {
//            play("drop.wav");
//        });

    }

    public enum EntityType {
        PLAYER, COIN
    }

    /**
     * handling the UI
     */
    protected void initUI() {

        var brickTexture = FXGL.getAssetLoader().loadTexture("brick.png");
        brickTexture.setTranslateX(50);
        brickTexture.setTranslateY(450);

        Text textPixels = new Text();
        textPixels.setTranslateX(50);  // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(FXGL.getWorldProperties().intProperty("pixelsMoved").asString());

        FXGL.getGameScene().addUINode(textPixels);
        FXGL.getGameScene().addUINode(brickTexture);
    }

    /**
     * creating a game variable
     * @param vars vars
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }




}
