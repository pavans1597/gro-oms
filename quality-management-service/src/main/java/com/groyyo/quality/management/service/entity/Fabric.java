package com.groyyo.quality.management.service.entity;

import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name = "fabric")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Fabric extends AbstractJpaEntity {


    @Column(name = "name", nullable = false, columnDefinition = "char(40)")

    private String name;
    @Column(name = "type", nullable = false,columnDefinition = "char(40)")

    private String type;

}
