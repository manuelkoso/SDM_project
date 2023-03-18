import exceptions.InvalidBoardSizeException;
import exceptions.InvalidPositionException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Board implements Iterable<Map.Entry<Position, Cell>> {

    private final Map<Position, Cell> cells = new HashMap<>();

    public Board(int numberOfRows, int numberOfColumns) throws InvalidBoardSizeException {
        if (!isSizeOfBoardValid(numberOfRows, numberOfColumns))
            throw new InvalidBoardSizeException("The size of the board must be at least 1x1");
        for (int i = 1; i <= numberOfRows; i++) {
            for (int j = 1; j <= numberOfColumns; j++) {
                cells.put(new Position(i, j), new Cell());
            }
        }
    }

    private boolean isSizeOfBoardValid(int numberOfRows, int numberOfColumns) {
        return numberOfRows > 0 && (numberOfRows == numberOfColumns);
    }

    @NotNull
    public Cell getCell(@NotNull Position position) throws InvalidPositionException {
        Cell cell = cells.get(position);
        if (cell == null) throw new InvalidPositionException("Invalid board position");
        return cell;
    }

    public void clearBoard() {
        for (Cell cell : cells.values()) {
            cell.clear();
        }
    }

    @NotNull
    public List<Cell> getAdjacentCells(@NotNull Position cellPosition) {
        List<Cell> adjacentCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (j == 0 && i == 0) continue;
                    Position adjacentCellPosition = new Position(cellPosition.getRow() + i, cellPosition.getColumn() + j);
                    adjacentCells.add(cells.get(adjacentCellPosition));
                } catch (InvalidPositionException ignored) {
                }
            }
        }
        return adjacentCells;
    }

    public boolean areAdjacentCellsOccupied(@NotNull Position cellPosition) {
        List<Cell> adjacentCell = getAdjacentCells(cellPosition);
        for(Cell cell : adjacentCell) {
            if(!cell.isOccupied()) return false;
        }
        return true;
    }

    public boolean hasBoardMoreThanOneFreeCell() {
        int numberOfFreeCellsOnBoard = 0;
        for (Map.Entry<Position, Cell> cellOnBoard : cells.entrySet()) {
            if (!cellOnBoard.getValue().isOccupied()) numberOfFreeCellsOnBoard++;
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
