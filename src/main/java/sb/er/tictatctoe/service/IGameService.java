package sb.er.tictatctoe.service;

import sb.er.tictatctoe.dto.GameDto;
import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.model.Turn;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface IGameService {
    /**
     * Получение текущей доски
     *
     * @param sessionId идентификатор сессии игрока
     */
    GameDto get(UUID sessionId);

    /**
     * Начать новую партию
     *
     * @param sessionId идентификатор сессии игрока
     * @param first     кто делает в новой партии первый ход: пользователь или машина
     * @return пустая доска (если ход пользователя), иначе доска с ходом машины
     */
    GameDto create(UUID sessionId, Turn.Player first);

    /**
     * Сделать ход
     *
     * @param sessionId идентификатор сессии игрока
     * @param turnDto   ход пользователя
     * @return В ответе возвращается доска где сделан ответный ход машины
     */
    GameDto doMove(UUID sessionId, TurnDto turnDto);

    /**
     * Отменить ход
     * @param sessionId идентификатор сессии игрока
     */
    GameDto undo(UUID sessionId);

    /**
     * Выполнить очищение партий игр, по которым не было ходов некоторое время
     * @param before партии необходимо удалить ранее этой даты
     * @return
     */
    int removeOld(OffsetDateTime before);
}
