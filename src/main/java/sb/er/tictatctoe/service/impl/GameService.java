package sb.er.tictatctoe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sb.er.tictatctoe.dto.GameDto;
import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.exception.GameRuntimeException;
import sb.er.tictatctoe.exception.TurnRuntimeException;
import sb.er.tictatctoe.exception.UndoRuntimeException;
import sb.er.tictatctoe.mapper.IGameMapperService;
import sb.er.tictatctoe.model.Game;
import sb.er.tictatctoe.model.Game.GameState;
import sb.er.tictatctoe.model.Turn;
import sb.er.tictatctoe.model.Turn.Player;
import sb.er.tictatctoe.repo.GameRepo;
import sb.er.tictatctoe.service.IGameService;
import sb.er.tictatctoe.service.ITurnService;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static sb.er.tictatctoe.Constants.BOARD_SIZE;

@Service
@RequiredArgsConstructor
public class GameService implements IGameService {
    private final ITurnService turnService;
    private final IGameMapperService gameMapperService;
    private final GameRepo gameRepo;

    @Override
    public GameDto get(UUID sessionId) {
        return gameMapperService.toDto(gameRepo.findBySessionId(sessionId)
                .orElseThrow(GameRuntimeException::new));
    }

    @Override
    public GameDto create(UUID sessionId, Player first) {
        List<Turn> turns = first == Player.AI
                ? List.of(turnService.createAiTurn(Collections.emptyList()))
                : Collections.emptyList();

        return gameMapperService.toDto(gameRepo.save(Game.builder()
                .sessionId(sessionId)
                .state(GameState.IN_PROGRESS)
                .turns(turns)
                .build()
        ));
    }

    @Override
    @Transactional
    public GameDto doMove(UUID sessionId, TurnDto turnDto) {
        Game game = gameRepo.findBySessionId(sessionId)
                .orElseThrow(TurnRuntimeException::new);

        if (game.getState() != GameState.IN_PROGRESS) {
            throw new TurnRuntimeException();
        }

        List<Supplier<Turn>> newTurns = List.of(
                () -> turnService.createPersonTurn(game.getTurns(), turnDto),
                () -> turnService.createAiTurn(game.getTurns())
        );

        for (Supplier<Turn> turn : newTurns) {
            game.getTurns().add(turn.get());
            game.setState(calculateNewState(game));

            if (game.getState() != GameState.IN_PROGRESS) {
                return gameMapperService.toDto(gameRepo.save(game));
            }
        }

        return gameMapperService.toDto(gameRepo.save(game));
    }

    @Override
    @Transactional
    public GameDto undo(UUID sessionId) {
        Game game = gameRepo.findBySessionId(sessionId)
                .orElseThrow(UndoRuntimeException::new);

        if (game.getState() != GameState.IN_PROGRESS || game.getTurns().size() < 2) {
            throw new UndoRuntimeException();
        }

        List<Long> toRemove = game.getTurns()
                .stream()
                .sorted(Comparator.comparing(Turn::getCreatedAt))
                .skip(game.getTurns().size() - 2)
                .map(Turn::getId)
                .collect(Collectors.toList());

        game.getTurns().removeIf(turn -> toRemove.contains(turn.getId()));

        return gameMapperService.toDto(gameRepo.save(game));
    }

    @Override
    public int removeOld(OffsetDateTime before) {
        return gameRepo.deleteByModifiedAtBefore(before);
    }

    private GameState calculateNewState(Game game) {
        if (game.getTurns().size() <= BOARD_SIZE + BOARD_SIZE - 1) {
            return GameState.IN_PROGRESS;
        }

        Player[][] board = new Player[BOARD_SIZE][BOARD_SIZE];
        for (Turn turn : game.getTurns()) {
            board[turn.getRow()][turn.getColumn()] = turn.getPlayer();
        }

        List<Function<Player[][], GameState>> boardStates = List.of(
                this::calculateDiagonalsState,
                this::calculateRowsState,
                this::calculateColumnsState
        );

        for (Function<Player[][], GameState> boardState : boardStates) {
            GameState state = boardState.apply(board);

            if (state != GameState.IN_PROGRESS) {
                return state;
            }
        }

        return GameState.IN_PROGRESS;
    }

    private GameState calculateDiagonalsState(Player[][] board) {
        Player leftCornerPlayer = board[0][0];
        Player rightCornerPlayer = board[0][BOARD_SIZE - 1];

        if (leftCornerPlayer == null || rightCornerPlayer == null) {
            return GameState.IN_PROGRESS;
        }

        boolean leftMatching = true;
        boolean rightMatching = true;

        for (int i = 1; i < board.length - 1 && (leftMatching || rightMatching); i++) {
            leftMatching = leftMatching && board[i][i] == leftCornerPlayer;
            rightMatching = rightMatching && board[i][BOARD_SIZE - i - 1] == rightCornerPlayer;
        }

        if (leftMatching) {
            return GameState.ofPlayer(leftCornerPlayer);
        } else if (rightMatching) {
            return GameState.ofPlayer(rightCornerPlayer);
        } else {
            return GameState.IN_PROGRESS;
        }
    }

    private GameState calculateRowsState(Player[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            Player player = board[i][i];

            if (player != null) {
                boolean matching = true;

                for (int j = 1; j < BOARD_SIZE && matching; j++) {
                    matching = matching && board[i][j] == player;
                }

                if (matching) {
                    return GameState.ofPlayer(player);
                }
            }
        }

        return GameState.IN_PROGRESS;
    }

    private GameState calculateColumnsState(Player[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            Player player = board[i][i];

            if (player != null) {
                boolean matching = true;

                for (int j = 1; j < BOARD_SIZE && matching; j++) {
                    matching = matching && board[j][i] == player;
                }

                if (matching) {
                    return GameState.ofPlayer(player);
                }
            }
        }

        return GameState.IN_PROGRESS;
    }
}
