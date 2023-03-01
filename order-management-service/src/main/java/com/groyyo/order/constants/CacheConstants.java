package com.groyyo.order.constants;

import java.util.Arrays;
import java.util.List;


public class CacheConstants {

    public static final String ENTITY = "entity";

    public static final String ALL = "all";
    public static final String COLOR = "colors";
    public static final String DEFECT = "defects";
    public static final String FIT = "fits";
    public static final String PART = "parts";
    public static final String PRODUCT = "products";
    public static final String PRODUCT_DEFECT = "productDefects";
    public static final String SEASON = "seasons";
    public static final String SIZE = "sizes";
    public static final String SIZE_GROUP = "sizeGroups";

    public static final List<String> ENTITY_LIST_GET = Arrays.asList(COLOR, DEFECT, FIT, PART, PRODUCT, PRODUCT_DEFECT, SEASON, SIZE, SIZE_GROUP);
    public static final List<String> ENTITY_LIST_POPULATE = Arrays.asList(ALL, COLOR, DEFECT, FIT, PART, PRODUCT, PRODUCT_DEFECT, SEASON, SIZE, SIZE_GROUP);

    public static final Integer CACHE_EXPIRE_AFTER_TIME = 30;
}
