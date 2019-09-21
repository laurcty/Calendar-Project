package com.lucy.calendarproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class background extends AsyncTask<String, Void,String> {

    // This class was created with the help of the tutorial https://www.youtube.com/watch?v=4e8be8xseqE&t=19s

    AsyncTaskListener listener;
    AlertDialog dialog;
    Context context;

    public background(Context context) {
        this.context = context;
        listener= (AsyncTaskListener)context;
    }


    @Override
    protected void onPreExecute() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("Login Status");
    }
    @Override
    protected void onPostExecute(String s) {
        dialog.setMessage(s);
        dialog.show();

        System.out.println("I'm in the bit before calling updateResult!!!!!!!!!");
        listener.updateResult(s);

        /*
        if(s.contains("Login successful")) {
            Intent intent_name = new Intent();
            intent_name.setClass(context.getApplicationContext(),MainActivity.class);
            context.startActivity(intent_name);
        }else{
            dialog.show();
        }
        */

    }
    @Override
    protected String doInBackground(String... voids) {

        String result = "";
        String columnName1 = voids[0];
        String columnName2 = voids[1];
        String data1 = voids[2];
        String data2 = voids[3];
        String connstr = "http://192.168.1.113:8080/login.php";

        System.out.println("I'm in the doInBackground");

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops,"UTF-8"));
            String data = URLEncoder.encode(columnName1,"UTF-8")+"="+URLEncoder.encode(data1,"UTF-8")
                    +"&&"+URLEncoder.encode(columnName2,"UTF-8")+"="+URLEncoder.encode(data2,"UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
            String line ="";
            while ((line = reader.readLine()) != null)
            {
                result += line;
            }
            reader.close();
            ips.close();
            http.disconnect();
            return result;

        } catch (MalformedURLException e) {
            result = e.getMessage();
            System.out.println("In the first catch malformedURL: "+e);

        } catch (IOException e) {
            result = e.getMessage();
            System.out.println("In the second catch IOException: "+e);
        }

        //listener.updateResult(result);
        return result;
    }
}

