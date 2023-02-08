package sb.er.tictatctoe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.exception.TurnRuntimeException;
import sb.er.tictatctoe.model.Turn;
import sb.er.tictatctoe.service.ITurnService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sb.er.tictatctoe.Constants.BOARD_SIZE;

@SpringBootTest
@Transactional
class TurnServiceTest {
    @Autowired
    private ITurnService turnService;

    @Test
    void test_createPersonTurn_is_valid() {
        TurnDto turnDto = new TurnDto(1, 2);

        Turn personTurn = turnService.createPersonTurn(Collections.emptyList(), turnDto);

        Assertions.assertEquals(turnDto.getRow(), personTurn.getRow());
        Assertions.assertEquals(turnDto.getColumn(), personTurn.getColumn());

        List<Turn> turns = List.of(personTurn);
        Assertions.assertThrows(
                TurnRuntimeException.class,
                () -> turnService.createPersonTurn(turns, turnDto)
        );
    }

    @Test
    void test_createAiTurn_is_valid() {
        List<Turn> turns = new ArrayList<>();

        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            Assertions.assertDoesNotThrow(
                    () -> turns.add(turnService.createAiTurn(turns))
            );
        }

        // Assert that there is no same turns (duplicates)
        for (int i = 0; i < turns.size() - 1; i++) {
            for (int j = i + 1; j < turns.size(); j++) {
                Turn t1 = turns.get(i);
                Turn t2 = turns.get(j);

                Assertions.assertFalse(t1.getRow().equals(t2.getRow()) && t1.getColumn().equals(t2.getColumn()));
            }
        }
    }
}
