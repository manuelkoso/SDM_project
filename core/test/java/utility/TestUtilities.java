package utility;

import com.badlogic.gdx.graphics.Color;
import it.units.sdm.project.board.Position;
import it.units.sdm.project.board.Stone;
import it.units.sdm.project.board.MapBoard;
import it.units.sdm.project.board.Board;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;
import java.util.stream.IntStream;

public class TestUtilities {

    public static void fillBoardWithWhiteStones(@NotNull Board<Stone> board) {
        for (Position position : board.getPositions()) {
            board.putPiece(new Stone(Color.WHITE), position);
        }
    }

    public static @NotNull Board<Stone> parseBoardFromString(@NotNull String printedBoard, int numberOfRows, int numberOfColumns) {
        Scanner scanner = new Scanner(printedBoard);
        Board<Stone> board = new MapBoard<>(numberOfRows, numberOfColumns);
        while (scanner.hasNextLine()) {
            if (scanner.hasNextInt()) {
                int currentRow = scanner.nextInt() - 1;
                IntStream.rangeClosed(0, numberOfColumns - 1)
                        .forEach(currentColumn -> {
                            String placeholder = scanner.next("[WB-]");
                            if (placeholder.equals("W")) {
                                board.putPiece(new Stone(Color.WHITE), Position.fromCoordinates(currentRow, currentColumn));
                            } else if (placeholder.equals("B")) {
                                board.putPiece(new Stone(Color.BLACK), Position.fromCoordinates(currentRow, currentColumn));
                            }
                        });
                scanner.nextLine();
            } else {
                scanner.close();
                break;
            }
        }
        return board;
    }

}
