package com.groyyo.order.management.service.impl;

import com.groyyo.order.management.adapter.LineCheckerAdapter;
import com.groyyo.order.management.db.service.LineCheckerAssignmentDbService;
import com.groyyo.order.management.dto.request.LineAssignment;
import com.groyyo.order.management.dto.request.LineAssignmentRequestDto;
import com.groyyo.order.management.entity.LineCheckerAssignment;
import com.groyyo.order.management.service.LineCheckerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class LineCheckerServiceImpl implements LineCheckerService {
//	@Autowired
//	UserClientApi userClientApi ;

	@Autowired
	private LineCheckerAssignmentDbService lineCheckerAssignmentDbService;

//	@Override
//	public List<UserResponseDto> getLineUsers(String factoryId, String departmentId, String searchKeyWord, LineType lineType) {
//		try {
//			return  userClientApi.getLineUsers(factoryId,departmentId,,lineType);
//		} catch (Exception e) {
//			log.error("exception occured while calling getLineUsers service ");
//		}
//		return null;
//	}

//
//
//	@Override
//	public List<LineResponseDto> getLines(String factoryId, LineType lineType) {
//		try {
//			return  userClientApi.getLines(factoryId,lineType);
//		} catch (Exception e) {
//			log.error("exception occured while calling getLines service ");
//		}
//		return null;
//	}

	@Override
	public List<LineCheckerAssignment> lineCheckerAssignment(LineAssignmentRequestDto lineAssignmentRequestDto, String factoryId) {
		try {

			String purchaseOrderId = lineAssignmentRequestDto.getPurchaseOrderId();
			String salesOrderId = lineAssignmentRequestDto.getSalesOrderId();
			List<LineAssignment> assignments = lineAssignmentRequestDto.getAssignment();

			List<LineCheckerAssignment>  lineCheckerAssignments = new ArrayList<>();
			for (LineAssignment  lineAssignment : assignments ) {
				LineCheckerAssignment lineCheckerAssignment = LineCheckerAdapter.buildLineCheckerAssignmentFromRequest(lineAssignment, purchaseOrderId, salesOrderId, factoryId);
				lineCheckerAssignments.add(lineCheckerAssignment);
			}
			 return lineCheckerAssignmentDbService.saveAllLineCheckerAssignemnt(lineCheckerAssignments);

		} catch (Exception e) {
			log.error("exception occured while Line Assignment  ");
		}
		return null;
	}

}
