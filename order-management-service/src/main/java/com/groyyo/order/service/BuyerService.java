package com.groyyo.order.service;

import com.groyyo.order.dto.request.BuyerRequestDto;
import com.groyyo.order.dto.response.BuyerResponseDto;

import java.util.List;


public interface BuyerService {


    List<BuyerResponseDto> getAllBuyers(Boolean status);


    BuyerResponseDto getBuyerById(String id);


    BuyerResponseDto addBuyer(BuyerRequestDto buyerRequestDto);


    BuyerResponseDto updateBuyer(BuyerRequestDto buyerRequestDto);


    BuyerResponseDto activateDeactivateBuyer(String id, boolean status);


    void consumeBuyer(BuyerResponseDto buyerResponseDto);
}
