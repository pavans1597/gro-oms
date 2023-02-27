package com.groyyo.order.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "purchase_order_quantity")
@Table(name = "purchase_order_quantity")
public class PurchaseOrderQuantity extends AbstractJpaEntity {
    @Column(name = "purchase_order_id", columnDefinition = "char(40)", nullable = false)
    private String purchaseOrderId;
    @Column(name = "size_id", columnDefinition = "char(40)", nullable = false)
    private String sizeId;
    @Column(name = "quantity", columnDefinition = "char(40)", nullable = false)
    private double quantity;
    @Column(name = "fabric_id", columnDefinition = "char(40)", nullable = false)
    private String fabricId;
    @Column(name = "colour_id", columnDefinition = "char(40)", nullable = false)
    private String colourId;

    @Column(name = "tolerance", columnDefinition = "char(40)", nullable = false)
    private String tolerance;

    @Column(name = "targetQuantity", columnDefinition = "char(40)", nullable = false)
    private String targetQuantity;
}
