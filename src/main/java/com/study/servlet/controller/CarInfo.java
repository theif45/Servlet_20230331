package com.study.servlet.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet("/car")
public class CarInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

//	get은 조회할 때 사용
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		Map<String, String> carMap = new HashMap<>();
		
		carMap.put("1", "쏘나타");
		carMap.put("2", "K5");
		carMap.put("3", "SM6");
		
		String id = request.getParameter("id");
		String findModel = carMap.get(id);
		
		JsonObject responseData = new JsonObject();
		
		if(findModel == null) {
			responseData.addProperty("statusCode", 400);
			responseData.addProperty("errorMessage", "Not Found!!");
		} else {
			responseData.addProperty("id", id);
			responseData.addProperty("model", findModel);
		}
		
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().println(responseData.toString());
			
		
	}

//	post는 추가할 때 사용
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		Gson gson = new Gson();
		
		ServletInputStream inputStream = request.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
//		String json = "";
//		String line = null;
//		while ((line = bufferedReader.readLine()) != null) {
//			json += line;
//		}
//		json = json.replaceAll(" ", "");
		
		String requestJson = bufferedReader.lines().collect(Collectors.joining("\n"));
		System.out.println(requestJson);
		List<Map<String, String>> carList = gson.fromJson(requestJson, List.class);
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		carList.forEach(carMap -> {
			System.out.println("id("+ carMap.get("id") + "):"+ carMap.get("model"));
			out.println("id("+ carMap.get("id") + "):"+ carMap.get("model"));
		});
		
		
	}

}
