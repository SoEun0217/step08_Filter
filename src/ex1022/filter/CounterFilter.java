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
		urlPatterns = { "/*" }, //��� ��û���� �� ���͸� ��ġ�ڴ�.
		initParams = { 
				@WebInitParam(name = "fileName", value = "clickcount.properties")
		})

public class CounterFilter implements Filter {
	
	String fileName;
	File file;
	Properties pro=new Properties();
	
	public void init(FilterConfig fConfig) throws ServletException {
		fileName = fConfig.getInitParameter("fileName");
		
		//���� ������Ʈ�� root��θ� ��������
		ServletContext application = fConfig.getServletContext();
		String path = application.getRealPath("/");
		System.out.println("path : "+path);
		
		file = new File(path+"/"+fileName);
		try {
			if(!file.exists()) {//���ٸ�
				file.createNewFile();
			}
			
			pro.load(new FileInputStream(file));
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}//init

	/**
	 * ����ó�� ���μ����� �л���� ���� ������ �۾� �� �� �ֵ��� �ϴ� ��
	 * jdk 1.5 �߰�
	 *    : Executor  �⺻ ������ ����
	 *    : ExecutorService : Executor�������̽��� Ȯ�� interface�� 
	 *    	�������� ����������Ŭ�� ���������� �����ϴ� �޼ҵ带 �����Ѵ�.
	 *      (����~�Ҹ�Ǳ���� ���õ� �޼ҵ� �����Ѵ�.)
	 * 
	 * */
	
	
	ExecutorService service= Executors.newSingleThreadExecutor();
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//����ó��
		//��û�� url�ּҸ� ���
		service.execute(new Runnable() {
			@Override
			public void run() {//�����忡�� �����ư��鼭 ����
				HttpServletRequest req=(HttpServletRequest)request;//ServletXxx�� �ٿ�ĳ�������ش�
				String url=req.getRequestURL().toString();
				String value=pro.getProperty(url);
				if(value==null) {//clickcount 1�� ����
					pro.setProperty(url, "1");
				}else {//url�� �����Ѵٸ�
					int count=Integer.parseInt(value)+1;
					pro.setProperty(url, count+"");//�� �� String�� �����ϴ�.
				}
			}
		});
		chain.doFilter(request, response);//���� �ִ� response,request�� �ٽ� �����ִ� ��
		//����ó���� chain.doFilter(request,response)�ڿ� �����Ѵ�. �׸��� �߰�ȣ�� ���� �޼ҵ尡 �����
	}//doFilter

	@Override
	public void destroy() {//������ ���� �ǰ� ����
		//���� properties��ü�� �ִ� �����͸� ���Ϸ� �����Ѵ�.
		try {
			FileWriter fw=new FileWriter(file);
			pro.store(fw, "");//������ ������ �����ϴ� ��
			fw.flush();
			fw.close();
		}catch (Exception e ) {
			e.printStackTrace();
		}
		
	}//destroy

}//Filter
