package com.groyyo.quality.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "purchase_order_quantity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class PurchaseOrderQuantity extends AbstractJpaEntity {


    @Column(name = "purchase_order_id",columnDefinition = "char(40)", nullable = false)
    private String purchaseOrderId;
    @Column(name = "size_id",columnDefinition = "char(40)", nullable = false)
    private String sizeId;
    @Column(name = "quantity",columnDefinition = "char(40)", nullable = false)
    private double quantity;
    @Column(name = "fabric_id",columnDefinition = "char(40)", nullable = false)
    private String fabricId;
    @Column(name = "colour_id",columnDefinition = "char(40)", nullable = false)
    private String colourId;



}
