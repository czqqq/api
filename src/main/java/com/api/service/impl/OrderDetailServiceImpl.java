package com.api.service.impl;

import com.api.dao.OrderDao;
import com.api.dao.OrderDetailDao;
import com.api.dao.ProductDao;
import com.api.dao.UserDao;
import com.api.model.Order;
import com.api.model.OrderDetail;
import com.api.model.Product;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderDetailService;
import com.api.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {


    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Override
    public  List<OrderDetailVo> getDetails(Long orderId) {
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(orderId);
        List<OrderDetail> details = orderDetailDao.selectByEntity(detail);
        return convertPoToVo(details);
    }

    private  List<OrderDetailVo> convertPoToVo(List<OrderDetail> detailPos) {
        List<OrderDetailVo> details = new ArrayList<OrderDetailVo>();
        for (OrderDetail detailPo : detailPos) {
            OrderDetailVo detailVo = new OrderDetailVo();
            Product product = productDao.selectById(detailPo.getProductId());
            detailVo.setId(detailPo.getId());
            detailVo.setSize(detailPo.getSize());
            detailVo.setProductId(detailPo.getProductId());
            detailVo.setCount(detailPo.getCount());
            detailVo.setColor(detailPo.getColor());
            detailVo.setProductName(product.getName());
            details.add(detailVo);
        }
        return details;
    }
}
