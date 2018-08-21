package com.nettyserver.service;

import com.nettyserver.bean.RequestBean;

public class BaseHttpService {

    private RequestBean requestBean;

    public BaseHttpService(RequestBean requestBean) {
        this.requestBean = requestBean;
    }
}
