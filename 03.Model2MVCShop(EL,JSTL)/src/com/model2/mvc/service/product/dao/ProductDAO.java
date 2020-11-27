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

		String sql = "insert into PRODUCT values (seq_product_prod_no.nextval,?,?,?,?,?,sysdate)";//prodNo�κ��� nextVal�� ���� ��Ͻ� �ϳ��� �����ϰ��Ѵ�
		
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, product.getProdName());
		stmt.setString(2, product.getProdDetail());
		stmt.setString(3, product.getManuDate());
		stmt.setInt(4, product.getPrice());
		stmt.setString(5, product.getFileName());
		//stmt.setString(7, product.getProTranCode());
		stmt.executeUpdate();
		
		stmt.close();//����
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
		
		System.out.println("ProductDAO::Original SQL :: " + sql);//�����
		
		//==> TotalCount GET  page���� ã�ƿ�.
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDAO :: totalCount  :: " + totalCount);

		sql = makeCurrentPageSql(sql, search);
		PreparedStatement stmt = 
			con.prepareStatement(	sql,
									ResultSet.TYPE_SCROLL_INSENSITIVE,//Ŀ���̵����SCROLL�� �����ϳ� ����� ������ ������� ����(�����, ��ũ�� �� ������Ʈ �ݿ�����)
									ResultSet.CONCUR_UPDATABLE);//Ŀ���� ��ġ���� ���� ������Ʈ ����.ResultSet�� �����ϰ� �ִ� ���ڵ���� ���� �����ؾ� �� ���.(Resultset Object�� ������ ����)
		ResultSet rs = stmt.executeQuery();
		
		System.out.println(search);

		//rs.last();//��������� ����
		//int total = rs.getRow();//���ȣ
		//System.out.println("�ο��� ��:" + total);

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
		//==> totalCount ���� ����
				map.put("totalCount", new Integer(totalCount));
				//==> currentPage �� �Խù� ���� ���� List ����
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
	
	//////////////////////////////////page����߰�///////////////////////////////
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
	
	// �Խ��� currentPage Row ��  return 
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