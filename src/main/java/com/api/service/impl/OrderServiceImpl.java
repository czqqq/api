package com.api.service.impl;

import com.api.dao.OrderDao;
import com.api.dao.OrderDetailDao;
import com.api.dao.ProductDao;
import com.api.dao.ProductTypeDao;
import com.api.model.Order;
import com.api.model.OrderDetail;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductTypeDao productTypeDao;

    @Override
    public Long modifyOrder(OrderVo order, OrderDetailVo detail) {
        Order orderPo;
        OrderDetail detailPo ;
        if (order.getId()!=null){
            //编辑
            orderPo = orderDao.selectById(order.getId());
            detailPo  = new OrderDetail();
           if(orderPo!=null){
               covertOrderVoToPo(order,detail,orderPo,detailPo);
           }
           orderDao.update(orderPo);

        }else{
            //新增
            orderPo = new Order();
            detailPo  = new OrderDetail();
            covertOrderVoToPo(order,detail,orderPo,detailPo);
        }

        return order.getId();
    }

    private void covertOrderVoToPo(OrderVo order, OrderDetailVo detail, Order orderPo, OrderDetail detailPo) {

    }

    @Override
    public void deleteOrder(OrderVo order) {
        orderDetailDao.deleteByOrderId(order.getId());
        orderDao.deleteById(order.getId());
    }

    @Override
    public OrderVo getOrder(Long orderId) {

        return null;
    }

    @Override
    public Page<OrderVo> inquireOrders(OrderVo order, int nextPage, int pageSize) {
        return null;
    }
}
