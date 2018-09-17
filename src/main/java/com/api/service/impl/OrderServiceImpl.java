package com.api.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.api.dao.*;
import com.api.model.Order;
import com.api.model.OrderDetail;
import com.api.model.Product;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Autowired
    private UserDao userDao;

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
            orderPo.setMt(new Date());
            orderDao.update(orderPo);
            detailPo.setOrderId(orderPo.getId());
            orderDetailDao.updateSelective(detailPo);
        }else{
            //新增
            orderPo = new Order();
            detailPo  = new OrderDetail();
            covertOrderVoToPo(order,detail,orderPo,detailPo);
            orderPo.setMt(new Date());
            Long id = orderDao.insert(orderPo);
            detailPo.setOrderId(id);
            detailPo.setCt(new Date());
            orderDetailDao.insert(detailPo);
        }
        return order.getId();
    }

    private void covertOrderVoToPo(OrderVo order, OrderDetailVo detail, Order orderPo, OrderDetail detailPo) {
        orderPo.setRecAddress(order.getRecAddress());
        orderPo.setRecMobile(order.getRecMobile());
        orderPo.setRecName(order.getRecName());
        orderPo.setStatus(order.getStatus());
        orderPo.setTotalPrice(order.getTotalPrice());
        orderPo.setUserId(order.getUserId());
        orderPo.setId(order.getId());
        detailPo.setColor(detail.getColor());
        detailPo.setCount(detail.getCount());
        detailPo.setProductId(detail.getProductId());
        detailPo.setSize(detail.getSize());
        detailPo.setId(detail.getId());
    }

    @Override
    public void deleteOrder(OrderVo order) {
        orderDetailDao.deleteByOrderId(order.getId());
        orderDao.deleteById(order.getId());
    }

    @Override
    public OrderVo getOrder(Long orderId) {
        Order po = orderDao.selectById(orderId);
        OrderVo vo ;
        if(po != null){
            vo = new OrderVo();
            OrderDetail detailPo = new OrderDetail();
            detailPo.setOrderId(po.getId());
            detailPo = orderDetailDao.selectByEntity(detailPo);
            convertPoToVo(vo,po,detailPo);
            return vo ;
        }else{
            return null;
        }

    }

    private void convertPoToVo(OrderVo vo, Order po, OrderDetail detailPo) {
        OrderDetailVo detailVo = new OrderDetailVo();
        Product product = productDao.selectById(detailPo.getProductId());
        detailVo.setId(detailPo.getId());
        detailVo.setSize(detailPo.getSize());
        detailVo.setProductId(detailPo.getProductId());
        detailVo.setCount(detailPo.getCount());
        detailVo.setColor(detailPo.getColor());
        detailVo.setProductName(product.getName());

        vo.setOrderDetailVo(detailVo);
        vo.setCode(po.getCode());
        vo.setId(po.getId());
        vo.setRecAddress(po.getRecAddress());
        vo.setRecMobile(po.getRecMobile());
        vo.setRecName(po.getRecName());
        vo.setStatus(po.getStatus());
        vo.setTotalPrice(po.getTotalPrice());
        vo.setTradeNumber(po.getTradeNumber());
        vo.setUserId(po.getUserId());
        User user = userDao.selectById(po.getId());
        vo.setUserName(user.getName());
    }

    @Override
    public Page<OrderVo> inquireOrders(OrderVo order, int nextPage, int pageSize) {
        return null;
    }
}
