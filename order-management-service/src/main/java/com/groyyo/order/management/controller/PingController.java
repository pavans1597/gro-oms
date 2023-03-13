/**
 * 
 */
package com.groyyo.order.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groyyo.core.base.common.dto.ResponseDto;

import lombok.extern.log4j.Log4j2;

/**
 * @author nipunaggarwal
 *
 */
@Log4j2
@RestController
@RequestMapping("ping")
public class PingController {

	@GetMapping("")
	public ResponseDto<Void> ping() {
		log.info("Pinging the project");

		return ResponseDto.success("Project is UP !!");
	}
}
