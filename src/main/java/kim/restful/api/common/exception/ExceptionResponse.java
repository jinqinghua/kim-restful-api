package kim.restful.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private Integer code;
    private String message;
    private String trackingId;

    public ExceptionResponse(String message) {
        this.message = message;
    }

}
