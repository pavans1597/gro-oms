package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.sqlPostgresJpa.specification.utils.GroyyoSpecificationBuilder;
import com.groyyo.core.sqlPostgresJpa.specification.utils.PaginationUtility;
import com.groyyo.order.management.adapter.PurchaseOrderAdapter;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.enums.PurchaseOrderStatus;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import com.groyyo.order.management.service.PurchaseOrderService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	private PurchaseOrderDbService purchaseOrderDbService;

	@Autowired
	private PurchaseOrderQuantityService purchaseOrderQuantityService;

	@Autowired
	private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;

	@Override
	public List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status) {

		log.info("Serving request to get all purchaseOrders");

		List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = new ArrayList<PurchaseOrderResponseDto>();

		List<PurchaseOrder> purchaseOrderEntities = Objects.isNull(status) ? purchaseOrderDbService.getAllPurchaseOrders()
				: purchaseOrderDbService.getAllPurchaseOrdersForStatus(status);

		if (CollectionUtils.isEmpty(purchaseOrderEntities)) {
			log.error("No PurchaseOrders found in the system");
			return new ArrayList<>();
		}

		purchaseOrderResponseDtos = PurchaseOrderAdapter.buildResponsesFromEntities(purchaseOrderEntities);
		log.info("Found total: {} purchase orders to show in listing ", purchaseOrderResponseDtos.size());

		List<String> purchaseOrderIds = purchaseOrderResponseDtos.stream().map(PurchaseOrderResponseDto::getUuid).collect(Collectors.toList());
		log.info("Fetched distinct purchase ids: {} from the list of purchase orders ", purchaseOrderIds.size());

		populateTotalQuantitiesForPurchaseOrder(purchaseOrderResponseDtos);

		// TODO fetch purchase order line data
		return purchaseOrderResponseDtos;
	}

	@Override
	public PurchaseOrderResponseDto getPurchaseOrderById(String id) {

		log.info("Serving request to get a purchaseOrder by id:{}", id);

		PurchaseOrder purchaseOrder = purchaseOrderDbService.getPurchaseOrderById(id);

		if (Objects.isNull(purchaseOrder)) {
			String errorMsg = "PurchaseOrder with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
	}

	@Override
	public PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto) {

		log.info("Serving request to add a purchaseOrder with request object:{}", purchaseOrderRequestDto);

		PurchaseOrder purchaseOrder = PurchaseOrderAdapter.buildPurchaseOrderFromRequest(purchaseOrderRequestDto);

		purchaseOrder = purchaseOrderDbService.savePurchaseOrder(purchaseOrder);

		if (Objects.isNull(purchaseOrder)) {
			log.error("Unable to add purchaseOrder for object: {}", purchaseOrderRequestDto);
			return null;
		}

		updatePurchaseOrderStatus(purchaseOrder);

		return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
	}

	private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {

		purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.YET_TO_START);

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentDbService.getLineCheckerAssignmentForPurchaseOrder(purchaseOrder.getUuid());

		if (CollectionUtils.isNotEmpty(lineCheckerAssignments)) {
			purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.ONGOING);
		}

		purchaseOrderDbService.saveAndFlush(purchaseOrder);
	}

	@Override
	public PurchaseOrderResponseDto updatePurchaseOrder(PurchaseOrderUpdateDto purchaseOrderUpdateDto) {

		log.info("Serving request to update a purchaseOrder with request object:{}", purchaseOrderUpdateDto);

		PurchaseOrder purchaseOrder = purchaseOrderDbService.getPurchaseOrderById(purchaseOrderUpdateDto.getId());

		if (Objects.isNull(purchaseOrder)) {
			log.error("PurchaseOrder with id: {} not found in the system", purchaseOrderUpdateDto.getId());
			return null;
		}

		runValidations(purchaseOrderUpdateDto);

		purchaseOrder = PurchaseOrderAdapter.clonePurchaseOrderWithRequest(purchaseOrderUpdateDto, purchaseOrder);

		purchaseOrderDbService.savePurchaseOrder(purchaseOrder);

		return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
	}

	private void populateTotalQuantitiesForPurchaseOrder(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.forEach(purchaseOrderResponseDto -> {

			purchaseOrderResponseDto.setTotalQuantity(purchaseOrderQuantityService.getTotalQuantityForPurchaseOrder(purchaseOrderResponseDto.getUuid()));
			purchaseOrderResponseDto.setTotalTargetQuantity(purchaseOrderQuantityService.getTotalTargetQuantityForPurchaseOrder(purchaseOrderResponseDto.getUuid()));
		});
	}

	@Override
	public PageResponse<PurchaseOrderResponseDto> getPurchaseOrderListing(int page, int limit) {

		Specification<PurchaseOrder> specification = getSpecificationForPurchaseOrderListing();

		Pageable pageable = PaginationUtility.getPageRequest(page, limit, "updatedAt", Direction.DESC);

		Page<PurchaseOrder> data = purchaseOrderDbService.findAll(specification, pageable);

		if (Objects.nonNull(data))
			return new PageResponse<PurchaseOrderResponseDto>(limit, data.getNumberOfElements(), data.getTotalPages(), data.getTotalElements(), convertPurchaseOrderPageDataToResponseDtos(data));
		else
			return new PageResponse<PurchaseOrderResponseDto>(limit, 0, 0, 0, null);

	}

	private List<PurchaseOrderResponseDto> convertPurchaseOrderPageDataToResponseDtos(Page<PurchaseOrder> data) {

		List<PurchaseOrder> purchaseOrders = data.get().collect(Collectors.toList());

		List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = PurchaseOrderAdapter.buildResponsesFromEntities(purchaseOrders);

		populateTotalQuantitiesForPurchaseOrder(purchaseOrderResponseDtos);

		return purchaseOrderResponseDtos;
	}

	/**
	 * @return
	 */
	private Specification<PurchaseOrder> getSpecificationForPurchaseOrderListing() {
		GroyyoSpecificationBuilder<PurchaseOrder> groyyoSpecificationBuilder = new GroyyoSpecificationBuilder<PurchaseOrder>();

		return groyyoSpecificationBuilder.build();
	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && purchaseOrderDbService.isEntityExistsByName(name);
	}

	private void runValidations(PurchaseOrderRequestDto purchaseOrderRequestDto) {

		validateName(purchaseOrderRequestDto);
	}

	private void validateName(PurchaseOrderRequestDto purchaseOrderRequestDto) {

		if (isEntityExistsWithName(purchaseOrderRequestDto.getPurchaseOrderNumber())) {
			String errorMsg = "PurchaseOrder cannot be created/updated as record already exists with name: " + purchaseOrderRequestDto.getPurchaseOrderNumber();
			throw new RecordExistsException(errorMsg);
		}
	}
}
