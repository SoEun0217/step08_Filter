package ex1022.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SimpleFilter implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("SimpleFilter�� init() ȣ���");
		//init-param���� �ޱ�
		String fileName=filterConfig.getInitParameter("fileName");
		System.out.println("fileName : "+fileName);
	}//init
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("SimpleFilter�� ���� ó���Դϴ�....");
		chain.doFilter(request, response);
		System.out.println("SimpleFilter�� ���� ó���Դϴ�...");
	}//doFilter
	
	@Override
	public void destroy() {
		System.out.println("SimpleFilter�� destroy() ȣ���...");
	}//destroy

}//SimpleFilter