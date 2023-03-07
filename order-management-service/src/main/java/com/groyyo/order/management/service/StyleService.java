package com.groyyo.order.management.service;


import com.groyyo.order.management.dto.request.StyleRequestDto;
import com.groyyo.order.management.dto.request.StyleUpdateDto;
import com.groyyo.order.management.dto.response.StyleResponseDto;

import java.util.List;




public interface StyleService {


    List<StyleResponseDto> getAllStyles(Boolean status);


    StyleResponseDto getStyleById(String id);


    StyleResponseDto addStyle(StyleRequestDto styleRequestDto);


    StyleResponseDto updateStyle(StyleUpdateDto styleUpdateDto);


    StyleResponseDto activateDeactivateStyle(String id, boolean status);


    void consumeStyle(StyleResponseDto styleResponseDto);
}
