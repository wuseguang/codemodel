package com.wuseguang.web.utils.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegexTest {

	@Test
	public void test() {
		//fail("Not yet implemented");
		String old="%{pojo}";
		String news=old.replaceAll("%\\{pojo\\}", "a");
		System.out.println(news);
	}

}
