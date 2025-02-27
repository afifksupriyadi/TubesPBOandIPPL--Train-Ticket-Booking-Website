/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.RuteDAO;
import dao.SearchingDAO;
import dao.StasiunDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.ListKereta;
import model.Stasiun;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {

    private StasiunDAO stasiunDAO;
    private RuteDAO ruteDAO;
    private SearchingDAO searchingDAO;

    public SearchServlet() {
        this.stasiunDAO = new StasiunDAO();
        this.ruteDAO = new RuteDAO();
        this.searchingDAO = new SearchingDAO();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // VIEW(1): MENAMPILKAN KEMBALI SEARCHING FORM
        // Mengambil data stasiun dan data tanggal dari database
        List<Stasiun> stationList = stasiunDAO.getListStations();
        List<String> dateList = ruteDAO.getListDatesOnDatabase();
        
        
        
        // VIEW(2): MENAMPILKAN HASIL PENCARIAN TIKET
        // Menerima parameter inputan dari searching form
        String stasiunAsal = request.getParameter("stasiunAsal");
        String stasiunTujuan = request.getParameter("stasiunTujuan");
        String tanggal = request.getParameter("tanggal");
        int jumlahPenumpang = Integer.parseInt(request.getParameter("jumlahPenumpang"));

        // Menjalankan query untuk searching
        List<ListKereta> trainResults = searchingDAO.searchKereta(stasiunAsal, stasiunTujuan, tanggal, jumlahPenumpang);
        // Membuat dan menampilkan date navigation
        String previousDate = null;
        String nextDate = null;
        for (int i = 0; i < dateList.size(); i++) {
            if (dateList.get(i).equals(tanggal)) {
                if (i > 0) {
                    previousDate = dateList.get(i - 1);
                }
                if (i < dateList.size() - 1) {
                    nextDate = dateList.get(i + 1);
                }
                break;
            }
        }

        // Mengirim data ke JSP
        // DATA(1): Data untuk menampilkan kembali searching form
        request.setAttribute("stations", stationList);
        request.setAttribute("dates", dateList);
        
       
        // DATA(2): Data untuk menampilkan date navigation
        request.setAttribute("previousDate", previousDate);
        request.setAttribute("nextDate", nextDate);
        request.setAttribute("currentSearchDate", tanggal);
        
        // DATA(3): Data untuk menampilkan hasil pencarian
        request.setAttribute("jumlahPenumpang", jumlahPenumpang);
        request.setAttribute("trainResults", trainResults);
        

        // Proses mengirim semua data ke JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("searching-result.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
