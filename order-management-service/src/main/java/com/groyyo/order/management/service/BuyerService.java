package com.groyyo.order.management.service;

import com.groyyo.order.management.dto.request.BuyerRequestDto;
import com.groyyo.order.management.dto.response.BuyerResponseDto;

import java.util.List;


public interface BuyerService {


    List<BuyerResponseDto> getAllBuyers(Boolean status);


    BuyerResponseDto getBuyerById(String id);


    BuyerResponseDto addBuyer(BuyerRequestDto buyerRequestDto);


    BuyerResponseDto updateBuyer(BuyerRequestDto buyerRequestDto);


    BuyerResponseDto activateDeactivateBuyer(String id, boolean status);


    void consumeBuyer(BuyerResponseDto buyerResponseDto);
}
