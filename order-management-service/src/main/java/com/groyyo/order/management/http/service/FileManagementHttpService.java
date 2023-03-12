/**
 * 
 */
package com.groyyo.order.management.http.service;

import com.groyyo.core.base.common.dto.ResponseDto;
import com.groyyo.core.dto.fileManagement.dto.response.FileResponseDto;

/**
 * @author nipunaggarwal
 *
 */
public interface FileManagementHttpService {

	/**
	 * @param imageUuid
	 * @return
	 */
	ResponseDto<FileResponseDto> getStyleImage(String imageUuid);

}
