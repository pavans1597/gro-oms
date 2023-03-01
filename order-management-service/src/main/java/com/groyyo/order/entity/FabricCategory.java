package com.groyyo.order.entity;

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
@Entity(name = "fabric_category")
@Table(name = "fabric_category", uniqueConstraints = { @UniqueConstraint(name = "UK_fabric_category_name", columnNames = { "name" }) })
public class FabricCategory extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false, columnDefinition = "char(40)")
	private String name;

	@Column(name = "type", nullable = false, columnDefinition = "char(40)")
	private String type;

}
