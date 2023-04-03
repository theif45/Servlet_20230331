package com.study.servlet.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.study.servlet.dto.RequestDto;
import com.study.servlet.dto.ResponseDto;
import com.study.servlet.entity.User;
import com.study.servlet.service.UserService;
import com.study.servlet.service.UserServiceImpl;


@WebServlet("/auth")
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserService userService;
	
	private Gson gson;
	
	public Authentication() {
//		내용을 갈아끼워 사용 가능
//		필요한 메소드는 인터페이스에서 추가
		userService = UserServiceImpl.getInstance();
		gson = new Gson();
	}

//	요청 데이터: 쿼리파람, 응답 데이터: JSON(User)
//	UserList에서 username을 통해 사용자 정보 찾기
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		
	}
//	요청 데이터: JSON(User), 응답 데이터: {"success": "true"}
//	JSON을 User객체로 변환 후 ToString으로 출력
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = RequestDto.<User>convertRequestBody(request, User.class);
		
		boolean duplicatedFlag = userService.duplicatedUsername(user.getUsername());
		
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		if(duplicatedFlag) {
			// true == 중복, flase == 가입가능
			ResponseDto<Boolean> responseDto = new ResponseDto<Boolean>(400, "duplicated username", duplicatedFlag);
			out.println(gson.toJson(responseDto));
//			return을 해줘야 밑에까지 안내려감
			return;
		}
//		201번은 생성할 때의 코드
		ResponseDto<Integer> responseDto = new ResponseDto<Integer>(201, "signup", userService.addUser(user));
		out.println(gson.toJson(responseDto));
		
	}

}
