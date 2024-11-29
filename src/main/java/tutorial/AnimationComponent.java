package tutorial;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 * class for AnimationComponent
 */
public class AnimationComponent extends Component {

    // checking if the player is moving
    private int speed = 0;

    // manages the display animations of the character
    private final AnimatedTexture texture;

    // two animations that the character can perform
    private final AnimationChannel animIdle;
    private final AnimationChannel animWalk;

    /**
     * constructor for AnimationComponent
     */
    public AnimationComponent() {

        // when the character is idle
        animIdle = new AnimationChannel(FXGL.image("newdude.png"), 4, 32, 32,
                Duration.seconds(1), 1, 1);

        // when the character is walking
        animWalk = new AnimationChannel(FXGL.image("newdude.png"), 4, 32, 42,
                Duration.seconds(1), 0, 3);

        // character starts in idle state
        texture = new AnimatedTexture(animIdle);
    }

    /**
     * adding animation to the game
     */
    @Override
    public void onAdded() {

        // setting the origin point for scaling the character
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));

        // displays the character on the screen
        entity.getViewComponent().addChild(texture);
    }

    /**
     * called in every frame to update player's state
     * @param tpf time per frame
     */
    @Override
    public void onUpdate(double tpf) {

        // moves the entity along the x-axis
        entity.translateX(speed * tpf);

        // handling animations when player is moving
        if (speed != 0) {

            // switching the animation to walk
            if (texture.getAnimationChannel() == animIdle) {
                texture.loopAnimationChannel(animWalk);
            }

            // slowing down the player
            speed = (int) (speed * 0.9);

            // player is stopped if the speed is less than 1
            if(FXGLMath.abs(speed) < 1) {
                speed = 0;
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    /**
     * method for player moving right
     */
    public void moveRight() {

        // players speed is set to 150 pixels / second
        speed = 150;

        // sprite is displayed normally without flipping
        getEntity().setScaleX(1);
    }

    /**
     * method for player moving left
     */
    public void moveLeft() {

        // player speed is set to -150 pixels / second
        speed = -150;

        // sprite turns the other way around
        getEntity().setScaleX(-1);
    }

}
