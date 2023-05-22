package com.groyyo.order.management.entity;

import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "purchase_order")
@Table(name = "purchase_order", uniqueConstraints = {  @UniqueConstraint(name = "UK_purchase_order_name", columnNames = { "name" }) })
public class PurchaseOrder extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(40)")
	private String name;

	@Column(name = "purchase_order_status")
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private PurchaseOrderStatus purchaseOrderStatus = PurchaseOrderStatus.DRAFT;

	@Column(name = "style_id", columnDefinition = "varchar(40)", nullable = false)
	private String styleId;

	@Column(name = "style_number", columnDefinition = "varchar(40)", nullable = false)
	private String styleNumber;

	@Column(name = "style_name", columnDefinition = "varchar(40)", nullable = false)
	private String styleName;

	@Column(name = "product_id", columnDefinition = "varchar(40)", nullable = false)
	private String productId;

	@Column(name = "product_name", columnDefinition = "varchar(40)", nullable = false)
	private String productName;

	@Column(name = "fabric_id", columnDefinition = "varchar(40)", nullable = false)
	private String fabricId;

	@Column(name = "fabric_name", columnDefinition = "varchar(40)", nullable = false)
	private String fabricName;

	@Column(name = "buyer_id", columnDefinition = "varchar(40)", nullable = false)
	private String buyerId;

	@Column(name = "buyer_name", columnDefinition = "varchar(40)", nullable = false)
	private String buyerName;

	@Column(name = "tolerance", columnDefinition = "Decimal(10,2)")
	private Double tolerance;

	@Column(name = "receive_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date receiveDate;

	@Column(name = "ex_fty_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date exFtyDate;

	@Column(name = "season_id", columnDefinition = "varchar(40)")
	private String seasonId;

	@Column(name = "season_name", columnDefinition = "varchar(40)")
	private String seasonName;

	@Column(name = "fit_id", columnDefinition = "varchar(40)")
	private String fitId;

	@Column(name = "fit_name", columnDefinition = "varchar(40)")
	private String fitName;

	@Column(name = "part_id", columnDefinition = "varchar(40)")
	private String partId;

	@Column(name = "part_name", columnDefinition = "varchar(40)")
	private String partName;

	@Column(name = "order_id", columnDefinition = "varchar(40)")
	private String orderId;

	@Column(name = "order_name", columnDefinition = "varchar(40)")
	private String orderName;

	@Column(name = "quantity", columnDefinition = "BIGINT")
	private Long totalQuantity;

	@Column(name = "targetQuantity", columnDefinition = "BIGINT")
	private Long totalTargetQuantity;

	@Column(columnDefinition = "varchar(40)")
	private String factoryId;

	@Transient
	private boolean assignedWithColours;

}
