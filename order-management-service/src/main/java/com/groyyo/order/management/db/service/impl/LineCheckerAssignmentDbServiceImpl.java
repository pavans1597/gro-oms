package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.repository.LineCheckerAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LineCheckerAssignmentDbServiceImpl extends AbstractJpaServiceImpl<LineCheckerAssignment, Long, LineCheckerAssignmentRepository> implements LineCheckerAssignmentDbService {

	@Autowired
	private LineCheckerAssignmentRepository lineCheckerAssignmentRepository;

	@Override
	protected LineCheckerAssignmentRepository getJpaRepository() {
		return lineCheckerAssignmentRepository;
	}

	@Override
	public List<LineCheckerAssignment> getAllLineCheckerAssignments(String factoryId) {
		return (!Objects.isNull(factoryId)?
				lineCheckerAssignmentRepository.findAllByFactoryId(factoryId)
				: lineCheckerAssignmentRepository.findAll());
	}

	@Override
	public List<LineCheckerAssignment> getAllLineCheckerAssignmentsForStatus(boolean status,String factoryId) {

		return (!Objects.isNull(factoryId)?
				lineCheckerAssignmentRepository.findByStatusAndFactoryId(status,factoryId)
				: lineCheckerAssignmentRepository.findByStatus(status));
	}

	@Override
	public LineCheckerAssignment getLineCheckerAssignmentById(String id) {
		return lineCheckerAssignmentRepository.findByUuid(id);
	}

	@Override
	public LineCheckerAssignment saveLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment) {
		return lineCheckerAssignmentRepository.saveAndFlush(lineCheckerAssignment);
	}

	@Override
	public LineCheckerAssignment activateDeactivateLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment, boolean status) {
		lineCheckerAssignment.setStatus(status);
		return lineCheckerAssignmentRepository.saveAndFlush(lineCheckerAssignment);
	}

	@Override
	public List<LineCheckerAssignment> saveAllLineCheckerAssignemnt(List<LineCheckerAssignment> lineCheckerAssignments) {

		return lineCheckerAssignmentRepository.saveAllAndFlush(lineCheckerAssignments);
	}

	@Override
	public List<LineCheckerAssignment> getLineCheckerAssignmentForPurchaseOrder(String purchaseOrderId,String factoryId) {
		return (!Objects.isNull(factoryId)?
				lineCheckerAssignmentRepository.findAllByPurchaseOrderIdAndFactoryId(purchaseOrderId,factoryId)
				: lineCheckerAssignmentRepository.findAllByPurchaseOrderId(purchaseOrderId));
	}

	@Override
	public Long countLineCheckerByfactoryId(String factoryId, LineType lineType,boolean status) {
		return (!Objects.isNull(factoryId)?
				lineCheckerAssignmentRepository.countByFactoryIdAndLineTypeAndStatus(factoryId,lineType,status)
				: lineCheckerAssignmentRepository.countByLineTypeAndStatus(lineType,status));
	}
}
