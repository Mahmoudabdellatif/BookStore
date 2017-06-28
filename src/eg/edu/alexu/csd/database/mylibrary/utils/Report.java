package eg.edu.alexu.csd.database.mylibrary.utils;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class Report {
	
	public void prepareReports(Connection connection){
		
		JasperReportBuilder report1 = DynamicReports.report();//a new report
		report1
		  .columns(
		  	Columns.column("ISBN", "ISBN", DataTypes.stringType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT),
		  	Columns.column("Title", "title", DataTypes.stringType()),
		  	Columns.column("Price", "price", DataTypes.stringType()),
		    Columns.column("Total", "total", DataTypes.integerType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT)	
		  )
		  .title(//title of the report
				  Components.text("BookStore Report\n\nThe total sales for books in the previous month\n\n")
		  			.setHorizontalAlignment(HorizontalAlignment.CENTER)
		  )
		  .pageFooter(Components.pageXofY())//show page number on the page footer
		  .setDataSource("SELECT customer_purchases.ISBN, SUM(purchases_count) as total, book.price, book.title " +   
"FROM   customer_purchases, book " +  
"WHERE  cp_date > (NOW() - INTERVAL 1 MONTH) AND book.ISBN = customer_purchases.ISBN " + 
"group by customer_purchases.ISBN", connection);

		JasperReportBuilder report2 = DynamicReports.report();//a new report
		report2
		  .columns(
		  	Columns.column("Username", "user_name", DataTypes.stringType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT),
		  	Columns.column("FirstName", "F_name", DataTypes.stringType()),
		  	Columns.column("LastName", "L_name", DataTypes.stringType()),
		    Columns.column("Total", "total", DataTypes.integerType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT)	
		  )
		  .title(//title of the report
				  Components.text("BookStore Report\n\nThe top 5 customers who purchase the most purchase amount in descending order for the last three months\n\n")
		  			.setHorizontalAlignment(HorizontalAlignment.CENTER)
		  )
		  .pageFooter(Components.pageXofY())//show page number on the page footer
		  .setDataSource("Select user.user_name, F_name, L_name, SUM(purchases_count) AS total from user, customer_purchases where user.user_name = customer_purchases.user_name " +  
						"group by customer_purchases.user_name " +  
						"ORDER BY total DESC", connection);

		JasperReportBuilder report3 = DynamicReports.report();//a new report
		report3
		  .columns(
		  	Columns.column("ISBN", "ISBN", DataTypes.stringType())
		  		.setHorizontalAlignment(HorizontalAlignment.LEFT),
		  	Columns.column("Title", "title", DataTypes.stringType()),
		  	Columns.column("Total", "total", DataTypes.stringType()).setHorizontalAlignment(HorizontalAlignment.LEFT)	
		  )
		  .title(//title of the report
				  Components.text("BookStore Report\n\nThe top 10 selling books for the last three months\n\n")
		  			.setHorizontalAlignment(HorizontalAlignment.CENTER)
		  )
		  .pageFooter(Components.pageXofY())//show page number on the page footer
		  .setDataSource("Select book.ISBN, title, SUM(purchases_count) AS total from book, customer_purchases where book.ISBN = customer_purchases.ISBN " +
						"group by customer_purchases.ISBN "+ 
						"ORDER BY total DESC", connection);

		
		showReport(report1, "report1");
		showReport(report2, "report2");
		showReport(report3, "report3");
	}
	private void showReport(JasperReportBuilder report, String reportName){
		try {
			report.show();//show the report
			report.toPdf(new FileOutputStream(reportName+".pdf"));//export the report to a pdf file
		} catch (DRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
