package com.groyyo.order.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "purchase_order_quantity")
@Table(name = "purchase_order_quantity", uniqueConstraints = { @UniqueConstraint(name = "UK_purchase_order_name", columnNames = { "name" }) })
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
}
