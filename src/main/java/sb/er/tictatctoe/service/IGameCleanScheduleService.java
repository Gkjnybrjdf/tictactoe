package sb.er.tictatctoe.service;

/**
 * Сервис, запускающий очищение партий, по которым не было ходов более суток
 */
public interface IGameCleanScheduleService {
    void schedule();
}
