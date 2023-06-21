package it.units.sdm.project.game;

import com.badlogic.gdx.graphics.Color;
import it.units.sdm.project.board.Position;
import it.units.sdm.project.board.Piece;
import it.units.sdm.project.exceptions.InvalidPositionException;
import it.units.sdm.project.board.Board;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This class represents a line of {@link Piece}s with the same {@link Color}.
 * The line {@link Position}s are stored in a
 * {@link TreeSet} by taking into account the custom ordering defined in the {@link Position} class.
 * Every {@link FreedomLine} has its own {@link Direction}, {@link Color} and {@link Piece} {@link Position}s.
 */
public class FreedomLine {
    @NotNull
    private final Board<? extends Piece> board;
    @Nullable
    private Color color;
    @Nullable
    private Direction direction;
    @NotNull
    private final SortedSet<Position> cellPositions = new TreeSet<>();

    /**
     * Creates a {@link FreedomLine} instance
     *
     * @param board The {@link Board} on which this {@link FreedomLine} is located
     */
    public FreedomLine(@NotNull Board<? extends Piece> board) {
        this.color = null;
        this.direction = null;
        this.board = board;
    }

    /**
     * Creates a {@link FreedomLine} instance from a starting {@link Position}
     *
     * @param board           {@link Board} on which this {@link FreedomLine} is located
     * @param initialPosition Initial {@link Position} of the {@link FreedomLine}
     */
    public FreedomLine(@NotNull Board<? extends Piece> board, @NotNull Position initialPosition) {
        this.board = board;
        addPosition(initialPosition);
    }

    /**
     * Adds a {@link Piece} to this {@link FreedomLine}. This method checks if the
     * {@link Position} to add is valid according to the {@link FreedomLine}'s {@link Direction}, to the last added
     * {@link Piece} {@link Position} and to the last added {@link Piece}'s {@link Color}, if any.
     *
     * @param position The {@link Position} to add
     * @throws InvalidPositionException If the {@link Position} isn't valid according to the above-mentioned criteria
     */
    public void addPosition(@NotNull Position position) throws InvalidPositionException {
        Piece stone = board.getPiece(position);
        if (stone == null) throw new InvalidPositionException("There is no piece on the current position");
        if (cellPositions.isEmpty()) color = stone.getPieceColor();
        if (checkFreedomLineDirection(position)) {
            if (stone.getPieceColor() == color) {
                cellPositions.add(position);
            } else {
                throw new InvalidPositionException("Stone on the current position has a different color");
            }
        } else {
            throw new InvalidPositionException("Invalid position");
        }
    }

    /**
     * Adds a set of {@link Piece}s to this {@link FreedomLine}. This method checks if the
     * {@link Position}s to add are valid according to the {@link FreedomLine}'s {@link Direction}, to the last added
     * {@link Piece}'s {@link Position} and to the last added {@link Piece}'s {@link Color}, if any.
     *
     * @param positions The {@link Position}s to add
     * @throws InvalidPositionException If any of the {@link Position}s aren't valid according to the above-mentioned criteria
     */
    public void addPositions(@NotNull Set<Position> positions) throws InvalidPositionException {
        for (Position position : positions) {
            addPosition(position);
        }
    }

    private boolean checkFreedomLineDirection(@NotNull Position nextPosition) throws InvalidPositionException {
        if (cellPositions.isEmpty()) return true;
        if (direction == null) setDirection(nextPosition);
        switch (direction) {
            case VERTICAL:
                if (!isVertical(nextPosition)) return false;
                break;
            case HORIZONTAL:
                if (!isHorizontal(nextPosition)) return false;
                break;
            case DIAGONAL_LEFT:
                if (!isDiagonalLeft(nextPosition)) return false;
                break;
            default:
                if (!isDiagonalRight(nextPosition)) return false;
        }
        return true;
    }

    private void setDirection(@NotNull Position nextPosition) throws InvalidPositionException {
        if (isHorizontal(nextPosition)) {
            direction = Direction.HORIZONTAL;
            return;
        }
        if (isVertical(nextPosition)) {
            direction = Direction.VERTICAL;
            return;
        }
        if (isDiagonalLeft(nextPosition)) {
            direction = Direction.DIAGONAL_LEFT;
            return;
        }
        if (isDiagonalRight(nextPosition)) {
            direction = Direction.DIAGONAL_RIGHT;
            return;
        }
        throw new InvalidPositionException("The next position is not adjacent to the last stone of this line");
    }

    private boolean isHorizontal(@NotNull Position position) {
        if(cellPositions.first().getRow() == position.getRow() && cellPositions.first().getColumn() == position.getColumn() + 1) return true;
        return cellPositions.last().getRow() == position.getRow() && cellPositions.last().getColumn() == position.getColumn() - 1;
    }

    private boolean isVertical(@NotNull Position position) {
        if(cellPositions.first().getColumn() == position.getColumn() && cellPositions.first().getRow() - 1 == position.getRow())  return true;
        return cellPositions.last().getColumn() == position.getColumn() && cellPositions.last().getRow() + 1 == position.getRow();
    }

    private boolean isDiagonalLeft(@NotNull Position position) {
        if(cellPositions.first().getColumn() == position.getColumn() - 1 && cellPositions.first().getRow() == position.getRow() + 1) return true;
        return cellPositions.last().getColumn() == position.getColumn() + 1 && cellPositions.last().getRow() == position.getRow() - 1;
    }

    private boolean isDiagonalRight(@NotNull Position position) {
        if(cellPositions.first().getColumn() == position.getColumn() + 1 && cellPositions.first().getRow() == position.getRow() + 1) return true;
        return cellPositions.last().getColumn() == position.getColumn() - 1 && cellPositions.last().getRow() == position.getRow() - 1;
    }

    /**
     * Gets the {@link FreedomLine} {@link Color}
     *
     * @return This {@link FreedomLine}'s {@link Color}
     */
    public @Nullable Color getColor() {
        return color;
    }

    /**
     * Returns all this {@link FreedomLine}'s {@link Position}s
     *
     * @return This {@link FreedomLine}'s {@link Position}s
     */
    @NotNull
    public Set<Position> getCellPositions() {
        return cellPositions;
    }

    /**
     * Returns this {@link FreedomLine}'s first {@link Position} according to the {@link Position} ordering.
     *
     * @return The first {@link Position} in this {@link FreedomLine}
     */
    @NotNull
    public Position first() {
        return cellPositions.first();
    }

    /**
     * Returns this {@link FreedomLine}'s last {@link Position} according to the {@link Position} ordering.
     *
     * @return The last {@link Position} in this {@link FreedomLine}
     */
    @NotNull
    public Position last() {
        return cellPositions.last();
    }

    /**
     * Returns this {@link FreedomLine}'s size.
     *
     * @return The size of this {@link FreedomLine}
     */
    public int size() {
        return cellPositions.size();
    }

    /**
     * Two {@link FreedomLine}s are equal if they have the same {@link Color} and the same {@link Piece} {@link Position}s
     *
     * @param o The {@link Object} to compare with
     * @return {@code true} if the {@link FreedomLine}s are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FreedomLine)) return false;
        FreedomLine that = (FreedomLine) o;
        return Objects.equals(color, that.color) && Objects.equals(cellPositions, that.cellPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, color, direction, cellPositions);
    }

    /**
     * {@link String} representation of the {@link FreedomLine}
     *
     * @return A {@link String} composed by line {@link Color}, {@link FreedomLine} {@link Direction} and {@link Piece} {@link Position}s.
     */
    @Override
    public String toString() {
        return "FreedomLine{" +
                "color=" + color +
                ", direction=" + direction +
                ", cellPositions=" + cellPositions +
                '}';
    }

    /**
     * Describes all the possible {@link Direction}s that a {@link FreedomLine} can have on a {@link Board}
     */
    public enum Direction {
        /**
         * A {@link FreedomLine} has a horizontal {@link Direction} if all the
         * {@link Piece}s are placed on the same row
         */
        HORIZONTAL,
        /**
         * A {@link FreedomLine} has a vertical {@link Direction} if all the {@link Piece}s
         * are placed on the same column
         */
        VERTICAL,
        /**
         * A {@link FreedomLine} has a diagonal-left {@link Direction} if all the {@link Piece}s are placed
         * diagonally on the left with respect to the starting {@link Position}
         */
        DIAGONAL_LEFT,
        /**
         * A {@link FreedomLine} has a diagonal-right {@link Direction} if all the {@link Piece}s are placed
         * diagonally on the right with respect to the starting {@link Position}
         */
        DIAGONAL_RIGHT
    }
}
