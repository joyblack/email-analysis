package com.sunrun.emailanalysis.data.request.account.warning;

import java.util.List;

public class SetStateData {
    public Integer state;

    public List<Long> ids;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "SetStateData{" +
                "state=" + state +
                ", ids=" + ids +
                '}';
    }
}
