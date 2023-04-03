/**
 * 
 */
package com.groyyo.order.management.http.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.fileManagement.dto.response.FileResponseDto;
import com.groyyo.core.enums.ServiceName;
import com.groyyo.core.file.management.client.api.FileManagementApi;
import com.groyyo.order.management.http.service.FileManagementHttpService;

import lombok.extern.log4j.Log4j2;

/**
 * @author nipunaggarwal
 *
 */
@Log4j2
@Service
public class FileManagementHttpServiceImpl implements FileManagementHttpService {

	@Autowired
	private FileManagementApi fileManagementApi;

	@Override
	public ResponseDto<FileResponseDto> getStyleImage(String imageUuid) {

		ResponseDto<FileResponseDto> styleImage = null;

		try {

			styleImage = fileManagementApi.getInternalSignedUrl(imageUuid, ServiceName.ORDER, Boolean.TRUE);

		} catch (Exception e) {

			log.error("Exception caught while getting style image from FMS for uuid: {}", imageUuid, e);
		}

		return styleImage;
	}

}
