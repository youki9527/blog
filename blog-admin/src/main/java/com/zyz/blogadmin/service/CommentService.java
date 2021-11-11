package com.zyz.blogadmin.service;

import com.zyz.blogadmin.vo.Result;
import com.zyz.blogadmin.vo.params.CommentPageParams;

import java.util.List;

/**
 * @author zyz
 * @version 1.0
 */
public interface CommentService {

	Result listComment(CommentPageParams commentPageParams);


	Result deleteById(Long id);

	Result deleteCommentList(List<Long> ids);
}
