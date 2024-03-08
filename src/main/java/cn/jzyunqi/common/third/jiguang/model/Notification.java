package cn.jzyunqi.common.third.jiguang.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Getter
@Setter
public class Notification implements Serializable {
    private static final long serialVersionUID = -8231739836430939611L;

    /**
     * 通知的内容，会被特定平台的内容覆盖
     */
    private String alert;

    /**
     * android 平台
     */
    private Android android;

    /**
     * iOS 平台
     */
    private Ios ios;
}
