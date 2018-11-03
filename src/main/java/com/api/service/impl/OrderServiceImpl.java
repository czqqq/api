package com.api.service.impl;

import com.api.dao.*;
import com.api.model.Order;
import com.api.model.OrderDetail;
import com.api.model.Product;
import com.api.model.User;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Long addOrder(OrderVo order) {
        Order orderPo;
        List<OrderDetail> detailPos ;
        //新增
        orderPo = new Order();
        detailPos  =new ArrayList<OrderDetail>();
        covertOrderVoToPo(order,order.getOrderDetails(),orderPo,detailPos);
        orderPo.setCt(new Date());
        Long id = orderDao.insertSelective(orderPo);
        for(OrderDetail detailPo : detailPos){
            detailPo.setOrderId(id);
            if(detailPo.getCount()==null){
                detailPo.setCount(1);
            }
            detailPo.setCt(new Date());
            orderDetailDao.insertSelective(detailPo);
        }
        return orderPo.getId();
    }

    @Override
    @Transactional
    public Long modifyOrder(OrderVo order) {
        Order orderPo;
        List<OrderDetail> detailPos ;
        //新增
        orderPo = new Order();
        detailPos  =new ArrayList<OrderDetail>();
        covertOrderVoToPo(order,order.getOrderDetails(),orderPo,detailPos);
        orderPo.setMt(new Date());
        orderDao.update(orderPo);
        for(OrderDetail detailPo : detailPos){
            detailPo.setOrderId(order.getId());
            if(detailPo.getCount()==null){
                detailPo.setCount(1);
            }
            detailPo.setCt(new Date());
            orderDetailDao.updateSelective(detailPo);
        }
        return orderPo.getId();
    }

    private void covertOrderVoToPo(OrderVo order, List<OrderDetailVo> details, Order orderPo, List<OrderDetail> detailPos) {
        orderPo.setRecAddress(order.getRecAddress());
        orderPo.setRecMobile(order.getRecMobile());
        orderPo.setRecName(order.getRecName());
        orderPo.setStatus(order.getStatus());
        orderPo.setTotalPrice(order.getTotalPrice());
        orderPo.setUserId(order.getUserId());
        orderPo.setId(order.getId());
        if(details!=null&&details.size()>0){
            for(OrderDetailVo detail : details){
                OrderDetail detailPo = new OrderDetail();
                detailPo.setColor(detail.getColor());
                detailPo.setCount(detail.getCount());
                detailPo.setProductId(detail.getProductId());
                detailPo.setSize(detail.getSize());
                detailPo.setId(detail.getId());
                detailPos.add(detailPo);
            }
        }
    }

    @Override
    @Transactional
    public void deleteOrder(OrderVo order) {
        orderDetailDao.deleteByOrderId(order.getId());
        orderDao.deleteById(order.getId());
    }

    @Override
    public OrderVo getOrder(Long orderId,Long userId) {
        Order condition = new Order();
        condition.setId(orderId);
        if (userId != null) {
            condition.setUserId(userId);
        }
        List<Order> orders = orderDao.selectByEntity(condition);
        if(orders != null && orders.size() > 0){
            OrderVo vo = new OrderVo();
            List<OrderDetail> detailPos = new ArrayList<OrderDetail>();
            OrderDetail detailPo = new OrderDetail();
            detailPo.setOrderId(orders.get(0).getId());
            detailPos = orderDetailDao.selectByEntity(detailPo);
            convertPoToVo(vo,orders.get(0),detailPos);
            return vo ;
        }else{
            return null;
        }

    }

    private void convertPoToVo(OrderVo vo, Order po, List<OrderDetail> detailPos) {
        List<OrderDetailVo> details = new ArrayList<OrderDetailVo>();
        for(OrderDetail detailPo: detailPos){
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
        vo.setOrderDetails(details);
        vo.setCode(po.getCode());
        vo.setId(po.getId());
        vo.setRecAddress(po.getRecAddress());
        vo.setRecMobile(po.getRecMobile());
        vo.setRecName(po.getRecName());
        vo.setStatus(po.getStatus());
        switch (vo.getStatus()){
            case 0:{
                vo.setStatusDesc("待支付");
                break;
            }
            case 1:{
                vo.setStatusDesc("支付成功");
                break;
            }
            default:{
                vo.setStatusDesc("未知");
            }
        }
        vo.setTotalPrice(po.getTotalPrice());
        vo.setTradeNumber(po.getTradeNumber());
        vo.setUserId(po.getUserId());
        User user = userDao.selectById(po.getId());
        vo.setUserName(user.getName());
    }

    @Override
    public PageInfo<OrderVo>  inquireOrders(Order order, Integer pageIndex, Integer pageSize) {
        PageHelper.startPage(pageIndex,pageSize);
        List<OrderVo> orderList = orderDao.selectJoinByEntity(order);

        PageInfo<OrderVo> pageOrder = new PageInfo<OrderVo>(orderList);
        return pageOrder;
    }

    @Override
    public OrderVo getOrderByOrderNo(String orderNo) {
        Order condition = new Order();
        OrderVo order = null;
        condition.setCode(orderNo);
        List<OrderVo> orders = orderDao.selectJoinByEntity(condition);
        if(orders!=null && orders.size()>0){
            order =  orders.get(0);
        }
        return  order ;
    }
}
