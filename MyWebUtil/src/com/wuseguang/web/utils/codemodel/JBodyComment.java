package com.wuseguang.web.utils.codemodel;

import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JStatement;

public class JBodyComment implements JStatement {
	private String comment="todo...";
	public JBodyComment(String comment) {
		super();
		this.comment = comment;
	}
	public JBodyComment() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public void state(JFormatter f) {
		// TODO Auto-generated method stub
		f.p("//"+comment).nl();
	}

}
