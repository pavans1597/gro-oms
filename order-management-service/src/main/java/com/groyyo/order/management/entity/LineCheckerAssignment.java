package com.groyyo.order.management.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.groyyo.core.dto.userservice.LineType;
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
@Entity(name = "LineCheckerAssignment")
public class LineCheckerAssignment extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String userId;

	@Column(columnDefinition = "varchar(100)", nullable = false)
	private String userName;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String lineId;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String lineName;

	@Enumerated(EnumType.STRING)
	private LineType lineType;

	@Column(columnDefinition = "varchar(40)", nullable = true)
	private String factoryId;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String purchaseOrderId;

	@Column(columnDefinition = "varchar(40)")
	private String salesOrderId;
	@Column(name = "colour_name", columnDefinition = "varchar(100)", nullable = true)
	private String colourName;

	@Column(name = "quantity", columnDefinition = "BIGINT", nullable = true)
	private Long quantity;
}
