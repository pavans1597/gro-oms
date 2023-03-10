package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.specification.utils.CriteriaOperation;
import com.groyyo.core.sqlPostgresJpa.specification.utils.GroyyoSpecificationBuilder;
import com.groyyo.core.sqlPostgresJpa.specification.utils.PaginationUtility;
import com.groyyo.order.management.adapter.LineCheckerAssignmentAdapter;
import com.groyyo.order.management.adapter.PurchaseOrderAdapter;
import com.groyyo.order.management.constants.FilterConstants;
import com.groyyo.order.management.constants.SymbolConstants;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.db.service.PurchaseOrderDbService;
import com.groyyo.order.management.dto.filter.PurchaseOrderFilterDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.request.UserLineDetails;
import com.groyyo.order.management.dto.response.PurchaseOrderResponseDto;
import com.groyyo.order.management.dto.response.StyleDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.enums.PurchaseOrderStatus;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import com.groyyo.order.management.service.PurchaseOrderService;
import com.groyyo.order.management.service.StyleService;

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

	@Autowired
	private StyleService styleService;

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

		populateTotalQuantitiesForPurchaseOrder(purchaseOrderResponseDtos);
		populateLineCheckerAssignmentsForPurchaseOrder(purchaseOrderResponseDtos);

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

		log.info("Serving request to add a purchaseOrder with request object: {}", purchaseOrderRequestDto);

		PurchaseOrder purchaseOrder = PurchaseOrder.builder().build();

		addRunTimeStyle(purchaseOrderRequestDto);

		purchaseOrder = PurchaseOrderAdapter.buildPurchaseOrderFromRequest(purchaseOrderRequestDto);

		purchaseOrder = purchaseOrderDbService.savePurchaseOrder(purchaseOrder);

		if (Objects.isNull(purchaseOrder)) {
			log.error("Unable to add purchaseOrder for object: {}", purchaseOrderRequestDto);
			return null;
		}

		addPurchaseOrderQuantities(purchaseOrderRequestDto, purchaseOrder);

		updateVitalFieldsAndSave(purchaseOrder);

		return PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);
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

	@Override
	public PageResponse<PurchaseOrderResponseDto> getPurchaseOrderListing(PurchaseOrderFilterDto purchaseOrderFilterDto, PurchaseOrderStatus purchaseOrderStatus, int page, int limit) {

		Specification<PurchaseOrder> specification = getSpecificationForPurchaseOrderListing(purchaseOrderFilterDto, purchaseOrderStatus);

		Pageable pageable = PaginationUtility.getPageRequest(page, limit, FilterConstants.UPDATED_AT, Direction.DESC);

		Page<PurchaseOrder> data = purchaseOrderDbService.findAll(specification, pageable);

		if (Objects.nonNull(data))
			return new PageResponse<PurchaseOrderResponseDto>(limit, data.getNumberOfElements(), data.getTotalPages(), data.getTotalElements(), convertPurchaseOrderPageDataToResponseDtos(data));
		else
			return new PageResponse<PurchaseOrderResponseDto>(limit, 0, 0, 0, null);

	}

	private void updateVitalFieldsAndSave(PurchaseOrder purchaseOrder) {

		updatePurchaseOrderStatus(purchaseOrder);

		setTotalQuantitiesForPurchaseOrder(purchaseOrder);

		purchaseOrderDbService.saveAndFlush(purchaseOrder);
	}

	private void addPurchaseOrderQuantities(PurchaseOrderRequestDto purchaseOrderRequestDto, PurchaseOrder purchaseOrder) {

		purchaseOrderQuantityService.addBulkPurchaseOrderQuantity(purchaseOrderRequestDto.getPurchaseOrderQuantityRequest(), purchaseOrder.getUuid(), purchaseOrderRequestDto.getTolerance());
	}

	private void addRunTimeStyle(PurchaseOrderRequestDto purchaseOrderRequestDto) {

		String styleUuid = Objects.nonNull(purchaseOrderRequestDto) ? purchaseOrderRequestDto.getStyleRequestDto().getUuid() : null;

		if (StringUtils.isBlank(styleUuid)) {

			StyleDto styleResponse = styleService.addStyle(purchaseOrderRequestDto.getStyleRequestDto());
			purchaseOrderRequestDto.setStyleRequestDto(styleResponse);
		}
	}

	private void populateTotalQuantitiesForPurchaseOrder(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.forEach(purchaseOrderResponseDto -> {

			purchaseOrderResponseDto.setTotalQuantity(purchaseOrderQuantityService.getTotalQuantityForPurchaseOrder(purchaseOrderResponseDto.getUuid()));
			purchaseOrderResponseDto.setTotalTargetQuantity(purchaseOrderQuantityService.getTotalTargetQuantityForPurchaseOrder(purchaseOrderResponseDto.getUuid()));
		});
	}

	private void setTotalQuantitiesForPurchaseOrder(PurchaseOrder purchaseOrder) {

		purchaseOrder.setTotalQuantity(purchaseOrderQuantityService.getTotalQuantityForPurchaseOrder(purchaseOrder.getUuid()));
		purchaseOrder.setTotalTargetQuantity(purchaseOrderQuantityService.getTotalTargetQuantityForPurchaseOrder(purchaseOrder.getUuid()));
	}

	private void populateLineCheckerAssignmentsForPurchaseOrder(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.forEach(purchaseOrderResponseDto -> {

			populateLineCheckerAssignments(purchaseOrderResponseDto);
		});
	}

	private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {

		purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.YET_TO_START);

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentDbService.getLineCheckerAssignmentForPurchaseOrder(purchaseOrder.getUuid());

		if (CollectionUtils.isNotEmpty(lineCheckerAssignments)) {
			purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.ONGOING);
		}
	}

	private void populateLineCheckerAssignments(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentDbService.getLineCheckerAssignmentForPurchaseOrder(purchaseOrderResponseDto.getUuid());

		List<UserLineDetails> userLineDetails = LineCheckerAssignmentAdapter.buildUserLineDetailsFromEntities(lineCheckerAssignments);
		purchaseOrderResponseDto.setUserLineDetails(userLineDetails);

		Map<LineType, List<UserLineDetails>> userDetailsMap = userLineDetails.stream().collect(Collectors.groupingBy(UserLineDetails::getLineType));
		purchaseOrderResponseDto.setUserLineDetailsMap(userDetailsMap);
	}

	private List<PurchaseOrderResponseDto> convertPurchaseOrderPageDataToResponseDtos(Page<PurchaseOrder> data) {

		List<PurchaseOrder> purchaseOrders = data.get().collect(Collectors.toList());

		List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = PurchaseOrderAdapter.buildResponsesFromEntities(purchaseOrders);

		populateTotalQuantitiesForPurchaseOrder(purchaseOrderResponseDtos);
		populateLineCheckerAssignmentsForPurchaseOrder(purchaseOrderResponseDtos);

		return purchaseOrderResponseDtos;
	}

	/**
	 * @return
	 */
	private Specification<PurchaseOrder> getSpecificationForPurchaseOrderListing(PurchaseOrderFilterDto purchaseOrderFilterDto, PurchaseOrderStatus purchaseOrderStatus) {
		GroyyoSpecificationBuilder<PurchaseOrder> groyyoSpecificationBuilder = new GroyyoSpecificationBuilder<PurchaseOrder>();

		groyyoSpecificationBuilder.with(FilterConstants.STATUS, CriteriaOperation.TRUE, Boolean.TRUE);

		if (Objects.nonNull(purchaseOrderStatus))
			groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_STATUS, CriteriaOperation.ENUM_EQ, purchaseOrderStatus);

		if (Objects.nonNull(purchaseOrderFilterDto)) {

			if (Objects.nonNull(purchaseOrderFilterDto.getId()))
				groyyoSpecificationBuilder.with(FilterConstants.ID, CriteriaOperation.EQ, purchaseOrderFilterDto.getId());

			if (Objects.nonNull(purchaseOrderFilterDto.getUuid()))
				groyyoSpecificationBuilder.with(FilterConstants.UUID, CriteriaOperation.EQ, purchaseOrderFilterDto.getUuid());

			if (Objects.nonNull(purchaseOrderFilterDto.getName()))
				groyyoSpecificationBuilder.with(FilterConstants.NAME, CriteriaOperation.EQ, purchaseOrderFilterDto.getName());

			if (Objects.nonNull(purchaseOrderFilterDto.getCreatedAt()))
				groyyoSpecificationBuilder.with(FilterConstants.CREATED_AT, CriteriaOperation.DATE_EQ, purchaseOrderFilterDto.getCreatedAt());

			if (Objects.nonNull(purchaseOrderFilterDto.getCreatedBy()))
				groyyoSpecificationBuilder.with(FilterConstants.CREATED_BY, CriteriaOperation.EQ, purchaseOrderFilterDto.getCreatedBy());

			if (Objects.nonNull(purchaseOrderFilterDto.getUpdatedAt()))
				groyyoSpecificationBuilder.with(FilterConstants.UPDATED_AT, CriteriaOperation.DATE_EQ, purchaseOrderFilterDto.getUpdatedAt());

			if (Objects.nonNull(purchaseOrderFilterDto.getUpdatedBy()))
				groyyoSpecificationBuilder.with(FilterConstants.UPDATED_BY, CriteriaOperation.EQ, purchaseOrderFilterDto.getUpdatedBy());

			if (Objects.nonNull(purchaseOrderFilterDto.getExFtyDate()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_EX_FTY_DATE, CriteriaOperation.DATE_EQ, purchaseOrderFilterDto.getExFtyDate());

			if (Objects.nonNull(purchaseOrderFilterDto.getReceiveDate()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_RECEIVE_DATE, CriteriaOperation.DATE_EQ, purchaseOrderFilterDto.getReceiveDate());

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getPurchaseOrderNumber()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_NUMBER, CriteriaOperation.LIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getPurchaseOrderNumber() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getFabricName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_FABRIC_NAME, CriteriaOperation.LIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getFabricName() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getBuyerName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_BUYER_NAME, CriteriaOperation.LIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getBuyerName() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getStyleNumber()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_STYLE_NUMBER, CriteriaOperation.LIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getStyleNumber() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getStyleName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_STYLE_NAME, CriteriaOperation.LIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getStyleName() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getProductName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_PRODUCT_NAME, CriteriaOperation.LIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getProductName() + SymbolConstants.SYMBOL_PERCENT);

		}

		return groyyoSpecificationBuilder.build();
	}

	@Override
	public void changeStatusOfPurchaseOrder(String purchaseOrderId, PurchaseOrderStatus desiredPurchaseOrderStatus, Boolean forceUpdate) {

		PurchaseOrder purchaseOrder = purchaseOrderDbService.findByUuid(purchaseOrderId);

		if (Objects.isNull(purchaseOrder)) {
			log.error("Not able to find valid purchase order entity with id: {}", purchaseOrderId);
			return;
		}

		PurchaseOrderStatus currentPurchaseOrderStatus = purchaseOrder.getPurchaseOrderStatus();
		log.info("Current status of purchase order with uuid: {} is: {}", purchaseOrder.getUuid(), currentPurchaseOrderStatus);

		if (desiredPurchaseOrderStatus.getSequenceId() > currentPurchaseOrderStatus.getSequenceId()) {
			log.info("Changing the status of purchase order from current status: {} to desired status: {}", currentPurchaseOrderStatus.getSequenceId(),
					desiredPurchaseOrderStatus.getSequenceId());
			purchaseOrder.setPurchaseOrderStatus(desiredPurchaseOrderStatus);
		}

		if (desiredPurchaseOrderStatus.getSequenceId() < currentPurchaseOrderStatus.getSequenceId()) {
			log.info("Cannot move the status to a level: {} lower than the current level: {}", desiredPurchaseOrderStatus.getSequenceId(), currentPurchaseOrderStatus.getSequenceId());

			if (forceUpdate) {
				log.info("Changing the status of purchase order forcefully from current status: {} to desired status: {}", currentPurchaseOrderStatus.getSequenceId(),
						desiredPurchaseOrderStatus.getSequenceId());
				purchaseOrder.setPurchaseOrderStatus(desiredPurchaseOrderStatus);
			}
		}

		purchaseOrderDbService.saveAndFlush(purchaseOrder);
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
