package com.maoshen.util.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsUtil {

    /**
     * 忽视证书HostName
     */
    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String s, SSLSession sslsession) {
            return true;
        }
    };

     /**
     * Ignore Certification
     */
    private static TrustManager ignoreCertificationTrustManger = new X509TrustManager() {


        private X509Certificate[] certificates;


        public void checkClientTrusted(X509Certificate certificates[],
                String authType) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = certificates;
            }


        }


        public void checkServerTrusted(X509Certificate[] ax509certificate,
                String s) throws CertificateException {
            if (this.certificates == null) {
                this.certificates = ax509certificate;
            }
        }


        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    };


    public static String getMethod(String urlString) {


        ByteArrayOutputStream buffer = new ByteArrayOutputStream(512);
        try {

            URL url = new URL(urlString);


            /*
             * use ignore host name verifier
             */
            HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();


            // Prepare SSL Context
            TrustManager[] tm = { ignoreCertificationTrustManger };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());


            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            connection.setSSLSocketFactory(ssf);
            
            InputStream reader = connection.getInputStream();
            byte[] bytes = new byte[512];
            int length = reader.read(bytes);


            do {
                buffer.write(bytes, 0, length);
                length = reader.read(bytes);
            } while (length > 0);


            // result.setResponseData(bytes);
            reader.close();
            
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        String repString= new String (buffer.toByteArray());
        return repString;
    }


    public static void main(String[] args) {
        String urlString = "https://openapi.youku.com/v2/videos/show_basic.json?video_url=http%3A%2F%2Fv.youku.com%2Fv_show%2Fid_XMzk4MzEwMzI4.html&client_id=b10e1ed88022517f";
        System.out.println(HttpsUtil.getMethod(urlString));
    }
}