package com.groyyo.order.management.entity;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "LineCheckerAssignment")
public class LineCheckerAssignment extends AbstractJpaEntity {

    @Column(columnDefinition = "varchar(40)", nullable = false)
    String userId ;
    @Column(columnDefinition = "varchar(100)", nullable = false)
    String userName ;
    @Column(columnDefinition = "varchar(40)", nullable = false)
    String lineId ;
    @Enumerated(EnumType.STRING)
    LineType lineType ;
    @Column(columnDefinition = "varchar(40)", nullable = false)
    String factoryId ;
    @Column(columnDefinition = "varchar(40)", nullable = false)
    String purchaseOrderId ;
    @Column(columnDefinition = "varchar(40)")
    String salesOrderId ;









}
