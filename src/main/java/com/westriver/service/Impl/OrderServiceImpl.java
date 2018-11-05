package com.westriver.service.Impl;

import com.alipay.api.domain.Car;
import com.google.common.collect.Lists;
import com.westriver.common.Const;
import com.westriver.common.ServerResponse;
import com.westriver.dao.*;
import com.westriver.pojo.*;
import com.westriver.service.IOrderService;
import com.westriver.util.BigDecimalUtil;
import com.westriver.util.DateTimeUtil;
import com.westriver.vo.OrderItemVo;
import com.westriver.vo.OrderVo;
import com.westriver.vo.ShippingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;

/**
 * Created by zack on 2018/11/4.
 */
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderMapper orderMapper;

    public ServerResponse<OrderVo> createOrderVo(Integer userId, Integer shippingId) {
        //先从购物车获取将要结算的商品
        List<Cart> cartList = cartMapper.selectByUserId(userId);

        //计算每个商品的订单详情,统一计算每个商品的数量以及总价等，价格和数量必须重新从数据库中读取最新的值，并不能从购物车中获取，因为价格和库存总是变化的
        ServerResponse response = this.getCartOrderItem(userId, cartList);

        List<OrderItem> orderItemList = (List<OrderItem>) response.getData();

        //计算整个购物车中，全部商品的总价
        BigDecimal payment = this.getOrderItemTotalPrice(orderItemList);

        //生成订单，这个订单使用订单id跟上面每一个商品的订单详情关联
        Order order = this.assembleOrder(userId, shippingId, payment);

        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }

        //批量插入订单详情
        orderItemMapper.batchInsert(orderItemList);

        //订单生成成功后，减小库存
        this.reduceProductStock(orderItemList);

        //同时清空购物车
        this.cleanCartList(cartList);


        OrderVo orderVo = this.createOrderVo(order, orderItemList, shippingId);
        return ServerResponse.createBySuccess(orderVo);
    }


    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();

        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();

            //每次最新的产品状态都是从数据库重新读入
            Product product = productMapper.selectProductById(cartItem.getProductId());

            //先判断库存， 如果库存不足，就直接告诉用户
            if (product.getStock() < cartItem.getQuantity()) {
                return ServerResponse.createByErrorMessage(product.getName() + " 库存不足");
            }

            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartItem.getQuantity().doubleValue()));

            orderItemList.add(orderItem);
        }

        return ServerResponse.createBySuccess(orderItemList);
    }

    private BigDecimal getOrderItemTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal totalPrice = new BigDecimal("0");

        for (OrderItem orderItem : orderItemList) {
            totalPrice = BigDecimalUtil.add(orderItem.getTotalPrice().doubleValue(), totalPrice.doubleValue());
        }

        return totalPrice;
    }

    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        Long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPayType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);

        order.setUserId(userId);
        order.setShippingId(shippingId);

        orderMapper.insertOrder(order);

        return order;
    }

    private long generateOrderNo(){
        long currentTime =System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }

    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectProductById(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());

            productMapper.updateProduct(product);
        }
    }

    private void cleanCartList(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteProductById(cart.getId());
        }
    }

    private OrderVo createOrderVo(Order order, List<OrderItem> orderItemList, Integer shippingId) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymengType(order.getPayType());
        orderVo.setPaymentDesc(Const.PaymentTypeEnum.codeOf(order.getPayType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusString(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());

        Shipping shipping = shippingMapper.selectById(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(this.assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateTimeUtil.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeUtil.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeUtil.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeUtil.dateToStr(order.getCloseTime()));


        orderVo.setImageHost("image host");

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }

        orderVo.setOrderItemVoList(orderItemVoList);

        return orderVo;

    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateTimeUtil.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }
}
