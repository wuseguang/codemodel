package com.wuseguang.web.utils.codemodel;

import java.util.ArrayList;
import java.util.Collection;

import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JGenerable;
import com.sun.codemodel.JStatement;

public class JListString implements JStatement {
	private Collection<String> clc;
	
	public JListString() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JListString(Collection<String> collection) {
		super();
		this.clc = collection;
	}

	@Override
	public void state(JFormatter f) {
		// TODO Auto-generated method stub
		if(clc==null){
			f.p("").nl();
			return;
		}
		Collection<JGenerable> c=new ArrayList<JGenerable>();
		for(final String item:clc){
			c.add(new JGenerable(){
				@Override
				public void generate(JFormatter f) {
					// TODO Auto-generated method stub
					f.p(item);
				}	
			});
		}
		f.g(c);
	}

}
