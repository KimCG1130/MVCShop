package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;

public class AddProductAction extends Action {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Product productVO = new Product();
	
		productVO.setProdName(request.getParameter("prodName"));
		productVO.setProdDetail(request.getParameter("prodDetail"));
		productVO.setManuDate(request.getParameter("manuDate"));
		productVO.setPrice(Integer.parseInt(request.getParameter("price")));
		productVO.setFileName(request.getParameter("fileName"));

		System.out.println("AddProduct진행중: "+productVO); // 디버깅 

		ProductService service = new ProductServiceImpl(); // 왜 ProductServiceImpl() 로 인스턴스 생성? 
													 // ProductService 의 추상메소드들을 Impl에서 구현하고 있기 때문에
		service.addProduct(productVO); // addProduct 에서 하는 일 : productDAO.insertProduct(productVO);

		return "redirect:/product/addProductView.jsp"; // URI 정보를 String으로 리턴, 프로덕트 정보창 띄우기
	}
}