package com.groyyo.order.management.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity(name = "fabric")
@Table(name = "fabric", uniqueConstraints = {@UniqueConstraint(name = "UK_fabric_name", columnNames = {"name"})})
public class Fabric extends AbstractJpaEntity {

    private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false, columnDefinition = "varchar(40)")
    private String name;

    @Column(name = "fabric_category", nullable = false, columnDefinition = "varchar(40)")
    private String fabricCategory;

    @Column(name = "image_url", nullable = false, columnDefinition = "char(255)")
    private String imageUrl;

    @Column(name = "fabric_code", nullable = false, columnDefinition = "varchar(40)")
    private String fabricCode;
}
