package com.nettyserver.service;

import com.nettyserver.bean.RequestBean;
import com.nettyserver.bean.TestProduct;

public class ProductHttpService extends BaseHttpService {


    public ProductHttpService(RequestBean requestBean) {
        super(requestBean);
    }

    public TestProduct productDetail() {
        TestProduct product = new TestProduct();
        product.setProductName("苹果");
        product.setProductNum(20);
        return product;
    }
}
