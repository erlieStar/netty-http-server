package com.nettyserver.service;

import com.nettyserver.bean.RequestBean;
import com.nettyserver.bean.TestUser;

public class UserHttpService extends BaseHttpService {


    public UserHttpService(RequestBean requestBean) {
        super(requestBean);
    }

    public TestUser userDetail() {
        TestUser testUser = new TestUser();
        testUser.setUsername("zhangsan");
        testUser.setGender("male");
        testUser.setAge(10);
        return testUser;
    }

}
