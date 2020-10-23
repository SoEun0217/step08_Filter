/**
 * LogFilter.java
 * 2020. 10. 22 諛뺤꽭吏�
 * 
 * �궗�슜�옄�쓽 �젒�냽 湲곕줉�쓣 �뙆�씪�뿉 湲곕줉.
 * 
 */

package ex1023.log.test;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@WebFilter("/*")
public class LogFilter implements Filter {
	Log log;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log=LogFactory.getLog(getClass());
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
	
	}

	public void printLog(String msg) throws IOException {
	   log.info(msg);
	
	}
}
