package net.aimeizi.service.impl;

import net.aimeizi.algorithm.SingleKeyModuloTableShardingAlgorithm;
import net.aimeizi.entity.Order;
import net.aimeizi.entity.OrderExample;
import net.aimeizi.list.ListUtil;
import net.aimeizi.mapper.OrderMapper;
import net.aimeizi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SingleKeyModuloTableShardingAlgorithm singleKeyModuloTableShardingAlgorithm;


    @Override
    public List<Order> getAllOrder() {
        return orderMapper.selectByExample(null);
    }


    @Override
    public void addOrder(Order o) {
        orderMapper.insertSelective(o);
    }

    @Override
    public void addOrders(List<Order> orders) {
        //不同库对应不同key
        Map<String, List<Order>> map = ListUtil.getMapByKeyProperty(orders, "userId");
        for (String userId : map.keySet()) {
            //不同表对应不同key
            Map<String, List<Order>> map2 = ListUtil.getMapByModKeyProperty(map.get(userId), "orderId",
                    singleKeyModuloTableShardingAlgorithm.getTableCount());
            for (String key : map2.keySet()) {
                orderMapper.insertBatch(map2.get(key));
            }
        }
    }

    /**
     *
     * @param orders
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addOrdersTrans(List<Order> orders) {
        Map<String, List<Order>> map = ListUtil.getMapByKeyProperty(orders, "userId");
        for (String userId : map.keySet()) {
            Map<String, List<Order>> map2 = ListUtil.getMapByModKeyProperty(map.get(userId), "orderId",
                    singleKeyModuloTableShardingAlgorithm.getTableCount());
            for (String key : map2.keySet()) {
                orderMapper.insertBatch(map2.get(key));
            }
            //模拟异常回滚
            if (!userId.equals("1")) {
                throw new RuntimeException("模拟异常回滚");
            }
        }
    }

    @Override
    public void updateOrders(List<Integer> userIds, String newOrderStatus) {
        Order o = new Order();
        o.setStatus(newOrderStatus);
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdIn(userIds);
        orderMapper.updateByExampleSelective(o, example);
    }

    @Override
    public void deleteAll() {
        orderMapper.deleteByExample(null);
    }

    @Override
    public int getCount(OrderExample example) {
        return orderMapper.countByExample(example);
    }

    @Override
    public void delete(Order order) {
        orderMapper.delete(order);
    }

    @Override
    public void update(Order order) {
        OrderExample example = new OrderExample();
        example.createCriteria()
                .andUserIdEqualTo(order.getUserId())
                .andOrderIdEqualTo(order.getOrderId());
        orderMapper.updateByExampleSelective(order, example);
    }

    @Override
    public int getMaxOrderId(OrderExample example) {
        return orderMapper.maxOrderIdByExample(example);
    }

    @Override
    public int getMinOrderId(OrderExample example) {
        return orderMapper.minOrderIdByExample(example);
    }

    @Override
    public int getMaxUserId(OrderExample example) {
        return orderMapper.maxUserIdByExample(example);
    }

    @Override
    public int getMinUserId(OrderExample example) {
        return orderMapper.minUserIdByExample(example);
    }
}
