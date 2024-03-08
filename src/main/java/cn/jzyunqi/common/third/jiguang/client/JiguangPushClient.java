package cn.jzyunqi.common.third.jiguang.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.jiguang.enums.Platform;
import cn.jzyunqi.common.third.jiguang.model.Android;
import cn.jzyunqi.common.third.jiguang.model.Audience;
import cn.jzyunqi.common.third.jiguang.model.Ios;
import cn.jzyunqi.common.third.jiguang.model.Message;
import cn.jzyunqi.common.third.jiguang.model.MessagePushReqDto;
import cn.jzyunqi.common.third.jiguang.model.Notification;
import cn.jzyunqi.common.third.jiguang.model.Options;
import cn.jzyunqi.common.third.jiguang.model.ScheduleDto;
import cn.jzyunqi.common.third.jiguang.model.SingleTrigger;
import cn.jzyunqi.common.third.jiguang.model.SmsMessage;
import cn.jzyunqi.common.third.jiguang.model.Trigger;
import cn.jzyunqi.common.third.jiguang.response.ErrorRsp;
import cn.jzyunqi.common.third.jiguang.response.MessagePushRsp;
import cn.jzyunqi.common.third.jiguang.response.RegistrationIdDetailRsp;
import cn.jzyunqi.common.third.jiguang.response.RegistrationIdListRsp;
import cn.jzyunqi.common.third.jiguang.response.SchedulePageRsp;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wiiyaya
 * @date 2018/8/17.
 */
@Slf4j
public class JiguangPushClient {

    private static final String MESSAGE_PUSH_URL = "https://api.jpush.cn/v3/push";

    private static final String SCHEDULES_PAGE_URL = "https://api.jpush.cn/v3/schedules?page=%s";

    private static final String SCHEDULES_ADD_URL = "https://api.jpush.cn/v3/schedules";

    private static final String SCHEDULES_EDIT_URL = "https://api.jpush.cn/v3/schedules/%s";

    private static final String SCHEDULE_DELETE_URL = "https://api.jpush.cn/v3/schedules/%s";

    private static final String REGISTRATION_ID_LIST_URL = "https://device.jpush.cn/v3/aliases/%s";

    private static final String ALIASES_DELETE_URL = "https://device.jpush.cn/v3/aliases/%s";

    private static final String REGISTRATION_ID_DETAIL_URL = "https://device.jpush.cn/v3/devices/%s";

    /**
     * 鉴权字符串
     */
    private final String base64Auth;

    /**
     * APNs是否生产环境
     */
    private final Boolean apnsProduction;

    /**
     * 短信补发延迟时间
     */
    private final Integer smsDelaySeconds;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public JiguangPushClient(String appKey, String masterSecret, Boolean apnsProduction, Integer smsDelaySeconds, RestTemplate restTemplate, ObjectMapper objectMapper) throws Exception {
        this.apnsProduction = apnsProduction;
        this.smsDelaySeconds = smsDelaySeconds;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        base64Auth = Base64.encodeBase64String((appKey + ":" + masterSecret).getBytes(StringUtilPlus.UTF_8));
    }

    /**
     * 发送极光消息
     *
     * @param platformList 平台列表
     * @param aliasList    别名列表
     * @param message      消息
     * @param extMessage   扩展消息
     * @return 消息id
     */
    public String pushRealTimeMessage(Set<Platform> platformList, List<String> aliasList, String message, Object extMessage, Long smsTempId, Object smsTempParams) throws BusinessException {
        MessagePushReqDto requestDto = prepareRequestDto(platformList, aliasList, message, extMessage, smsTempId, smsTempParams);

        MessagePushRsp messagePushRsp;
        try {
            URI pushUri = new URIBuilder(MESSAGE_PUSH_URL).build();

            RequestEntity<MessagePushReqDto> requestEntity = new RequestEntity<>(requestDto, getJpushHeader(), HttpMethod.POST, pushUri);
            ResponseEntity<MessagePushRsp> responseEntity = restTemplate.exchange(requestEntity, MessagePushRsp.class);

            messagePushRsp = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper pushRealTimeMessage client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_push_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_push_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper pushRealTimeMessage other error:", e);
            throw new BusinessException("common_error_jg_push_error", "-1", "other error");
        }

        if (messagePushRsp != null) {
            return messagePushRsp.getMsgId();
        } else {
            log.error("======JiguangPushHelper pushRealTimeMessage 200 error no body!");
            throw new BusinessException("common_error_jg_push_failed");
        }
    }

    /**
     * 查询极光定时消息
     *
     * @param page 第几页从1开始
     */
    public SchedulePageRsp findScheduleMessagePage(Integer page) throws BusinessException {
        SchedulePageRsp schedulePageRsp;
        try {
            URI findScheduleUri = new URIBuilder(String.format(SCHEDULES_PAGE_URL, page)).build();
            RequestEntity<Object> requestEntity = new RequestEntity<>(getJpushHeader(), HttpMethod.GET, findScheduleUri);
            ResponseEntity<SchedulePageRsp> responseEntity = restTemplate.exchange(requestEntity, SchedulePageRsp.class);
            schedulePageRsp = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper findScheduleMessagePage client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_schedule_page_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_schedule_page_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper findScheduleMessagePage error:", e);
            throw new BusinessException("common_error_jg_schedule_page_error");
        }

        if (schedulePageRsp != null) {
            return schedulePageRsp;
        } else {
            log.error("======JiguangPushHelper scheduleMessageAdd 200 error no body!");
            throw new BusinessException("common_error_jg_schedule_add_failed");
        }
    }

    /**
     * 新增定时消息推送
     *
     * @param sendTime     发送时间
     * @param platformList 平台列表
     * @param aliasList    别名列表
     * @param message      消息
     * @param extMessage   扩展消息
     * @return 定时任务id
     */
    public String scheduleMessageAdd(LocalDateTime sendTime, Set<Platform> platformList, List<String> aliasList, String message, Object extMessage, Long smsTempId, Object smsTempParams) throws BusinessException {
        Trigger trigger = new Trigger();
        SingleTrigger singleTrigger = new SingleTrigger();
        singleTrigger.setTime(sendTime);
        trigger.setSingle(singleTrigger);

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setName("Schedule_Name");
        scheduleDto.setEnabled(Boolean.TRUE);
        scheduleDto.setTrigger(trigger);
        scheduleDto.setPush(prepareRequestDto(platformList, aliasList, message, extMessage, smsTempId, smsTempParams));

        try {
            URI scheduleUri = new URIBuilder(SCHEDULES_ADD_URL).build();

            RequestEntity<ScheduleDto> requestEntity = new RequestEntity<>(scheduleDto, getJpushHeader(), HttpMethod.POST, scheduleUri);
            ResponseEntity<ScheduleDto> responseEntity = restTemplate.exchange(requestEntity, ScheduleDto.class);
            scheduleDto = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper scheduleMessageAdd client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_schedule_add_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_schedule_add_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper scheduleMessageAdd other error:", e);
            throw new BusinessException("common_error_jg_schedule_add_error", "-1", "other error");
        }

        if (scheduleDto != null) {
            return scheduleDto.getId();
        } else {
            log.error("======JiguangPushHelper scheduleMessageAdd 200 error no body!");
            throw new BusinessException("common_error_jg_schedule_add_failed");
        }
    }

    /**
     * 编辑定时消息推送
     *
     * @param scheduleId   定时消息id
     * @param sendTime     发送时间
     * @param platformList 平台列表
     * @param aliasList    别名列表
     * @param message      消息
     * @param extMessage   扩展消息
     */
    public void scheduleMessageEdit(String scheduleId, LocalDateTime sendTime, Set<Platform> platformList, List<String> aliasList, String message, Object extMessage, Long smsTempId, Object smsTempParams) throws BusinessException {
        Trigger trigger = new Trigger();
        SingleTrigger singleTrigger = new SingleTrigger();
        singleTrigger.setTime(sendTime);
        trigger.setSingle(singleTrigger);

        ScheduleDto scheduleDto = new ScheduleDto();
        scheduleDto.setName("Schedule_Name");
        scheduleDto.setEnabled(Boolean.TRUE);
        scheduleDto.setTrigger(trigger);
        scheduleDto.setPush(prepareRequestDto(platformList, aliasList, message, extMessage, smsTempId, smsTempParams));

        try {
            URI updateScheduleUri = new URIBuilder(String.format(SCHEDULES_EDIT_URL, scheduleId)).build();
            RequestEntity<ScheduleDto> requestEntity = new RequestEntity<>(scheduleDto, getJpushHeader(), HttpMethod.PUT, updateScheduleUri);
            restTemplate.exchange(requestEntity, Object.class);

        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper scheduleMessageEdit client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_schedule_edit_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_schedule_edit_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JpushServiceImpl scheduleMessageEdit error:", e);
            throw new BusinessException("common_error_jg_schedule_edit_error");
        }
    }

    /**
     * 删除极光定时消息
     *
     * @param jpushMessageId 消息id
     */
    public void deleteScheduleMessage(String jpushMessageId) throws BusinessException {
        try {
            URI deleteScheduleUri = new URIBuilder(String.format(SCHEDULE_DELETE_URL, jpushMessageId)).build();
            RequestEntity<Object> requestEntity = new RequestEntity<>(getJpushHeader(), HttpMethod.DELETE, deleteScheduleUri);
            restTemplate.exchange(requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper deleteScheduleMessage client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_schedule_del_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_schedule_del_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper deleteScheduleMessage error:", e);
            throw new BusinessException("common_error_jg_schedule_del_error");
        }
    }

    /**
     * 通过别名查找设备ID列表
     *
     * @param alias 别名
     * @return 设备id列表
     */
    public List<String> findRegistrationIdList(String alias) throws BusinessException {
        RegistrationIdListRsp registrationIdListRsp;
        try {
            URI findRegistartionIdUri = new URIBuilder(String.format(REGISTRATION_ID_LIST_URL, alias)).build();
            RequestEntity<Object> requestEntity = new RequestEntity<>(getJpushHeader(), HttpMethod.GET, findRegistartionIdUri);
            ResponseEntity<RegistrationIdListRsp> responseEntity = restTemplate.exchange(requestEntity, RegistrationIdListRsp.class);
            registrationIdListRsp = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper findRegistrationIdList client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_registration_id_list_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_registration_id_list_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper findRegistrationIdList error:", e);
            throw new BusinessException("common_error_jg_registration_id_list_error");
        }

        if (registrationIdListRsp != null) {
            return registrationIdListRsp.getRegistrationIdList();
        } else {
            log.error("======JiguangPushHelper findRegistrationIdList 200 error no body!");
            throw new BusinessException("common_error_jg_registration_id_list_failed");
        }
    }

    /**
     * 解绑所有绑定了指定别名的设备
     *
     * @param alias 别名
     */
    public void deleteAlias(String alias) throws BusinessException {
        try {
            URI deleteAliasUri = new URIBuilder(String.format(ALIASES_DELETE_URL, alias)).build();
            RequestEntity<Object> requestEntity = new RequestEntity<>(getJpushHeader(), HttpMethod.DELETE, deleteAliasUri);
            restTemplate.exchange(requestEntity, RegistrationIdListRsp.class);
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper deleteAlias client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_alias_delete_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_alias_delete_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper deleteAlias error:", e);
            throw new BusinessException("common_error_jg_alias_delete_error");
        }
    }

    /**
     * 获取设备ID的详情
     *
     * @param registrationId 设备ID
     * @return 详情
     */
    public RegistrationIdDetailRsp getRegistrationIdDetail(String registrationId) throws BusinessException {
        RegistrationIdDetailRsp registrationIdDetailRsp;
        try {
            URI findScheduleUri = new URIBuilder(String.format(REGISTRATION_ID_DETAIL_URL, registrationId)).build();
            RequestEntity<Object> requestEntity = new RequestEntity<>(getJpushHeader(), HttpMethod.GET, findScheduleUri);
            ResponseEntity<RegistrationIdDetailRsp> responseEntity = restTemplate.exchange(requestEntity, RegistrationIdDetailRsp.class);
            registrationIdDetailRsp = responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error("======JiguangPushHelper findScheduleMessagePage client error:", e);
            try {
                ErrorRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), ErrorRsp.class);
                throw new BusinessException("common_error_jg_registration_id_detail_error", errorRsp.getError().getCode(), errorRsp.getError().getMessage());
            } catch (IOException e1) {
                throw new BusinessException("common_error_jg_registration_id_detail_error", "-1", "readValue error");
            }
        } catch (Exception e) {
            log.error("======JiguangPushHelper getRegistrationIdDetail error:", e);
            throw new BusinessException("common_error_jg_registration_id_detail_error");
        }

        if (registrationIdDetailRsp != null) {
            return registrationIdDetailRsp;
        } else {
            log.error("======JiguangPushHelper getRegistrationIdDetail 200 error no body!");
            throw new BusinessException("common_error_jg_registration_id_detail_failed");
        }
    }

    /**
     * 获取发送鉴权
     *
     * @return 鉴权头信息
     */
    private HttpHeaders getJpushHeader() {
        HttpHeaders jpushHeader = new HttpHeaders();
        jpushHeader.setContentType(MediaType.APPLICATION_JSON);
        jpushHeader.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        jpushHeader.set(HttpHeaders.AUTHORIZATION, "Basic " + base64Auth);
        return jpushHeader;
    }

    /**
     * 组装消息
     *
     * @param platformList 平台列表
     * @param aliasList    别名列表
     * @param message      消息
     * @param extMessage   扩展消息
     * @return 消息
     */
    private MessagePushReqDto prepareRequestDto(Set<Platform> platformList, List<String> aliasList, String message, Object extMessage, Long smsTempId, Object smsTempParams) {
        MessagePushReqDto requestDto = new MessagePushReqDto();

        //设置别名
        if (CollectionUtilPlus.Collection.isNotEmpty(aliasList)) {
            Audience audience = new Audience();
            audience.setAlias(aliasList);
            requestDto.setAudience(audience);
        }

        //设置平台通知
        Notification notification = new Notification();
        //notification.setAlert(message); //安卓会收到这个通知，不要
        if (CollectionUtilPlus.Collection.isNotEmpty(platformList)) {
            requestDto.setPlatform(platformList);
            //if(platformList.contains(Platform.android)){//安卓不发送通知
            //    notification.setAndroid(prepareAndroidNotice(message, extMessage));
            //}

            if (platformList.contains(Platform.ios)) {
                notification.setIos(prepareIosNotice(message, extMessage));
            }
        } else {
            //notification.setAndroid(prepareAndroidNotice(message, extMessage)); //安卓不发送通知
            notification.setIos(prepareIosNotice(message, extMessage));
        }
        requestDto.setNotification(notification);

        //设置透传消息
        Message pushMessage = new Message();
        pushMessage.setMsgContent(message);
        pushMessage.setExtras(extMessage);
        requestDto.setMessage(pushMessage);

        //设置短信内容
        if (smsTempId != null) {
            SmsMessage smsMessage = new SmsMessage();
            smsMessage.setDelayTime(smsDelaySeconds);
            smsMessage.setTempId(smsTempId);
            smsMessage.setTempPara(smsTempParams);
            requestDto.setSmsMessage(smsMessage);
        }

        //设置配置
        Options options = new Options();
        options.setApnsProduction(apnsProduction);
        requestDto.setOptions(options);

        return requestDto;
    }

    /**
     * 组装ANDROID通知
     *
     * @param message    通知内容
     * @param extMessage 扩展消息
     * @return ANDROID通知
     */
    private Android prepareAndroidNotice(String message, Object extMessage) {
        Android android = new Android();
        android.setAlert(message);
        android.setExtras(extMessage);
        return android;
    }

    /**
     * 组装IOS通知
     *
     * @param message    通知内容
     * @param extMessage 扩展消息
     * @return IOS通知
     */
    private Ios prepareIosNotice(String message, Object extMessage) {
        Ios ios = new Ios();
        ios.setAlert(message);
        ios.setSound("default");
        ios.setExtras(extMessage);
        return ios;
    }
}
