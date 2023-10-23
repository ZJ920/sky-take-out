package com.sky.service.impl;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        return null;
    }
}
