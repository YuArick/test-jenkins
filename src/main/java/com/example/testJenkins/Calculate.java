package com.example.testJenkins;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Calculate {
	@RequestMapping("/")
	public String calculate() {
		return "index";
	}

	@RequestMapping("/method1")
	@ResponseBody
	public String method1(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) + Integer.parseInt((String) params.get("b")) + "";
		return ans;
	}
	@RequestMapping("/method2")
	@ResponseBody
	public String method2(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) - Integer.parseInt((String) params.get("b")) + "";
		return ans+1;
	}
	@RequestMapping("/method3")
	@ResponseBody
	public String method3(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) * Integer.parseInt((String) params.get("b")) + "";
		return ans;
	}
	@RequestMapping("/method4")
	@ResponseBody
	public String method4(@RequestBody Map<String, Object> params) {
		String ans = Integer.parseInt((String) params.get("a")) / Integer.parseInt((String) params.get("b")) + "";
		return ans;
	}
}
