package it.units.sdm.project.game.gui.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import it.units.sdm.project.game.GameStatusHandler;
import it.units.sdm.project.game.gui.FreedomGame;
import org.jetbrains.annotations.NotNull;

public class LastMoveDialog extends Dialog {

    public static final String POSITIVE_TEXT = "Yes";
    public static final String NEGATIVE_TEXT = "No";
    @NotNull
    private final FreedomGame game;
    @NotNull
    private final Skin skin;
    @NotNull
    private final GameStatusHandler handler;

    public LastMoveDialog(@NotNull FreedomGame game, @NotNull Skin skin, @NotNull GameStatusHandler handler) {
        super("", skin);
        this.game = game;
        this.skin = skin;
        this.handler = handler;
        text("Do you want to put the last stone?");
        button("Yes", POSITIVE_TEXT);
        button("No", NEGATIVE_TEXT);
        setSize(500, 200);
        padBottom(40f);
    }
    @Override
    protected void result(Object object) {
        super.result(object);
        if(!(object instanceof String)) {
            return;
        }
        String message = (String) object;
        if (message.equals(POSITIVE_TEXT)) {
            hide();
        } else {
            hide();
            GameOverDialog gameOverDialog = new GameOverDialog(game, skin, handler);
            gameOverDialog.show(getStage());
        }
    }
}
