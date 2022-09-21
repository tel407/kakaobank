package com.code.kakaobank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

@SpringBootApplication
public class KakaobankApplication {

	public static void main(String[] args) {
		System.setProperty("file.encoding","UTF-8");
		try{
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		}
		catch(Exception e){
		}

		SpringApplication.run(KakaobankApplication.class, args);
	}

}
