package com.textbook.common;

/**
 * 系统常量类
 */
public class Constants {

    /**
     * 用户Token请求头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户类型
     */
    public static final String USER_TYPE_USER = "user";
    public static final String USER_TYPE_ADMIN = "admin";

    /**
     * 状态：禁用
     */
    public static final int STATUS_DISABLE = 0;

    /**
     * 状态：正常
     */
    public static final int STATUS_NORMAL = 1;

    /**
     * 教材状态
     */
    public static final int TEXTBOOK_STATUS_OFF = 0;    // 下架
    public static final int TEXTBOOK_STATUS_ON = 1;     // 在售
    public static final int TEXTBOOK_STATUS_SOLD = 2;   // 已售

    /**
     * 审核状态
     */
    public static final int AUDIT_STATUS_PENDING = 0;   // 待审核
    public static final int AUDIT_STATUS_PASS = 1;      // 通过
    public static final int AUDIT_STATUS_REJECT = 2;    // 拒绝

    /**
     * 订单状态
     */
    public static final int ORDER_STATUS_PENDING = 0;   // 待确认
    public static final int ORDER_STATUS_CONFIRMED = 1; // 已确认
    public static final int ORDER_STATUS_COMPLETED = 2; // 已完成
    public static final int ORDER_STATUS_CANCELLED = 3; // 已取消

    /**
     * 消息类型
     */
    public static final int MESSAGE_TYPE_SYSTEM = 1;    // 系统通知
    public static final int MESSAGE_TYPE_PRIVATE = 2;   // 私信

    /**
     * 是否已读
     */
    public static final int READ_NO = 0;
    public static final int READ_YES = 1;

    /**
     * 是否认证
     */
    public static final int VERIFIED_NO = 0;
    public static final int VERIFIED_YES = 1;

    /**
     * 默认分页参数
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
}
