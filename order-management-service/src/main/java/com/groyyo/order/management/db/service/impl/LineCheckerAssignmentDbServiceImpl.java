package com.groyyo.order.management.db.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.dto.userservice.LineType;
import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.repository.LineCheckerAssignmentRepository;

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
		return (Objects.nonNull(factoryId) ? lineCheckerAssignmentRepository.findAllByFactoryId(factoryId) : lineCheckerAssignmentRepository.findAll());
	}

	@Override
	public List<LineCheckerAssignment> getAllLineCheckerAssignmentsForStatus(boolean status, String factoryId) {

		return (Objects.nonNull(factoryId) ? lineCheckerAssignmentRepository.findByStatusAndFactoryId(status, factoryId)
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
	public List<LineCheckerAssignment> activateDeactivateLineCheckerAssignments(List<LineCheckerAssignment> lineCheckerAssignments, boolean status) {

		for (LineCheckerAssignment lineCheckerAssignment : lineCheckerAssignments) {
			lineCheckerAssignment.setStatus(status);
		}

		return lineCheckerAssignmentRepository.saveAllAndFlush(lineCheckerAssignments);
	}

	@Override
	public List<LineCheckerAssignment> saveAllLineCheckerAssignemnt(List<LineCheckerAssignment> lineCheckerAssignments) {

		return lineCheckerAssignmentRepository.saveAllAndFlush(lineCheckerAssignments);
	}

	@Override
	public List<LineCheckerAssignment> getLineCheckerAssignmentForPurchaseOrder(String purchaseOrderId, String factoryId) {
		return (!Objects.isNull(factoryId) ? lineCheckerAssignmentRepository.findAllByPurchaseOrderIdAndFactoryId(purchaseOrderId, factoryId)
				: lineCheckerAssignmentRepository.findAllByPurchaseOrderId(purchaseOrderId));
	}

	@Override
	public long countLineCheckerByFactoryId(String factoryId, LineType lineType, boolean status) {
		ArrayList<LineCheckerAssignment> userIdByFactoryIdAndLineTypeAndStatus = lineCheckerAssignmentRepository.findUserIdByFactoryIdAndLineTypeAndStatus(factoryId, lineType, status);
		Set<String> collect = userIdByFactoryIdAndLineTypeAndStatus.stream().map(LineCheckerAssignment::getUserId).collect(Collectors.toSet());
		return collect.size();
	}
}
