package com.groyyo.quality.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name="purchase_order_checker")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class PurchaseOrderChecker extends AbstractJpaEntity {



    @Column(name="purchase_order_id",columnDefinition = "char(40)", nullable = false)
    private String purchaseOrderId;
    @Column(name = "checker_id",columnDefinition = "char(40)", nullable = false)
    private String checkerId;

}
