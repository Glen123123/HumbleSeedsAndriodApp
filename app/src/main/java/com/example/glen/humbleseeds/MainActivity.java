package com.example.glen.humbleseeds;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    private TextView tempJsonItem;
    private TextView moistJsonItem;
    private TextView dateJsonItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHit = (Button) findViewById(R.id.btnHit);

        //btnHit.setBackgroundColor(Color.GREEN);

        tempJsonItem = (TextView)findViewById(R.id.tempJsonItem);
        moistJsonItem = (TextView)findViewById(R.id.moistJsonItem);
        dateJsonItem = (TextView)findViewById(R.id.dateJsonItem);

        //run the json fetch when on create runs
        JSONTask jtask = new JSONTask();
        jtask.execute("https://pay-me.ie/humble/sensorAPI.php?username=glen&password=1234");

        btnHit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                JSONTask jtask = new JSONTask();
                jtask.execute("https://pay-me.ie/humble/sensorAPI.php?username=glen&password=1234");
            }
        });


    }


    private class  JSONTask extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            URL url = null;
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){

            super.onPostExecute(result);

            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String temp = null;
            String date = null;
            String moist = null;
            try {
                moist = jsonObj.getString("moist");
                temp = jsonObj.getString("temp");
                date = jsonObj.getString("date");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(temp);
            System.out.println(moist);
            System.out.println(date);

            tempJsonItem.setText("Current Temp: "+temp+"\u00b0"+"C");
            moistJsonItem.setText("Current Moisture: "+moist+"%");
            dateJsonItem.setText("Last Updated "+date);




        }
    }

//    public void onClickRefresh(View view) throws IOException {
//        TextView temp = (TextView) findViewById(R.id.temp);
//        TextView moist = (TextView) findViewById(R.id.moist);
//
//        temp.setText("Ho");
//        moist.setText("There");
//
//    }




}