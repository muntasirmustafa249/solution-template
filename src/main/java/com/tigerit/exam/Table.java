/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tigerit.exam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 *
 * @author Muntasir
 */
public class Table{
    private String tableName;
    private String alias;
    private int rowNum;
    private int columnNum;
    public String[] columnNames;
    public ArrayList<int[]> data;
    
    public Table(){}
    public Table(Table table) // clone constructor
    {
        this.tableName = table.getTableName();
        this.rowNum = table.rowNum;
        this.columnNum = table.columnNum;
        
        this.columnNames = new String[this.columnNum];
        System.arraycopy(table.columnNames, 0, this.columnNames, 0, this.columnNum);
        
        this.data = (ArrayList<int[]>) table.data.clone();
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    public void setRowNum(int rowNum)
    {
        this.rowNum = rowNum;
    }
    public void setColumnNum(int columnNum)
    {
        this.columnNum = columnNum;
        this.columnNames = new String[this.columnNum];
    }
    public String getTableName()
    {
        return tableName;
    }
    public String getAlias()
    {
        return alias;
    }
    public int getRowNum()
    {
        return rowNum;
    }
    public int getColumnNum()
    {
        return columnNum;
    }
    
    public Table crossJoin(Table table1, Table table2)
    {
        Table result = new Table();
        
        int resultColumnNumber = table1.getColumnNum()+table2.getColumnNum();
        int resultRowNumber = table1.getRowNum()+table2.getRowNum();
        
        result.setColumnNum(resultColumnNumber);
        result.setRowNum(resultRowNumber);
        
        result.data = new ArrayList<int[]>();
        
        String[] table1ColumnNames = table1.columnNames;
        String[] table2ColumnNames = table2.columnNames;
        
        for(int i = 0;i < table1ColumnNames.length;i++)
        {
            if(table1.getAlias() == null)
                table1ColumnNames[i] = table1.getTableName()+"."+table1ColumnNames[i];
            else
                table1ColumnNames[i] = table1.getAlias()+"."+table1ColumnNames[i];
        }
        for(int i = 0;i < table2ColumnNames.length;i++)
        {
            if(table2.getAlias() == null)
                table2ColumnNames[i] = table2.getTableName()+"."+table2ColumnNames[i];
            else
                table2ColumnNames[i] = table2.getAlias()+"."+table2ColumnNames[i];
        }
        
        System.arraycopy(table1ColumnNames, 0, result.columnNames, 0, table1ColumnNames.length);
        System.arraycopy(table2ColumnNames, 0, result.columnNames, table1ColumnNames.length, table2ColumnNames.length);
        
        for(int i = 0;i < table1.data.size();i++)
        {
            int[] table1Row = table1.data.get(i);
            for(int j = 0;j < table2.data.size();j++)
            {
                int[] table2Row = table2.data.get(j);
                
                int[] resultRow = new int[table1Row.length + table2Row.length];
                System.arraycopy(table1Row, 0, resultRow, 0, table1Row.length);
                System.arraycopy(table2Row, 0, resultRow, table1Row.length, table2Row.length);
                
                result.data.add(resultRow);
            }
        }
        
        return result;
    }
    
    public Table sortData(Table table)
    {
        int rowNum = table.getRowNum();
        int columnNum = table.getColumnNum();
        
        String[] numbersInRows = new String[rowNum];
        
        for(int x = 0;x < rowNum;x++)
        {
            numbersInRows[x] = "";
            int[] row = table.data.get(x);
            for(int y = 0;y < columnNum;y++)
            {
                numbersInRows[x] = numbersInRows[x] + row[y];
            }
        }
        
        for(int c = 0;c < (rowNum - 1);c++)
        {
            for(int d = 0;d < (rowNum - c - 1);d++)
            {
                if(numbersInRows[c].compareTo(numbersInRows[d]) > 0)
                {
                    String temp = numbersInRows[c];
                    numbersInRows[c] = numbersInRows[d];
                    numbersInRows[d] = temp;
                    
                    Collections.swap(table.data, c, d);
                }
                else if(numbersInRows[c].compareTo(numbersInRows[d]) == 0)
                {
                    int[] row1 = table.data.get(c);
                    int[] row2 = table.data.get(d);
                    
                    for(int x = 0;x < columnNum;x++)
                    {
                        String num1 = "" + row1[x];
                        String num2 = "" + row2[x];
                        
                        if(num1.compareTo(num2) > 0)
                        {
                            Collections.swap(table.data, c, d);
                            break;
                        }
                    }
                }
            }
        }
        
        return table;
    }
    
    public Table innerJoin(Table table, String condition)
    {
        Table result = new Table();
        result.data = new ArrayList<int[]>();
        
        result.setColumnNum(table.getColumnNum());
        result.columnNames = table.columnNames;
        int resultRowCount = 0;
        
        String[] conditionParts = condition.split("=");
        String conditionPart1 = conditionParts[0].trim();
        String conditionPart2 = conditionParts[1].trim();
        
        int conditionPart1Address = -1, conditionPart2Address = -1;
        
        for(int i = 0, done = 0;i < table.columnNames.length;i++)
        {
            if(Objects.equals(table.columnNames[i], conditionPart1))
            {
                conditionPart1Address = i;
                done++;
            }
            if(Objects.equals(table.columnNames[i], conditionPart2))
            {
                conditionPart2Address = i;
                done++;
            }
            
            if(done >= 2)
                break;
        }
        
        if(conditionPart1Address < 0 || conditionPart2Address < 0)
        {
            //System.err.println("JOIN condition invalid");
            //System.exit(1); // invalid query
        }
        
        for(int i = 0;i < table.data.size();i++)
        {
            int[] row = table.data.get(i);
            
            if(row[conditionPart1Address] == row[conditionPart2Address])
            {
                result.data.add(row);
                resultRowCount++;
            }
        }
        
        result.setRowNum(resultRowCount);
        
        return result;
    }
    
    public Table select(Table table, String selectPart)
    {
        Table result = new Table();
        
        if(Objects.equals(selectPart, "*")) // in the case of "SELECT *"
        {
            result = new Table(table);
            
            for(int i = 0;i < result.getColumnNum();i++)
            {
                String[] columnNameParts = result.columnNames[i].split("\\.");
                result.columnNames[i] = columnNameParts[1];
            }
        }
        else
        {
            String[] selectParts = selectPart.split(",");
            int resultColumnNum = selectParts.length;
            
            result.setRowNum(table.getRowNum());
            result.setColumnNum(resultColumnNum);
            
            result.columnNames = new String[resultColumnNum];
            
            for(int i = 0;i < resultColumnNum;i++)
            {
                result.columnNames[i] = selectParts[i].trim();
            }
            
            int[] columnAddress = new int[resultColumnNum];
            
            for(int i = 0;i < resultColumnNum;i++)
            {
                for(int j = 0;j < table.getColumnNum();j++)
                {
                    if(Objects.equals(result.columnNames[i], table.columnNames[j]))
                    {
                        columnAddress[i] = j;
                        break;
                    }
                }
            }
            
            result.data = new ArrayList<int[]>();
            int resultRowNum = result.getRowNum();
            
            for(int i = 0;i < resultRowNum;i++)
            {
                int[] row = new int[resultColumnNum];
                for(int j = 0;j < resultColumnNum;j++)
                {
                    row[j] = table.data.get(i)[columnAddress[j]];
                }
                result.data.add(row);
            }
            
            for(int i = 0;i < result.getColumnNum();i++)
            {
                String[] columnNameParts = result.columnNames[i].split("\\.");
                result.columnNames[i] = columnNameParts[1];
            }
        }
        
        result = result.sortData(result);
        
        return result;
    }
}
