package sb.er.tictatctoe.mapper.impl;

import org.springframework.stereotype.Service;
import sb.er.tictatctoe.dto.GameDto;
import sb.er.tictatctoe.model.Game;
import sb.er.tictatctoe.model.Turn;
import sb.er.tictatctoe.mapper.IGameMapperService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sb.er.tictatctoe.Constants.BOARD_SIZE;

@Service
public class GameMapperService implements IGameMapperService {

    @Override
    public GameDto toDto(Game game) {
        List<List<Character>> boardState = getEmptyBoardState();

        for (Turn turn : game.getTurns()) {
            List<Character> row = boardState.get(turn.getRow());
            row.set(turn.getColumn(), turn.getPlayer().getIcon());
        }

        return GameDto.builder()
                .gameState(game.getState())
                .turns(boardState)
                .build();
    }

    private List<List<Character>> getEmptyBoardState() {
        List<List<Character>> boardState = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            ArrayList<Character> row = new ArrayList<>();
            Collections.addAll(row, new Character[BOARD_SIZE]);
            boardState.add(row);
        }

        return boardState;
    }
}
