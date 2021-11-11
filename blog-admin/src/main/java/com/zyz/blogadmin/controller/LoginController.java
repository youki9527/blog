package com.zyz.blogadmin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyz
 * @version 1.0
 */
@Controller
@RequestMapping
public class LoginController {

	@RequestMapping("/loginPage")
	public String login() {
		return "login";
	}
}

