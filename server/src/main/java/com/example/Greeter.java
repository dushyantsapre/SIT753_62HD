package com.example;

/**
 * This is a class.
 */
public class Greeter {

  /**
   * This is a constructor.
   */
  public Greeter() {

  }

  //TODO: Add javadoc comment
  public String greet(String someone) {
    return String.format("Hello, %s!", someone);
  }

  public class RegisterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageTag = System.getenv("IMAGE_TAG");
        request.setAttribute("IMAGE TAG", IMAGE_TAG);
        request.getRequestDispatcher("/src/main/webapp/index.jsp").forward(request, response);
    }
  }
}
