package com.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.api.controller.dto.BaseResult;
import com.api.controller.dto.ResultCode;
import com.api.controller.util.AliPayUtil;
import com.api.model.Order;
import com.api.model.OrderDetail;
import com.api.model.User;
import com.api.model.UserAddress;
import com.api.model.vo.OrderDetailVo;
import com.api.model.vo.OrderVo;
import com.api.service.OrderDetailService;
import com.api.service.OrderService;
import com.api.service.UserAddressService;
import com.api.service.UserService;
import com.api.util.JwtUtil;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.net.URLDecoder;
import java.util.*;

@RestController
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAddressService userAddressService;

    @RequestMapping("addOrder")
    public BaseResult addOrder( OrderVo order) {
        BaseResult result = new BaseResult();
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        UserAddress userAddress = userAddressService.getDefaultAddress(user.getId());
        if (userAddress != null) {
            order.setRecAddress(userAddress.getRecAddress());
            order.setRecMobile(userAddress.getRecMobile());
            order.setRecName(userAddress.getRecMobile());
        }
        order.setStatus(Byte.valueOf("0"));
        order.setUserName(user.getName());
        order.setUserId(user.getId());
        //获取默认地址信息
        List<OrderDetailVo> detailVos = new ArrayList<OrderDetailVo>();
        Long id = orderService.addOrder(order);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("id", id);
        result.setData(resultMap);
        return result;
    }

    @RequestMapping("modifyOrder")
    public BaseResult modifyOrder( OrderVo order) {
        OrderVo ordert = orderService.getOrder(order.getId(),null);
        if (ordert == null) {
            return new BaseResult(ResultCode.FAILURE, "当前订单不存在，请联系管理员", null);
        } else {
            //获取地址信息
            ordert.setStatus(order.getStatus());
            orderService.modifyOrder(order);
            return new BaseResult(ResultCode.SUCCESS, "修改成功", null);
        }
    }

    @ResponseBody
    @RequestMapping("fetchOrderDetail")
    public BaseResult fetchOrderDetail(@RequestParam(name = "orderId", value = "orderId") Long orderId) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        BaseResult result = new BaseResult();
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        OrderVo order = orderService.getOrder(orderId, user.getId());
        if (order == null) {
            result.setMessage("当前订单不存在，请联系管理员");
        } else {
            result.setData(order);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrderDetailWeb")
    public BaseResult fetchOrderDetailWeb(@RequestParam(name = "orderId", value = "orderId") Long orderId) {
        OrderVo order = orderService.getOrder(orderId, null);
        if (order == null) {
            return new BaseResult(ResultCode.FAILURE, "当前订单不存在，请联系管理员", null);
        } else {
            BaseResult result = new BaseResult();
            result.setData(order);
            return result;
        }
    }

    @ResponseBody
    @RequestMapping("modifyOrderAddress")
    public BaseResult fetchOrderDetail(@RequestParam(name = "orderId", value = "orderId") Long orderId, @RequestParam(name = "addressId", value = "addressId") Long addressId) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        OrderVo order = orderService.getOrder(orderId, user.getId());
        if (order == null) {
            return new BaseResult(ResultCode.FAILURE, "当前订单不存在，请联系管理员", null);
        } else {
            //获取地址信息
            UserAddress userAddress = userAddressService.getUserAddress(user.getId(), addressId);
            if (userAddress != null) {
                order.setRecAddress(userAddress.getRecAddress());
                order.setRecMobile(userAddress.getRecMobile());
                order.setRecName(userAddress.getRecMobile());
            }
            orderService.modifyOrder(order);
            return new BaseResult(ResultCode.SUCCESS, "修改成功", null);
        }
    }

    @ResponseBody
    @GetMapping("verifyPay")
    public BaseResult verifyPay(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        BaseResult result = new BaseResult();
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, AliPayUtil.ALIPAY_PUBLIC_KEY, AliPayUtil.CHARSET, "RSA2");
            if(flag){
                if("TRADE_SUCCESS".equals(params.get("trade_status"))){
                    //商户订单号
                    String orderNo = params.get("out_trade_no");
                    //支付宝交易号
                    String tradeNumber = params.get("trade_no");
                    OrderVo order = orderService.getOrderByOrderNo(orderNo);
                    order.setStatus(Byte.valueOf("1"));
                    order.setTradeNumber(tradeNumber);
                    orderService.modifyOrder(order);
                    result.setData("success");

                    //支付完成后计算佣金、用户等级
                    userService.addParentCommission(order.getUserId(),order.getTotalPrice());
                    userService.calcUserLevel(order.getUserId());
                }else{
                    result.setData("fail");
                }
            }else{
                result.setData("fail");
            }
        } catch (AlipayApiException e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("getSign")
    public BaseResult getSign(@RequestParam(name = "orderId", value = "orderId") Long orderId) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        OrderVo order = orderService.getOrder(orderId, user.getId());
        if (order == null) {
            return new BaseResult(ResultCode.FAILURE, "当前订单不存在，请联系管理员", null);
        }
        if(!order.getStatus().equals(Byte.valueOf("0"))){
            return new BaseResult(ResultCode.FAILURE, "该订单已支付", null);
        }
        AlipayClient alipayClient = new DefaultAlipayClient(AliPayUtil.GATE,
                AliPayUtil.APPID,
                AliPayUtil.APP_PRIVATE_KEY,
                "json",
                AliPayUtil.CHARSET,
                AliPayUtil.ALIPAY_PUBLIC_KEY,
                "RSA2");

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();

        model.setSubject("【天磊商城】:"+order.getOrderDetails().get(0).getProductName());
        model.setOutTradeNo(order.getCode());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(order.getTotalPrice().toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AliPayUtil.NOTIFY_URL);
        AlipayTradeAppPayResponse response = null;
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            response = alipayClient.sdkExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        BaseResult result = new BaseResult();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("sign", response.getBody());
        result.setData(resultMap);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrder")
    public BaseResult fetchOrder(
            @RequestParam(name = "orderNo", value = "orderNo", required = false) String orderNo,
            @RequestParam(name = "pageSize", value = "pageSize") Integer pageSize,
            @RequestParam(name = "pageIndex", value = "pageIndex") Integer pageIndex) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        Order order = new Order();
        order.setUserId(user.getId());
        PageInfo<OrderVo> datas = orderService.inquireOrders(order, pageIndex, pageSize);
        BaseResult result = new BaseResult();
        result.setData(datas);
        return result;
    }

    @ResponseBody
    @RequestMapping("fetchOrderWeb")
    public BaseResult fetchOrderWeb(
            @RequestParam(name = "code", value = "code", required = false) String code,
            @RequestParam(name = "mobile", value = "mobile", required = false) String mobile,
            @RequestParam(name = "pageSize", value = "pageSize") Integer pageSize,
            @RequestParam(name = "pageIndex", value = "pageIndex") Integer pageIndex) {
        Order order = new Order();
        if (StringUtil.isNotEmpty(mobile)) {
            User user = userService.getUserByLoginName(mobile);
            order.setUserId(user.getId());
        }
        order.setCode(code);
            PageInfo<OrderVo> datas = orderService.inquireOrders(order, pageIndex, pageSize);
            for(OrderVo vo : datas.getList()){
            List<OrderDetailVo > detailVos = orderDetailService.getDetails(vo.getId());
            vo.setOrderDetails(detailVos);
        }
        BaseResult result = new BaseResult();
        result.setData(datas);
        return result;
    }

}
