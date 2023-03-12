package com.groyyo.order.management.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "order")
@Table(name = "order", schema = "public", uniqueConstraints = { @UniqueConstraint(name = "UK_order_name", columnNames = { "name" }) })
public class Order extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", columnDefinition = "varchar(40)", nullable = false)
	private String name;

	@Column(name = "style_id", columnDefinition = "varchar(40)", nullable = false)
	private String styleId;

	@Column(name = "style_number", columnDefinition = "varchar(40)", nullable = false)
	private String styleNumber;

	@Column(name = "style_name", columnDefinition = "varchar(40)")
	private String styleName;

	@Column(name = "product_id", columnDefinition = "varchar(40)", nullable = false)
	private String productId;

	@Column(name = "product_name", columnDefinition = "varchar(40)")
	private String productName;
	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String factoryId;
}
