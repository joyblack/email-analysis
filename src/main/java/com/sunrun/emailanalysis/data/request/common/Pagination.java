package com.sunrun.emailanalysis.data.request.common;

public class Pagination {
    // page index.
    private Integer pageIndex;
    // page size
    private Integer pageSize;
    // shift.
    private Integer shift;
    // sort
    private String sort;
    // order
    private String order;

    public Pagination(){}


    public Pagination(Integer pageIndex, Integer pageSize, Integer shift, String sort, String order) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.shift = shift;
        this.sort = sort;
        this.order = order;
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


    @Override
    public String toString() {
        return "Pagination{" +
                "pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                ", shift=" + shift +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
