/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thupnm.controllers;

import com.sun.xml.internal.ws.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import thupnm.dao.CategoryDAO;
import thupnm.dto.CategoryDTO;

/**
 *
 * @author ThuPMNSE62369
 */
@MultipartConfig
public class AddAndUpdateCategory extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            String cateId = request.getParameter("cateId");
            String cateName = request.getParameter("cateName");
            Part filePart = request.getPart("imgPic");

            CategoryDAO dao = new CategoryDAO();
            CategoryDTO cate = new CategoryDTO();
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = null;

            //xu li main image
            boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
            System.out.println("aaa" + isMultiPart);
            if (!isMultiPart) {
            } else {
                try {
                    items = upload.parseRequest(request);
                    System.out.println("1111 " + items.toString());
                } catch (FileUploadException ex) {
                    ex.printStackTrace();
                }
                Iterator iter = items.iterator();
                Hashtable params = new Hashtable();
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String realPath = null;
                FileItem itemImg = null;
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (item.isFormField()) {
                        params.put(item.getFieldName(), item.getString());
                    } else {
                        try {
                            String itemName = item.getName();
                            if (fileName == null) {
                                fileName = itemName;
                                System.out.println("Path " + fileName);
                                realPath = getServletContext().getRealPath("/") + "img\\" + fileName;
                                System.out.println("Realpath " + realPath);
                                File savedFile = new File(realPath);
                                item.write(savedFile);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                cate.setImgPic(fileName);
                System.out.println("aaaaaaa " + cate.getImgPic());
                cate.setCategoryName(cateName);

                if (cateId.isEmpty() || Objects.isNull(cateId)) {
                    if (dao.createNewCategory(cate)) {
                        request.setAttribute("RESULT", "Add new category successfully!");
                        request.getRequestDispatcher("GetAllCategoriesController").forward(request, response);
                    } else {
                        request.setAttribute("RESULT", "Add new category failed!");
                        request.getRequestDispatcher("GetAllCategoriesController").forward(request, response);
                    }
                } else {
                    if (dao.updateCategory(cate)) {
                        request.setAttribute("RESULT", "Update category successfully!");
                        //request.setAttribute("IMG", cate.getImgPic());
                        request.getRequestDispatcher("GetAllCategoriesController").forward(request, response);
                    } else {
                        request.setAttribute("RESULT", "Update category failed!");
                        request.getRequestDispatcher("GetAllCategoriesController").forward(request, response);
                    }
                }
            }
        } catch (Exception e) {
            log("Error at AddAndUpdateCategoryController " + e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
