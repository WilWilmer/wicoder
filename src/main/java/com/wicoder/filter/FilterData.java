package com.wicoder.filter;

import java.util.List;


public class FilterData {
    private  String mode;
    private List<SearchCriteria> data;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<SearchCriteria> getData() {
        return data;
    }

    public void setData(List<SearchCriteria> data) {
        this.data = data;
    }
}
