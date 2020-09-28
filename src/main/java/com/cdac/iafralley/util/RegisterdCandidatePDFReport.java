package com.cdac.iafralley.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.cdac.iafralley.Dao.RalleyCandidateDetailsDAO;
import com.cdac.iafralley.controllers.RalleyRegistrationFormController;
import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RallyApplicantAllocation;
import com.cdac.iafralley.entity.RallySlotMaster;
import com.cdac.iafralley.services.RalleyDetailsService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@PropertySource({"classpath:applicantfilepath.properties"})
public class RegisterdCandidatePDFReport
{
	
	
	private static final Logger logger = LoggerFactory.getLogger(RegisterdCandidatePDFReport.class);
	
	@Value("${applicant.filepath}")
	private static String APPLICANT_IMAGE_PATH;
	
	public static String convertDate(Date d)
	{
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		String s = formatter.format(d);
		return s;
	}
	
	public static String convertTimeStampDate(Date d)
	{
		Date date=new Date(d.getTime());
		Format formatter = new SimpleDateFormat("HH:mm");
		String s = formatter.format(date);
		return s;
	}
	
	
	
	
public static void createPDF(RalleyCandidateDetails candidate,RalleyDetails rd,RallyApplicantAllocation rdata,RallySlotMaster slotdata,String FILE_PATH) {
    try
    {
    	Files.createDirectories(Paths.get("/IAFRalleyReport"));
    	System.out.println(FILE_PATH+rdata.getCandidate_registration_no()+"_"+candidate.getId()+".pdf");
    	
    	Document document = new Document(PageSize.A4);
    	//document.setMargins(50, 50, 50, 50); 
       PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_PATH+rdata.getCandidate_registration_no()+"_"+candidate.getId()+".pdf"));
       document.open();
      
       InputStream resource= RegisterdCandidatePDFReport.class.getResourceAsStream("/images/watermark.jpg");

      
     logger.info(resource.toString());
     
       Image waterMarkImage = Image.getInstance(ImageIO.read(resource),null);
       
       
       
       
       
       //Get width and height of whole page
       float pdfPageWidth = document.getPageSize().getWidth();
       float pdfPageHeight = document.getPageSize().getHeight();
       
       
       System.out.println("w:"+pdfPageWidth+" h:"+pdfPageHeight);

       //Set waterMarkImage on whole page
       writer.getDirectContentUnder().addImage(waterMarkImage,
                    400, 0, 0, 460, 100, 100);
       
       Rectangle rect= new Rectangle(577,825,18,15); // you can resize rectangle 
       rect.enableBorderSide(1);
       rect.enableBorderSide(2);
       rect.enableBorderSide(4);
       rect.enableBorderSide(8);
      // rect.setBorderColor(BaseColor.BLACK);
     //  rect.setBorderWidth(1);
       document.add(rect);
       
      
       InputStream resource2= RegisterdCandidatePDFReport.class.getResourceAsStream("/images/badge.png");
      
     //  logger.info();
       
       Image image1 = Image.getInstance(ImageIO.read(resource2),null);
	     image1.scaleAbsolute(100f, 100f);//image width,height	
	     image1.setAlignment(Image.ALIGN_CENTER);
	   
	     document.add(image1);

	   //heading
//	     Font Hf = new Font(FontFamily.TIMES_ROMAN, 25.0f, Font.BOLD, BaseColor.WHITE);
//	     Chunk Hc = new Chunk("CENTRAL AIRMEN SELECTION BOARD", Hf);
//	    
//	    // Hc.setBackground(BaseColor.BLUE);
//	    Hc.setTextRise(-5f);
//	     Hc.setBackground(BaseColor.BLUE);
//	     Paragraph Hp = new Paragraph(Hc);
//	     Hp.setAlignment(Element.ALIGN_CENTER);
//	     
//	     
//	     Hp.setSpacingBefore(2f);
	    
	      
	       InputStream resource3= RegisterdCandidatePDFReport.class.getResourceAsStream("/images/Heading.JPG");
	      
	     Image Hp = Image.getInstance(ImageIO.read(resource3),null);
	     Hp.scaleAbsolute(500f, 35f);//image width,height	
	     Hp.setAlignment(Image.ALIGN_CENTER);
	   
	     document.add(Hp);
      //provisional Title 
       Font f = new Font();
       f.setStyle(Font.BOLD);
       f.setStyle(Font.UNDERLINE);
       f.setSize(15);
       
       
       URL fontURL = RegisterdCandidatePDFReport.class.getResource("/images/arialbd.ttf");
       FontFactory.register(fontURL.toString(), "test_font");
       Font Arialfont = FontFactory.getFont("test_fon");
      Arialfont.setStyle(Font.BOLD);
      Arialfont.setStyle(Font.UNDERLINE);
      Arialfont.setSize(15);
	  
       Paragraph heading = new Paragraph("PROVISIONAL ADMIT CARD",Arialfont);
       
       
       heading.setAlignment(Element.ALIGN_CENTER);
       
       Paragraph Rheading=new Paragraph ("IAF RECRUITMENT RALLY "+rd.getCity_name(),Arialfont);
       Rheading.setAlignment(Element.ALIGN_CENTER);
       Rheading.setSpacingBefore(10f);
       
       Paragraph IntakeH=new Paragraph("INTAKE 01/2021",Arialfont);
       IntakeH.setAlignment(Element.ALIGN_CENTER);
       IntakeH.setSpacingBefore(5f);
       document.add(heading);
       document.add(Rheading);
       document.add(IntakeH);
       

	    
	   
	     
	     

	//Inserting Table in PDF
	     float[] columnWidths2 = {2, 5, 2,5};
	     PdfPTable table=new PdfPTable(columnWidths2);
	     table.setWidthPercentage(100f);
	     
	     
	     PdfPCell cell = new PdfPCell (new Paragraph ("RALLY REGISTRATION NUMBER:"));
	     PdfPCell regno = new PdfPCell (new Paragraph (rdata.getCandidate_registration_no()));
	     cell.setColspan (2);
	     regno.setColspan(2);
	      cell.setHorizontalAlignment (Element.ALIGN_LEFT);
	      cell.setPadding (8.0f);
	      regno.setHorizontalAlignment (Element.ALIGN_CENTER);
	      regno.setPadding (8.0f);
	      
	      PdfPCell cellA = new PdfPCell (new Paragraph ("ACKNOWLEDGEMENT NUMBER:"));
		     PdfPCell regnoA = new PdfPCell (new Paragraph (rdata.getCandidate_acknowledgement_no()));
		     cellA.setColspan (2);
		     regnoA.setColspan(2);
		      cellA.setHorizontalAlignment (Element.ALIGN_LEFT);
		      cellA.setPadding (8.0f);
		      regnoA.setHorizontalAlignment (Element.ALIGN_CENTER);
		      regnoA.setPadding (8.0f);
	     
	     
               PdfPCell cell1 = new PdfPCell (new Paragraph ("PERSONAL DETAIL"));
		      cell1.setColspan (4);
		      cell1.setHorizontalAlignment (Element.ALIGN_LEFT);
		      cell1.setPadding (8.0f);
		      cell1.setBackgroundColor (new BaseColor (135,206,250));
		      
		      PdfPCell cell2 = new PdfPCell (new Paragraph ("Qualification DETAIL"));

		      cell2.setColspan (4);
		      cell2.setHorizontalAlignment (Element.ALIGN_LEFT);
		      cell2.setPadding (8.0f);
		      cell2.setBackgroundColor (new BaseColor (135,206,250));
		      
		      PdfPCell cell3 = new PdfPCell (new Paragraph ("VENU DETAIL"));

		      cell3.setColspan (4);
		      cell3.setHorizontalAlignment (Element.ALIGN_LEFT);
		      cell3.setPadding (8.0f);
		      cell3.setBackgroundColor (new BaseColor (135,206,250));
		      
		      table.addCell(cell);
		      table.addCell(regno);
		      
		      table.addCell(cellA);
		      table.addCell(regnoA);
		      
		      
		      table.addCell(cell1);
		      





		      						               

			table.addCell("Name:");
			table.addCell(candidate.getName());
			table.addCell("Father Name:");
			table.addCell(candidate.getFathername());
			table.addCell("Email id:");
			table.addCell(candidate.getEmailid());
			table.addCell("Contact no:");
			table.addCell(candidate.getContactno());
			table.addCell("DOB:");
			table.addCell(convertDate(candidate.getDateOfBirth()));
			table.addCell("Maritial Status");
			Boolean status=candidate.isMaritial_status();
			if(status == true)
			table.addCell("Married");
			else
				table.addCell("UnMarried");
				
			table.addCell("Height:");
			table.addCell(candidate.getHeight());
			table.addCell("Domicile-State");
			
			  table.addCell(candidate.getState());
			
			  table.addCell("Domicile-City");
				
		      table.addCell(candidate.getCity());
		      table.addCell("");table.addCell("");
		      
		      table.addCell(cell2);
		      
			table.addCell("Degree:");
			table.addCell(candidate.getPassed_exam_detail());
			table.addCell("Degree Details:");
			table.addCell(candidate.getOtherDetailPassedDetail());
			table.addCell("Passing Percentage:");
			table.addCell(candidate.getPassed_exam_percentage().toString());
			table.addCell("English Percentage:");
			table.addCell(candidate.getEnglish_percentage().toString());
		      
		      
		      
		      
		      table.addCell(cell3);
		      
		      table.addCell("Date of Reporting");
		     table.addCell(convertDate(slotdata.getRallyDate()));
		      table.addCell("Time of Reporting");
		      
		      table.addCell(slotdata.getRallyReportTime());
		      PdfPCell venucell=new PdfPCell(new Phrase("Venu Details: "+rd.getVenue_details()));
		      venucell.setColspan(4);
		      venucell.setRowspan(2);
		      table.addCell(venucell);
		     
		      
		      
		      
		      
		      
		      
		      
		      
		      
		      table.setSpacingBefore(30.0f);       // Space Before table starts, like margin-top in CSS
		      table.setSpacingAfter(18.0f);        // Space After table starts, like margin-Bottom in CSS								          
		      
		     
		      							          
		      
		     
		    //  document.add(table);
		      
		   //List base Admit card
		      List CDetails=new List(List.ORDERED,List.NUMERICAL);
		      CDetails.setIndentationLeft(10);
		     Font Df=new Font();
		     Df.setStyle(Font.BOLD);
		    
		     Df.setSize(14);
		     
		      
		      CDetails.add(new ListItem(" Registration Number: "+rdata.getCandidate_registration_no(),Df));
		      Paragraph N=new Paragraph(" Name: "+candidate.getName(),Df);
		      N.setSpacingAfter(6f);
		      N.setSpacingBefore(6f);
		      CDetails.add(new ListItem(N));
		      Paragraph FN=new Paragraph(" Father's Name: "+candidate.getFathername(),Df);
		      FN.setSpacingAfter(6f);
		     
		      CDetails.add(new ListItem(FN));
		      Paragraph DOB=new Paragraph(" Date of Birth: "+convertDate(candidate.getDateOfBirth()),Df);
		      DOB.setSpacingAfter(6f);
		      
		      CDetails.add(new ListItem(DOB));
		      Paragraph StateCity=new Paragraph(" District & State: "+candidate.getCity()+" , "+candidate.getState().toUpperCase(),Df);
		      StateCity.setSpacingAfter(6f);
		      
		      
		      CDetails.add(new ListItem(StateCity));
		      
		      Paragraph RDT=new Paragraph(" Reporting Date & Time: "+convertDate(slotdata.getRallyDate())+", "+slotdata.getRallyReportTime(),Df);
		      RDT.setSpacingAfter(6f);
		      
		      CDetails.add(new ListItem(RDT));
		      
		      Paragraph VD=new Paragraph(" Reporting Venue: "+rd.getVenue_details(),Df);
		      VD.setSpacingAfter(6f);
		      
		      CDetails.add(new ListItem(VD));
		      CDetails.setAlignindent(true);
		      
		      PdfPTable CDtable=new PdfPTable(1);
		      PdfPCell CDtableCell=new PdfPCell();
		   // Creating an ImageData object   
		      
		      
		     // File imgFile = new  File(FILE_PATH+candidate.getCandidate_photograph());
		      Path file = Paths.get(FILE_PATH+"applicantDetails/"+candidate.getCandidate_photograph());
		      logger.info(FILE_PATH+""+Files.exists(file));
		      
		      BufferedImage image = ImageIO.read(new FileInputStream(file.toString()));
		    //  for(String fileNames : file.list()) System.out.println(fileNames);
		     
		      Image imgdata = Image.getInstance(image,null);
		      imgdata.scaleAbsolute(100f, 110f);
		      imgdata.setAbsolutePosition(435f,455f);
		      imgdata.setBorder(Rectangle.BOX); 
		      imgdata.setBorderColor(BaseColor.BLACK);
		      imgdata.setBorderWidth(1f);
		      
            //  imgdata.BorderWidth = 3.0f;
              //imgdata.BorderColor = Color.RED; 
		   //   imgdata.setBorder(Rectangle.BOX);
		    //  imgdata.setAbsolutePosition(500f, 650f);
		                   
		   
		    //  PdfPCell imageCell=new PdfPCell();
		     // imageCell.addElement(imgdata);
		      //imageCell.disableBorderSide(Rectangle.BOX);
		      CDtableCell.addElement(CDetails);
		     // CDtableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		     CDtableCell.setBorderWidth(0);
		    //  CDtable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		      
		      CDtable.setWidthPercentage(100f);
			   CDtable.addCell(CDtableCell);
			 //  CDtable.addCell(imageCell);
			   CDtable.setSpacingBefore(18f);
			   CDtable.setSpacingAfter(10f);
			   
		      
		      document.add(CDtable);
		      document.add(imgdata);
		    
		      
		      Paragraph Instheading = new Paragraph("IMPORTANT INSTRUCTIONS FOR CANDIDATES",Arialfont);
		       
		       
		      Instheading.setAlignment(Element.ALIGN_CENTER);
		      Instheading.setSpacingAfter(15f);
		      
		       
		       document.add(Instheading);
		       
		       Paragraph instruction = new Paragraph();
		       instruction.add("Candidates are to mandatorily carry the following documents in original while reporting at the venue of recruitment rally as per the date & time mentioned above:-");
		       instruction.setFirstLineIndent(40);
		       document.add(instruction);
		       
		       List ordered=new List(List.ORDERED,List.ALPHABETICAL);
		       ordered.setLowercase(List.LOWERCASE);
		       ordered.setIndentationLeft(70);
		       ordered.setAutoindent(true);
		       ordered.add(new ListItem("Provisional Admit Card"));
		       ordered.add(new ListItem("Domicile Certificate"));
		       ordered.add(new ListItem("Mark sheets & Pass Certificate of Intermediate or equivalent"));
		       ordered.add(new ListItem("Pass Certificate of Martric"));
		       ordered.add(new ListItem("Valid Photo ID"));
		       ordered.add(new ListItem("NCC / SOAFP / Service Certificate, if applicable "));
		       
		       document.add(ordered);
		       Font note=new Font();
		       note.setStyle(Font.BOLD);
		       note.setSize(12);
		       document.add(new Paragraph("Note:-",note));
		       Paragraph spp=new Paragraph();
		      spp.setSpacingAfter(2f);
		       document.add(spp);
		       document.add(new Paragraph("(i) Failure to bring the above mentioned documents while reporting for the recruitment rally may result in cancellation of your candidature to appear in the selection test."));
		       document.add(new Paragraph(""));
		       document.add(new Paragraph("(ii)  Given the prevailing risk of COVID-19, candidates are to follow all COVID-19 protocols/ instructions/ preventive measures issued by Central/ State governments from time to time, throughout the duration of exam and during travel."));
		       


		       
		       
		      

	 
       
       document.close();
       writer.close();
       
       logger.info("pdf genreated successfully for"+candidate.getEmailid()+" with app id:"+candidate.getId());
       logger.info("update file name in DB");
       
      
       
       
       
       
       
    }
    catch(Exception ex){
    	ex.printStackTrace();
    	
    }
}

}


