package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.order.management.adapter.PurchaseOrderQuantityAdapter;
import com.groyyo.order.management.db.service.PurchaseOrderQuantityDbService;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityCreateDto;
import com.groyyo.order.management.dto.request.PurchaseOrderQuantityRequestDto;
import com.groyyo.order.management.dto.response.PurchaseOrderQuantityResponseDto;
import com.groyyo.order.management.entity.PurchaseOrderQuantity;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PurchaseOrderQuantityServiceImpl implements PurchaseOrderQuantityService {

	@Autowired
	private PurchaseOrderQuantityDbService purchaseOrderQuantityDbService;

	@Override
	public List<PurchaseOrderQuantityResponseDto> getAllPurchaseOrderQuantitiesForPurchaseOrder(String purchaseOrderId) {

		log.info("Serving request to get all purchaseOrderQuantities for a purchase order");

		List<PurchaseOrderQuantity> purchaseOrderQuantityEntities = purchaseOrderQuantityDbService.getAllPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderId);

		if (CollectionUtils.isEmpty(purchaseOrderQuantityEntities)) {
			log.error("No PurchaseOrderQuantities found in the system");
			return new ArrayList<PurchaseOrderQuantityResponseDto>();
		}

		return PurchaseOrderQuantityAdapter.buildResponsesListFromEntities(purchaseOrderQuantityEntities);
	}

	@Override
	public PurchaseOrderQuantityResponseDto getPurchaseOrderQuantityById(String id) {

		log.info("Serving request to get a purchaseOrderQuantity by id:{}", id);

		PurchaseOrderQuantity purchaseOrderQuantity = purchaseOrderQuantityDbService.getPurchaseOrderQuantityById(id);

		if (Objects.isNull(purchaseOrderQuantity)) {
			String errorMsg = "PurchaseOrderQuantity with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return PurchaseOrderQuantityAdapter.buildResponseFromEntity(purchaseOrderQuantity);
	}

	@Override
	public PurchaseOrderQuantityResponseDto addPurchaseOrderQuantity(PurchaseOrderQuantityRequestDto purchaseOrderQuantityRequestDto, String purchaseOrderId, Double tolerance) {

		log.info("Serving request to add a purchaseOrderQuantity with request object:{}", purchaseOrderQuantityRequestDto);

		PurchaseOrderQuantity purchaseOrderQuantity = PurchaseOrderQuantityAdapter.buildPurchaseOrderQuantityFromRequest(purchaseOrderQuantityRequestDto, purchaseOrderId, tolerance);

		purchaseOrderQuantity = purchaseOrderQuantityDbService.savePurchaseOrderQuantity(purchaseOrderQuantity);

		if (Objects.isNull(purchaseOrderQuantity)) {
			log.error("Unable to add purchaseOrderQuantity for object: {}", purchaseOrderQuantityRequestDto);
			return null;
		}

		PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto = PurchaseOrderQuantityAdapter.buildResponseFromEntity(purchaseOrderQuantity);

		return purchaseOrderQuantityResponseDto;
	}

	@Override
	public List<PurchaseOrderQuantityResponseDto> addBulkPurchaseOrderQuantity(List<PurchaseOrderQuantityRequestDto> purchaseOrderQuantityRequestList, String purchaseOrderId, Double tolerance) {

		log.info("Serving request to bulk add purchaseOrderQuantity with request object:{}", purchaseOrderQuantityRequestList);

		List<PurchaseOrderQuantity> purchaseOrderQuantityList = PurchaseOrderQuantityAdapter.buildPurchaseOrderQuantityListFromRequestList(purchaseOrderQuantityRequestList, purchaseOrderId,
				tolerance);

		purchaseOrderQuantityList = purchaseOrderQuantityDbService.savePurchaseOrderQuantityList(purchaseOrderQuantityList);

		if (Objects.isNull(purchaseOrderQuantityList)) {
			log.error("Unable to add purchaseOrderQuantity for object: {}", purchaseOrderQuantityRequestList);
			return null;
		}

		return PurchaseOrderQuantityAdapter.buildResponsesListFromEntities(purchaseOrderQuantityList);
	}

	@Override
	public PurchaseOrderQuantityResponseDto updatePurchaseOrderQuantity(PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto) {

		log.info("Serving request to update a purchaseOrderQuantity with request object:{}", purchaseOrderQuantityCreateDto);

		PurchaseOrderQuantity purchaseOrderQuantity = purchaseOrderQuantityDbService.getPurchaseOrderQuantityById(purchaseOrderQuantityCreateDto.getId());

		if (Objects.isNull(purchaseOrderQuantity)) {
			log.error("PurchaseOrderQuantity with id: {} not found in the system", purchaseOrderQuantityCreateDto.getId());
			return null;
		}

		runValidations(purchaseOrderQuantityCreateDto);

		purchaseOrderQuantity = PurchaseOrderQuantityAdapter.clonePurchaseOrderQuantityWithRequest(purchaseOrderQuantityCreateDto, purchaseOrderQuantity);

		purchaseOrderQuantityDbService.savePurchaseOrderQuantity(purchaseOrderQuantity);

		PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto = PurchaseOrderQuantityAdapter.buildResponseFromEntity(purchaseOrderQuantity);

		return purchaseOrderQuantityResponseDto;
	}

	@Override
	public Map<String, List<PurchaseOrderQuantityResponseDto>> getQuantitiesForPurchaseOrders(List<String> purchaseOrderIds) {
		Map<String, List<PurchaseOrderQuantityResponseDto>> purchaseOrderQuantitiesMap = new HashMap<String, List<PurchaseOrderQuantityResponseDto>>(purchaseOrderIds.size());

		purchaseOrderIds.forEach(purchaseOrderId -> {

			List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = getAllPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderId);
			purchaseOrderQuantitiesMap.put(purchaseOrderId, purchaseOrderQuantityResponseDtos);
		});

		return purchaseOrderQuantitiesMap;
	}

	@Override
	public Long getTotalQuantityForPurchaseOrder(String purchaseOrderId) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = getAllPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderId);

		Long totalQuantityOfPurchaseOrder = 0L;

		for (PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto : purchaseOrderQuantityResponseDtos) {
			totalQuantityOfPurchaseOrder += purchaseOrderQuantityResponseDto.getQuantity();
		}

		return totalQuantityOfPurchaseOrder;
	}

	@Override
	public Long getTotalTargetQuantityForPurchaseOrder(String purchaseOrderId) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = getAllPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderId);

		Long totalTargetQuantityOfPurchaseOrder = 0L;

		for (PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto : purchaseOrderQuantityResponseDtos) {
			totalTargetQuantityOfPurchaseOrder += purchaseOrderQuantityResponseDto.getTargetQuantity();
		}

		return totalTargetQuantityOfPurchaseOrder;
	}

	@Override
	public Pair<Long, Long> getTotalQuantityAndTotalTargetQuantityForPurchaseOrder(String purchaseOrderId) {

		return Pair.of(getTotalQuantityForPurchaseOrder(purchaseOrderId), getTotalTargetQuantityForPurchaseOrder(purchaseOrderId));
	}

	@Override
	public Map<String, Pair<Long, Long>> getTotalQuantityAndTotalTargetQuantityForPurchaseOrders(List<String> purchaseOrderIds) {

		Map<String, Pair<Long, Long>> purchaseOrderIdAndTotalQuantityMap = new HashMap<String, Pair<Long, Long>>(purchaseOrderIds.size());

		purchaseOrderIds.forEach(purchaseOrderId -> {

			purchaseOrderIdAndTotalQuantityMap.put(purchaseOrderId, getTotalQuantityAndTotalTargetQuantityForPurchaseOrder(purchaseOrderId));
		});

		return purchaseOrderIdAndTotalQuantityMap;
	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && purchaseOrderQuantityDbService.isEntityExistsByName(name);
	}

	private void runValidations(PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto) {
		validateName(purchaseOrderQuantityCreateDto);
	}

	private void validateName(PurchaseOrderQuantityCreateDto purchaseOrderQuantityCreateDto) {

		if (isEntityExistsWithName(purchaseOrderQuantityCreateDto.getName())) {
			String errorMsg = "PurchaseOrderQuantity cannot be created/updated as record already exists with name: " + purchaseOrderQuantityCreateDto.getName();
			throw new RecordExistsException(errorMsg);
		}
	}
}
