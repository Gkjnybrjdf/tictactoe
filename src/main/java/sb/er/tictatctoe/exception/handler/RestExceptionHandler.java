package sb.er.tictatctoe.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sb.er.tictatctoe.exception.GameRuntimeException;
import sb.er.tictatctoe.exception.TurnRuntimeException;
import sb.er.tictatctoe.exception.UndoRuntimeException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({TurnRuntimeException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handle(TurnRuntimeException exception) {
        exception.printStackTrace();
    }

    @ExceptionHandler({UndoRuntimeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handle(UndoRuntimeException exception) {
        exception.printStackTrace();
    }

    @ExceptionHandler({GameRuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(GameRuntimeException exception) {
        exception.printStackTrace();
    }
}
