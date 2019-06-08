package com.mycompany.client;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Client {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://localhost:4567/file");   

            String auth = "piano";     
            String name = "username_metadata.csv";
            
            StringBody authType = new StringBody(auth);
            StringBody fileName = new StringBody(name);
            
            FileBody bin = new FileBody(new File("./csv_files/" + name));
            
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("authType", authType)
                    .addPart("fileName", fileName)
                    .addPart("uploaded_file", bin)
                    .build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        
    }
}
