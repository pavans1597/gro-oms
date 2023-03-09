package com.groyyo.order.management.db.service;

import com.groyyo.order.management.entity.LineCheckerAssignment;

import java.util.List;

public interface LineCheckerAssignmentDbService {


    List<LineCheckerAssignment> getAllLineCheckerAssignments();

    List<LineCheckerAssignment> getAllLineCheckerAssignmentsForStatus(boolean status);

    LineCheckerAssignment getLineCheckerAssignmentById(String id);

    LineCheckerAssignment saveLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment);

    LineCheckerAssignment activateDeactivateLineCheckerAssignment(LineCheckerAssignment lineCheckerAssignment, boolean status);


    List<LineCheckerAssignment> saveAllLineCheckerAssignemnt(List<LineCheckerAssignment> lineCheckerAssignments);
}
