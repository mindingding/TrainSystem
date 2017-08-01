package UI;

import Station.*;
import Train.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadFile {
	@SuppressWarnings("deprecation")
	public enum FLAG{NEUTRAL, STRING, TIME, TRAIN_NUM};
	public enum ADDVALUESTATE{NEUTRAL, STATION, TIME};
	static int dir = -1;
	Train tempT = null;
	
	public ReadFile(){
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("./시간표.xlsx");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(fis); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int rowindex=0;
		int columnindex=0;	
		XSSFSheet sheet=workbook.getSheetAt(0);
		//행의 수
		int rows=sheet.getPhysicalNumberOfRows();
		
		FLAG type = FLAG.NEUTRAL;
		ADDVALUESTATE addtype = ADDVALUESTATE.NEUTRAL;
		
		for(rowindex=0;rowindex<rows;rowindex++){
		    //행을읽는다
		    XSSFRow row=sheet.getRow(rowindex);
		    if(row !=null){
		        //셀의 수
		        int cells=row.getPhysicalNumberOfCells();
		        for(columnindex=0;columnindex<=cells;columnindex++){
		            //셀값을 읽는다
		            XSSFCell cell=row.getCell(columnindex);
		            String value="";
		            //셀이 빈값일경우를 위한 널체크
		            if(cell==null){
		              //  continue;
		            }else{
		                //타입별로 내용 읽기
		                switch (cell.getCellType()){
		                case XSSFCell.CELL_TYPE_FORMULA:
		                    value=cell.getCellFormula();
		                    break;
		                case XSSFCell.CELL_TYPE_NUMERIC:
		                    value=cell.getNumericCellValue()+"";
		                    if( Double.parseDouble(value) < 1)
		                    	type = FLAG.TIME;
		                    else type = FLAG.TRAIN_NUM;
		                    break;
		                case XSSFCell.CELL_TYPE_STRING:
		                    value=cell.getStringCellValue()+"";
		                    type = FLAG.STRING;
		                    break;
		                case XSSFCell.CELL_TYPE_BLANK:
		                    value=cell.getBooleanCellValue()+"";
		                    break;
		                case XSSFCell.CELL_TYPE_ERROR:
		                    value=cell.getErrorCellValue()+"";
		                    break;
		                }
		            }
		            
		            if( type == FLAG.STRING && columnindex == 0){ //새로운 기차역 리스트추가
		            	columnindex++;
		            	if( addtype == ADDVALUESTATE.NEUTRAL) dir++;
		            	addtype = ADDVALUESTATE.STATION;
		            }
		            else if( type == FLAG.TRAIN_NUM && columnindex == 0){
		            	columnindex++;
		            	String traintype = row.getCell(columnindex).getStringCellValue() + "";

		            	tempT = new Train( traintype, (int)Double.parseDouble(value), dir);
		            	addtype = ADDVALUESTATE.TIME;
		            }
		            else addValue(value, addtype, columnindex);
		            
		       //     System.out.print(value + " ");
		        }
		  //      System.out.println();
		        if( addtype == ADDVALUESTATE.TIME){
		        	AdministerUI.TL.AddTrain(tempT);
		        	tempT = null; addtype = ADDVALUESTATE.NEUTRAL;
		        }
		    }
		}
	}
	
	public void addValue(String value, ADDVALUESTATE type, int columnindex){
		if( type == ADDVALUESTATE.STATION){
			Station temp = new Station(value, columnindex - 2);
		//	System.out.println(dir);
			AdministerUI.SM[dir].AddStation(temp);
		}
		else if( type == ADDVALUESTATE.TIME){
			if( value != ""){
				//tempT.addRoute(columnindex-2);
				tempT.addTime( Double.parseDouble(value)); 
			}
		}
	}
}
