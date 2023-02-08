package sb.er.tictatctoe.mapper;

import sb.er.tictatctoe.dto.GameDto;
import sb.er.tictatctoe.model.Game;

public interface IGameMapperService {

    /**
     * Преобразование партии в DTO для выдачи пользователям
     */
    GameDto toDto(Game game);

}
