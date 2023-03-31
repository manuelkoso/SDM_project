package it.units.sdm.project.core;

import it.units.sdm.project.Position;
import it.units.sdm.project.Stone;
import it.units.sdm.project.exceptions.InvalidBoardSizeException;
import it.units.sdm.project.exceptions.InvalidPositionException;
import it.units.sdm.project.interfaces.Board;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FreedomBoard implements Iterable<Position>, Board {

    private final SortedMap<Position, FreedomCell> cells = new TreeMap<>();
    private final int numberOfRows;
    private final int numberOfColumns;

    public FreedomBoard(int numberOfRows, int numberOfColumns) throws InvalidBoardSizeException {
        if (!isBoardSizeValid(numberOfRows, numberOfColumns))
            throw new InvalidBoardSizeException("The size of the board must be at least 1x1");
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        initBoardWithEmptyCells();
    }

    private void initBoardWithEmptyCells() {
        for (int i = 1; i <= numberOfRows; i++) {
            for (int j = 1; j <= numberOfColumns; j++) {
                cells.put(Position.fromCoordinates(i, j), new FreedomCell());
            }
        }
    }

    private boolean isBoardSizeValid(int numberOfRows, int numberOfColumns) {
        return numberOfRows > 0 && (numberOfRows == numberOfColumns);
    }

    @Override
    public int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    @Override
    public boolean isCellOccupied(@NotNull Position position) throws InvalidPositionException {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        return cell.isOccupied();
    }

    public void putStone(@NotNull Position position, @NotNull Stone.Color stoneColor) {
        FreedomCell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        Stone stone = new Stone(stoneColor);
        cell.putStone(stone);
    }

    @Nullable
    public Stone getStone(@NotNull Position position) {
        FreedomCell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        return cell.getStone();
    }

    @Override
    public void clearCell(@NotNull Position position) {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        cell.clear();
    }

    @Override
    public void clearBoard() {
        for (Cell cell : cells.values()) {
            cell.clear();
        }
    }

    public Set<Position> getAdjacentPositions(Position position) throws InvalidPositionException {
        if (isPositionOutOfBoardBounds(position)) {
            throw new InvalidPositionException("The specified position is outside the board");
        }
        Set<Position> adjacentPositions = new HashSet<>(8);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (i == 0 && j == 0) continue;
                    if (position.getRow() + i <= numberOfRows && position.getColumn() + j <= numberOfColumns) {
                        adjacentPositions.add(Position.fromCoordinates(position.getRow() + i, position.getColumn() + j));
                    }
                } catch (InvalidPositionException ignored) {
                }
            }
        }
        return adjacentPositions;
    }

    private boolean isPositionOutOfBoardBounds(@NotNull Position position) {
        return position.getRow() > numberOfRows || position.getColumn() > numberOfColumns;
    }

    public boolean areAdjacentCellsOccupied(@NotNull Position cellPosition) throws InvalidPositionException {
        Set<Position> adjacentPositions = getAdjacentPositions(cellPosition);
        for (Position position : adjacentPositions) {
            if (!isCellOccupied(position)) return false;
        }
        return true;
    }

    public boolean hasBoardMoreThanOneFreeCell() {
        int numberOfFreeCellsOnBoard = 0;
        for (Cell cell : cells.values()) {
            if (!cell.isOccupied()) ++numberOfFreeCellsOnBoard;
            if (numberOfFreeCellsOnBoard > 1) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = numberOfRows; i > 0; i--) {
            for (int j = 1; j <= numberOfColumns; j++) {
                if (j == 1) {
                    if (i < 10) {
                        sb.append(" ").append(i).append(" ");
                    } else {
                        sb.append(i).append(" ");
                    }
                }
                if (isCellOccupied(Position.fromCoordinates(i, j))) {
                    if (getStone(Position.fromCoordinates(i, j)).getColor() == Stone.Color.WHITE) {
                        sb.append("W");
                    } else {
                        sb.append("B");
                    }
                } else {
                    sb.append("-");
                }
                if (j < numberOfColumns) {
                    sb.append("  ");
                } else {
                    sb.append("\n");
                }
            }
        }
        sb.append(" ");
        for (int j = 0; j < numberOfColumns; j++) {
            sb.append("  ").append((char) ('A' + j));
        }
        return sb.toString();
    }

    @NotNull
    @Override
    public Iterator<Position> iterator() {
        return this.cells.keySet().iterator();
    }

    private static class FreedomCell implements Cell {
        @Nullable
        private Stone stone;

        public FreedomCell() {
        }

        @Nullable
        public Stone getStone() {
            return stone;
        }

        @Override
        public boolean isOccupied() {
            return stone != null;
        }

        public void putStone(@NotNull Stone stone) {
            this.stone = stone;
        }

        @Override
        public void clear() {
            this.stone = null;
        }

    }
}
