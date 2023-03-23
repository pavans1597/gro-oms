package com.groyyo.order.management.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

@Log4j2
@UtilityClass
public class MapperUtils {

    public void getTrimmedDto(Object dto) {

        try {

            trimDtoStrings(dto);

        } catch (IllegalAccessException e) {

            log.info("Not able to trim dto: {}", dto, e);
        }
    }

    public void trimDtoStrings(Object dto) throws IllegalAccessException {

        Field[] fields = dto.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (field.getType() == String.class) {

                field.setAccessible(true);

                String value = (String) field.get(dto);

                if (value != null) {
                    field.set(dto, value.trim());
                }
            }
        }
    }
}