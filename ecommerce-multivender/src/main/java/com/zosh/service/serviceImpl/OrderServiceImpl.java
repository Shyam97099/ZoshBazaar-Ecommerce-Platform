package com.zosh.service.serviceImpl;
import com.zosh.domain.OrderStatus;
import com.zosh.domain.PaymentStatus;
import com.zosh.model.*;
import com.zosh.repository.AddressRepository;
import com.zosh.repository.OrderItemRepository;
import com.zosh.repository.OrderRepository;
import com.zosh.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) throws Exception {
        if(!user.getAddresses().contains(shippingAddress)){
        user.getAddresses().add(shippingAddress);
        }
        Address address = (shippingAddress.getId() != null)
                ? shippingAddress
                : addressRepository.save(shippingAddress);
        Map<Long,List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item->item.getProduct().getSeller().getId()));
        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new Exception("Cart is empty");
        }
        Set<Order> orders = new HashSet<>();

        for(Map.Entry<Long, List<CartItem>> entry:itemsBySeller.entrySet()){
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();
            int totalMrpPrice = items.stream().mapToInt(CartItem::getMrpPrice).sum();
            int totalSellingPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalMrpPrice);
            createdOrder.setTotalSellingPrice(totalSellingPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);

            PaymentDetails  payment=new PaymentDetails();
            payment.setStatus(PaymentStatus.PENDING);
            createdOrder.setPaymentDetails(payment);

            Order savedOrder=orderRepository.save(createdOrder);
            orders.add(savedOrder);

//            List<OrderItem> orderItems=new ArrayList<>();

            for(CartItem item:items){
                OrderItem orderItem=new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                savedOrder.getOrderItems().add(savedOrderItem);
//                orderItems.add(savedOrderItem);
            }
            orderRepository.save(savedOrder);
        }
        return orders;
    }
    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(()-> new Exception("Order not found..."));
    }
    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }
    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
       Order order =findOrderById(orderId);
//        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
//            throw new Exception("Cannot update a cancelled order");
//        }
//        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
//            throw new Exception("Cannot update a delivered order");
//        }
//        if (!isValidTransition(order.getOrderStatus(), orderStatus)) {
//            throw new Exception("Invalid status transition from "
//                    + order.getOrderStatus() + " to " + orderStatus);
//        }

       order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

//    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
//        switch (current) {
//            case PENDING:
//                return next == OrderStatus.PLACED || next == OrderStatus.CANCELLED;
//            case PLACED:
//                return next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
//            case CONFIRMED:
//                return next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
//            case SHIPPED:
//                return next == OrderStatus.DELIVERED;
//            case DELIVERED:
//            case CANCELLED:
//                return false;
//            default:
//                return false;
//        }
//    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws Exception {
        Order order =findOrderById(orderId);

        if(!user.getId().equals(order.getUser().getId())){
            throw new Exception("you don't have access to this order ");
        }
//        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
//            throw new Exception("Order is already cancelled");
//        }
//        if (order.getOrderStatus() == OrderStatus.SHIPPED ||
//                order.getOrderStatus() == OrderStatus.DELIVERED) {
//            throw new Exception("Cannot cancel order after it has been shipped or delivered");
//        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id)
                .orElseThrow(()-> new Exception("Order item not exist...."));

    }

}
