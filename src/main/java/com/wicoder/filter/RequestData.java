package com.wicoder.filter;

import java.util.List;


public class RequestData {
    private String pq_datatype;
    private FilterData pq_filter;
    private int pq_curpage;
    private int pq_rpp;
    private List<SortData> pq_sort;

    public String getPq_datatype() {
        return pq_datatype;
    }

    public void setPq_datatype(String pq_datatype) {
        this.pq_datatype = pq_datatype;
    }

    public FilterData getPq_filter() {
        return pq_filter;
    }

    public void setPq_filter(FilterData pq_filter) {
        this.pq_filter = pq_filter;
    }

    public int getPq_curpage() {
        return pq_curpage;
    }

    public void setPq_curpage(int pq_curpage) {
        this.pq_curpage = pq_curpage;
    }

    public int getPq_rpp() {
        return pq_rpp;
    }

    public void setPq_rpp(int pq_rpp) {
        this.pq_rpp = pq_rpp;
    }

    public List<SortData> getPq_sort() {
        return pq_sort;
    }

    public void setPq_sort(List<SortData> pq_sort) {
        this.pq_sort = pq_sort;
    }
}
