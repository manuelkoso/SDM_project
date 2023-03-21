import exceptions.InvalidBoardSizeException;
import exceptions.InvalidPositionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Board implements Iterable<Map.Entry<Position, Cell>> {

    private final SortedMap<Position, Cell> cells = new TreeMap<>();
    private final int numberOfRows;
    private final int numberOfColumns;

    public Board(int numberOfRows, int numberOfColumns) throws InvalidBoardSizeException {
        if (!isBoardSizeValid(numberOfRows, numberOfColumns))
            throw new InvalidBoardSizeException("The size of the board must be at least 1x1");
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        for (int i = 1; i <= numberOfRows; i++) {
            for (int j = 1; j <= numberOfColumns; j++) {
                cells.put(new Position(i, j), new Cell());
            }
        }
    }

    private boolean isBoardSizeValid(int numberOfRows, int numberOfColumns) {
        return numberOfRows > 0 && (numberOfRows == numberOfColumns);
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public boolean isCellOccupied(@NotNull Position position) throws InvalidPositionException {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        return cell.isOccupied();
    }

    public void putStone(@NotNull Position position, @NotNull Stone.Color stoneColor) {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        Stone stone = new Stone(stoneColor);
        cell.putStone(stone);
    }

    public Stone getStone(@NotNull Position position) {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        return cell.getStone();
    }

    public void clearCell(@NotNull Position position) {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        cell.clear();
    }

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
                    adjacentPositions.add(new Position(position.getRow() + i, position.getColumn() + j));
                } catch (InvalidPositionException ignored) {
                }
            }
        }
        return adjacentPositions;
    }

    private boolean isPositionOutOfBoardBounds(Position position) {
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
        for (Map.Entry<Position, Cell> cellOnBoard : cells.entrySet()) {
            if (!isCellOccupied(cellOnBoard.getKey())) numberOfFreeCellsOnBoard++;
            if (numberOfFreeCellsOnBoard > 1) return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Iterator<Map.Entry<Position, Cell>> iterator() {
        return this.cells.entrySet().iterator();
    }

}
