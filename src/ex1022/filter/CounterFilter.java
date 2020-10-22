package ex1022.filter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


/**
 * Servlet Filter implementation class CounterFilter
 */
@WebFilter(
		urlPatterns = { "/*" }, //모든 요청들이 이 필터를 거치겠다.
		initParams = { 
				@WebInitParam(name = "fileName", value = "clickcount.properties")
		})

public class CounterFilter implements Filter {
	
	String fileName;
	File file;
	Properties pro=new Properties();
	
	public void init(FilterConfig fConfig) throws ServletException {
		fileName = fConfig.getInitParameter("fileName");
		
		//실제 프로젝트의 root경로를 가져오기
		ServletContext application = fConfig.getServletContext();
		String path = application.getRealPath("/");
		System.out.println("path : "+path);
		
		file = new File(path+"/"+fileName);
		try {
			if(!file.exists()) {//없다면
				file.createNewFile();
			}
			
			pro.load(new FileInputStream(file));
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}//init

	/**
	 * 병렬처리 프로세스를 분산시켜 좀더 빠르게 작업 할 수 있도록 하는 것
	 * jdk 1.5 추가
	 *    : Executor  기본 스레드 제공
	 *    : ExecutorService : Executor인터페이스의 확장 interface로 
	 *    	스레드의 라이프사이클을 전반적으로 관리하는 메소드를 제공한다.
	 *      (생성~소멸되기까지 관련된 메소드 제공한다.)
	 * 
	 * */
	
	
	ExecutorService service= Executors.newSingleThreadExecutor();
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//사전처리
		//요청된 url주소를 얻기
		service.execute(new Runnable() {
			@Override
			public void run() {//스레드에서 번갈아가면서 실행
				HttpServletRequest req=(HttpServletRequest)request;//ServletXxx를 다운캐스팅해준다
				String url=req.getRequestURL().toString();
				String value=pro.getProperty(url);
				if(value==null) {//clickcount 1로 변경
					pro.setProperty(url, "1");
				}else {//url이 존재한다면
					int count=Integer.parseInt(value)+1;
					pro.setProperty(url, count+"");//들어갈 때 String만 가능하다.
				}
			}
		});
		chain.doFilter(request, response);//원래 있던 response,request를 다시 보내주는 것
		//사후처리는 chain.doFilter(request,response)뒤에 들어가야한다. 그리고 중괄호를 만나 메소드가 종료됨
	}//doFilter

	@Override
	public void destroy() {//서버가 중지 되고 나서
		//최종 properties객체에 있는 데이터를 파일로 저장한다.
		try {
			FileWriter fw=new FileWriter(file);
			pro.store(fw, "");//수정된 파일을 저장하는 것
			fw.flush();
			fw.close();
		}catch (Exception e ) {
			e.printStackTrace();
		}
		
	}//destroy

}//Filter
