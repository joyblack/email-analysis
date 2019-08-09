package com.sunrun.emailanalysis.data.request;

public class RequestParameterValue {
    // Common
    public static final Integer INCLUDE_YES = 1;
    public static final Integer INCLUDE_NO = 0;
    public static final Integer INCLUDE_ALL = -1;

    // page
    public static final Integer PAGE_INDEX_DEFAULT = 0;
    public static final Integer PAGE_SIZE_DEFAULT = 10;
    public static final Integer SHIFT_DEFAULT = 0;

    // sort
    public static final String SORT_DEFAULT = null;
    public static final String ORDER_DEFAULT = null;

    // analysis
    public static final Integer ENABLE_ENGLISH_DEFAULT = 0;
    public static final Integer ENABLE_CHINESE_DEFAULT = 1;

    // case
    public static final Long CASE_ID_DEFAULT = 0L;
    public static final String CASE_NAME_DEFAULT = null;

    // statistic
    public static final Integer STATISTIC_TYPE_DIVIDE = 0;
    public static final Integer STATISTIC_TYPE_COMBINE = 1;



}
