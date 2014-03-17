package com.wuseguang.web.utils.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import javax.swing.JTextField;

public class HtmlGenerator {
	private CodeModelApp app;
	private String insertTemplate = null;
	private String updateTemplate = null;
	private String searchTemplate = null;
	private String infoTemplate = null;

	public HtmlGenerator(CodeModelApp app) throws IOException {
		super();
		this.app = app;
		String templatePath = System.getProperty("user.dir") + "/template/";
		this.insertTemplate = app.readFile(templatePath + "insert.html");
		this.updateTemplate=app.readFile(templatePath + "update.html");
		this.infoTemplate=app.readFile(templatePath+"info.htnl");
	}

	protected void generateInsertHtml() {
		final String columnTemplate = "<dl>\n<dt>label</dt>\n<dd><input name='pojo.#name' type='text'/></dd>\n</dl>\n";
		String html = this.insertTemplate.replaceAll("%\\{pojoLower\\}",
				app.getLowerPojo()).replaceAll("%\\{url\\}",
				app.getUrlPattern() + "insert");
		String columnHtml = "";
		for (Entry<String, JTextField> entry : app.getName2label().entrySet()) {
			columnHtml += columnTemplate
					.replace("label", entry.getValue().getText())
					.replace("pojo", app.getLowerPojo())
					.replace("#name", entry.getKey());
		}
		html = this.getHtmlComment()
				+ html.replace("%{columnList}", columnHtml);
		app.writeFile(app.getLowerPojo()+"_insert.html", html);
	}
	protected void generateUpdateHtml(){
		final String columnTemplate = "<dl>\n<dt>label</dt>\n<dd><input name='pojo.#name' type='text' value='#value'/></dd>\n</dl>\n";
		String html = this.updateTemplate
				.replaceAll("%\\{pojoLower\\}",app.getLowerPojo())
				.replaceAll("%\\{url\\}",app.getUrlPattern() + "update")
				.replaceAll("%\\{dataSource\\}", app.getUrlPattern() + "info?id=#{id}");
		String columnHtml = "";
		for (Entry<String, JTextField> entry : app.getName2label().entrySet()) {
			columnHtml += columnTemplate
					.replace("label", entry.getValue().getText())
					.replace("pojo", app.getLowerPojo())
					.replace("#name", entry.getKey())
					.replace("#value", "${info."+entry.getKey()+"}");
		}
		html = this.getHtmlComment()
				+ html.replace("%{columnList}", columnHtml);
		app.writeFile(app.getLowerPojo()+"_update.html", html);
	}
	protected void generateInfoHtml(){
		final String columnTemplate="<dl><dt>label</dt><dt>${info.#name}</dt></dl>\n";
		String html=this.infoTemplate.replace("#dataSource", app.getUrlPattern()+"info?id=#{id}");
		String columnHtml = "";
		for (Entry<String, JTextField> entry : app.getName2label().entrySet()) {
			columnHtml += columnTemplate
					.replace("label", entry.getValue().getText())
					.replace("#name", entry.getKey());
		}
		html = this.getHtmlComment()
				+ html.replace("%{columnList}", columnHtml);
		app.writeFile(app.getLowerPojo()+"_info.html", html);
	}
	protected void generateSearchHtml(){
		
	}
	private String getHtmlComment() {
		return "<!--" + app.getComment() + "-->\n";
	}
}
