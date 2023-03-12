package com.groyyo.order.management.adapter;


import com.groyyo.order.management.dto.request.BuyerRequestDto;
import com.groyyo.order.management.dto.response.BuyerResponseDto;
import com.groyyo.order.management.entity.Buyer;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class BuyerAdapter {

    public Buyer buildBuyerFromRequest(BuyerRequestDto buyerRequestDto,String factoryId) {

        return Buyer
                .builder()
                .name(buyerRequestDto.getName())
                .factoryId(factoryId)
                .build();
    }

    public Buyer buildBuyerFromResponse(BuyerResponseDto buyerResponseDto,String factoryId) {

        return Buyer
                .builder()
                .name(buyerResponseDto.getName())
                .factoryId(factoryId)
                .build();
    }

    public Buyer cloneBuyerWithRequest(BuyerRequestDto buyerRequestDto, Buyer buyer) {

        if (StringUtils.isNotBlank(buyerRequestDto.getName()))
            buyer.setName(buyerRequestDto.getName());

        return buyer;
    }

    public BuyerResponseDto buildResponseFromEntity(Buyer buyer) {

        return BuyerResponseDto
                .builder()
                .uuid(buyer.getUuid())
                .name(buyer.getName())
                .status(buyer.isStatus())
                .build();
    }

    public List<BuyerResponseDto> buildResponsesFromEntities(List<Buyer> buyers) {

        return buyers.stream().map(BuyerAdapter::buildResponseFromEntity).collect(Collectors.toList());
    }

}
