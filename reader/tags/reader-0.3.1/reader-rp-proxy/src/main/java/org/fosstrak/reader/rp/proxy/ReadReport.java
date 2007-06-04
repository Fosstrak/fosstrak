/*
 * Copyright (C) 2007 ETH Zurich
 *
 * This file is part of Accada (www.accada.org).
 *
 * Accada is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1, as published by the Free Software Foundation.
 *
 * Accada is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Accada; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package org.accada.reader.rp.proxy;

import java.util.List;

import org.accada.reader.rprm.core.msg.reply.ReadReportType;
import org.accada.reader.rprm.core.msg.reply.TagType;
import org.accada.reader.rprm.core.msg.reply.ReadReportType.SourceReport;
import org.accada.reader.rprm.core.msg.util.HexUtil;

/**
 * This class stores a read report.
 * 
 * @author regli
 */
public class ReadReport {
	
	/** the read report */
	private final ReadReportType report;

	/**
	 * Constructor sets report.
	 * 
	 * @param report read report
	 */
	public ReadReport(ReadReportType report) {
		
		this.report = report;
		
	}
	
	/**
	 * Constructor creates read report from string array data.
	 * 
	 * @param data string array with tag ids
	 */
	public ReadReport(String[] data) {
		
		// create source report
		SourceReport sourceReport = new SourceReport();
		
//		// create source info
//		SourceInfoType sourceInfo = new SourceInfoType();
//		sourceInfo.setSourceName();
//		sourceInfo.setSourceProtocol();
//		sourceInfo.setSourceFrequency();
//		
//		// set source info
//		sourceReport.setSourceInfo(sourceInfo);
		
		// add tags
		List<TagType> tags = sourceReport.getTag();
		for (int i = 0; i < data.length; i+=4) {
			TagType tag = new TagType();
			tag.setTagID(HexUtil.hexToByteArray(data[i]));
			tag.setTagIDAsPureURI(data[i + 1]);
			tag.setTagIDAsTagURI(data[i + 2]);
			tag.setTagType(data[i + 3]);
			tags.add(tag);

		}
		
		// create read report
		report = new ReadReportType();
		
		// add source report to read report
		report.getSourceReport().add(sourceReport);
		
	}
	
	/**
	 * This method returns the read report.
	 * 
	 * @return read report
	 */
	public ReadReportType getReport() {
		
		return report;
		
	}
	
}
