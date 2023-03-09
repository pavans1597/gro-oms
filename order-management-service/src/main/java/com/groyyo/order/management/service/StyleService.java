package com.groyyo.order.management.service;


import com.groyyo.order.management.dto.response.StyleDto;

import java.util.List;




public interface StyleService {


    List<StyleDto> getAllStyles(Boolean status);


    StyleDto getStyleById(String id);


    StyleDto addStyle(StyleDto styleRequestDto);


    StyleDto updateStyle(StyleDto styleUpdateDto);


    StyleDto activateDeactivateStyle(String id, boolean status);


    void consumeStyle(StyleDto styleDto);
}
