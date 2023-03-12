package com.groyyo.order.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.exception.NoRecordException;
import com.groyyo.core.base.exception.RecordExistsException;
import com.groyyo.core.base.http.utils.HeaderUtil;
import com.groyyo.core.kafka.dto.KafkaDTO;
import com.groyyo.core.kafka.producer.NotificationProducer;
import com.groyyo.order.management.adapter.BuyerAdapter;
import com.groyyo.order.management.db.service.BuyerDbService;
import com.groyyo.order.management.dto.request.BuyerRequestDto;
import com.groyyo.order.management.dto.response.BuyerResponseDto;
import com.groyyo.order.management.entity.Buyer;
import com.groyyo.order.management.service.BuyerService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BuyerServiceImpl implements BuyerService {

	@Value("${kafka.base.topic}")
	private String kafkaMasterDataUpdatesTopic;

	@Autowired
	private NotificationProducer notificationProducer;

	@Autowired
	private BuyerDbService buyerDbService;

	@Override
	public List<BuyerResponseDto> getAllBuyers(Boolean status) {

		log.info("Serving request to get all buyers");

		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		List<Buyer> buyerEntities = Objects.isNull(status) ? buyerDbService.getAllBuyers(factoryId)
				: buyerDbService.getAllBuyersForStatus(status, factoryId);

		if (CollectionUtils.isEmpty(buyerEntities)) {
			log.error("No Buyers found in the system");
			return new ArrayList<BuyerResponseDto>();
		}

		return BuyerAdapter.buildResponsesFromEntities(buyerEntities);
	}

	@Override
	public BuyerResponseDto getBuyerById(String id) {

		log.info("Serving request to get a buyer by id:{}", id);

		Buyer buyer = buyerDbService.getBuyerById(id);

		if (Objects.isNull(buyer)) {
			String errorMsg = "Buyer with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		return BuyerAdapter.buildResponseFromEntity(buyer);
	}

	@Override
	public BuyerResponseDto addBuyer(BuyerRequestDto buyerRequestDto) {

		log.info("Serving request to add a buyer with request object:{}", buyerRequestDto);

		runValidations(buyerRequestDto);
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();
		Buyer buyer = BuyerAdapter.buildBuyerFromRequest(buyerRequestDto, factoryId);

		buyer = buyerDbService.saveBuyer(buyer);

		if (Objects.isNull(buyer)) {
			log.error("Unable to add buyer for object: {}", buyerRequestDto);
			return null;
		}

		// publishBuyer(buyerResponseDto, KafkaConstants.KAFKA_SIZE_TYPE,
		// KafkaConstants.KAFKA_SIZE_SUBTYPE_CREATE, kafkaMasterDataUpdatesTopic);

		return BuyerAdapter.buildResponseFromEntity(buyer);
	}

	@Override
	public BuyerResponseDto updateBuyer(BuyerRequestDto buyerRequestDto) {

		log.info("Serving request to update a buyer with request object:{}", buyerRequestDto);

		Buyer buyer = buyerDbService.getBuyerById(buyerRequestDto.getId());

		if (Objects.isNull(buyer)) {
			log.error("Buyer with id: {} not found in the system", buyerRequestDto.getId());
			return null;
		}

		runValidations(buyerRequestDto);

		buyer = BuyerAdapter.cloneBuyerWithRequest(buyerRequestDto, buyer);

		buyerDbService.saveBuyer(buyer);

		BuyerResponseDto buyerResponseDto = BuyerAdapter.buildResponseFromEntity(buyer);

//        publishBuyer(buyerResponseDto, KafkaConstants.KAFKA_SIZE_TYPE, KafkaConstants.KAFKA_SIZE_SUBTYPE_UPDATE, kafkaMasterDataUpdatesTopic);

		return buyerResponseDto;
	}

	@Override
	public BuyerResponseDto activateDeactivateBuyer(String id, boolean status) {

		log.info("Serving request to activate / deactivate a buyer with id:{}", id);

		Buyer buyer = buyerDbService.getBuyerById(id);

		if (Objects.isNull(buyer)) {
			String errorMsg = "Buyer with id: " + id + " not found in the system ";
			throw new NoRecordException(errorMsg);
		}

		buyer = buyerDbService.activateDeactivateBuyer(buyer, status);

		return BuyerAdapter.buildResponseFromEntity(buyer);
	}

	@SuppressWarnings("unused")
	private void publishBuyer(BuyerResponseDto buyerResponseDto, String type, String subType, String topicName) {

		KafkaDTO kafkaDTO = new KafkaDTO(type, subType, BuyerResponseDto.class.getName(), buyerResponseDto);
		notificationProducer.publish(topicName, kafkaDTO.getClassName(), kafkaDTO);
	}

	@Override
	public void consumeBuyer(BuyerResponseDto buyerResponseDto) {
		String factoryId = HeaderUtil.getFactoryIdHeaderValue();

		Buyer buyer = BuyerAdapter.buildBuyerFromResponse(buyerResponseDto, factoryId);

		if (Objects.isNull(buyer)) {
			log.error("Unable to build buyer from response object: {}", buyerResponseDto);
			return;
		}

		buyerDbService.saveBuyer(buyer);
	}

	private boolean isEntityExistsWithName(String name) {

		return StringUtils.isNotBlank(name) && buyerDbService.isEntityExistsByName(name);
	}

	private void runValidations(BuyerRequestDto buyerRequestDto) {

		validateName(buyerRequestDto);
	}

	private void validateName(BuyerRequestDto buyerRequestDto) {

		if (isEntityExistsWithName(buyerRequestDto.getName())) {
			String errorMsg = "Buyer cannot be created/updated as record already exists with name: " + buyerRequestDto.getName();
			throw new RecordExistsException(errorMsg);
		}
	}
}
