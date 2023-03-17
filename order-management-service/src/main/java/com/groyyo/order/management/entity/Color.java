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
@Entity(name = "color")
@Table(name = "color", uniqueConstraints = { @UniqueConstraint(name = "UK_color_name_hex_code_factory_id", columnNames = { "name", "hex_code", "factory_id" }) })
public class Color extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
	private String name;

	@Column(name = "hex_code", columnDefinition = "varchar(100)", nullable = false)
	private String hexCode;

	@Column(name = "master_id", columnDefinition = "varchar(40)", nullable = true)
	private String masterId;

	@Column(name = "factory_id", columnDefinition = "varchar(40)", nullable = true)
	private String factoryId;
}
