package com.groyyo.quality.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "order_qc_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class OrderQcHistory extends AbstractJpaEntity {



    @Column(name = "purchase_order_id",nullable = false,columnDefinition = "char(40)")
    private String purchaseOrderId;

    @Column(name = "size_id",nullable = false,columnDefinition = "char(40)")
    private String sizeId;
    @Column(name="quantity",columnDefinition = "double")
    private double quantity;
    @Column(name="fabric_id",nullable = false,columnDefinition = "char(40)")
    private String fabricId;
    @Column(name="colour_id",nullable = false,columnDefinition = "char(40)")
    private String colourId;
    @Column(name="qc_stage",nullable = false,columnDefinition = "char(40)")
    private String qcStage;

    @Column(name = "line_number",columnDefinition = "char(40)")
    private String lineNumber;
    @Column(name = "action",nullable = false,columnDefinition = "char(40)")
    private String action;
    @Column(name="defect_id",nullable = false,columnDefinition = "char(40)")
    private String defectId;
    @Column(name="comments",columnDefinition = "text")
    private String comments;
    @Column(name="defect_location",columnDefinition = "char(40)")
    private String defectLocation;




}
