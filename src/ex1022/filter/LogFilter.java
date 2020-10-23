/**
 * LogFilter.java
 * 2020. 10. 22 諛뺤꽭吏�
 * 
 * �궗�슜�옄�쓽 �젒�냽 湲곕줉�쓣 �뙆�씪�뿉 湲곕줉.
 * 
 */

package ex1022.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

@WebFilter("/*")
public class LogFilter implements Filter {
	
	private String logPath;
	private String logFileName;

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext sc = filterConfig.getServletContext();
		logPath = sc.getRealPath("/");
		logFileName = filterConfig.getInitParameter("LogFile");//log.txt
		
		System.out.println("logFile = \"" + logPath + logFileName + "\"");
		logFile = new File(logPath + logFileName);//폴더안에 파일이 만들어진 것이 아니라 파일개념이 된것
		try {
			/*
			 * if (!logFile.exists()) { logFile.createNewFile();//이것이 파일이 만들어지는 메소드이다.
			 * System.out.println(logFileName + " has been created"); }
			 */
			pw = new BufferedWriter(new FileWriter(logFile, true));//true - 이어쓰기, false - 덮어쓰기
			//FileWriter의 특징 상 위에 파일의 존재유무를 물어보지 않아도 없으면 파일이 생성된다.-그래서 주석처리가능
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

	    printLog("Access from IP: " + req.getRemoteAddr() + ":" + req.getRemotePort() + " >> " + req.getServerName() + ":" + req.getServerPort());

	    HttpServletRequest hsr = (HttpServletRequest) req;
		printLog(hsr.getRequestURL().toString());
		printLog("User-Agent: " + hsr.getHeader("User-Agent"));//현재 브라우저 버전 정보
		
	    long timeBefore = System.currentTimeMillis();
		chain.doFilter(req, resp);
		long timeAfter = System.currentTimeMillis();

		printLog("Filter took in: " + (timeAfter - timeBefore) + " ms");
		printLog(new Date().toLocaleString() + "\n");
	}

	@Override
	public void destroy() {
		if (pw != null) {
			try {
				pw.flush();
				pw.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void printLog(String msg) throws IOException {
	   // System.out.println(msg);
	    pw.write(msg);
		pw.flush(); // Flush every time to testing
	}
}
