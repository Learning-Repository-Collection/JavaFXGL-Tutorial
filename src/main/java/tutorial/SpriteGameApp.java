package tutorial;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;

/**
 * class for SpriteGameApp
 */
public class SpriteGameApp extends GameApplication {

    private Entity player;


    @Override
    protected void initSettings(GameSettings gameSettings) {

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * adding in the input
     */
    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Right") {

            @Override
            protected void onAction() {
                player.getComponent(AnimationComponent.class).moveRight();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Left") {

            @Override
            protected void onAction() {
                player.getComponent(AnimationComponent.class).moveLeft();
            }

        }, KeyCode.A);

    }


    @Override
    protected void initGame() {
        player = FXGL.entityBuilder()
                .at(200, 200)
                .with(new AnimationComponent())
                .buildAndAttach();
    }


}
