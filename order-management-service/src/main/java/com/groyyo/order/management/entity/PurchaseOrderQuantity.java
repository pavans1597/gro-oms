package com.groyyo.order.management.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "purchase_order_quantity")
@Table(name = "purchase_order_quantity")
public class PurchaseOrderQuantity extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
	private String name;

	@Column(name = "purchase_order_id", columnDefinition = "varchar(40)", nullable = false)
	private String purchaseOrderId;

	@Column(name = "size_id", columnDefinition = "varchar(40)", nullable = false)
	private String sizeId;

	@Column(name = "colour_id", columnDefinition = "varchar(40)", nullable = false)
	private String colourId;

	@Column(name = "quantity", columnDefinition = "BIGINT", nullable = false)
	private Long quantity;

	@Column(name = "targetQuantity", columnDefinition = "BIGINT", nullable = false)
	private Long targetQuantity;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String factoryId;
}
