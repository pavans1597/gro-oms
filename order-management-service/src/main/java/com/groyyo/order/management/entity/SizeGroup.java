package com.groyyo.order.management.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.groyyo.core.sqlPostgresJpa.conveter.StringListConverter;
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
@Entity(name = "size_group")
@Table(name = "size_group", uniqueConstraints = { @UniqueConstraint(name = "UK_size_group_name", columnNames = { "name" }) })
public class SizeGroup extends AbstractJpaEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", columnDefinition = "varchar(100)", nullable = false)
	private String name;

	@Convert(converter = StringListConverter.class)
	@Column(name = "size_ids", columnDefinition = "text", nullable = false)
	private List<String> sizeIds;

	@Column(name = "master_id", columnDefinition = "char(40)", nullable = true)
	private String masterId;

}
