package com.wuseguang.web.utils.app;

import java.io.File;

import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JStatement;
import com.sun.codemodel.JVar;
import com.wuseguang.web.utils.codemodel.JBodyComment;
import com.wuseguang.web.utils.codemodel.JListString;

public class ServletGenerator {
	private CodeModelApp app;

	public ServletGenerator(CodeModelApp app) {
		super();
		this.app = app;
	}

	protected void generateInserServlet() throws Exception {
		JCodeModel cm = new JCodeModel();
		JDefinedClass dc = cm._class(app.getInsertServletName());
		dc.javadoc().append(app.getComment());
		dc.constructor(JMod.PUBLIC);
		dc._extends(cm.ref(app.getBasePackage() + ".servlet.InsertServlet"));
		JAnnotationUse ja = dc.annotate(cm
				.ref("javax.servlet.annotation.WebServlet"));
		ja.param("urlPatterns", app.getUrlPattern() + "insert");
		JAnnotationArrayMember initParams = ja.paramArray("initParams");
		JClass webInitParam = cm.ref("javax.servlet.annotation.WebInitParam");
		initParams.annotate(webInitParam).param("name", "daoClassName")
				.param("value", app.getDaoName());
		initParams.annotate(webInitParam).param("name", "beanNames")
				.param("value", app.getLowerPojo());
		initParams.annotate(webInitParam).param("name", "beanClasses")
				.param("value", app.getPojoName());
		initParams.annotate(webInitParam).param("name", "pojo")
				.param("value", app.getLowerPojo());
		// JMethod m = dc.method(0, int.class, "foo");
		// m.body()._return(JExpr.lit(5));
		File file = new File(app.getSrcDir());
		file.mkdirs();
		cm.build(file);
	}

	protected void generateUpdateServlet() throws Exception {
		JCodeModel cm = new JCodeModel();
		JDefinedClass dc = cm._class(app.getUpdateServletName());
		dc.javadoc().append(app.getComment());
		dc.constructor(JMod.PUBLIC);
		dc._extends(cm.ref(app.getBasePackage() + ".servlet.UpdateServlet"));
		JAnnotationUse ja = dc.annotate(cm
				.ref("javax.servlet.annotation.WebServlet"));
		ja.param("urlPatterns", app.getUrlPattern() + "update");
		JAnnotationArrayMember initParams = ja.paramArray("initParams");
		JClass webInitParam = cm.ref("javax.servlet.annotation.WebInitParam");
		initParams.annotate(webInitParam).param("name", "daoClassName")
				.param("value", app.getDaoName());
		initParams.annotate(webInitParam).param("name", "beanNames")
				.param("value", app.getLowerPojo());
		initParams.annotate(webInitParam).param("name", "beanClasses")
				.param("value", app.getPojoName());
		initParams.annotate(webInitParam).param("name", "pojo")
				.param("value", app.getLowerPojo());
		// JMethod m = dc.method(0, int.class, "foo");
		// m.body()._return(JExpr.lit(5));
		File file = new File(app.getSrcDir());
		file.mkdirs();
		cm.build(file);
	}

	protected void generateSearchServlet() throws Exception {
		JCodeModel cm = new JCodeModel();
		JDefinedClass dc = cm._class(app.getSearchServletName());
		JClass HttpServletRequest = cm
				.ref("javax.servlet.http.HttpServletRequest");
		JClass BaseExample = cm.ref("com.wuseguang.report.db.pojo.BaseExample");
		JClass Example = cm.ref("com.wuseguang.report.db.pojo." + app.getPojo()
				+ "Example");

		// 构造函数
		dc.javadoc().append(app.getComment());
		dc.constructor(JMod.PUBLIC);
		dc._extends(cm.ref(app.getBasePackage() + ".servlet.SearchServlet"));
		// 注解
		JAnnotationUse ja = dc.annotate(cm
				.ref("javax.servlet.annotation.WebServlet"));
		ja.param("urlPatterns", app.getUrlPattern() + "search");
		JAnnotationArrayMember initParams = ja.paramArray("initParams");
		JClass webInitParam = cm.ref("javax.servlet.annotation.WebInitParam");
		initParams.annotate(webInitParam).param("name", "daoClassName")
				.param("value", app.getDaoName());

		// getExample函数
		JMethod getExample = dc.method(JMod.PROTECTED, BaseExample,
				"getExample");
		getExample.annotate(Override.class);
		getExample.param(HttpServletRequest, "request");
		JBlock body = getExample.body();
		JVar example = body.decl(Example, "example", JExpr._new(Example));
		body.add(new JBodyComment("请填写example规则\n"));
		body._return(example);

		// getColumns函数
		JMethod getColumns = dc.method(JMod.PROTECTED, String.class,
				"getColumns");
		getColumns.annotate(Override.class);
		getColumns.param(HttpServletRequest, "request");
		JBlock column_body = getColumns.body();
		// column_body.add(new JBodyComment("请填写columns\n"));
		getColumns.body()._return(JExpr.lit(app.getJoinColumns()));
		// JMethod m = dc.method(0, int.class, "foo");
		// m.body()._return(JExpr.lit(5));
		File file = new File(app.getSrcDir());
		file.mkdirs();
		cm.build(file);
	}

	protected void generateDeleteServlet() throws Exception {
		JCodeModel cm = new JCodeModel();
		JDefinedClass dc = cm._class(app.getDeleteServletName());
		dc.constructor(JMod.PUBLIC);
		dc.javadoc().append(app.getComment());
		dc._extends(cm.ref(app.getBasePackage() + ".servlet.DeleteServlet"));
		JAnnotationUse ja = dc.annotate(cm
				.ref("javax.servlet.annotation.WebServlet"));
		ja.param("urlPatterns", app.getUrlPattern() + "delete");
		JAnnotationArrayMember initParams = ja.paramArray("initParams");
		JClass webInitParam = cm.ref("javax.servlet.annotation.WebInitParam");
		initParams.annotate(webInitParam).param("name", "daoClassName")
				.param("value", app.getDaoName());
		initParams.annotate(webInitParam).param("name", "required")
				.param("value", "id");
		File file = new File(app.getSrcDir());
		file.mkdirs();
		cm.build(file);
	}

	protected void generateInfoServlet() throws Exception {
		JCodeModel cm = new JCodeModel();
		JClass HttpServletRequest = cm
				.ref("javax.servlet.http.HttpServletRequest");
		JDefinedClass dc = cm._class(app.getInfoServletName());
		dc.constructor(JMod.PUBLIC);
		dc.javadoc().append(app.getComment());
		dc._extends(cm.ref(app.getBasePackage() + ".servlet.InfoServlet"));

		JAnnotationUse ja = dc.annotate(cm
				.ref("javax.servlet.annotation.WebServlet"));
		ja.param("urlPatterns", app.getUrlPattern() + "info");
		JAnnotationArrayMember initParams = ja.paramArray("initParams");
		JClass webInitParam = cm.ref("javax.servlet.annotation.WebInitParam");
		initParams.annotate(webInitParam).param("name", "daoClassName")
				.param("value", app.getDaoName());
		initParams.annotate(webInitParam).param("name", "required")
				.param("value", "id");

		// getColumns函数
		JMethod getColumns = dc.method(JMod.PROTECTED, String.class,
				"getColumns");
		getColumns.annotate(Override.class);
		getColumns.param(HttpServletRequest, "request");
		JBlock column_body = getColumns.body();
		// column_body.add(new JBodyComment("请填写columns\n"));
		getColumns.body()._return(JExpr.lit(app.getJoinColumns()));
		File file = new File(app.getSrcDir());
		file.mkdirs();
		cm.build(file);
	}
}
