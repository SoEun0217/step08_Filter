package ex1023.log.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogTest {
	Log log=LogFactory.getLog(getClass());//클래스이름이 들어간다.
	
	public void test() {
		//로그기록
		log.trace("trace에 관련된 메시지 입니다.");//%m 이 된다.
		log.debug("debug에 관련된 메시지 입니다.");
		log.info("info에 관련된 메시지 입니다.");
		log.warn("warn에 관련된 메시지 입니다.");
		log.error("error에 관련된 메시지 입니다.");
		log.fatal("fatal에 관련된 메시지 입니다.");
	}
	
	public static void main(String[] args) {
		System.out.println("---시작---");
		LogTest t= new LogTest();
		t.test();
		System.out.println("--끝--");
	}

}
