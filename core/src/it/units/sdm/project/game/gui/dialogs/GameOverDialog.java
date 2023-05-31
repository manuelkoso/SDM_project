package it.units.sdm.project.game.gui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import it.units.sdm.project.game.GameStatusHandler;
import it.units.sdm.project.game.Player;
import it.units.sdm.project.game.gui.FreedomGame;
import it.units.sdm.project.game.gui.GameScreen;
import org.jetbrains.annotations.NotNull;

public class GameOverDialog extends Dialog {
    @NotNull
    private final FreedomGame game;
    @NotNull
    private final GameStatusHandler handler;

    public GameOverDialog(@NotNull FreedomGame game, @NotNull Skin skin, @NotNull GameStatusHandler handler) {
        super("", skin);
        this.game = game;
        this.handler = handler;
        button("Play again", "Play again");
        button("Quit", "Quit");
        setSize(500, 200);
        Player winner = game.getCurrentWinner();
        if (winner != null) {
            text("The winner is " + winner);
        } else {
            text("Tie!");
        }
    }

    @Override
    protected void result(Object object) {
        super.result(object);
        if (!(object instanceof String)) {
            return;
        }
        String message = (String) object;
        if (message.equals("Quit")) {
            Gdx.app.exit();
        } else {
            // play again
            handler.proceedToNextState();
            game.setScreen(new GameScreen(game));
        }
    }
}
