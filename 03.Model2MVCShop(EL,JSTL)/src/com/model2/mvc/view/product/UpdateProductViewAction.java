package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;

public class UpdateProductViewAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("UpdateProductView시작: "); // 디버깅 
		int prodNo = (Integer.parseInt(request.getParameter("prodNo")));

		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(prodNo);
		
		System.out.println("UpdateProductView진행중: "+prodNo); // 디버깅 

		request.setAttribute("product", product);
		
		System.out.println("UpdateProductView진행중2: "+prodNo); // 디버깅 

		return "forward:/product/updateProduct.jsp";
	}
}
