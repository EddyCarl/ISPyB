/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/

package ispyb.common.util.export;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jboss.util.collection.ReverseListIterator;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;

import ispyb.common.util.Constants;
import ispyb.common.util.Formatter;
import ispyb.common.util.PathUtils;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.mx.services.autoproc.SpaceGroup3Service;
import ispyb.server.mx.services.collections.DataCollection3Service;
import ispyb.server.mx.services.collections.Image3Service;
import ispyb.server.mx.services.collections.IspybCrystalClass3Service;
import ispyb.server.mx.services.ws.rest.datacollectiongroup.DataCollectionGroupRestWsService;
import ispyb.server.mx.vos.autoproc.SpaceGroup3VO;
import ispyb.server.mx.vos.collections.IspybCrystalClass3VO;
import ispyb.server.mx.vos.collections.Session3VO;

/**
 * allows creation of PDF or RTF report - general report for EXI, available in the
 * 
 * @author Solange Delageniere
 * 
 */
public class ExiPdfRtfExporter {

	private final static Logger LOG = Logger.getLogger(ExiPdfRtfExporter.class);

	// constants color
	public final static Color GREEN_COLOR = new Color(17, 197, 3);

	public final static Color RED_COLOR = new Color(202, 58, 2);

	public final static Color WHITE_COLOR = new Color(255, 255, 255);

	public final static Color BLUE_COLOR = new Color(160, 200, 237);

	public final static Color LIGHT_GREY_COLOR = new Color(192, 192, 192);

	public final static Color LIGHT_BLUE_COLOR = new Color(126, 182, 235);

	public final static Color SUBWEDGE_COLOR = new Color(238, 248, 252);

	public final static Color WEDGE_COLOR = new Color(171, 222, 245);

	public final static Color LIGHT_YELLOW_COLOR = new Color(245, 246, 206);

	public final static Color GREEN_COLOR_RANK = new Color(0, 139, 139);

	public final static Color NULL_COLOR_RANK = new Color(104, 136, 168);

	// constant fonts
	public final static Font FONT_HELVETICA_10 = new Font(Font.HELVETICA, 10, Font.BOLD);

	public final static Font VERY_SMALL_FONT = new Font(Font.HELVETICA, 2);

	public final static Font FONT_SPACE = new Font(Font.HELVETICA, 6);

	public final static Font FONT_TITLE = new Font(Font.HELVETICA, 8, Font.BOLD | Font.UNDERLINE);

	public final static Font FONT_TITLE_11 = new Font(Font.HELVETICA, 11, Font.BOLD | Font.UNDERLINE);

	public final static Font FONT_DOC = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
	
	public final static Font FONT_DOC_SMALL = new Font(Font.HELVETICA, 6, Font.NORMAL, Color.BLACK);
	
	public final static Font FONT_DOC_BLUE = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLUE);
	public final static Font FONT_DOC_ORANGE = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.ORANGE);
	public final static Font FONT_DOC_RED = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.RED);

	public final static Font FONT_DOC_ITALIC = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_11 = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

	public final static Font FONT_DOC_11_ITALIC = new Font(Font.HELVETICA, 11, Font.ITALIC, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

	public final static Font FONT_DOC_EXPONENT_BLUE = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLUE);

	public final static Font FONT_DOC_BOLD = new Font(Font.HELVETICA, 8, Font.BOLD);
	
	public final static Font FONT_DOC_SMALL_BOLD = new Font(Font.HELVETICA, 6, Font.BOLD);
	
	public final static Font FONT_DOC_SMALL_CENTERED = new Font(Font.HELVETICA, 6, Font.BOLD);

	public final static Font FONT_DOC_PARAM_TITLE = new Font(Font.HELVETICA, 8, Font.NORMAL, LIGHT_GREY_COLOR);

	public final static Font FONT_INDEXING_NOTDONE = new Font(Font.HELVETICA, 8, Font.NORMAL, LIGHT_GREY_COLOR);

	public final static Font FONT_INDEXING_FAILED = new Font(Font.HELVETICA, 6, Font.BOLD, RED_COLOR);

	public final static Font FONT_INDEXING_SUCCESS = new Font(Font.HELVETICA, 6, Font.BOLD, GREEN_COLOR);

	public final static int NB_COL_DATACOLLECTION = 7;
	
	public final static int NB_COL_DATA_ANALYSIS = 12;

	public final static int SIZE_FONT = 8;

	public final static int SMALL_FONT = 6;

	public final static int TEXT_RISE_EXP = 2;

	public final static int TEXT_RISE_SUB = -2;

	public final static int NB_DATA_COLLECTION_PER_PAGE = 2;

	public final static float INDENTATION_LEFT = 10f;

	public final static float GREY_FILL_DATA = 0.99f;

	public final static float GREY_FILL_DATA_COLLECT = 0.8f;

	public final static float GREY_FILL_HEADER = 0.6f;

	// images size
	public final static float CRYSTAL_IMAGE_WIDTH = 281;

	public final static float CRYSTAL_IMAGE_HEIGHT = 174;

	//public final static float IMAGE_HEIGHT = 120;
	public final static float IMAGE_HEIGHT = 80;
	public final static float IMAGE_HEIGHT_SMALL = 50;
	public final static float IMAGE_HEIGHT_SNAPSHOT = 60;

	// public final static float CRYSTAL_IMAGE_WIDTH = 160;
	// public final static float CRYSTAL_IMAGE_HEIGHT = 99;
	public final static float DIFF_IMAGE_WIDTH = 174;

	public final static float DIFF_IMAGE_HEIGHT = 174;
	
	public final static double MIN_RMERGE = 10;

	//proposalId
	int proposalId;
	// proposal
	String proposalDesc;

	// session info
	Integer sessionId;
	
	// nb row to export
	Integer nbRowsMax;
	
	private Session3VO slv;


	// dataCollectionViewObject List
	List<Map<String, Object>> dataCollections = new ArrayList<Map<String, Object>>();

	DecimalFormat df2;

	DecimalFormat df3;

	private final Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
	
	private Session3Service sessionService;
	
	private DataCollection3Service dcService;
	
	private DataCollectionGroupRestWsService dcGroupService;
	
	private Image3Service imageService;
	
	private SpaceGroup3Service spacegroupService;
	
	private Map <String, Integer> spgMap = new HashMap <String, Integer> ();
		
	public ExiPdfRtfExporter(int proposalId, String proposalDesc, Integer sessionId,
			List<Map<String, Object>> dataCollections, Integer nbRowsMax) throws Exception {
		this.proposalDesc = proposalDesc;
		this.sessionId = sessionId;
		this.dataCollections = dataCollections;
		this.nbRowsMax = nbRowsMax;
		init();
	}

	private void init() throws Exception {
		df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df2.applyPattern("#####0.00");
		df3 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		df3.applyPattern("#####0.000");

		sessionService = (Session3Service) ejb3ServiceLocator
				.getLocalService(Session3Service.class);
		dcService = (DataCollection3Service) ejb3ServiceLocator
				.getLocalService(DataCollection3Service.class);
		dcGroupService = (DataCollectionGroupRestWsService) ejb3ServiceLocator
				.getLocalService(DataCollectionGroupRestWsService.class);
		imageService = (Image3Service) ejb3ServiceLocator
				.getLocalService(Image3Service.class);
		spacegroupService = (SpaceGroup3Service) ejb3ServiceLocator
				.getLocalService(SpaceGroup3Service.class);
		
		slv = sessionService.findByPk(sessionId, false/*withDataCollectionGroup*/, false/*withEnergyScan*/, false/*withXFESpectrum*/);
		
		List<SpaceGroup3VO> spaceGroups = spacegroupService.findAll();
		
		for (Iterator<SpaceGroup3VO> iterator = spaceGroups.iterator(); iterator.hasNext();) {
			SpaceGroup3VO spg = (SpaceGroup3VO) iterator.next();
			spgMap.put(spg.getSpaceGroupName(), spg.getSpaceGroupNumber());
		}
		LOG.info("spgMap=" + spgMap.toString());

	}

	/**
	 * export datacollection report
	 * 
	 * @param rtfFormat
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream exportDataCollectionReport(boolean rtfFormat) throws Exception {
		
		this.init();
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			PdfWriter.getInstance(document, baos);
		} else {
			RtfWriter2.getInstance(document, baos);
		}

		// =============================
		// Header + footer
		// =============================

		setHeader(document);
		setFooter(document);
		document.open();

		// ===============================
		// Body
		// ===============================
		// Crystallographer added only for IFX proposal in case of MXPress
		// experiment
		setCrystallographer(document);
		// Session comments
		setSessionComments(document);
		// session title& info
		setSessionTable(document);

		// ======================
		// Data Collection table
		// ======================
		document.add(new Paragraph(" "));
		setDataCollectionTable(document);

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;

	}

	public ByteArrayOutputStream exportDataCollectionAnalysisReport(boolean rtfFormat) throws Exception {
		
		this.init();
		// create simple doc and write to a ByteArrayOutputStream
		Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (!rtfFormat) {
			PdfWriter.getInstance(document, baos);
		} else {
			RtfWriter2.getInstance(document, baos);
		}

		// =============================
		// Header + footer
		// =============================

		setHeader(document);
		setFooter(document);
		document.open();

		// ===============================
		// Body
		// ===============================
		// Crystallographer added only for IFX proposal in case of MXPress
		// experiment
		setCrystallographer(document);
		// Session comments
		setSessionComments(document);
		// session title& info
		setSessionTable(document);

		// ======================
		// Data Collection analysis table
		// ======================
		document.add(new Paragraph(" "));
		setDataAnalysisTable(document);

		// ======================
		// End of file
		// ======================
		document.close();
		return baos;

	}
	/**
	 * sets the header in the specified document
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setHeader(Document document) throws Exception {
		HeaderFooter header;
		String h = "Data Collections for Proposal: " + proposalDesc;
		if (slv != null) {
			h += " on Beamline: " + (slv.getBeamlineName() == null ? "" : slv.getBeamlineName())
					+ "  ---  Session start date: "
					+ (slv.getStartDate() == null ? "" : Formatter.formatDate(slv.getStartDate()));
		}
		header = new HeaderFooter(new Phrase(h, FONT_HELVETICA_10), false);
		header.setAlignment(Element.ALIGN_CENTER);
		header.setBorderWidth(1);
		header.getBefore().getFont().setSize(SIZE_FONT);
		document.setHeader(header);
	}

	/**
	 * set footer in the specified document
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setFooter(Document document) throws Exception {
		HeaderFooter footer = new HeaderFooter(new Phrase("Page n."), true);
		footer.setAlignment(Element.ALIGN_RIGHT);
		footer.setBorderWidth(1);
		footer.getBefore().getFont().setSize(SMALL_FONT);
		document.setFooter(footer);
	}

	/**
	 * set the crystallographer - only for IFX proposal
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setCrystallographer(Document document) throws Exception {
		// Crystallographer added only for IFX proposal in case of MXPress
		// experiment
		if (proposalDesc.toLowerCase().contains(Constants.PROPOSAL_CODE_FX) && slv != null) {
			String beamlineOp = "";
			if (slv.getBeamlineOperator() != null) {
				beamlineOp = slv.getBeamlineOperator();
			}
			document.add(new Paragraph("Crystallographer:", FONT_TITLE));
			document.add(new Paragraph(beamlineOp, FONT_DOC));
		}
	}

	/**
	 * set sessions comments
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setSessionComments(Document document) throws Exception {
		if (slv != null) {
			document.add(new Paragraph(Constants.SESSION_VISIT_CAP + " comments:", FONT_TITLE));
			document.add(new Paragraph(slv.getComments(), FONT_DOC));
		}
	}

	/***
	 * sets the sessions informations in the pdf document for fx or ix accounts
	 * (Issue 1049)
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setSessionTable(Document document) throws Exception {
		String proposalCode = proposalDesc.substring(0, 2);
		if (slv != null
				&& (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX) || proposalCode
						.equals(Constants.PROPOSAL_CODE_IX))) {
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) { // session
																					// title
																					// only
																					// for
																					// FX
																					// accounts
				document.add(new Paragraph("Session title:", FONT_TITLE));
				document.add(new Paragraph(slv.getSessionTitle(), FONT_DOC));
			}
			Table sessionTable = new Table(2);
			// sessionTable.setWidth(50); // percentage
			sessionTable.setPadding(3);
			sessionTable.setCellsFitPage(true);
			sessionTable.getDefaultCell().setBorderWidth(0);
			sessionTable.setBorder(0);
			sessionTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			boolean hasData = false;
			// print only if the value > 0
			if (proposalCode.toLowerCase().equals(Constants.PROPOSAL_CODE_FX)) { // structure
																					// determinations
																					// only
																					// for
																					// FX
																					// accounts
				if (slv.getStructureDeterminations() != null && !slv.getStructureDeterminations().isNaN()
						&& slv.getStructureDeterminations() != 0) {
					hasData = true;
					sessionTable.addCell(new Paragraph("Structure determinations", FONT_DOC_BOLD));
					sessionTable.addCell(new Paragraph("" + slv.getStructureDeterminations(), FONT_DOC));
				}
			}
			if (slv.getDewarTransport() != null && !slv.getDewarTransport().isNaN() && slv.getDewarTransport() != 0) {
				hasData = true;
				sessionTable.addCell(new Paragraph("Dewar transport", FONT_DOC_BOLD));
				sessionTable.addCell(new Paragraph("" + slv.getDewarTransport(), FONT_DOC));
			}
			if (slv.getDatabackupFrance() != null && !slv.getDatabackupFrance().isNaN()
					&& slv.getDatabackupFrance() != 0) {
				hasData = true;
				sessionTable.addCell(new Paragraph("Data backup & Express delivery France", FONT_DOC_BOLD));
				sessionTable.addCell(new Paragraph("" + slv.getDatabackupFrance(), FONT_DOC));
			}
			if (slv.getDatabackupEurope() != null && !slv.getDatabackupEurope().isNaN()
					&& slv.getDatabackupEurope() != 0) {
				hasData = true;
				sessionTable.addCell(new Paragraph("Data backup & Express delivery Europe", FONT_DOC_BOLD));
				sessionTable.addCell(new Paragraph("" + slv.getDatabackupEurope(), FONT_DOC));
			}
			if (hasData) {
				document.add(sessionTable);
			}
		}
	}

	/**
	 * set the dataCollection table
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setDataCollectionTable(Document document) throws Exception {
				
		document.add(new Paragraph("Data Collections:", FONT_TITLE));
		document.add(new Paragraph(" "));
		
		if (dataCollections.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", FONT_DOC));
		} else {
			
			document.add(new Paragraph(" "));
						
			// need the list of DCgroups for crystal class summary
			Map<String, String> mapDataCollectionGroupIdCClass = new HashMap<String, String>();

			// DataCollection Rows
			//Iterator<Map<String, Object>> it = dataCollections.iterator();
			Iterator<Map<String, Object>> it2 = new ReverseListIterator <Map<String, Object>>(dataCollections);
					
			int i = 0;
			while (it2.hasNext() && i < nbRowsMax) {
				Map<String, Object> dataCollectionMapData = it2.next();
				LOG.info("dcMap=" + dataCollectionMapData.toString());
				setDataCollectionMapData(document, dataCollectionMapData);
				
				if (getCellParam(dataCollectionMapData, "DataCollectionGroup_crystalClass", null) != null) {
					String dcgId = getCellParam(dataCollectionMapData, "DataCollectionGroup_dataCollectionGroupId", null);
					if (!mapDataCollectionGroupIdCClass.containsKey(dcgId)){
							mapDataCollectionGroupIdCClass.put(dcgId, getCellParam(dataCollectionMapData, "DataCollectionGroup_crystalClass", null));
					}
				}					
				i++;
			}
			//setCrystalClassSummary(document, mapDataCollectionGroupIdCClass);
			document.add(new Paragraph(" "));
		}
		document.add(new Paragraph(" "));			
			
	}
	
	/**
	 * set the dataCollection table
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setDataAnalysisTable(Document document) throws Exception {
		document.add(new Paragraph("Data Collections & Analysis results:", FONT_TITLE));
		document.add(new Paragraph(" "));
		if (dataCollections.isEmpty()) {
			document.add(new Paragraph("There is no data collection in this report", FONT_DOC));
		} else {
			//document.add(new Paragraph(" "));
			
			// DataCollection Rows
			//Iterator<Map<String, Object>> it = dataCollections.iterator();
			Iterator<Map<String, Object>> it2 = new ReverseListIterator <Map<String, Object>>(dataCollections);

			int i = 0;
			while (it2.hasNext() && i < nbRowsMax) {
				Map<String, Object> dataCollectionMapData = it2.next();
				LOG.info("dcMap=" + dataCollectionMapData.toString());
				setDataAnalysisMapData(document, dataCollectionMapData);				
				i++;
			}
			document.add(new Paragraph(" "));
		}
	}

	/**
	 * set a line for a specified dataCollection in the dataCollection table
	 * 
	 * @param document
	 * @param table
	 * @param col
	 * @param session
	 * @param df2
	 * @param df3
	 * @throws Exception
	 */
	private void setDataCollectionMapData(Document document, Map<String, Object> dataCollectionMapItem) throws Exception {

		// 1st row
		String parag = getCellParam(dataCollectionMapItem, "DataCollectionGroup_experimentType", null) 
				+ " " + getCellParam(dataCollectionMapItem, "DataCollection_startTime", null);
		Paragraph p = new Paragraph(parag, FONT_DOC_BLUE);
		document.add(p);
		
		//row2		
		parag = getCellParam(dataCollectionMapItem,"DataCollection_imageDirectory", null);
		document.add(new Paragraph(parag, FONT_DOC_ITALIC));	
	
		//row3
		Table table = new Table(NB_COL_DATACOLLECTION);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorderWidth(0);
		table.setBorder(0);
		table.setCellsFitPage(true);
		table.setWidth(90);

		// 1st Cell
		parag = "Workflow:\n" 
				+ "Protein:\n" 
				+ 	"Sample:\n" 
				+ 	"Prefix:\n" 
				+ 	"Run #:\n" 
				+ 	"Images:\n" 
				+ 	"Transmission:\n";
		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC);
		table.addCell(p);
		
		// Cell2
		parag = getCellParam(dataCollectionMapItem, "Workflow_workflowType", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "Protein_acronym", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "BLSample_name", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_imagePrefix", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionNumber", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_numberOfImages", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "transmission", df2) + "%\n";
		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC_BOLD);
		table.addCell(p);
		
		// 3 Cell
		
		parag = "Resolution (corner): \n"
				+ 	"Wavelength: \n" 
				+ 	"Omega range: \n" 
				+ 	"Omega start: \n" 
				+ 	"Exposure time: \n" 
				+ 	"Flux start: \n" 
				+ 	"Flux end: \n" ;

		table.addCell(new Paragraph(parag, FONT_DOC));
		
		
		// Cell 4
		parag = getCellParam(dataCollectionMapItem, "DataCollection_resolution", df2) + Constants.ANGSTROM
			+ " ("+ getCellParam(dataCollectionMapItem, "DataCollection_resolutionAtCorner", df2) + Constants.ANGSTROM + ") \n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_wavelength", df3) + Constants.ANGSTROM + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_axisRange", df2) + Constants.DEGREE + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_omegaStart", df2) + Constants.DEGREE + "\n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_exposureTime", df2) + "s \n" 
		+ 	getCellParam(dataCollectionMapItem, "DataCollection_flux", null) + "ph/sec \n" 
		+	getCellParam(dataCollectionMapItem, "DataCollection_flux_end", null) + "ph/sec \n" ;
		
		table.addCell(new Paragraph(parag, FONT_DOC_BOLD));
		
		// 5 Cell : thumbnail
		
		if (!getCellParam(dataCollectionMapItem, "lastImageId", null).isEmpty()) {
			String thumbnailPath = (imageService.findByPk(new Integer(getCellParam(dataCollectionMapItem, "lastImageId", null)))).getJpegThumbnailFileFullPath();
			Cell cellThumbnail = getCellImage(thumbnailPath, IMAGE_HEIGHT);
			cellThumbnail.setBorderWidth(0);
			table.addCell(cellThumbnail);
		} else {
			table.addCell(" ");
		}
				
		// 6 Cell : snapshot
		Cell cellSnapshot = getCellImage(dataCollectionMapItem,"DataCollection_xtalSnapshotFullPath1", IMAGE_HEIGHT_SNAPSHOT);
		cellSnapshot.setBorderWidth(0);
		table.addCell(cellSnapshot);
		
		// 7 Cell : graph or other plot
		if (!getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionId", null).isEmpty()) {
			String plotPath = (dcService.findByPk(new Integer(getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionId", null)), false, false)).getImageQualityIndicatorsPlotPath();
			Cell cellGraph = getCellImage(plotPath, IMAGE_HEIGHT);
			cellGraph.setBorderWidth(0);
			table.addCell(cellGraph);
		} else {
			table.addCell(" ");
		}

		document.add(table);
		
		// row3
		if (dataCollectionMapItem.get("DataCollection_comments") != null && dataCollectionMapItem.get("DataCollection_comments") != "")
			document.add(new Paragraph(dataCollectionMapItem.get("DataCollection_comments").toString(), FONT_DOC));
		else
			document.add(new Paragraph(" "));
				
		return;
	}
	
	/**
	 * set a line for a specified dataCollection in the dataCollection table
	 * 
	 * @param document
	 * @param table
	 * @param col
	 * @param session
	 * @param df2
	 * @param df3
	 * @throws Exception
	 */
	private void setDataAnalysisMapData(Document document, Map<String, Object> dataCollectionMapItem) throws Exception {
	
		//row 1
		String parag = getCellParam(dataCollectionMapItem, "DataCollectionGroup_experimentType", null) 
				+ " " + getCellParam(dataCollectionMapItem, "DataCollection_startTime", null);
		Paragraph p = new Paragraph(parag, FONT_DOC_BLUE);
		//document.add(p);

		Table table = new Table(NB_COL_DATA_ANALYSIS);
		table.setWidth(100);
		table.setCellsFitPage(true);
		table.setBorder(0);

		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorderWidth(0);

		// 1st Cell
		parag = "Protein: \n\n" 
				+ 	"Prefix: \n\n" 
				+ 	"Images: \n\n" ;

		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC_SMALL);
		table.addCell(p);
		
		// 2st Cell
		parag = getCellParam(dataCollectionMapItem, "Protein_acronym", null)+ "\n\n" 
				+ 	getCellParam(dataCollectionMapItem, "DataCollection_imagePrefix", null) + "\n\n" 
				+ 	getCellParam(dataCollectionMapItem, "DataCollection_numberOfImages", null) + "\n\n" ;

		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC_SMALL_BOLD);
		table.addCell(p);
		
		//  Cell 3
		parag = "Type: \n" 
				+ "Res. (corner): \n" 
				+ "Wavelength: \n" ; 
		LOG.info("parag=" + parag);
		p = new Paragraph(parag, FONT_DOC_SMALL);
		table.addCell(p);

		// Cell 4
		parag = getCellParam(dataCollectionMapItem, "Workflow_workflowType", null) + "\n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_resolution", df2) + Constants.ANGSTROM
					+ "("+ getCellParam(dataCollectionMapItem, "DataCollection_resolutionAtCorner", df2) + Constants.ANGSTROM + ") \n" 
				+ getCellParam(dataCollectionMapItem, "DataCollection_wavelength", df3) + Constants.ANGSTROM + "\n" ;
		
		p = new Paragraph(parag, FONT_DOC_SMALL_BOLD);
		table.addCell(p);

		//  Cell 5 : image quality indicator  plot
		if (!getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionId", null).isEmpty()) {
			String plotPath = (dcService.findByPk(new Integer(getCellParam(dataCollectionMapItem, "DataCollection_dataCollectionId", null)), false, false)).getImageQualityIndicatorsPlotPath();
			Cell cellGraph = getCellImage(plotPath, IMAGE_HEIGHT);
			cellGraph.setBorderWidth(0);
			table.addCell(cellGraph);
		} else {
			table.addCell(" ");
		}

		// Cell 6 7 8 9 10 11 indexed/strategy or completeness
		Boolean indexing = getBoolean(dataCollectionMapItem, "ScreeningOutput_indexingSuccess");
		Boolean strategy = getBoolean(dataCollectionMapItem, "ScreeningOutput_strategySuccess");
		String autoprocSpaceGroup = getCellParam(dataCollectionMapItem, "AutoProc_spaceGroup", null);
		boolean existAutoProcSpaceGroup = (autoprocSpaceGroup != null && !autoprocSpaceGroup.isEmpty() ) 
				||  ( dataCollectionMapItem.get("AutoProc_spaceGroups") != null && dataCollectionMapItem.get("Autoprocessing_cell_a") != null);
		
		p = new Paragraph(); 
		String [] bestRmerge = null;
			
		if (existAutoProcSpaceGroup && extractBestAutoproc(dataCollectionMapItem) != null){
			// Cell 6
			bestRmerge = extractBestAutoproc(dataCollectionMapItem);
			parag = bestRmerge[0] + "\n"
					+ "Overall\n"
					+ "Inner\n"
					+ "Outer\n";
			p = new Paragraph(parag, FONT_DOC_SMALL);
			table.addCell(p);				

			// Cell 7			
			p = new Paragraph("Completeness\n", FONT_DOC_SMALL);
			Chunk chu =  getCompletenessChunk(bestRmerge[16]);	
			p.add(chu);
			p.add("\n");
			chu =  getCompletenessChunk(bestRmerge[2]);	
			p.add(chu);
			p.add("\n");
			chu =  getCompletenessChunk(bestRmerge[12]);	
			p.add(chu);
					
			table.addCell(p);				

			// Cell 8 
			parag = "Res. \n" 
					+ bestRmerge[17]+ "\n " 
					+ bestRmerge[3] + "\n"
					+ bestRmerge[13] + "\n" ; 
			p = new Paragraph(parag, FONT_DOC_SMALL);
			table.addCell(p);

			// Cell 9 		
			parag = "Rmerge \n" 
					+ bestRmerge[15] + "\n"
					+ bestRmerge[1] + "\n"
					+ bestRmerge[11] + "\n"; 
			p = new Paragraph(parag, FONT_DOC_SMALL);
			table.addCell(p);

			// cell parameters of innerShell
			// Cell 10 a alpha
			parag = "a \n" + bestRmerge[4]
			+ "\n alpha \n" + bestRmerge[7] ;
			p = new Paragraph(parag, FONT_DOC_SMALL_CENTERED);
			p.setAlignment(Element.ALIGN_CENTER); 
			table.addCell(p);
		
			// Cell 11 b beta
			parag = "b \n" + bestRmerge[5]
			+ "\n beta \n" + bestRmerge[8] ;
			p = new Paragraph(parag, FONT_DOC_SMALL_CENTERED);
			p.setAlignment(Element.ALIGN_CENTER); 
			table.addCell(p);

			// Cell 12 
			parag = "c \n" + bestRmerge[6]
			+ "\n gamma \n" + bestRmerge[9] ;

			p = new Paragraph(parag, FONT_DOC_SMALL_CENTERED);
			p.setAlignment(Element.ALIGN_CENTER); 
			table.addCell(p);
			
		} else if (indexing != null && strategy != null){
			// Cell 6
			parag = "\nIndexed: \n "
					+ "\nStrategy: \n";
			p = new Paragraph(parag, FONT_DOC_SMALL);
			table.addCell(p);

			// Cell 7	
			p = new Paragraph(); 
			Chunk chu2 =  new Chunk( "KO", FONT_INDEXING_FAILED);	
			if (indexing.booleanValue() ){
				chu2 =  new Chunk( "OK", FONT_INDEXING_SUCCESS);						
			} 
			p.add(chu2);
			
			chu2 =  new Chunk( "KO", FONT_INDEXING_FAILED);	
			if (strategy.booleanValue() ){
				chu2 =  new Chunk( "OK", FONT_INDEXING_SUCCESS);	
			}
			p.add("\n");
			p.add(chu2);			
			table.addCell(p);

			// Cell 8
			parag = "Space group: \n" 
					+ "Mosaicity: \n" ; 
			p = new Paragraph(parag, FONT_DOC_SMALL);
			table.addCell(p);
			
			// Cell 9 
			parag = getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_spaceGroup", null) + "\n" 
					+ getCellParam(dataCollectionMapItem, "ScreeningOutput_mosaicity", null)+ "\n" ;
			p = new Paragraph(parag, FONT_DOC_SMALL_BOLD);
			table.addCell(p);
			
			// Cell 10 
			parag = "a \n" + getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_unitCell_a", null) 
			+ "\n alpha \n" + getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_unitCell_alpha", null) ;
			p = new Paragraph(parag, FONT_DOC_SMALL_CENTERED);
			p.setAlignment(Element.ALIGN_CENTER); 
			table.addCell(p);

			// Cell 11 
			parag = "b \n" + getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_unitCell_b", null) 
			+ "\n beta \n" + getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_unitCell_beta", null) ;
			p = new Paragraph(parag, FONT_DOC_SMALL_CENTERED);
			p.setAlignment(Element.ALIGN_CENTER); 
			table.addCell(p);

			// Cell 12 
			parag = "c \n" + getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_unitCell_c", null) 
			+ "\n gamma \n" + getCellParam(dataCollectionMapItem, "ScreeningOutputLattice_unitCell_gama", null) ;

			p = new Paragraph(parag, FONT_DOC_SMALL_CENTERED);
			p.setAlignment(Element.ALIGN_CENTER); 
			table.addCell(p);
		
		} else {
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
			table.addCell(" ");
		}
		// to avoid splitting table
		Table containingTable = new Table(1);
		containingTable.setCellsFitPage(true);
		containingTable.setBorder(0);
		containingTable.getDefaultCell().setBorderWidth(0);

		Cell cell = new Cell(table);
		containingTable.addCell(cell);
		
		document.add(containingTable);
						
		return;
	}
	
	/**
	 * get the value or replace by blank if null to fill a cell paragraph
	 * 
	 * @param param
	 * @param dataCollectionMap
	 * @throws Exception
	 */
	private String getCellParam(Map<String, Object> dataCollectionMap, String param, DecimalFormat df) throws Exception {

		String paramValue = "";
		if (dataCollectionMap.get(param) != null) {	
			if (df == null){
				paramValue = dataCollectionMap.get(param).toString();
			} else {
				paramValue = df.format(dataCollectionMap.get(param)).toString();
			}
		}
		return paramValue;
		
	}
	
	/**
	 * get the value as boolean to fill a cell and return a null value if no value or not an integer
	 * 
	 * @param param
	 * @param dataCollectionMap
	 * @throws Exception
	 */
	private Boolean getBoolean(Map<String, Object> dataCollectionMap, String param) throws Exception {

		Boolean cellBool = null;		
		if (dataCollectionMap.get(param) != null) {				
			cellBool = new Boolean (dataCollectionMap.get(param).toString());
		}
		return cellBool;		
	}

	/**
	 * returns a simple cell witha given value inside
	 * 
	 * @param value
	 * @return
	 */
	private Cell getCellValue(String value) {
		Cell cell = new Cell();
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.add(new Paragraph(value, FONT_DOC_BOLD));
		cell.setColspan(1);
		return cell;
	}

	/**
	 * returns a cell with a given image inside
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private Cell getCellImage(Map<String, Object> dataCollectionMapItem, String imageParam, float image_size) throws Exception {
		
		if (dataCollectionMapItem.get(imageParam) != null && !(dataCollectionMapItem.get(imageParam).toString()).equals("") ) {
			String imagePath = dataCollectionMapItem.get(imageParam).toString();			
			return this.getCellImage(imagePath, image_size);
		}
		return new Cell(new Paragraph("", FONT_DOC));
	}

	/**
	 * returns a cell with a given image inside
	 * 
	 * @param image
	 * @return
	 * @throws Exception
	 */
	private Cell getCellImage(String imagePath, float image_size) throws Exception {
		
		if (imagePath != null ) {
			String image = PathUtils.getPath(imagePath);
			try {				
				Image jpg1 = Image.getInstance(image);
				jpg1.scaleAbsolute(jpg1.getWidth() * image_size / jpg1.getHeight(), image_size);
				Cell cell = new Cell(jpg1);
				cell.setLeading(0);
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				return cell;
			} catch (IOException e) {
				return new Cell(new Paragraph(image + " not found", FONT_DOC));
			}
		}
		return new Cell(new Paragraph("", FONT_DOC));
	}
	private Chunk getChunkImage(String image) throws BadElementException, MalformedURLException, IOException {
		Image jpg = Image.getInstance(image);
		jpg.scaleAbsolute(jpg.getWidth() * 10 / jpg.getHeight(), 10);
		return new Chunk(jpg, 0, 0);
	}

	private Cell getEmptyCell(int colspan) throws Exception {
		Cell emptyCell = new Cell(new Paragraph("", FONT_DOC));
		emptyCell.setColspan(colspan);
		return emptyCell;
	}
	
	/**
	 * set the summary - IFX proposal
	 * 
	 * @param document
	 * @throws Exception
	 */
	private void setCrystalClassSummary(Document document, Map<String, String> mapDataCollectionGroupIdCClass) throws Exception {
		
		if (proposalDesc.toLowerCase().startsWith(Constants.PROPOSAL_CODE_FX) ) {
			
			IspybCrystalClass3Service ispybCrystalClassService = (IspybCrystalClass3Service) ejb3ServiceLocator
					.getLocalService(IspybCrystalClass3Service.class);
			List<IspybCrystalClass3VO> listOfCrystalClass = ispybCrystalClassService.findAll();
			List<Integer> listOfNbCrystalPerClass = getListOfNbCrystalPerClass(listOfCrystalClass, mapDataCollectionGroupIdCClass);

			document.add(new Paragraph(" "));
			document.add(new Paragraph("Summary:", FONT_TITLE));
			document.add(new Paragraph(" "));

			int NumColumnsCC = 2;
			Table tableCC = new Table(NumColumnsCC);
			int headerwidthsCC[] = { 30, 15 }; // percentage
			tableCC.setWidths(headerwidthsCC);

			tableCC.setWidth(50); // percentage
			tableCC.setPadding(3);
			tableCC.setCellsFitPage(true);
			tableCC.getDefaultCell().setBorderWidth(1);
			tableCC.getDefaultCell().setGrayFill(GREY_FILL_HEADER);
			tableCC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			tableCC.addCell(new Paragraph("Crystal class", FONT_DOC_BOLD));
			tableCC.addCell(new Paragraph("Number of crystals", FONT_DOC_BOLD));
			tableCC.getDefaultCell().setGrayFill(GREY_FILL_DATA);

			int nbCC = listOfCrystalClass.size();
			for (int cc = 0; cc < nbCC; cc++) {
				if (listOfNbCrystalPerClass.get(cc) > 0) {
					tableCC.addCell(new Paragraph(listOfCrystalClass.get(cc).getCrystalClassName() + " ("
							+ listOfCrystalClass.get(cc).getCrystalClassCode() + ")", FONT_DOC));
					tableCC.addCell(new Paragraph(listOfNbCrystalPerClass.get(cc).toString(), FONT_DOC));
				}
				LOG.debug("classe " + listOfCrystalClass.get(cc).getCrystalClassCode() + ": "
						+ listOfNbCrystalPerClass.get(cc).toString());
			}

			document.add(tableCC);

			// total
			// int nbPuckScreen = nbCrystal3;
			// int nbTotal = nbCrystal1 + nbCrystal2 + nbCrystalT;
			int nbPuckScreen = listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "PS"));
			// nbTotal = C+CR+T+SC+SCR
			int nbTotal = listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "C"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "SC"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "T"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "CR"))
					+ listOfNbCrystalPerClass.get(getCrystalClassIndex(listOfCrystalClass, "SCR"));
			document.add(new Paragraph(" "));
			document.add(new Paragraph("Total number of tests: " + new String(new Integer(nbTotal).toString()),
					FONT_DOC));
			document.add(new Paragraph("Nb of puck screens: " + new String(new Integer(nbPuckScreen).toString()),
					FONT_DOC));
			document.add(new Paragraph("Total number of samples: "
					+ new String(new Integer(nbTotal + (nbPuckScreen * 10)).toString()), FONT_DOC));
		}
	}

	private int getCrystalClassIndex(List<IspybCrystalClass3VO> list, String crystalClass) {
		int nb = list.size();
		for (int i = 0; i < nb; i++) {
			if (crystalClass != null && list.get(i).getCrystalClassCode().equals(crystalClass)) {
				return i;
			}
		}
		return -1;
	}
	
	
	private List<Integer> getListOfNbCrystalPerClass(List<IspybCrystalClass3VO> listOfCrystalClass,
			Map<String, String> mapDataCollectionGroupIdCClass) {
		
		List<Integer> listOfNbCrystalPerClass = new ArrayList<Integer>(); // number of crystals / crystal classes
		String crystalClass;

			// for MXPress users, the table with crystal class will be displayed
			// calculate number of Crystals of class 1,2, ... 5, T
			// Browse dataCollections
			int nbCC = listOfCrystalClass.size();
			for (int i = 0; i < nbCC; i++) {
				listOfNbCrystalPerClass.add(0);
			}
			for (Iterator<String> it = mapDataCollectionGroupIdCClass.keySet().iterator(); it.hasNext();) {
				crystalClass = mapDataCollectionGroupIdCClass.get(it.next());
				int idCc = getCrystalClassIndex(listOfCrystalClass, crystalClass);
				if (idCc != -1) {
					listOfNbCrystalPerClass.set(idCc, listOfNbCrystalPerClass.get(idCc) + 1);
				}
			}
		
		return listOfNbCrystalPerClass;
	}

	private String[] extractBestAutoproc(Map<String, Object> dataCollectionMapItem) throws Exception {

		String [] bestRmerge = null;
		String listString = (String)dataCollectionMapItem.get("completenessList");
		
		if (dataCollectionMapItem.get("completenessList") != null && !listString.isEmpty() && dataCollectionMapItem.get("AutoProc_spaceGroups") != null) {	
						
			listString.trim();
			List<String> completenessList = new ArrayList<String>(Arrays.asList((listString.split(","))));
			LOG.debug("completenessList = " + completenessList.toString());	
			List<String> spaceGroupsList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("AutoProc_spaceGroups")).trim().split(",")));
			LOG.debug("spaceGroupsList = " + spaceGroupsList.size() + spaceGroupsList.toString());	
			List<String> resolutionsLimitLowList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("resolutionsLimitLow")).trim().split(",")));
			LOG.debug("resolutionsLimitLowList = " + resolutionsLimitLowList.size() + resolutionsLimitLowList.toString());	
			List<String> resolutionsLimitHighList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("resolutionsLimitHigh")).trim().split(",")));
			LOG.debug("resolutionsLimitHighList = " + resolutionsLimitHighList.toString());				
			List<String> rmergesList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("rMerges")).trim().split(",")));
			LOG.debug("rmergesList = " + rmergesList.size() + rmergesList.toString() );	
			List<String> scalingStatisticsTypesList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("scalingStatisticsTypes")).trim().split(",")));
			LOG.debug("scalingStatisticsTypesList = " + scalingStatisticsTypesList.size() + scalingStatisticsTypesList.toString());	
			List<String> anomalousList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_anomalous")).trim().split(",")));
			LOG.debug("anomalousList = " + anomalousList.size() + anomalousList.toString());	
			
			bestRmerge = new String[18];
			int i = 0;
			Double rmergeMin = 1000.000;
			int indexRmergeMin = 0;
			Set<Integer> indexSet = new HashSet<Integer>();
			
			for (Iterator<String> iterator = scalingStatisticsTypesList.iterator(); iterator.hasNext();) {
				String type = (String) iterator.next();
				// select also no anom 		
				if (type.contains("innerShell") && (new Integer(anomalousList.get(i).trim())).intValue() < 1) {
					double rm = new Double(rmergesList.get(i)).doubleValue();
					LOG.debug("rm = " + rm);
					if (rm > 0 && rm < MIN_RMERGE) {
						indexSet.add(i);
						LOG.debug("index kept: " + i);
					} else if (rm > 0 && rm < rmergeMin) {
						rmergeMin = rm;
						indexRmergeMin = i;						
					}
				}
				i=i+1;
			}
			
			// select higher symmetry			
			if (!indexSet.isEmpty()) {
				String spgTemp;
				int spgNb = 0;				
				for (Iterator<Integer> iterator = indexSet.iterator(); iterator.hasNext();) {
					Integer index = (Integer) iterator.next();
					spgTemp = spaceGroupsList.get(index).trim();
					LOG.debug("index : " + index + " spgtemp: " + spgTemp);
					if (spgMap.get(spgTemp)!= null && spgNb <= spgMap.get(spgTemp).intValue() ) {
						spgNb = spgMap.get(spgTemp).intValue();
						LOG.debug("index : " + index + " spgNb: " + spgMap.get(spgTemp));
						indexRmergeMin = index;
					}
				}				
			}
			
			bestRmerge[0] = spaceGroupsList.get(indexRmergeMin);
			bestRmerge[1] = rmergesList.get(indexRmergeMin);
			bestRmerge[2]= completenessList.get(indexRmergeMin);
			bestRmerge[3] = resolutionsLimitLowList.get(indexRmergeMin) + "/" + resolutionsLimitHighList.get(indexRmergeMin);
			
			List<String> tmpList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_cell_a")).trim().split(",")));
			bestRmerge[4] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_cell_b")).trim().split(",")));
			bestRmerge[5] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_cell_c")).trim().split(",")));
			bestRmerge[6] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_cell_alpha")).trim().split(",")));
			bestRmerge[7] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_cell_beta")).trim().split(",")));
			bestRmerge[8] = tmpList.get(indexRmergeMin);
			tmpList = new ArrayList<String>(Arrays.asList(((String)dataCollectionMapItem.get("Autoprocessing_cell_gamma")).trim().split(",")));
			bestRmerge[9] = tmpList.get(indexRmergeMin);
			
			//outer
			int outerIndex = -1;
			if (scalingStatisticsTypesList.get(indexRmergeMin+1).contains("outerShell") ){
				outerIndex = indexRmergeMin+1;
			} else if (indexRmergeMin+2 <  scalingStatisticsTypesList.size() && scalingStatisticsTypesList.get(indexRmergeMin+2).contains("outerShell") ){
				outerIndex = indexRmergeMin+2;	
			}
			if (outerIndex > -1) {
				bestRmerge[10] = spaceGroupsList.get(outerIndex);
				bestRmerge[11] = rmergesList.get(outerIndex);
				bestRmerge[12]= completenessList.get(outerIndex);
				bestRmerge[13] = resolutionsLimitLowList.get(outerIndex) + "/" + resolutionsLimitHighList.get(outerIndex);
			}
		
			//overall
			int overallIndex=-1;
			if (indexRmergeMin-1 >= 0 && scalingStatisticsTypesList.get(indexRmergeMin-1).contains("overall")) {
				overallIndex = indexRmergeMin-1;
			} else if (indexRmergeMin-2 >= 0 && scalingStatisticsTypesList.get(indexRmergeMin-2).contains("overall")) {
				overallIndex = indexRmergeMin-2;
			} else if (indexRmergeMin+2 <  scalingStatisticsTypesList.size() && scalingStatisticsTypesList.get(indexRmergeMin+2).contains("overall")) {
				overallIndex = indexRmergeMin+2;
			} else if (indexRmergeMin+3 <  scalingStatisticsTypesList.size() && scalingStatisticsTypesList.get(indexRmergeMin+3).contains("overall")) {
					overallIndex = indexRmergeMin+3;
			}
				
			if (overallIndex > -1) {
				bestRmerge[14] = spaceGroupsList.get(overallIndex);
				bestRmerge[15] = rmergesList.get(overallIndex);
				bestRmerge[16]= completenessList.get(overallIndex);
				//TODO format to 2 figures after .
				bestRmerge[17] = resolutionsLimitLowList.get(overallIndex) + "/" + resolutionsLimitHighList.get(overallIndex);
			}
						
			LOG.info("bestRmerge = "  + bestRmerge[0] + "- " + bestRmerge[1]+ "- " + bestRmerge[2]+ "- " + bestRmerge[3]);	
		}
		
		return bestRmerge;		
	}
	
	private Chunk getCompletenessChunk(String completeness) {
		
		Chunk chu =  new Chunk( " ", FONT_DOC_SMALL_BOLD);	
		
		if (completeness != null && !completeness.isEmpty()) {		
			
			chu =  new Chunk( completeness, FONT_DOC_SMALL_BOLD);	
			chu.setBackground(BLUE_COLOR);
			if (completeness != null && new Double(completeness) < 90 ) {
				if (new Double(completeness) < 50) {
					chu.setBackground(RED_COLOR);
				} else {
					chu.setBackground(LIGHT_YELLOW_COLOR);
				}				
			}
		}
		return chu;
	}

}
