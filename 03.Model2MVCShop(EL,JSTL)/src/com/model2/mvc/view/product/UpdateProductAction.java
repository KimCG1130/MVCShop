package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.domain.Product;


public class UpdateProductAction extends Action {

	@Override
	public String execute(	HttpServletRequest request,
												HttpServletResponse response) throws Exception {
		
		System.out.println("UpdateProduct 액션 시작 ");
		int prodNo=(Integer.parseInt(request.getParameter("prodNo")));
		
		Product product=new Product();
		System.out.println("UpdateProductAction 진행 중1 ");
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		
		System.out.println("UpdateProductAction 진행 중2");
		
		ProductService service=new ProductServiceImpl();
		service.updateProduct(product);
		
		System.out.println("UpdateProductAction 진행 중3 ");
		
		HttpSession session=request.getSession();
		System.out.println("UpdateProductAction 진행 중4 ");
		//int sessionNo=((Product)session.getAttribute("product")).getProdNo();//?
		//System.out.println("UpdateProductAction 진행 중5 ");
			
		//if(sessionNo==prodNo){ 
			session.setAttribute("product", product);
		//}
		System.out.println("UpdateProductAction 진행 중5");
		
		return "redirect:/getProduct.do?prodNo="+prodNo;
	}
}