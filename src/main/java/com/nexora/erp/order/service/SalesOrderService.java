package com.nexora.erp.order.service;

import com.nexora.erp.audit.service.AuditService;
import com.nexora.erp.common.exception.BusinessRuleException;
import com.nexora.erp.common.exception.ResourceNotFoundException;
import com.nexora.erp.customer.entity.Customer;
import com.nexora.erp.customer.repository.CustomerRepository;
import com.nexora.erp.order.dto.SalesOrderCreateRequest;
import com.nexora.erp.order.dto.SalesOrderItemCreateRequest;
import com.nexora.erp.order.dto.SalesOrderResponse;
import com.nexora.erp.order.entity.SalesOrder;
import com.nexora.erp.order.entity.SalesOrderItem;
import com.nexora.erp.order.entity.SalesOrderStatus;
import com.nexora.erp.order.mapper.SalesOrderMapper;
import com.nexora.erp.order.repository.SalesOrderRepository;
import com.nexora.erp.product.entity.Product;
import com.nexora.erp.product.repository.ProductRepository;
import com.nexora.erp.stock.entity.StockMovement;
import com.nexora.erp.stock.entity.StockMovementType;
import com.nexora.erp.stock.repository.StockMovementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final SalesOrderMapper salesOrderMapper;
    private final AuditService auditService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             CustomerRepository customerRepository,
                             ProductRepository productRepository,
                             StockMovementRepository stockMovementRepository,
                             SalesOrderMapper salesOrderMapper,
                             AuditService auditService) {
        this.salesOrderRepository = salesOrderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.salesOrderMapper = salesOrderMapper;
        this.auditService = auditService;
    }

    @Transactional
    public SalesOrderResponse create(SalesOrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado."));

        if (!customer.getActive()) {
            throw new BusinessRuleException("Cliente inativo nao pode realizar pedido.");
        }

        SalesOrder order = new SalesOrder(customer);

        for (SalesOrderItemCreateRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado."));

            if (!product.getActive()) {
                throw new BusinessRuleException("Produto inativo nao pode ser vendido.");
            }

            order.addItem(new SalesOrderItem(product, itemRequest.getQuantity()));
        }

        SalesOrder savedOrder = salesOrderRepository.save(order);
        auditService.record("ORDER_CREATED", "SalesOrder", savedOrder.getId(), "Pedido criado.");
        return salesOrderMapper.toResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public SalesOrderResponse findById(Long id) {
        SalesOrder order = findOrderById(id);
        return salesOrderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<SalesOrderResponse> findAll(Pageable pageable) {
        return salesOrderRepository.findAll(pageable)
                .map(salesOrderMapper::toResponse);
    }

    @Transactional
    public SalesOrderResponse confirm(Long id) {
        SalesOrder order = findOrderById(id);

        for (SalesOrderItem item : order.getItems()) {
            if (!item.getProduct().getActive()) {
                throw new BusinessRuleException("Produto inativo nao pode ser vendido.");
            }

            item.getProduct().decreaseStock(item.getQuantity());
            stockMovementRepository.save(new StockMovement(
                    item.getProduct(),
                    StockMovementType.SAIDA,
                    item.getQuantity(),
                    "Confirmacao do pedido " + order.getId()
            ));
        }

        order.confirm();
        auditService.record("ORDER_CONFIRMED", "SalesOrder", order.getId(), "Pedido confirmado.");
        return salesOrderMapper.toResponse(order);
    }

    @Transactional
    public SalesOrderResponse cancel(Long id) {
        SalesOrder order = findOrderById(id);
        boolean wasConfirmed = order.getStatus() == SalesOrderStatus.CONFIRMADO;

        if (wasConfirmed) {
            for (SalesOrderItem item : order.getItems()) {
                item.getProduct().increaseStock(item.getQuantity());
                stockMovementRepository.save(new StockMovement(
                        item.getProduct(),
                        StockMovementType.ENTRADA,
                        item.getQuantity(),
                        "Cancelamento do pedido " + order.getId()
                ));
            }
        }

        order.cancel();
        auditService.record("ORDER_CANCELED", "SalesOrder", order.getId(), "Pedido cancelado.");
        return salesOrderMapper.toResponse(order);
    }

    private SalesOrder findOrderById(Long id) {
        return salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido nao encontrado."));
    }
}
