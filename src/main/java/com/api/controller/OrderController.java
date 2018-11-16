package com.api.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.UnsupportedEncodingException;
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
    public BaseResult addOrder( Double totalPrice,OrderDetailVo detailVo,String remark) {
        BaseResult result = new BaseResult();
        //获取userId
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new BaseResult(ResultCode.UNAUTHORIZED, "验证不通过", null);
        }
        String mobile = JwtUtil.getMobileBySubject(subject);
        User user = userService.getUserByLoginName(mobile);
        UserAddress userAddress = userAddressService.getDefaultAddress(user.getId());
        OrderVo order = new OrderVo();
        if (userAddress != null) {
            order.setRecAddress(userAddress.getRecAddress());
            order.setRecMobile(userAddress.getRecMobile());
            order.setRecName(userAddress.getRecMobile());
        }
        if(!StringUtils.isEmpty(remark)){
            order.setRemark(remark);
        }
        order.setStatus(Byte.valueOf("0"));
        order.setUserName(user.getName());
        order.setUserId(user.getId());
        order.setTotalPrice(totalPrice);
        //获取默认地址信息
        order.setOrderDetails(Arrays.asList(detailVo));
        Long id = orderService.addOrder(order);
        OrderVo returnOrder = orderService.getOrder(id,user.getId());
        result.setData(returnOrder);
        return result;
    }

    @RequestMapping("modifyOrder")
    public BaseResult modifyOrder( OrderVo order) {
        OrderVo ordert = orderService.getOrder(order.getId(),null);
        if (ordert == null) {
            return new BaseResult(ResultCode.FAILURE, "当前订单不存在，请联系管理员", null);
        } else {
            if(!ordert.getStatus().equals(Byte.valueOf("1"))){
                return new BaseResult(ResultCode.FAILURE, "订单状态不为已支付,不能调整状态", null);
            }
            //获取地址信息
            ordert.setStatus(order.getStatus());
            orderService.modifyOrder(ordert);
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
    @RequestMapping(value = "notify_url",method={RequestMethod.POST,RequestMethod.GET})
    public String  verifyPay(HttpServletRequest request) throws UnsupportedEncodingException {
        //获取支付宝POST过来反馈信息
        BaseResult result = new BaseResult();
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //2.封装必须参数
        String out_trade_no = request.getParameter("out_trade_no");            // 商户订单号
        String orderType = request.getParameter("body");                    // 订单内容
        String tradeStatus = request.getParameter("trade_status");            //交易状态
        //total_amount
        String total_amount = request.getParameter("total_amount");
        //3.签名验证(对支付宝返回的数据验证，确定是支付宝返回的)
        boolean signVerified = false;
        try {
            //3.1调用SDK验证签名
            signVerified = AlipaySignature.rsaCheckV1(params, AliPayUtil.ALIPAY_PUBLIC_KEY, AliPayUtil.CHARSET, "RSA2");
            logger.info("验证结果:"+signVerified+"，参数："+params.toString());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(signVerified){
            if("TRADE_SUCCESS".equals(params.get("trade_status"))){
                //商户订单号
                String orderNo = params.get("out_trade_no");
                //支付宝交易号
                String tradeNumber = params.get("trade_no");
                logger.info("订单号:"+orderNo);
                OrderVo order = orderService.getOrderByOrderNo(orderNo);
                logger.info("订单号:"+order.toString());
                if(order == null){
                    return "fail";
                }
                if(!order.getTotalPrice().equals(Double.valueOf(total_amount))){
                    return "fail";
                }
                order.setStatus(Byte.valueOf("1"));
                order.setTradeNumber(tradeNumber);
                orderService.modifyOrder(order);
                //支付完成后计算佣金、用户等级
                userService.addParentCommission(order.getUserId(),order.getTotalPrice());
                userService.calcUserLevel(order.getUserId());
                return "success";
            }else{
                return "fail";
            }
        }else{
            return "fail";
        }
    }
    @RequestMapping(value="return_url",method={RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Model returnUrl(@RequestParam("id") String id, HttpServletRequest request, Model model) throws UnsupportedEncodingException {
         Map  returnMap = new HashMap();
         try {

             OrderVo order = orderService.getOrderByOrderNo(id);
              // 返回值Map
             if(order !=null && order.getStatus() == 1){
                     User user = userService.getUserById(order.getUserId());
                     returnMap.put("phoneNum", user.getMobile());             //支付帐号
                     returnMap.put("tradeMoney", order.getTotalPrice()+"");        //订单金额

                 }else{
                      model.addAttribute("msg", "查询失败");
                      model.addAttribute("status", 0);
                 }
              model.addAttribute("returnMap", returnMap);
              System.err.println(returnMap);
              model.addAttribute("msg", "查询成功");
              model.addAttribute("status", 0);
             } catch (Exception e) {
                 model.addAttribute("msg", "查询失败");
                 model.addAttribute("status", 1);
             }

          return model;
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
        model.setOutTradeNo(order.getOrderCode());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(order.getTotalPrice().toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AliPayUtil.NOTIFY_URL);
        request.setReturnUrl(AliPayUtil.RETURN_URL);
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
