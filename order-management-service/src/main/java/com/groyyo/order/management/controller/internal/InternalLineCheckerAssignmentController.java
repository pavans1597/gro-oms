package com.groyyo.order.management.controller.internal;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.user.dto.response.LineUserResponseDto;
import com.groyyo.order.management.service.LineCheckerAssignmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("internal/line/assignment")
public class InternalLineCheckerAssignmentController {
    @Autowired
    private LineCheckerAssignmentService lineCheckerAssignmentService;

    @GetMapping("fetch/users")
    public ResponseDto<List<LineUserResponseDto>> getUsers(@RequestParam(value = "factoryId", required = true) String factoryId,
                                                           @RequestParam(value = "userIds", required = true) List<String> userIds) {
        log.info("Request received to fetch users for factoryId: {} and userIds: {}", factoryId, userIds);

        List<LineUserResponseDto> lineUserResponseDto = lineCheckerAssignmentService.getUsers(factoryId, userIds);

        return ResponseDto.success("Number of users found: " + lineUserResponseDto.size(), lineUserResponseDto);
    }
}
