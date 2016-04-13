package com.tigerit.exam;


import static com.tigerit.exam.IO.*;


import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Objects;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
    @Override
    public void run() {
        // your application entry point

        // sample input process
        //String string = readLine();

        //Integer integer = readLineAsInteger();

        // sample output process
        //printLine(string);
        //printLine(integer);
        
        solve();
        
    }
    
    public void solve()
    {
        int testCount;
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        testCount = Integer.parseInt(readInput(input)); //input.nextInt();
        
        for(int i = 1;i <= testCount;i++)
        {
            int tableCount = Integer.parseInt(readInput(input)); //input.nextInt();
            
            Table[] tables = new Table[tableCount];
            
            for(int j = 0;j < tableCount;j++)
            {
                tables[j] = new Table();
                String tableName = readInput(input);
                tables[j].setTableName(tableName);
                
                String[] columnAndRow = readInput(input).split(" ", 2);
                
                int columnNum = Integer.parseInt(columnAndRow[0]);
                int rowNum = Integer.parseInt(columnAndRow[1]);
                tables[j].setColumnNum(columnNum);
                tables[j].setRowNum(rowNum);
                
                String[] columnNames = readInput(input).split(" ", columnNum);
                System.arraycopy(columnNames, 0, tables[j].columnNames, 0, columnNum);
                
                tables[j].data = new ArrayList<int[]>();
                
                for(int k = 0;k < rowNum;k++)
                {
                    String[] rowStr = readInput(input).split(" ", columnNum);
                    int[] row = new int[columnNum];
                    for(int l = 0;l < columnNum;l++)
                    {
                        row[l] = Integer.parseInt(rowStr[l]);
                    }
                    tables[j].data.add(row);
                }
                
                //tables[j] = tables[j].sortData(tables[j]);
            }
            
            int queryNumber = Integer.parseInt(readInput(input));
            
            System.out.println("Test: "+i);
            
            for(int j = 1;j <= queryNumber;j++)
            {
                String query = "";
                for(int k = 0;k < 4;k++)
                {
                    query = query + " " + readInput(input);
                }
                readInput(input);
                
                String[] queryParts = query.split("\\s*FROM \\s*");
                
                String selectPart = queryParts[0].substring(7).trim(); // required
                
                String fromPart = queryParts[1].trim();
                
                String[] fromParts = fromPart.split("\\s*ON \\s*");
                String joinTable = fromParts[0].trim();
                String condition = fromParts[1].trim(); // required
                
                String[] joinTables = joinTable.split("\\s*JOIN \\s*");
                String table1Name = joinTables[0].trim();
                String table2Name = joinTables[1].trim();
                
                Table table1 = new Table();
                Table table2 = new Table();
                
                String[] table1Names = table1Name.split(" "); // required
                String[] table2Names = table2Name.split(" "); // required
                
                for(int k = 0;k < tableCount;k++)
                {
                    if(Objects.equals(tables[k].getTableName(), table1Names[0]))
                    {
                        table1 = new Table(tables[k]);
                        
                        if(table1Names.length == 2)
                        {
                            table1.setAlias(table1Names[1]);
                        }
                        break;
                    }
                }
                for(int k = 0;k < tableCount;k++)
                {
                    if(Objects.equals(tables[k].getTableName(), table2Names[0]))
                    {
                        table2 = new Table(tables[k]);
                        
                        if(table2Names.length == 2)
                        {
                            table2.setAlias(table2Names[1]);
                        }
                        break;
                    }
                }
                
                Table crossResult = table1.crossJoin(table1, table2);
                Table innerJoinResult = crossResult.innerJoin(crossResult, condition);
                Table finalResult = innerJoinResult.select(innerJoinResult, selectPart);
                
                String line;
                
                line = finalResult.columnNames[0];
                
                for(int l = 1;l < finalResult.getColumnNum();l++)
                {
                    line = line + " " + finalResult.columnNames[l];
                }
                
                System.out.println(line);
                
                for(int m = 0;m < finalResult.getRowNum();m++)
                {
                    int[] row = finalResult.data.get(m);
                    line = "" + row[0];
                    for(int n = 1;n < finalResult.getColumnNum();n++)
                    {
                        line = line + " " + row[n];
                    }
                    System.out.println(line);
                }
                
                System.out.println();
                
            }
        }
    }
    
    public static String readInput(BufferedReader reader)
    {
        String text;
        
        try
        {
            text = reader.readLine();
        }
        catch(IOException e)
        {
            text = null;
        }
        
        return text;
    }
}
