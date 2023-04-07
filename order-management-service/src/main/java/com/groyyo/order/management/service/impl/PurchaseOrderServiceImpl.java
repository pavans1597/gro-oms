package com.groyyo.order.management.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.groyyo.core.base.common.dto.PageResponse;
import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderQuantityResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderResponseDto;
import com.groyyo.core.dto.PurchaseOrder.PurchaseOrderStatus;
import com.groyyo.core.dto.PurchaseOrder.StyleDto;
import com.groyyo.core.dto.PurchaseOrder.UserLineDetails;
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
import com.groyyo.order.management.dto.request.BulkColorRequestDto;
import com.groyyo.order.management.dto.request.BulkOrderExcelRequestDto;
import com.groyyo.order.management.dto.request.BulkPurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderRequestDto;
import com.groyyo.order.management.dto.request.PurchaseOrderUpdateDto;
import com.groyyo.order.management.dto.response.PurchaseOrderStatusCountDto;
import com.groyyo.order.management.entity.Buyer;
import com.groyyo.order.management.entity.Color;
import com.groyyo.order.management.entity.Fit;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.entity.Part;
import com.groyyo.order.management.entity.Product;
import com.groyyo.order.management.entity.PurchaseOrder;
import com.groyyo.order.management.entity.PurchaseOrderQuantity;
import com.groyyo.order.management.entity.Season;
import com.groyyo.order.management.entity.Size;
import com.groyyo.order.management.entity.SizeGroup;
import com.groyyo.order.management.entity.Style;
import com.groyyo.order.management.service.BuyerService;
import com.groyyo.order.management.service.ColorService;
import com.groyyo.order.management.service.FitService;
import com.groyyo.order.management.service.LineCheckerAssignmentService;
import com.groyyo.order.management.service.PartService;
import com.groyyo.order.management.service.ProductService;
import com.groyyo.order.management.service.PurchaseOrderQuantityService;
import com.groyyo.order.management.service.PurchaseOrderService;
import com.groyyo.order.management.service.SeasonService;
import com.groyyo.order.management.service.SizeGroupService;
import com.groyyo.order.management.service.SizeService;
import com.groyyo.order.management.service.StyleService;
import com.groyyo.order.management.util.BuilderUtils;
import com.groyyo.order.management.util.MapperUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

	@Autowired
	private PurchaseOrderDbService purchaseOrderDbService;

	@Autowired
	private PurchaseOrderQuantityService purchaseOrderQuantityService;

	@Autowired
	private LineCheckerAssignmentService lineCheckerAssignmentService;

	@Autowired
	private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;

	@Autowired
	private StyleService styleService;

	@Autowired
	private SeasonService seasonService;

	@Autowired
	private PartService partService;

	@Autowired
	private FitService fitService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ColorService colorService;

	@Autowired
	private SizeService sizeService;

	@Autowired
	private SizeGroupService sizeGroupService;

	@Autowired
	private BuyerService buyerService;

	@Override
	public List<PurchaseOrderResponseDto> getAllPurchaseOrders(Boolean status) {

		log.info("Serving request to get all purchaseOrders");

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<PurchaseOrder> purchaseOrderEntities = Objects.isNull(status) ? purchaseOrderDbService.getAllPurchaseOrders(factoryId)
				: purchaseOrderDbService.getAllPurchaseOrdersForStatus(status, factoryId);

		if (CollectionUtils.isEmpty(purchaseOrderEntities)) {
			log.error("No PurchaseOrders found in the system");
			return new ArrayList<>();
		}

		return buildPurchaseOrderResponseWithQuantitiesAndAssignments(purchaseOrderEntities);
	}

	@Override
	public PurchaseOrderResponseDto getPurchaseOrderById(String id) {

		log.info("Serving request to get a purchaseOrder by id: {}", id);

		PurchaseOrder purchaseOrder = purchaseOrderDbService.getPurchaseOrderById(id);

		if (Objects.isNull(purchaseOrder)) {
			String errorMsg = "PurchaseOrder with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return buildPurchaseOrderResponseWithQuantitiesAndAssignments(purchaseOrder);
	}

	@Override
	public PurchaseOrderResponseDto addPurchaseOrder(PurchaseOrderRequestDto purchaseOrderRequestDto) {

		log.info("Serving request to add a purchaseOrder with request object: {}", purchaseOrderRequestDto);

		PurchaseOrder purchaseOrder = PurchaseOrder.builder().build();

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		addRunTimeStyle(purchaseOrderRequestDto, factoryId);

		purchaseOrder = PurchaseOrderAdapter.buildPurchaseOrderFromRequest(purchaseOrderRequestDto, factoryId);

		purchaseOrder = purchaseOrderDbService.savePurchaseOrder(purchaseOrder);

		if (Objects.isNull(purchaseOrder)) {
			log.error("Unable to add purchaseOrder for object: {}", purchaseOrderRequestDto);
			return null;
		}

		addPurchaseOrderQuantities(purchaseOrderRequestDto, purchaseOrder);

		updateVitalFieldsAndSave(purchaseOrder);

		return buildPurchaseOrderResponseWithQuantitiesAndAssignments(purchaseOrder);
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

		return buildPurchaseOrderResponseWithQuantitiesAndAssignments(purchaseOrder);
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

	private void addRunTimeStyle(PurchaseOrderRequestDto purchaseOrderRequestDto, String factoryId) {

		String styleUuid = Objects.nonNull(purchaseOrderRequestDto) ? purchaseOrderRequestDto.getStyleRequestDto().getUuid() : null;

		if (StringUtils.isBlank(styleUuid)) {

			StyleDto styleRequestDto = purchaseOrderRequestDto.getStyleRequestDto();
			styleRequestDto.setFactoryId(factoryId);

			StyleDto styleResponse = styleService.addStyle(styleRequestDto);
			purchaseOrderRequestDto.setStyleRequestDto(styleResponse);
		}
	}

	private void populateTotalQuantitiesForPurchaseOrders(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.forEach(purchaseOrderResponseDto -> {

			populateTotalQuantitiesForPurchaseOrder(purchaseOrderResponseDto);
		});
	}

	private void populateTotalQuantitiesForPurchaseOrder(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		purchaseOrderResponseDto.setTotalQuantity(getTotalQuantityForPurchaseOrder(purchaseOrderResponseDto));
		purchaseOrderResponseDto.setTotalTargetQuantity(getTotalTargetQuantityForPurchaseOrder(purchaseOrderResponseDto));
	}

	private void setTotalQuantitiesForPurchaseOrder(PurchaseOrder purchaseOrder) {

		populatePurchaseOrderQuantitiesForPurchaseOrder(purchaseOrder);
	}

	private void populateLineCheckerAssignmentsForPurchaseOrders(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.forEach(purchaseOrderResponseDto -> {

			populateLineCheckerAssignmentsForPurchaseOrder(purchaseOrderResponseDto);
		});
	}

	private void updatePurchaseOrderStatus(PurchaseOrder purchaseOrder) {

		purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.YET_TO_START);

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentDbService.getLineCheckerAssignmentForPurchaseOrder(purchaseOrder.getUuid(), purchaseOrder.getFactoryId());

		if (CollectionUtils.isNotEmpty(lineCheckerAssignments)) {
			purchaseOrder.setPurchaseOrderStatus(PurchaseOrderStatus.ONGOING);
		}
	}

	private void populateLineCheckerAssignmentsForPurchaseOrder(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		List<LineCheckerAssignment> lineCheckerAssignments = lineCheckerAssignmentDbService.getLineCheckerAssignmentForPurchaseOrder(purchaseOrderResponseDto.getUuid(),
				purchaseOrderResponseDto.getFactoryId());

		List<UserLineDetails> userLineDetails = LineCheckerAssignmentAdapter.buildUserLineDetailsFromEntities(lineCheckerAssignments);
		purchaseOrderResponseDto.setUserLineDetails(userLineDetails);

		Map<LineType, List<UserLineDetails>> userDetailsMap = userLineDetails.stream().collect(Collectors.groupingBy(UserLineDetails::getLineType));
		purchaseOrderResponseDto.setUserLineDetailsMap(userDetailsMap);
	}

	private List<PurchaseOrderResponseDto> convertPurchaseOrderPageDataToResponseDtos(Page<PurchaseOrder> data) {

		List<PurchaseOrder> purchaseOrders = data.get().collect(Collectors.toList());

		List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = buildPurchaseOrderResponseWithQuantitiesAndAssignments(purchaseOrders);

		return purchaseOrderResponseDtos;
	}

	private List<PurchaseOrderResponseDto> buildPurchaseOrderResponseWithQuantitiesAndAssignments(List<PurchaseOrder> purchaseOrderEntities) {

		List<PurchaseOrderResponseDto> purchaseOrderResponseDtos = PurchaseOrderAdapter.buildResponsesFromEntities(purchaseOrderEntities);
		log.info("Found total: {} purchase orders to show in listing ", purchaseOrderResponseDtos.size());

		populatePurchaseOrderQuantitiesForPurchaseOrders(purchaseOrderResponseDtos);
		populateTotalQuantitiesForPurchaseOrders(purchaseOrderResponseDtos);
		populateLineCheckerAssignmentsForPurchaseOrders(purchaseOrderResponseDtos);

		return purchaseOrderResponseDtos;
	}

	@SuppressWarnings("unused")
	private void populateStyleDtosForPurchaseOrders(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.stream().forEach(purchaseOrderResponseDto -> {

			populateStyleDtoForPurchaseOrder(purchaseOrderResponseDto);
		});
	}

	/**
	 * @param purchaseOrderResponseDto
	 */
	private void populateStyleDtoForPurchaseOrder(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		if (StringUtils.isNotBlank(purchaseOrderResponseDto.getStyleId())) {

			StyleDto styleDto = styleService.getStyleById(purchaseOrderResponseDto.getStyleId());

			purchaseOrderResponseDto.setStyleDto(styleDto);
		}
	}

	private PurchaseOrderResponseDto buildPurchaseOrderResponseWithQuantitiesAndAssignments(PurchaseOrder purchaseOrder) {

		PurchaseOrderResponseDto purchaseOrderResponseDto = PurchaseOrderAdapter.buildResponseFromEntity(purchaseOrder);

		populateStyleDtoForPurchaseOrder(purchaseOrderResponseDto);
		populatePurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderResponseDto);
		populateTotalQuantitiesForPurchaseOrder(purchaseOrderResponseDto);
		populateLineCheckerAssignmentsForPurchaseOrder(purchaseOrderResponseDto);

		return purchaseOrderResponseDto;
	}

	private List<PurchaseOrderQuantityResponseDto> getPurchaseOrderQuantitiesForPurchaseOrder(String purchaseOrderId) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = purchaseOrderQuantityService.getAllPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderId);

		return purchaseOrderQuantityResponseDtos;
	}

	private void populatePurchaseOrderQuantitiesForPurchaseOrder(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = getPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderResponseDto.getUuid());

		purchaseOrderResponseDto.setPurchaseOrderQuantityResponseDtos(purchaseOrderQuantityResponseDtos);
	}

	private void populatePurchaseOrderQuantitiesForPurchaseOrder(PurchaseOrder purchaseOrder) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = getPurchaseOrderQuantitiesForPurchaseOrder(purchaseOrder.getUuid());

		purchaseOrder.setTotalQuantity(getTotalQuantity(purchaseOrderQuantityResponseDtos));
		purchaseOrder.setTotalTargetQuantity(getTotalTargetQuantity(purchaseOrderQuantityResponseDtos));
	}

	private void populatePurchaseOrderQuantitiesForPurchaseOrders(List<PurchaseOrderResponseDto> purchaseOrderResponseDtos) {

		purchaseOrderResponseDtos.stream().forEach(purchaseOrderResponseDto -> {

			populatePurchaseOrderQuantitiesForPurchaseOrder(purchaseOrderResponseDto);
		});
	}

	private Long getTotalQuantityForPurchaseOrder(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = purchaseOrderResponseDto.getPurchaseOrderQuantityResponseDtos();

		return getTotalQuantity(purchaseOrderQuantityResponseDtos);
	}

	private Long getTotalQuantity(List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos) {

		Long totalQuantityOfPurchaseOrder = 0L;

		for (PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto : purchaseOrderQuantityResponseDtos) {
			totalQuantityOfPurchaseOrder += purchaseOrderQuantityResponseDto.getQuantity();
		}

		return totalQuantityOfPurchaseOrder;
	}

	private Long getTotalTargetQuantityForPurchaseOrder(PurchaseOrderResponseDto purchaseOrderResponseDto) {

		List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos = purchaseOrderResponseDto.getPurchaseOrderQuantityResponseDtos();

		return getTotalTargetQuantity(purchaseOrderQuantityResponseDtos);
	}

	private Long getTotalTargetQuantity(List<PurchaseOrderQuantityResponseDto> purchaseOrderQuantityResponseDtos) {

		Long totalTargetQuantityOfPurchaseOrder = 0L;

		for (PurchaseOrderQuantityResponseDto purchaseOrderQuantityResponseDto : purchaseOrderQuantityResponseDtos) {
			totalTargetQuantityOfPurchaseOrder += purchaseOrderQuantityResponseDto.getTargetQuantity();
		}

		return totalTargetQuantityOfPurchaseOrder;
	}

	@Override
	public PurchaseOrderStatusCountDto getPurchaseOrderStatusCounts(Boolean status) {

		PurchaseOrderStatusCountDto purchaseOrderStatusCounts = PurchaseOrderStatusCountDto.builder().build();

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<PurchaseOrder> purchaseOrderEntities = Objects.isNull(status) ? purchaseOrderDbService.getAllPurchaseOrders(factoryId)
				: purchaseOrderDbService.getAllPurchaseOrdersForStatus(status, factoryId);

		if (CollectionUtils.isNotEmpty(purchaseOrderEntities)) {

			Map<PurchaseOrderStatus, Long> countMap = purchaseOrderEntities.stream().collect(Collectors.groupingBy(PurchaseOrder::getPurchaseOrderStatus, Collectors.counting()));

			log.info("Found purchase order status wise counts map: {} for factory id: {}", countMap, factoryId);

			purchaseOrderStatusCounts.setPurchaseOrderStatusCount(countMap);

		}

		return purchaseOrderStatusCounts;
	}

	private Specification<PurchaseOrder> getSpecificationForPurchaseOrderListing(PurchaseOrderFilterDto purchaseOrderFilterDto, PurchaseOrderStatus purchaseOrderStatus) {
		GroyyoSpecificationBuilder<PurchaseOrder> groyyoSpecificationBuilder = new GroyyoSpecificationBuilder<PurchaseOrder>();

		groyyoSpecificationBuilder.with(FilterConstants.STATUS, CriteriaOperation.TRUE, Boolean.TRUE);

		if (Objects.nonNull(purchaseOrderStatus))
			groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_STATUS, CriteriaOperation.ENUM_EQ, purchaseOrderStatus);

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		if (StringUtils.isNotBlank(factoryId))
			groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_FACTORY_ID, CriteriaOperation.EQ, factoryId);

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
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_NUMBER, CriteriaOperation.ILIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getPurchaseOrderNumber() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getFabricName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_FABRIC_NAME, CriteriaOperation.ILIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getFabricName() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getBuyerName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_BUYER_NAME, CriteriaOperation.ILIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getBuyerName() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getStyleNumber()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_STYLE_NUMBER, CriteriaOperation.ILIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getStyleNumber() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getStyleName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_STYLE_NAME, CriteriaOperation.ILIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getStyleName() + SymbolConstants.SYMBOL_PERCENT);

			if (StringUtils.isNotBlank(purchaseOrderFilterDto.getProductName()))
				groyyoSpecificationBuilder.with(FilterConstants.PurchaseOrderFilterConstants.PURCHASE_ORDER_PRODUCT_NAME, CriteriaOperation.ILIKE,
						SymbolConstants.SYMBOL_PERCENT + purchaseOrderFilterDto.getProductName() + SymbolConstants.SYMBOL_PERCENT);

		}

		return groyyoSpecificationBuilder.build();
	}

	@SuppressWarnings("unused")
	private void addExternalSpecificationsForLikeSearch(String fieldName, String fieldValue, GroyyoSpecificationBuilder<PurchaseOrder> groyyoSpecificationBuilder) {

		Specification<PurchaseOrder> specification = new GroyyoSpecificationBuilder<PurchaseOrder>()
				.with(fieldName, CriteriaOperation.ILIKE, SymbolConstants.SYMBOL_PERCENT + fieldValue.toLowerCase() + SymbolConstants.SYMBOL_PERCENT)
				.build();

		groyyoSpecificationBuilder.addExternalSpecification(specification);
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

		if (desiredPurchaseOrderStatus.getSequenceId() >= currentPurchaseOrderStatus.getSequenceId()) {
			log.info("Changing the status of purchase order from current status: {} to desired status: {}", currentPurchaseOrderStatus, desiredPurchaseOrderStatus);
			purchaseOrder.setPurchaseOrderStatus(desiredPurchaseOrderStatus);
		}

		if (desiredPurchaseOrderStatus.getSequenceId() < currentPurchaseOrderStatus.getSequenceId()) {
			log.info("Cannot move the status to a level: {} lower than the current level: {}", desiredPurchaseOrderStatus.getSequenceId(), currentPurchaseOrderStatus.getSequenceId());

			if (forceUpdate) {
				log.info("Changing the status of purchase order forcefully from current status: {} to desired status: {}", currentPurchaseOrderStatus, desiredPurchaseOrderStatus);
				purchaseOrder.setPurchaseOrderStatus(desiredPurchaseOrderStatus);
			}
		}

		purchaseOrder.setUpdatedAt(new Date());
		purchaseOrderDbService.saveAndFlush(purchaseOrder);
	}

	@Override
	public void markPurchaseOrderCompleteAndRemoveAssignments(String purchaseOrderId) {

		changeStatusOfPurchaseOrder(purchaseOrderId, PurchaseOrderStatus.COMPLETED, Boolean.TRUE);

		lineCheckerAssignmentService.disableLineAssignmentsOnOrderCompletion(purchaseOrderId);
	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && purchaseOrderDbService.isEntityExistsByName(name);
	}

	private boolean isEntityExistsWithNameAndFactoryId(String name) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		return StringUtils.isNotBlank(name) && purchaseOrderDbService.isEntityExistsByNameAndFactoryId(name, factoryId);
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

	private List<PurchaseOrderResponseDto> addBulkPurchaseOrder(List<BulkPurchaseOrderRequestDto> purchaseOrderRequestsDto) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		List<PurchaseOrderResponseDto> purchaseOrderResponses = new ArrayList<>();
		purchaseOrderRequestsDto.forEach(purchaseOrderRequestDto -> {
			List<PurchaseOrderQuantity> purchaseOrderQuantities = new ArrayList<>();
			List<Size> sizes = new ArrayList<>();

			Product product = productService.findOrCreate(purchaseOrderRequestDto.getProductName());
			Buyer buyer = buyerService.findOrCreate(purchaseOrderRequestDto.getBuyerName());
			Season season = seasonService.findOrCreate(purchaseOrderRequestDto.getSeasonName());
			Fit fit = fitService.findOrCreate(purchaseOrderRequestDto.getFitName());
			Part part = partService.findOrCreate(purchaseOrderRequestDto.getPart().getName());
			Style style = styleService.findOrCreate(purchaseOrderRequestDto.getStyleName(), purchaseOrderRequestDto.getStyleNumber(), product);
			purchaseOrderRequestDto.getPart().getSizes().forEach(name -> sizes.add(sizeService.findOrCreate(name)));
			SizeGroup sizeGroup = sizeGroupService.findOrCreate(purchaseOrderRequestDto.getPart().getSizeGroup(), sizes);

			PurchaseOrder purchaseOrder = PurchaseOrder
					.builder()
					.name(purchaseOrderRequestDto.getName())
					.purchaseOrderStatus(PurchaseOrderStatus.YET_TO_START)
					.styleId(Objects.nonNull(style) ? style.getUuid() : null)
					.styleNumber(Objects.nonNull(style) ? style.getStyleNumber() : null)
					.styleName(Objects.nonNull(style) ? style.getName() : null)
					.fabricId("")
					.fabricName("")
					.buyerId(buyer.getUuid())
					.buyerName(buyer.getName())
					.tolerance(purchaseOrderRequestDto.getPart().getTolerance())
					.receiveDate(new Date())
					.exFtyDate(purchaseOrderRequestDto.getExFtyDate())
					.seasonId(season.getUuid())
					.seasonName(season.getName())
					.fitId(fit.getUuid())
					.fitName(fit.getName())
					.partId(part.getUuid())
					.partName(part.getName())
					.productId(Objects.nonNull(product) ? product.getUuid() : null)
					.productName(Objects.nonNull(product) ? product.getName() : null)
					.factoryId(factoryId)
					.build();
			purchaseOrder = purchaseOrderDbService.savePurchaseOrder(purchaseOrder);
			PurchaseOrder finalPurchaseOrder = purchaseOrder;
			purchaseOrderRequestDto.getPart().getColors().forEach(colorData -> {
				// temporary color hex code generation
				Random random = new Random();
				java.awt.Color colour = new java.awt.Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
				String colorCode = String.format("#%02x%02x%02x", colour.getRed(), colour.getGreen(), colour.getBlue());
				Color color = colorService.findOrCreate(colorData.getName(), colorCode);
				colorData.getSizes().forEach((k, v) -> {
					Long targetQuantity = Objects.nonNull(v) ? (long) (v + (v * purchaseOrderRequestDto.getPart().getTolerance()) / 100) : 0L;
					Size size;
					Optional<Size> result = sizes.stream()
							.filter(obj -> obj.getName().equals(k))
							.findFirst();

					if (result.isPresent()) {
						size = result.get();
					} else {
						throw new InputMismatchException("Size should be from size group");
					}
					purchaseOrderQuantities.add(
							PurchaseOrderQuantity
									.builder()
									.name(finalPurchaseOrder.getUuid())
									.purchaseOrderId(finalPurchaseOrder.getUuid())
									.sizeId(size.getUuid())
									.sizeName(size.getName())
									.sizeGroupId(sizeGroup.getUuid())
									.sizeGroupName(sizeGroup.getName())
									.colourId(color.getUuid())
									.colourName(color.getName())
									.quantity(v)
									.targetQuantity(targetQuantity)
									.factoryId(factoryId)
									.build());
				});
			});
			purchaseOrderQuantityService.addBulkPurchaseOrderQuantityWithEntity(purchaseOrderQuantities);
			if (Objects.isNull(purchaseOrder)) {
				log.error("Unable to add purchaseOrder for object: {}", purchaseOrderRequestDto);
			}
			purchaseOrderResponses.add(buildPurchaseOrderResponseWithQuantitiesAndAssignments(purchaseOrder));
		});
		return purchaseOrderResponses;
	}

	private List<BulkPurchaseOrderRequestDto> parseBulkOrderExcelData(List<BulkOrderExcelRequestDto> bulkOrderExcelList) {

		HashMap<String, String> poHash = new HashMap<>();
		HashMap<String, String> errorMessages = new HashMap<>();
		List<BulkPurchaseOrderRequestDto> bulkPurchaseOrderRequestData = new ArrayList<>();

		for (int i = 0; i < bulkOrderExcelList.size(); i++) {
			MapperUtils.getTrimmedDto(bulkOrderExcelList.get(i));
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getPurchaseOrderNumber())) {
				errorMessages.put("Row Number: " + i + " purchaseOrderNumber", "Purchase Order Number cannot be blank.");
			}
			if (isEntityExistsWithNameAndFactoryId(bulkOrderExcelList.get(i).getPurchaseOrderNumber())) {
				errorMessages.put("Row Number: " + i + " purchaseOrderNumber ", " " + bulkOrderExcelList.get(i).getPurchaseOrderNumber() + " is already present in the factory.");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getStyleName())) {
				errorMessages.put("Row Number: " + i + " styleName", " Please enter a valid style name");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getStyleNumber())) {
				errorMessages.put("Row Number: " + i + " styleNumber", "Please enter a valid style number");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getProductName())) {
				errorMessages.put("Row Number: " + i + " productName", "Please enter a valid product name");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getBuyerName())) {
				errorMessages.put("Row Number: " + i + " buyerName", "Please enter a valid buyer name");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getSeasonName())) {
				errorMessages.put("Row Number: " + i + " seasonName", "Please enter a valid season name");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getFitName())) {
				errorMessages.put("Row Number: " + i + " fitName", "Please enter a valid fit name");
			}
			if (StringUtils.isBlank(bulkOrderExcelList.get(i).getPart())) {
				errorMessages.put("Row Number: " + i + " part", "Please enter a valid part name");
			}
			validateDateFormat(bulkOrderExcelList, i);

			String hash = bulkOrderExcelList.get(i).getStyleNumber() + "$$$" + bulkOrderExcelList.get(i).getStyleName() + "$$$" + bulkOrderExcelList.get(i).getProductName() + "$$$"
					+ bulkOrderExcelList.get(i).getBuyerName() + "$$$" + bulkOrderExcelList.get(i).getSeasonName() + "$$$" + bulkOrderExcelList.get(i).getFitName() + "$$$"
					+ bulkOrderExcelList.get(i).getExFtyDate() + "$$$" + bulkOrderExcelList.get(i).getPart() + "$$$" + bulkOrderExcelList.get(i).getVariance();
			if (poHash.get(bulkOrderExcelList.get(i).getPurchaseOrderNumber()) == null) {
				poHash.put(bulkOrderExcelList.get(i).getPurchaseOrderNumber(), hash);
				bulkPurchaseOrderRequestData.add(BuilderUtils.buildBulkPOFromExcel(bulkOrderExcelList.get(i)));
			} else if (!StringUtils.equals(poHash.get(bulkOrderExcelList.get(i).getPurchaseOrderNumber()), hash)) {
				errorMessages.put("purchaseOrder", "For one po there should be same Style Number/Style Name/Product Name/Buyer/Season/Fit/Ex-Fty Date/Parts/Variance");
			} else {
				int finalI = i;
				bulkPurchaseOrderRequestData.forEach(x -> {
					if (StringUtils.equals(x.getName(), bulkOrderExcelList.get(finalI).getPurchaseOrderNumber())) {
						if (x.getPart().getColors().stream().anyMatch(y -> y.getName().equals(bulkOrderExcelList.get(finalI).getColor()))) {
							errorMessages.put("Row Number: " + finalI + " color ", " same color provided 2 times for a po");
						}
						x.getPart().getColors().add(new BulkColorRequestDto(bulkOrderExcelList.get(finalI).getColor(), bulkOrderExcelList.get(finalI).getSizes()));
					}
				});
			}
		}
		if (!errorMessages.isEmpty()) {
			throw new InputMismatchException(errorMessages.toString());
		}
		return bulkPurchaseOrderRequestData;
	}

	@Override
	public List<PurchaseOrderResponseDto> createBulkOrderFromExcel(List<BulkOrderExcelRequestDto> bulkOrderExcelRequestsDto) {
		List<BulkPurchaseOrderRequestDto> bulkPurchaseOrderRequestsDto = parseBulkOrderExcelData(bulkOrderExcelRequestsDto);
		return addBulkPurchaseOrder(bulkPurchaseOrderRequestsDto);
	}

	private static void validateDateFormat(List<BulkOrderExcelRequestDto> bulkOrderExcelList, int i) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate.parse(bulkOrderExcelList.get(i).getExFtyDate(), formatter);
		} catch (DateTimeParseException e) {
			throw new InputMismatchException("Row Number: " + i + " Invalid ex Fty date " + bulkOrderExcelList.get(i).getExFtyDate());
		}
	}

	@Override
	public Boolean existsByNameAndFactoryId(String purchaseOrderNumber, String factoryId) {

		return purchaseOrderDbService.existsByNameAndFactoryId(purchaseOrderNumber, factoryId);
	}

	@Override
	public void publishPurchaseOrderPackets(List<String> purchaseOrderIds) {

		for (String purchaseOrderId : purchaseOrderIds) {

			try {

				log.info("Running iteration to publish qc task assignments for purchase order id: {}", purchaseOrderId);

				lineCheckerAssignmentService.publishQcTaskAssignment(purchaseOrderId);

			} catch (Exception e) {

				log.error("Exception caught while publishing purchase order packet for purchase order id: {}", purchaseOrderId, e);

			}
		}

	}
}
