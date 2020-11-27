package com.model2.mvc.service.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;


public class ProductDAO {
	
	public ProductDAO(){
	}

	public void insertProduct(Product product) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "insert into PRODUCT values (seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";//prodNo부분은 nextVal을 통해 등록시 하나씩 증가하게한다
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate());
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		//stmt.setString(7, product.getProTranCode());
		stmt.executeUpdate();
		
		stmt.close();//성능
		con.close();
	}

	public Product findProduct(int prodNo) throws Exception {
		
		Connection con = DBUtil.getConnection();

		String sql = "select * from PRODUCT where PROD_NO=?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, prodNo);

		ResultSet rs = stmt.executeQuery();

		Product product = null;
		while (rs.next()) {
			product = new Product();
			product.setProdName(rs.getString("PROD_NAME"));
			product.setProdDetail(rs.getString("PROD_DETAIL"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("PRICE"));
			product.setProdNo(rs.getInt("PROD_NO"));
			product.setRegDate(rs.getDate("REG_DATE"));
			product.setFileName(rs.getString("IMAGE_FILE"));
			
		}
		
		rs.close();
		stmt.close();
		con.close();

		return product;
	}
//
	public Map<String,Object> getProductList(Search search) throws Exception {
		
		Map<String , Object>  map = new HashMap<String, Object>();
		
		Connection con = DBUtil.getConnection();
		
		String sql = "select * from PRODUCT ";
		if (search.getSearchCondition() != null) {
			if (search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("")) {
				sql += " where PROD_NO like '%" + search.getSearchKeyword()
						+ "%'";
			} else if (search.getSearchCondition().equals("1") &&  !search.getSearchKeyword().equals("")) {
				sql += " where PROD_NAME like '%" + search.getSearchKeyword()
						+ "%'";
			}else if (search.getSearchCondition().equals("2") &&  !search.getSearchKeyword().equals("")) {
				sql += " where PRICE= " + search.getSearchKeyword();
	}
		}
		sql += " order by prod_no";
		
		System.out.println("ProductDAO::Original SQL :: " + sql);//디버깅
		
		//==> TotalCount GET  page에서 찾아옴.
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);

		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = 
			con.prepareStatement(	sql,
									ResultSet.TYPE_SCROLL_INSENSITIVE,//커서이동방법SCROLL은 가능하나 변경된 상항은 적용되지 않음(양방향, 스크롤 시 업데이트 반영안함)
									ResultSet.CONCUR_UPDATABLE);//커서의 위치에서 정보 업데이트 가능.ResultSet이 저장하고 있는 레코드들을 직접 수정해야 할 경우.(Resultset Object의 변경이 가능)
		ResultSet rs = stmt.executeQuery();
		
		System.out.println(search);

		//rs.last();//마지막요소 선택
		//int total = rs.getRow();//행번호
		//System.out.println("로우의 수:" + total);

		//HashMap<String,Object> map = new HashMap<String,Object>();
		//map.put("count", new Integer(total));

		//rs.absolute(search.getPage() * search.getPageUnit() - search.getPageUnit()+1);
		//System.out.println("search.getPage():" + search.getPage());
		//System.out.println("search.getPageUnit():" + search.getPageUnit());

		ArrayList<Product> list = new ArrayList<Product>();
		while(rs.next()){
			//for (int i = 0; i < search.getPageUnit(); i++) {
				Product product = new Product();
				product.setProdNo(rs.getInt("PROD_NO"));
				product.setFileName(rs.getString("IMAGE_FILE"));
				product.setManuDate(rs.getString("MANUFACTURE_DAY"));
				product.setPrice(rs.getInt("PRICE"));
				product.setProdDetail(rs.getString("PROD_DETAIL"));
				product.setProdName(rs.getString("PROD_NAME"));
				product.setRegDate(rs.getDate("REG_DATE"));
				//product.setProTranCode(rs.getString("PROD_NO"));

				
				list.add(product);
				//if (!rs.next())
				//	break;
			}
		//==> totalCount 정보 저장
				map.put("totalCount", new Integer(totalCount));
				//==> currentPage 의 게시물 정보 갖는 List 저장
				map.put("list", list);

				rs.close();
				stmt.close();
				con.close();

				return map;
		}
		//System.out.println("list.size() : "+ list.size());
		//map.put("list", list);
		//System.out.println("map().size() : "+ map.size());

		//con.close();
			
		//return map;
	

	public void updateProduct(Product product) throws Exception {
		//
		Connection con = DBUtil.getConnection();

		String sql = "update PRODUCT set PROD_NAME=?, PROD_DETAIL=?,PRICE=? where PROD_NO=?";//
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setInt(3, product.getPrice());
		stmt.setInt(4, product.getProdNo());
		stmt.executeUpdate();
		
		stmt.close();
		con.close();
	}
	
	//////////////////////////////////page기능추가///////////////////////////////
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("UserDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
	
	
}