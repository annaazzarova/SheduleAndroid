/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parserspec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

/**
 *
 * @author Anna
 */
public class ParserSpec {

    static public ArrayList<Speciality> allSpec = new ArrayList<Speciality>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        String dir = new File(".").getAbsolutePath();
        String list[] = new File(dir + "\\src\\files\\").list();
        for(int i = 0; i < list.length; i++){
            if (list[i].endsWith(".xls")){
                ParseFaculty(dir + "\\src\\files\\" + list[i]);
            }
        }
        
        try(FileWriter writer = new FileWriter(dir + "\\src\\files\\" + "spec.txt", false))
        {
            for (int i = 0; i != allSpec.size(); ++i){
                writer.append(allSpec.get(i).faculty + "\t");
                writer.append(allSpec.get(i).name + "\t");
                writer.append(allSpec.get(i).group + "\t");
                writer.append(allSpec.get(i).code + "\t");
                writer.append(allSpec.get(i).type);
                if (i != allSpec.size()-1){
                    writer.append("\n");
                }
            }
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
    }
    
    public static void ParseFaculty(String path) throws Exception{
        InputStream in = new FileInputStream(path);
        HSSFWorkbook wb = new HSSFWorkbook(in);
        
        Sheet sheet = wb.getSheetAt(0);
        Cell tempCell = sheet.getRow(0).getCell(8);
        String curFaculty = tempCell.getStringCellValue();
        
        for (int i = 0; i != wb.getNumberOfSheets(); ++i){
            Sheet currSheet = wb.getSheetAt(i);
            Row currRow = currSheet.getRow(3);
            Iterator<Cell> cells = currRow.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING){
                    String currValue = cell.getStringCellValue();
                    if ((currValue != "") && (currValue != " ")){
                        StringTokenizer st = new StringTokenizer(currValue, "\t\n\r,.()-");
                        ArrayList<String> tempList = new ArrayList<String>();
                        Speciality currSpec = new Speciality();
                        while(st.hasMoreTokens()){
                            String temp = st.nextToken();
                            if (temp != " ")
                                tempList.add(temp);
                        }
                        currSpec.faculty = curFaculty;
                        currSpec.name = tempList.get(0);
                        currSpec.group = tempList.get(1);
                        currSpec.code = tempList.get(2);
                        currSpec.type = tempList.get(4);
                        
                        allSpec.add(currSpec);
                    }
                }
            }
        }
    }
    
}
