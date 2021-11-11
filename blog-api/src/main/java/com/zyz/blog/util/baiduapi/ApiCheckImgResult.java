package com.zyz.blog.util.baiduapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
@NoArgsConstructor
@Data
public class ApiCheckImgResult {

	@JsonProperty("log_id")
	private Long logId;
	@JsonProperty("conclusion")
	private String conclusion;
	@JsonProperty("conclusionType")
	private Integer conclusionType;
	@JsonProperty("data")
	private List<DataDTO> data;
	@JsonProperty("rawData")
	private List<RawDataDTO> rawData;
	@NoArgsConstructor
	@Data
	public static class DataDTO {
		@JsonProperty("type")
		private Integer type;
		@JsonProperty("subType")
		private Integer subType;
		@JsonProperty("conclusion")
		private String conclusion;
		@JsonProperty("conclusionType")
		private Integer conclusionType;
		@JsonProperty("msg")
		private String msg;
		@JsonProperty("probability")
		private Double probability;
		@JsonProperty("codes")
		private List<String> codes;
		@JsonProperty("stars")
		private List<StarsDTO> stars;
		@JsonProperty("datasetName")
		private String datasetName;
		@JsonProperty("completeness")
		private Double completeness;
		@JsonProperty("hits")
		private List<HitsDTO> hits;
		@JsonProperty("conclution")
		private String conclution;
		@JsonProperty("conclutionType")
		private Integer conclutionType;

		@NoArgsConstructor
		@Data
		public static class StarsDTO {
			@JsonProperty("probability")
			private Double probability;
			@JsonProperty("name")
			private String name;
		}

		@NoArgsConstructor
		@Data
		public static class HitsDTO {
			@JsonProperty("probability")
			private Integer probability;
			@JsonProperty("datasetName")
			private String datasetName;
			@JsonProperty("words")
			private List<String> words;
		}
	}

	@NoArgsConstructor
	@Data
	public static class RawDataDTO {
		@JsonProperty("type")
		private Integer type;
		@JsonProperty("results")
		private List<ResultsDTO> results;

		@NoArgsConstructor
		@Data
		public static class ResultsDTO {
			@JsonProperty("result")
			private List<ResultDTO> result;
			@JsonProperty("log_id")
			private Long logId;
			@JsonProperty("include_politician")
			private String includePolitician;
			@JsonProperty("result_num")
			private Integer resultNum;
			@JsonProperty("result_confidence")
			private String resultConfidence;

			@NoArgsConstructor
			@Data
			public static class ResultDTO {
				@JsonProperty("location")
				private LocationDTO location;
				@JsonProperty("stars")
				private List<StarsDTO> stars;

				@NoArgsConstructor
				@Data
				public static class LocationDTO {
					@JsonProperty("top")
					private Integer top;
					@JsonProperty("left")
					private Integer left;
					@JsonProperty("width")
					private Integer width;
					@JsonProperty("height")
					private Integer height;
				}

				@NoArgsConstructor
				@Data
				public static class StarsDTO {
					@JsonProperty("probability")
					private Double probability;
					@JsonProperty("name")
					private String name;
				}
			}
		}
	}
}
