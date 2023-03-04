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
@Entity(name = "style")
@Table(name = "style", uniqueConstraints = { @UniqueConstraint(name = "UK_style_group_name", columnNames = { "name" }) })
public class Style extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
	private String name;

	@Column(name = "style_number", columnDefinition = "varchar(100)", nullable = false)
	private String styleNumber;

	@Column(name = "image", columnDefinition = "varchar(255)")
	private String image;

	@Column(name = "cad_image", columnDefinition = "varchar(255)")
	private String cadImage;

	@Column(name = "product_id", columnDefinition = "char(40)")
	private String productId;
}
