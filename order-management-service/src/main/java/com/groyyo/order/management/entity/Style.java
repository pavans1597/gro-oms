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
@Entity(name = "style")
@Table(name = "style", uniqueConstraints = { @UniqueConstraint(name = "UK_style_number", columnNames = { "style_number" }) })
public class Style extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
	private String name;

	@Column(name = "style_number", columnDefinition = "varchar(100)", nullable = false)
	private String styleNumber;

	@Column(name = "style_image_id", columnDefinition = "varchar(255)")
	private String styleImageId;

	@Column(name = "cad_image_id", columnDefinition = "varchar(255)")
	private String cadImageId;

	@Column(name = "product_id", columnDefinition = "varchar(40)")
	private String productId;

	@Column(name = "product_name", columnDefinition = "varchar(100)")
	private String productName;
	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String factoryId;
}
