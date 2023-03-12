package com.groyyo.order.management.entity;

import com.groyyo.core.sqlPostgresJpa.conveter.StringListConverter;
import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

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

	@Column(name = "master_id", columnDefinition = "varchar(40)", nullable = true)
	private String masterId;
	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String factoryId;
}
