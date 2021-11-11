package com.zyz.blogadmin.vo.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyz
 * @version 1.0
 */
@Data
public class LogPageParams {
	private Integer currentPage;
	private Integer pageSize;
	private String createStartDate;
	private String createEndDate;
}
