package sb.er.tictatctoe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.exception.TurnRuntimeException;
import sb.er.tictatctoe.model.Turn;
import sb.er.tictatctoe.service.ITurnService;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static sb.er.tictatctoe.Constants.BOARD_SIZE;

@Service
@RequiredArgsConstructor
public class TurnService implements ITurnService {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public Turn createPersonTurn(List<Turn> existingTurns, TurnDto turnDto) {
        int row = turnDto.getRow();
        int column = turnDto.getColumn();

        existingTurns.stream()
                .filter(turn -> turn.getRow().equals(row) && turn.getColumn().equals(column))
                .findFirst()
                .ifPresent(turn -> {
                    throw new TurnRuntimeException();
                });

        return create(Turn.Player.PERSON, row, column);
    }

    @Override
    public Turn createAiTurn(List<Turn> existingTurns) {
        int row = getRandomRow(existingTurns);
        int column = getRandomColumn(row, existingTurns);

        return create(Turn.Player.AI, row, column);
    }

    /**
     * Поиск случайной незаполненной строки доски
     */
    private int getRandomRow(List<Turn> existingTurns) {
        // Заполненность ячеек по строкам
        int[] rowLoad = new int[BOARD_SIZE];

        for (Turn existingTurn : existingTurns) {
            rowLoad[existingTurn.getRow()]++;
        }

        // Список индексов незаполненных строк
        List<Integer> available = IntStream.range(0, rowLoad.length)
                .filter(idx -> rowLoad[idx] != BOARD_SIZE)
                .boxed()
                .collect(Collectors.toList());

        // Выбираем случайную незаполненную строку
        return available.get(RANDOM.nextInt(available.size()));
    }

    /**
     * Поиск случайной незаполненной колонки доски
     */
    private int getRandomColumn(int row, List<Turn> existingTurns) {
        // Заполненность ячеек строки
        int[] columnLoad = new int[BOARD_SIZE];

        existingTurns.stream()
                .filter(turn -> turn.getRow().equals(row))
                .forEach(turn -> columnLoad[turn.getColumn()]++);

        // Список индексов незаполненных ячеек строки
        List<Integer> available = IntStream.range(0, columnLoad.length)
                .filter(idx -> columnLoad[idx] == 0)
                .boxed()
                .collect(Collectors.toList());

        // Выбираем случайную незаполненную ячейку строки
        return available.get(RANDOM.nextInt(available.size()));
    }

    private Turn create(Turn.Player player, int row, int column) {
        return Turn.builder()
                .row(row)
                .column(column)
                .player(player)
                .createdAt(OffsetDateTime.now())
                .build();
    }
}
