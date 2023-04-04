package com.study.servlet.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.study.servlet.dto.ResponseDto;
import com.study.servlet.entity.User;
import com.study.servlet.service.UserService;
import com.study.servlet.service.UserServiceImpl;


@WebServlet("/auth/signin")
public class SignIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;
	private Gson gson;
	
	public SignIn() {
		userService = UserServiceImpl.getInstance();
		gson = new Gson();
	}

//	인증은 post로 해야함 비밀번호가 노출되므로
//	1. basic 토큰 기본적으로 http에서 제공하는 것
//	2. from 태그에서 submit하여 인증
//	3. jwt를 사용하여 인증
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
//		System.out.println("username: " + username);
//		System.out.println("password: " + password);
		
		User user = userService.getUser(username);
		
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		if(user == null) {
			//로그인 실패 1(아이디 찾을 수 없음)
			ResponseDto<Boolean> responseDto = new ResponseDto<Boolean>(400, "사용자 인증 실패", false);
			out.println(gson.toJson(responseDto));
			return;
		}
		
		if(!user.getPassword().equals(password)) {
			// 로그인 실패 2(비밀번호 틀림)
			ResponseDto<Boolean> responseDto = new ResponseDto<Boolean>(400, "사용자 인증 실패", false);
			out.println(gson.toJson(responseDto));
			return;
		}
		
		// 로그인 성공
		// 인증된 사용자를 기억하기 위해 session 사용
		// session 은 서버가 키값을 들고 있고 jwt 토큰은 클라이언트가 키값을 들고 있음
		// session은 사용자가 많아지면 서버가 많아져야함
		// jwt를 사용하면 마스터키를 이용하여 클라이언트에 접속이 가능하면 인증되어있는 것
		HttpSession session = request.getSession();
		session.setAttribute("AuthenticationPrincipal", user.getUserId());
		
		ResponseDto<Boolean> responseDto = new ResponseDto<Boolean>(200, "사용자 인증 성공", true);
		out.println(gson.toJson(responseDto));
		
		
	}

}
