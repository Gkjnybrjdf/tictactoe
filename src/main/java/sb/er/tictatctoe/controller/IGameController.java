package sb.er.tictatctoe.controller;

import sb.er.tictatctoe.dto.GameDto;
import sb.er.tictatctoe.dto.TurnDto;
import sb.er.tictatctoe.model.Turn;


public interface IGameController {

    /**
     * Возвращает текущую доску партии
     */
    GameDto get();

    /**
     * Начать новую партию
     *
     * @param first кто делает в новой партии первый ход: пользователь или машина
     * @return пустая доска (если ход пользователя), иначе доска с ходом машины
     */
    GameDto create(Turn.Player first);

    /**
     * Сделать ход
     *
     * @param turnDto ход пользователя
     * @return В ответе возвращается доска где сделан ответный ход машины
     */
    GameDto move(TurnDto turnDto);

    /**
     * Отменить ход
     */
    GameDto undo();
}
