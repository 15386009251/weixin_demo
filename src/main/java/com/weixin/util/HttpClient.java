package com.weixin.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class HttpClient {

    private static final String GW_URL = "http://10.4.149.132:8888";
//private static final String GW_URL = "http://10.4.238.20:8888";
//private static final String GW_URL = "http://127.0.0.1:8888";

    public static String getAccessToken(String weiCurl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(weiCurl);
            URLConnection theConnection = url.openConnection();
            if (!(theConnection instanceof HttpURLConnection)) {
                return "";
            }
            connection = (HttpURLConnection) theConnection;
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(7000);
            connection.setRequestProperty("Content-type",
                    "text/plain; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "closed");
            // connection.getOutputStream().write(reqMessage.getBytes("UTF-8"));
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
            }
            InputStream inputStream = connection.getInputStream();

            byte[] b = new byte[1024];
            int count = inputStream.read(b);
            byte[] bb = new byte[count];
            System.arraycopy(b, 0, bb, 0, count);
            String resultJson = new String(bb, "UTF-8");
            return resultJson;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    /**
     * @param weiCurl
     * @param reqBody
     * @return
     */
    public static String uploadToGW(String weiCurl, MultipartFile multipartFile) {
        final String NEWLINE = "\r\n"; // 换行，或者说是回车
        final String PREFIX = "--"; // 固定的前缀
        final String BOUNDARY = "#"; // 分界线，就是上面提到的boundary，可以是任意字符串，建议写长一点，这里简单的写了一个#
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GW_URL + "/upload");
            URLConnection theConnection = url.openConnection();
            if (!(theConnection instanceof HttpURLConnection)) {
                return "";
            }
            connection = (HttpURLConnection) theConnection;
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Content-type",
                    "multipart/form-data;boundary=#");
            connection.setRequestProperty("qy-wx-req-url",
                    weiCurl);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "closed");
            // 设置文件长度
            byte[] writebytes = multipartFile.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            if (multipartFile != null && multipartFile.getSize() > 0) {
                dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);// 像请求体中写分割线，就是前缀+分界线+换行
                String fileName = multipartFile.getName(); // 通过文件路径截取出来文件的名称，也可以作文参数直接传过来
                // 格式是:Content-Disposition: form-data; name="请求参数名"; filename="文件名"
                // 我这里吧请求的参数名写成了uploadFile，是死的，实际应用要根据自己的情况修改
                // 不要忘了换行
                dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                        + "file" + "\"" + "; filename=\"" + fileName
                        + "\"" + NEWLINE);
                // 换行，重要！！不要忘了
                dos.writeBytes(NEWLINE);
                dos.write(multipartFile.getBytes()); // 上传文件的内容
                dos.writeBytes(NEWLINE); // 最后换行
            }
            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE); // 最后的分割线，与前面的有点不一样是前缀+分界线+前缀+换行，最后多了一个前缀
            dos.flush();
            connection.connect();
            dos.close();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
            }
            InputStream inputStream = connection.getInputStream();

            byte[] b = new byte[1024];
            int count = inputStream.read(b);
            byte[] bb = new byte[count];
            System.arraycopy(b, 0, bb, 0, count);
            String resultJson = new String(bb, "UTF-8");
            return resultJson;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }


    /**
     * @param weiCurl
     * @param reqBody
     * @return
     */
    public static String postToGW(String weiCurl, String reqBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GW_URL + "/post");
            URLConnection theConnection = url.openConnection();
            if (!(theConnection instanceof HttpURLConnection)) {
                return "";
            }
            connection = (HttpURLConnection) theConnection;
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Content-type",
                    "text/plain; charset=UTF-8");
            connection.setRequestProperty("qy-wx-req-url",
                    weiCurl);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "closed");
            // 设置文件长度
            byte[] writebytes = reqBody.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(reqBody.getBytes());
            outputStream.flush();
            outputStream.close();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
            }
            InputStream inputStream = connection.getInputStream();

            byte[] b = new byte[1024];
            int count = inputStream.read(b);
            byte[] bb = new byte[count];
            System.arraycopy(b, 0, bb, 0, count);
            String resultJson = new String(bb, "UTF-8");
            return resultJson;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    /**
     * @param weiCurl
     * @return
     */
    public static String getToGW(String weiCurl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(GW_URL + "/get");
            URLConnection theConnection = url.openConnection();
            if (!(theConnection instanceof HttpURLConnection)) {
                return "";
            }
            connection = (HttpURLConnection) theConnection;
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Content-type",
                    "text/plain; charset=UTF-8");
            connection.setRequestProperty("qy-wx-req-url",
                    weiCurl);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "closed");
            // connection.getOutputStream().write(reqMessage.getBytes("UTF-8"));
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
            }
            InputStream inputStream = connection.getInputStream();

            byte[] b = new byte[1024];
            int count = inputStream.read(b);
            byte[] bb = new byte[count];
            System.arraycopy(b, 0, bb, 0, count);
            String resultJson = new String(bb, "UTF-8");
            return resultJson;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}
