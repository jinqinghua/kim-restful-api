package kim.restful.api.common.exception;

import java.io.Serializable;

public class MyException extends RuntimeException implements Serializable {

    public MyException() {
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }


}
