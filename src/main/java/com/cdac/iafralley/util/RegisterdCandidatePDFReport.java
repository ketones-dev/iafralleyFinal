package com.cdac.iafralley.util;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.cdac.iafralley.controllers.RalleyRegistrationFormController;
import com.cdac.iafralley.entity.RalleyCandidateDetails;
import com.cdac.iafralley.entity.RalleyDetails;
import com.cdac.iafralley.entity.RallyApplicantAllocation;
import com.cdac.iafralley.entity.RallySlotMaster;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
    	System.out.println(FILE_PATH+rdata.getCandidate_registration_no()+".pdf");
    	
    	Document document = new Document(PageSize.A4);
    	document.setMargins(50, 50, 50, 50); 
       PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_PATH+rdata.getCandidate_registration_no()+".pdf"));
       document.open();
       
       Rectangle rect= new Rectangle(577,825,18,15); // you can resize rectangle 
       rect.enableBorderSide(1);
       rect.enableBorderSide(2);
       rect.enableBorderSide(4);
       rect.enableBorderSide(8);
       rect.setBorderColor(BaseColor.BLACK);
       rect.setBorderWidth(1);
       document.add(rect);
       
       Font f = new Font();
       f.setStyle(Font.BOLD);
       f.setStyle(Font.UNDERLINE);
       f.setSize(17);
		//Inserting Image in PDF
       Paragraph heading = new Paragraph("PROVISIONAL ADMIT CARD",f);
       
       
       heading.setAlignment(Element.ALIGN_CENTER);
       
       document.add(heading);
       

	     Image image1 = Image.getInstance ("src/main/resources/images/badge.png");
	     image1.scaleAbsolute(50f, 50f);//image width,height	
	     image1.setAlignment(Image.LEFT);
	     
	     Image image2 = Image.getInstance ("src/main/resources/images/index.png");
	     image2.scaleAbsolute(50f, 50f);//image width,height	
	     image2.setAlignment(image2.RIGHT);
	     
	     float[] columnWidths = {2, 5, 2};
	     PdfPTable table2=new PdfPTable(columnWidths);
	     table2.setWidthPercentage(100f);
	     PdfPCell leftCell = new PdfPCell (image1);
	     leftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	     
	     table2.addCell(leftCell);

	     Font myFonColor = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.WHITE);
	     PdfPCell middleCell = new PdfPCell(new Paragraph("IAF RALLY RECRUITMENT ",myFonColor));
	     middleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	     middleCell.setPaddingTop(15.0f);
	     middleCell.setBackgroundColor(new BaseColor (135,206,250));
	     table2.addCell(middleCell);
	     PdfPCell rightCell = new PdfPCell (image2);
	     rightCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	     
	     table2.addCell(rightCell);
	     
	     

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
		      
		      table2.setSpacingBefore(30.0f);       // Space Before table starts, like margin-top in CSS
		      							          
		      
		      document.add(table2);
		      document.add(table);
		      
		      Paragraph Instheading = new Paragraph("IMPORTANT INSTRUCTIONS FOR CANDIDATES",f);
		       
		       
		      Instheading.setAlignment(Element.ALIGN_CENTER);
		      Instheading.setSpacingAfter(15f);
		      
		       
		       document.add(Instheading);
		       
		       Paragraph instruction = new Paragraph();
		       instruction.add("Candidates are to mandatorily carry following documents in original while reporting at the venue of recruitment rally as per the date & time given below:-");
		       instruction.setFirstLineIndent(40);
		       document.add(instruction);
		       
		       List ordered=new List(List.ORDERED,List.ALPHABETICAL);
		       ordered.setLowercase(List.LOWERCASE);
		       ordered.setIndentationLeft(70);
		       ordered.add(new ListItem("Provisional Admit Card"));
		       ordered.add(new ListItem("Domicile Certificate"));
		       ordered.add(new ListItem("Mark sheets & Pass Certificate of Intermediate or equivalent"));
		       ordered.add(new ListItem("Pass Certificate of Martric"));
		       ordered.add(new ListItem("Valid Photo ID"));
		       ordered.add(new ListItem("NCC / SOAFP / Service Certificate, if applicable "));
		       
		       document.add(ordered);
		       
		       document.add(new Chunk("Note:-"));
		       document.add(new Phrase(" Failure to bring the above mentioned documents while reporting for the recruitment rally may result in cancellation of your candidature to appear in the selection test."));
		       
		       


		       
		       
		      

	 
       
       document.close();
       writer.close();
       
       
       
       
       
    }
    catch(Exception ex){
    	ex.printStackTrace();
    	
    }
}

}


