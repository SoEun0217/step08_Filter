package ex1022.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
@WebFilter(//urlPatterns = {"/encodingResult.jsp","/encResult"} ,
			urlPatterns = {"/*"},
			initParams = {
					@WebInitParam(name="encoding" , value="UTF-8")
			})	


public class EncodingFilter implements Filter {
	 
	String encoding;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		encoding=filterConfig.getInitParameter("encoding");	
	}//init
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//사전처리
		request.setCharacterEncoding(encoding);
		chain.doFilter(request, response);//요청된 컴포넌트가 호출된다.
		//사후처리
		
		System.out.println("필터");
		
	}//doFilter

}//EncodingFilter