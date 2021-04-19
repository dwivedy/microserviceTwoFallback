package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.Data;

@EnableHystrixDashboard
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class MicroserviceTwoFallbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceTwoFallbackApplication.class, args);
	}

}

@FeignClient(name="microservice-one",url="${microservice-one.ribbon.listOfServers}")
interface ServiceOne{
	 @GetMapping("service1/status")
	String status();
}

@FeignClient(name="node-service",url="${node-service.ribbon.listOfServers}")
interface NodeService{
	 @GetMapping("/")
	String nodestatus();
}




@CrossOrigin
@RestController
//@RequestMapping("/service2")
 class UsersController {
    
	@Autowired
	 private ServiceOne serviceOne;
	
	@Autowired
	 private NodeService nodeService;
	
	@HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/status")
    public String status() {
    	return serviceOne.status();
    }
	
	@HystrixCommand(fallbackMethod = "fallback")
    @GetMapping("/statusNode")
    public String statusNode() {
    	return nodeService.nodestatus();
    }
	
	private String fallback() {
		 return "ERROR!!";
    }
    
    
    
    @GetMapping("/constHashcodeAndEquals")
    public ResponseEntity<Map<TestForHascodeAndEqualsReturnsConstant,String>> test() {
    	TestForHascodeAndEqualsReturnsConstant t1=new TestForHascodeAndEqualsReturnsConstant();
    	TestForHascodeAndEqualsReturnsConstant t2=new TestForHascodeAndEqualsReturnsConstant();
    	TestForHascodeAndEqualsReturnsConstant t3=new TestForHascodeAndEqualsReturnsConstant();
    	TestForHascodeAndEqualsReturnsConstant t4=new TestForHascodeAndEqualsReturnsConstant();
    	TestForHascodeAndEqualsReturnsConstant t5=new TestForHascodeAndEqualsReturnsConstant();
    	
    	Map<TestForHascodeAndEqualsReturnsConstant,String> map= new HashMap<>();
    	
    	map.put(t1, "t1");
    	map.put(t2, "t2");
    	map.put(t3, "t3");
    	map.put(t4, "t4");
    	map.put(t5, "t5");
    
    
    return new ResponseEntity<Map<TestForHascodeAndEqualsReturnsConstant, String>>(map,HttpStatus.OK);
    
}
}

@Data
class TestForHascodeAndEqualsReturnsConstant{
	
	
	@Override
	public int hashCode() {
		int result = 1;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return true;
	}
	
	
	
	
}

class c extends TestForHascodeAndEqualsReturnsConstant{
	
}

