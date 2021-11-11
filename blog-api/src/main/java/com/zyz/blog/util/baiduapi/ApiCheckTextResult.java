package com.zyz.blog.util.baiduapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zyz
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class ApiCheckTextResult {

	@JsonProperty("conclusion")
	private String conclusion;
	@JsonProperty("log_id")
	private Long logId;
	@JsonProperty("isHitMd5")
	private Boolean isHitMd5;
	@JsonProperty("conclusionType")
	private Integer conclusionType;
}
