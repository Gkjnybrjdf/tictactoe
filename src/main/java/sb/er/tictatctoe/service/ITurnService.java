package sb.er.tictatctoe.service;

import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.model.Turn;

import java.util.List;

public interface ITurnService {
    /**
     * Создание "хода" пользователя
     *
     * @param existingTurns уже выполненные ходы партии
     * @param turnDto       данные о ходе
     * @return объект хода
     */
    Turn createPersonTurn(List<Turn> existingTurns, TurnDto turnDto);

    /**
     * Создание случайного "хода" машины
     *
     * @param existingTurns уже выполненные ходы партии
     * @return объект хода
     */
    Turn createAiTurn(List<Turn> existingTurns);
}
