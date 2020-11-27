package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;

public class GetProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("GetProduct 액션 시작 ");
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));

		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(prodNo); // 파일번호 기반으로 상품찾기
		System.out.println("GetProduct진행중: "+prodNo); // 디버깅 

		request.setAttribute("product", product); // request 에 저장 
		
		System.out.println("GetProduct 액션끝 ");
		return "forward:/product/getProduct.jsp";

	}
}