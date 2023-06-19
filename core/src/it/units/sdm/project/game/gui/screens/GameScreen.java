package it.units.sdm.project.game.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.*;
import it.units.sdm.project.game.gui.FreedomGame;
import org.jetbrains.annotations.NotNull;

import static it.units.sdm.project.board.gui.GuiBoard.TILE_SIZE;

/**
 * Main {@link FreedomGame} {@link Screen}.
 * Should be displayed after the {@link it.units.sdm.project.game.Player}s and the {@link it.units.sdm.project.board.Board} are set
 * in the {@link FreedomGame}.
 */
public class GameScreen implements Screen {
    private static final int PADDING = 10;
    @NotNull
    private final Stage stage;
    @NotNull
    private final VisTextArea logArea;

    /**
     * Creates an instance of a {@link GameScreen} and it sets the {@link it.units.sdm.project.board.Board} layout and the log area to be displayed.
     *
     * @param game The {@link FreedomGame} using {@code this} {@link Screen}
     */
    public GameScreen(@NotNull FreedomGame game) {
        final int GAME_SCREEN_WORLD_WIDTH = TILE_SIZE * game.getNumberOfRowsAndColumns() + 800;
        final int GAME_SCREEN_WORLD_HEIGHT = TILE_SIZE * game.getNumberOfRowsAndColumns() + 600;
        stage = new Stage(new FitViewport(GAME_SCREEN_WORLD_WIDTH, GAME_SCREEN_WORLD_HEIGHT), new SpriteBatch());
        VisTable container = new VisTable();
        logArea = new VisTextArea("Welcome to Freedom ");
        logArea.setPrefRows(game.getNumberOfRowsAndColumns() * 2f);
        VisScrollPane logAreaScrollPane = new VisScrollPane(logArea);
        stage.addActor(container);
        Gdx.input.setInputProcessor(stage);
        container.setFillParent(true);
        logArea.setReadOnly(true);
        logArea.appendText(game.getWhitePlayer() + " and ");
        logArea.appendText(game.getBlackPlayer() + "!\n");
        logArea.appendText(game.getWhitePlayer() + ", tap anywhere on the board to begin!\n");
        container.add(logAreaScrollPane).growX().height((float) TILE_SIZE * game.getNumberOfRowsAndColumns()).pad(PADDING);
        container.add((Actor) game.getBoard()).pad(PADDING);
        container.row();
        container.add(new VisLabel("Check out the rules if you don't remember how to play: ")).left().pad(PADDING);
        container.row();
        container.add(new LinkLabel("link to the official repository", "https://github.com/peiva-git/SDM_project#rules")).left().pad(PADDING);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        // do nothing
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // do nothing
    }

    @Override
    public void resume() {
        // do nothing
    }

    @Override
    public void hide() {
        // do nothing
    }

    @Override
    public void dispose() {
        stage.dispose();
        stage.getBatch().dispose();
    }

    /**
     * Appends {@code textToAppend} to {@code this} {@link Screen}'s log area,
     * used to display messages to the user while playing
     *
     * @param textToAppend The {@link String} to append to the log area
     */
    public void appendTextToLogArea(@NotNull String textToAppend) {
        logArea.appendText(textToAppend);
    }

}
