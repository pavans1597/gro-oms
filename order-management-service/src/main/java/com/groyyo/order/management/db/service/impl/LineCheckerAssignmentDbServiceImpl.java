package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.repository.LineCheckerAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineCheckerAssignmentDbServiceImpl extends AbstractJpaServiceImpl<LineCheckerAssignment, Long, LineCheckerAssignmentRepository> implements LineCheckerAssignmentDbService {

	@Autowired
	private LineCheckerAssignmentRepository lineCheckerAssignmentRepository;

	@Override
	protected LineCheckerAssignmentRepository getJpaRepository() {
		return lineCheckerAssignmentRepository;
	}

	@Override
	public List<LineCheckerAssignment> getAllLineCheckerAssignments() {
		return lineCheckerAssignmentRepository.findAll();
	}

	@Override
	public List<LineCheckerAssignment> getAllLineCheckerAssignmentsForStatus(boolean status) {
		return lineCheckerAssignmentRepository.findByStatus(status);
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
	public List<LineCheckerAssignment> getLineCheckerAssignmentForPurchaseOrder(String purchaseOrderId) {

		return lineCheckerAssignmentRepository.findAllByPurchaseOrderId(purchaseOrderId);
	}

	@Override
	public Long countLineCheckerByfactoryId(String factoryId, LineType lineType,boolean status) {
		return lineCheckerAssignmentRepository.countByFactoryIdAndLineTypeAndStatus(factoryId,lineType,status);
	}
}
