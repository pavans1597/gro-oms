package com.groyyo.order.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "order")
@Table(name = "order", uniqueConstraints = {@UniqueConstraint(name = "UK_order_name", columnNames = {"name"})})
public class Order extends AbstractJpaEntity {
    @Column(name = "name", nullable = false, columnDefinition = "char(40)")
    private String name;
    @Column(name = "style_id", columnDefinition = "char(40)", nullable = false)
    private String styleId;
    @Column(name = "style_number", columnDefinition = "char(40)", nullable = false)
    private String styleNumber;
    @Column(name = "style_name", columnDefinition = "char(40)")
    private String styleName;
    @Column(name = "product_id", columnDefinition = "char(40)", nullable = false)
    private String productId;
    @Column(name = "product_name", columnDefinition = "char(40)")
    private String productName;
}
