package net.aimeizi.service;

import net.aimeizi.entity.Order;
import net.aimeizi.entity.OrderExample;

import java.util.List;

public interface OrderService {

    /**
     * 返回所有订单
     *
     * @return
     */
    List<Order> getAllOrder();

    /**
     * 添加单条订单
     *
     * @param o
     */
    void addOrder(Order o);

    /**
     * 批量添加订单
     *
     * @param orders
     */
    void addOrders(List<Order> orders);
    /**
     * 事务中批量添加订单
     * 模拟异常回滚
     * @param orders
     */
    void addOrdersTrans(List<Order> orders);

    /**
     * 批量批量用户的订单状态
     *
     * @param userIds
     */
    void updateOrders(List<Integer> userIds, String newOrderStatus);

    /**
     * 删除所有订单
     */
    void deleteAll();

    /**
     * 按条件查询订单总数
     *
     * @param example
     * @return
     */
    int getCount(OrderExample example);

    /**
     * 删除单条订单
     *
     * @return
     */
    void delete(Order order);

    /**
     * 更新单条订单
     */
    void update(Order order);

    /**
     * 查询最大订单号
     *
     * @param example
     */
    int getMaxOrderId(OrderExample example);

    /**
     * 查询最小订单号
     *
     * @param example
     */
    int getMinOrderId(OrderExample example);


    /**
     * 查询最大用户id
     *
     * @param example
     */
    int getMaxUserId(OrderExample example);


    /**
     * 查询最小用户id
     *
     * @param example
     */
    int getMinUserId(OrderExample example);
}
