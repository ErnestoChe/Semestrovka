package sample.FileSelection;

import sample.DataBaseController.dbHandler;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;

public class Parser {
    ArrayList<String> values;
    Date[] date;
    double[] open;
    double[] high;
    double[] low;
    double[] close;

    public void readData(File file)
    {
        int i = 0;
        values = new ArrayList<>();
        try{
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();

            while (line != null) {
                values.add(line);
                //System.out.println(line);
                i++;
                // считываем остальные строки в цикле
                line = reader.readLine();
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.remove(0);
    }

    public void sendData(String user)
    {
        System.out.println("user is = " + user + " in sendData()");
        date = new Date[values.size()];
        open = new double[values.size()];
        high = new double[values.size()];
        low = new double[values.size()];
        close = new double[values.size()];

        int j = 0;
        for (String str: values) {
            String[] tmp = str.split(",");
            date[j] = convertToDate(tmp[2]);
            open[j] = Double.parseDouble(tmp[4]);
            high[j] = Double.parseDouble(tmp[5]);
            low[j] = Double.parseDouble(tmp[6]);
            close[j] = Double.parseDouble(tmp[7]);
            j++;
        }
        String dates = date[0] + "-" + date[j-1];
        //dbHandler.logData(user, 1, dates, 0, "");
        dbHandler.addData(user, date, open, high, low, close);
    }
    public static File fileDowndload(String urlLine){
        try {
            URL url = new URL(urlLine);
            InputStream inputStream = url.openStream();
            File file = new File("text.txt");
            if(!file.exists()) {
                Files.copy(inputStream, new File("text.txt").toPath());
            }else{
                System.out.println("Файл уже существует");
            }
            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Date convertToDate(String line){
        Date date = Date.valueOf(convert(line));
        return date;
    }
    public static String convert(String line){
        String a = line.substring(0,4);
        a +="-";
        a+=line.substring(4,6);
        a +="-";
        a+=line.substring(6,8);
        return a;
    }
}
