/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ssm.db.DBConnection;
import ssm.dto.ProductDTO;

/**
 *
 * @author ThuPMNSE62369
 */
public class ProductDAO {

    private Connection conn;
    private PreparedStatement preStm;
    private ResultSet rs;

    public ProductDAO() {
    }

    private void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (preStm != null) {
                preStm.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createNewProduct(ProductDTO product) {
        boolean checked = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "insert into Product(productId, productName, categoryId, manufacturer, price, quantity, " +
                         "manuDate, expiredDate, description, status) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            preStm = conn.prepareStatement(sql);
            preStm.setString(1, product.getProductId());
            preStm.setString(2, product.getProductName());
            preStm.setInt(3, product.getCategoryId());
            preStm.setString(4, product.getManufacturer());
            preStm.setFloat(5, product.getPrice());
            preStm.setInt(6, product.getQuantity());
            preStm.setTimestamp(7, product.getManuDate());
            preStm.setTimestamp(8, product.getExpiredDate());
            preStm.setString(9, product.getDescription());
            preStm.setString(10, ProductDTO.STATUS_ACTIVE);
            checked = preStm.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return checked;
    }

}
