package com.sunrun.emailanalysis.data.request;

// 分页信息
public class CommonData {
    // search key word
    protected String keyword;
    // page index.
    protected Integer pageIndex;
    // page size
    protected Integer pageSize;
    // shift.
    protected Integer shift;
    // sort
    protected String sort;

    protected String order;

    public CommonData() {

    }

    public CommonData(String keyword, Integer pageIndex, Integer pageSize, Integer shift, String sort, String order) {
        this.keyword = keyword;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.shift = shift;
        this.sort = sort;
        this.order = order;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
