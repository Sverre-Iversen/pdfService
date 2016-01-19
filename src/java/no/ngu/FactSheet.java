package no.ngu;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author student
 */
//@WebServlet(name = "htmltopdf", urlPatterns = {"/render.pdf"})
public class FactSheet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    static final String SEP = ",,";
    String[] validQueryPatterns;
    String[] validRefererPatterns;
    String[] params;
    String sParams;
    ResourceBundle messages;
    static final String BUNDLE = "FactSheet";
    
    public FactSheet() {
        super();
        messages = ResourceBundle.getBundle(BUNDLE);
        //read patterns
        validQueryPatterns = messages.getString("PDF.validQueryPatterns").split(SEP);
        validRefererPatterns = messages.getString("PDF.validRefererPatterns").split(SEP);
        sParams = String.join(" ", messages.getString("PDF.params").split(SEP));
    }
    
    /**
     * Method: checkOriginAndTarget
     * Check if url has a valid url for origin and target
     * @param referer: url string
     * @param target: url string 
     * @return: boolean value for the validation of origin and target
     */
    protected boolean checkOriginAndTarget(String referer, String target) {
        //String urlQuery = request.getQueryString();
        //String urlReferer = request.getHeader("referer");
        //String urlReferer = "http://iversen-ws.ngu.no/myweb/NGU_Bilder/Opg4-3.htm";//statement for testing - comment statment when ok
        boolean bValid = false;

        //Checking origin url
        if (referer != null) {
            for (String validRefererPattern : this.validRefererPatterns) {
                if (Pattern.compile(validRefererPattern, Pattern.CASE_INSENSITIVE).matcher(referer).matches()) {
                    bValid = true;
                    break;
                }
                //if (Pattern.matches(validRefererPatterns[i], urlReferer)){bValid=true; break;}
            }
        }
        //bValid=true;//statement for testing - comment statment when ok

        //Checking query url
        if (bValid && target != null) {
            bValid = false;
            for (String validQueryPattern : this.validQueryPatterns) {
                if (Pattern.compile(validQueryPattern, Pattern.CASE_INSENSITIVE).matcher(target).matches()) {
                    bValid = true;
                    break;
                }
                //if (Pattern.matches(validQueryPatterns[i], urlQuery)){bValid=true; break;}
            }
        }
        //bValid=true;//statement for testing - comment statment when ok

        return bValid;
    }  
    
    protected void processRequestPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
               
        Runtime rt = Runtime.getRuntime();
        String url = request.getParameter("url");
        String outputFileName = request.getParameter("filename");
        
        //For testing
        //System.out.println(sParams);
        //System.out.println(System.getenv("PATH"));
        
        Process p;
        
        if (outputFileName != null) {
            response.addHeader("Content-Disposition", "attachment; filename=\""+
                    outputFileName +"\"");
        }
        if(url != null && url.length()>0){
            //hopefully this stops the program accessing local files.
            if(! (url.startsWith("http://") || url.startsWith("https://"))  ){
                url = "http://" + url;
            }
            
            //Checking url for origin and target and return nothing if url is invalid
            if (!checkOriginAndTarget(request.getHeader("referer"), url) || request.getContentLength() < 1) {
                return;
            }    
            
            //p = rt.exec("wkhtmltopdf --footer-center URL:"+url+" --disable-local-file-access --disable-external-links --javascript-delay 200 --zoom " + zoom + " "+ url +" -");
            p = rt.exec("wkhtmltopdf --footer-center URL:" + url + " " + sParams + " " + url + " -");
            
        }else{
            String html = request.getParameter("html");
            if(html == null){
                response.sendError(404);
                return;
            }
            p = rt.exec("wkhtmltopdf" + " " + sParams + " " + " - -");
            IOUtils.write(html, p.getOutputStream());
            p.getOutputStream().close();
        }
        //IOUtils.copy(p.getErrorStream(), System.out);
        //p.getErrorStream().close();
        try {
            IOUtils.copy(p.getInputStream(), response.getOutputStream());
        } finally {
            p.getInputStream().close();
            response.getOutputStream().close();
        }
    }

    protected void processRequestGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String url = request.getQueryString();
        
        //Checking url for origin and target and return nothing if url is invalid
        if (!checkOriginAndTarget(request.getHeader("referer"), url)) {
            return;
        }
        
        Runtime rt = Runtime.getRuntime();
        
        //For testing
        //System.out.println(sParams);
        //System.out.println(System.getenv("PATH"));
        
        Process p;
        
        if(url != null){
            //hopefully this stops the program accessing local files.
            if(! (url.startsWith("http://") || url.startsWith("https://"))  ){
                url = "http://" + url;
            } 
            
            p = rt.exec("wkhtmltopdf --footer-center URL:" + url + " " + sParams + " " + url + " -");
            
        }else{
           return;
        }
        //IOUtils.copy(p.getErrorStream(), System.out);
        //p.getErrorStream().close();
        try {
            IOUtils.copy(p.getInputStream(), response.getOutputStream());
        } finally {
            p.getInputStream().close();
            response.getOutputStream().close();
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestGet(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestPost(request, response);
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
