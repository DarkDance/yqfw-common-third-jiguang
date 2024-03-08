package cn.jzyunqi.common.third.jiguang.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class ErrorRsp implements Serializable {
    private static final long serialVersionUID = 1027051154723013836L;

    private ErrorBody error;

    @Getter
    @Setter
    public class ErrorBody implements Serializable{
        private static final long serialVersionUID = -7082767995491209216L;

        private String message;

        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
