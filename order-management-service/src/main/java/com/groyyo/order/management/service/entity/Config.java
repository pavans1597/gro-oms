package com.groyyo.quality.management.service.entity;

import com.groyyo.core.base.common.dto.AbstractDto;
import com.groyyo.core.sqlPostgresJpa.entity.AbstractJpaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name="config")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Config extends AbstractJpaEntity {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "key",nullable = false,unique = true,columnDefinition = "char(40)")
    private String key;
    @Column(name="value",nullable = false,columnDefinition = "text")
    private String value;


}
