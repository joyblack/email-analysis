package com.sunrun.emailanalysis.tool.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * To parser excel all content listener.
 */
public class ExcelListener extends AnalysisEventListener<List<String>> {

	// textContent
	private StringBuffer content;

	public ExcelListener() {
		this.content = new StringBuffer();
	}

	// handler every row data, now we just combine all content like
	// "[xx,xxx,xxx][851-40394701, 851-40300831, 773934746395]"
	public void invoke(List<String> rowContent, AnalysisContext context) {
		// 移除空值元素
		rowContent.removeAll(Collections.singleton(null));
		String strContent = rowContent.toString();
		//System.out.println("行内容为" + strContent);
		content.append(strContent);

	}

	public void doAfterAllAnalysed(AnalysisContext context) {
	}

	public String getContent() {
		return content.toString();
	}
}
