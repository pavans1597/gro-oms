package com.groyyo.order.management.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Entity(name = "purchase_order")
@Table(name = "purchase_order", uniqueConstraints = { @UniqueConstraint(name = "UK_purchase_order_name", columnNames = { "name" }) })
public class PurchaseOrder extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false, columnDefinition = "char(40)")
	private String name;

	@Column(name = "style_id", columnDefinition = "char(40)", nullable = false)
	private String styleId;

	@Column(name = "product_id", columnDefinition = "char(40)", nullable = false)
	private String productId;

	@Column(name = "product_name", columnDefinition = "char(40)", nullable = false)
	private String productName;

	@Column(name = "style_number", columnDefinition = "char(40)", nullable = false)
	private String styleNumber;

	@Column(name = "style_name", columnDefinition = "char(40)", nullable = false)
	private String styleName;

	@Column(name = "order_id", columnDefinition = "char(40)", nullable = false)
	private String orderId;

	@Column(name = "order_name", columnDefinition = "char(40)", nullable = false)
	private String orderName;

	@Column(name = "receive_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date receiveDate;

	@Column(name = "ex_fty_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date exFtyDate;

	@Column(name = "season_id", columnDefinition = "char(40)", nullable = false)
	private String seasonId;

	@Column(name = "fit_id", columnDefinition = "char(40)", nullable = false)
	private String fitId;

	@Column(name = "part_id", columnDefinition = "char(40)", nullable = false)
	private String partId;
}
