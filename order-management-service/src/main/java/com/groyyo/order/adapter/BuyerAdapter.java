package com.groyyo.order.adapter;


import com.groyyo.order.dto.request.BuyerRequestDto;
import com.groyyo.order.dto.response.BuyerResponseDto;
import com.groyyo.order.entity.Buyer;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class BuyerAdapter {

    public Buyer buildBuyerFromRequest(BuyerRequestDto buyerRequestDto) {

        return Buyer
                .builder()
                .name(buyerRequestDto.getName())
                .build();
    }

    public Buyer buildBuyerFromResponse(BuyerResponseDto buyerResponseDto) {

        return Buyer
                .builder()
                .name(buyerResponseDto.getName())
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
