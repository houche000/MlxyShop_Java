package com.zbkj.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.zbkj.common.constants.OnePassConstants;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class RazerPayUtils {

    @Autowired
    private static RestTemplate restTemplate;

    public static boolean status;
    public static final String RAZER_MCH_KEY = "razer_mch_id";
    public static final String RAZER_VKEY = "razer_vkey";
    public static final String RAZER_SECRETKEY = "razer_secretkey";
    public static final String RAZER_GETWAY = "razer_getway";
    public static final String RAZER_PAY_URL = "razer_payment_url";
    public static final String RAZER_CALLBACKURL_URL = "razer_callbackurl_url";
    public static final String RAZER__URL = "razer_getway";
    public static String mpsmerchantid = "luckygpbuy";
    public static String vkey = "b1c2c3aa10858c4b283056fe6ca54efa";
    public static String secretkey = "43768003f5300ec4bbca641594b76701";
    public static String getway = "https://api.molpay.com/";
    public static String mpschannel;
    public static String mpsamount;
    public static String mpsorderid;
    public static String mpsbill_name;
    public static String mpsbill_email;
    public static String mpsbill_mobile;
    public static String mpsbill_desc;
    public static String mpscountry = "MY";
    public static String mpsvcode;
    public static String mpscurrency;
    public static String mpslangcode = "en";
    public static int mpstimer;
    public static String mpstimerbox = "#counter";
    public static String mpscancelurl = "";
    public static String mpsreturnurl = "";
    public static String mpsapiversion = "3.17";
    public static String your_process_status = "false";



    public RazerPayUtils() {
        // Silences is golden
    }

    public static Map<String, String> queryOrder(String amount, String txID) {

        String skey = hash(txID + mpsmerchantid + vkey + amount);
        String surl = "";
        String type = "0";

        // 0 = plain text result (default)
        // 1 = result via POST method

        String blocks[] = {
                amount, txID, mpsmerchantid, skey, surl, type
        };

        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("amount");
        parameter.add("txID");
        parameter.add("domain");
        parameter.add("skey");
        parameter.add("url");
        parameter.add("type");
        String ourl = getway + "/MOLPay/API/gate-query/index.php";
        String urlParameters = "?";
        urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
        for (int i = 1; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        urlParameters = ourl.concat(urlParameters);
        Map<String, String> map = new HashMap<>();
        try {
            URL url = new URL(urlParameters);

            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] split = line.split(": ");
                if (split.length > 1) {
                    map.put(split[0], split[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, String> querByOrderIdTwo(String amount, String oID) {
//        String skey = hash(oID + mpsmerchantid + vkey + amount);
//        String type = "0";
//        String req4token = "0";
//        Map<String, Object> uriVariables = new HashMap<>();
//        String ourl = getway + "/MOLPay/query/q_by_oid.php";
//        uriVariables.put("url", ourl);
//        uriVariables.put("amount", amount);
//        uriVariables.put("oID", oID);
//        uriVariables.put("domain", mpsmerchantid);
//        uriVariables.put("skey", skey);
//        uriVariables.put("type", type);
//        uriVariables.put("req4token", req4token);


        Map<String,String> map = new HashMap<>();

        try{

            String skey = hash(oID + mpsmerchantid + vkey + amount);
            // String url = "";
            String type = "0";
            String req4token = "0";


            String blocks[] = {
                    amount, oID, mpsmerchantid, skey, "", type, req4token
            };

            ArrayList<String> parameter = new ArrayList<String>();
            parameter.add("amount");
            parameter.add("oID");
            parameter.add("domain");
            parameter.add("skey");
            parameter.add("url");
            parameter.add("type");
            parameter.add("req4token");

            String ourl = getway + "/MOLPay/query/q_by_oid.php";
            String urlParameters = "?";
            urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
            for (int i = 1; i < parameter.size(); i++) {
                urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
            }

            urlParameters = ourl.concat(urlParameters);
            URL url = new URL(urlParameters);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            connection.connect();// 连接会话
// 获取输⼊流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
                String[] split = line.split(":");
                if (split.length > 1) {
                    map.put(split[0], split[1]);
                }
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
            System.out.println(sb.toString());

            System.out.println(map.toString());

            return map;
        }catch (Exception e){
        }
        return map;
    }

    public static Map<String, String> querByOrderId(String amount, String oID) {
        String skey = hash(oID + mpsmerchantid + vkey + amount);
        // String url = "";
        String type = "0";
        String req4token = "0";


        String blocks[] = {
                amount, oID, mpsmerchantid, skey, "", type, req4token
        };

        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("amount");
        parameter.add("oID");
        parameter.add("domain");
        parameter.add("skey");
        parameter.add("url");
        parameter.add("type");
        parameter.add("req4token");

        String ourl = getway + "/MOLPay/query/q_by_oid.php";
        String urlParameters = "?";
        urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
        for (int i = 1; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        urlParameters = ourl.concat(urlParameters);
        Map<String, String> map = new HashMap<>();
        try {
            URL url = new URL(urlParameters);

            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] split = line.split(": ");
                if (split.length > 1) {
                    map.put(split[0], split[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String refundOrder(String txnID) {

        String skey = hash(txnID + mpsmerchantid + vkey);
        String url = "";
        String type = "0";


        String blocks[] = {
                txnID, mpsmerchantid, skey, url, type
        };

        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("txnID");
        parameter.add("domain");
        parameter.add("skey");
        parameter.add("url");
        parameter.add("type");

        String ourl = "https://api.molpay.com/MOLPay/refundAPI/refund.php";
        String urlParameters = "?";
        urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
        for (int i = 1; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        urlParameters = ourl.concat(urlParameters);

        return urlParameters;
    }

    public static String q_oid_batch(String oID) {

        String skey = hash(oID + mpsmerchantid + vkey);
        String url = "";
        String type = "0";
        String format = "0";
        String req4token = "0";

        String blocks[] = {
                oID, mpsmerchantid, skey, url, type, format, req4token
        };

        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("oID");
        parameter.add("domain");
        parameter.add("skey");
        parameter.add("url");
        parameter.add("type");
        parameter.add("format");
        parameter.add("req4token");

        String ourl = "https://api.molpay.com/MOLPay/query/q_oid_batch.php";
        String urlParameters = "?";
        urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
        for (int i = 1; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        urlParameters = ourl.concat(urlParameters);

        return urlParameters;
    }

    public static String q_by_oids(String oIDs, String delimiter) {

        String skey = hash(mpsmerchantid + oIDs + vkey);
        String url = "";
        String type = "0";
        String format = "0";
        String req4token = "0";

        String blocks[] = {
                oIDs, delimiter, mpsmerchantid, skey, url, type, format, req4token
        };

        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("oIDs");
        parameter.add("delimiter");
        parameter.add("domain");
        parameter.add("skey");
        parameter.add("url");
        parameter.add("type");
        parameter.add("format");
        parameter.add("req4token");

        String ourl = "https://api.molpay.com/MOLPay/query/q_by_oids.php";
        String urlParameters = "?";
        urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
        for (int i = 1; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        urlParameters = ourl.concat(urlParameters);

        return urlParameters;
    }

    public static String q_by_tids(String tIDs) {

        String skey = hash(mpsmerchantid + tIDs + vkey);
        String url = "";
        String type = "0";
        String format = "0";
        String req4token = "0";

        String blocks[] = {
                tIDs, mpsmerchantid, skey, url, type, format, req4token
        };

        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("tIDs");
        parameter.add("domain");
        parameter.add("skey");
        parameter.add("url");
        parameter.add("type");
        parameter.add("format");
        parameter.add("req4token");

        String ourl = "https://api.molpay.com/MOLPay/query/q_by_tids.php";
        String urlParameters = "?";
        urlParameters = urlParameters.concat("" + parameter.get(0) + "=" + blocks[0]);
        for (int i = 1; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        urlParameters = ourl.concat(urlParameters);

        return urlParameters;
    }


    public static int getRandomOrderId() {
        int result = (int) (Math.random() * 10000);
        return result;
    }

    /** required java.security.MessageDigest **/

    /***********************************************************
     * Either two of method could be used for verification
     * @param input String text
     * @return MD5 Hashing
     * @throws java.security.NoSuchAlgorithmException
     ************************************************************/

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /***********************************************************
     * Get the current redirect URL based on demo type
     * e.g. usage for form action=""
     * @param input
     * @return
     ************************************************************/

    public static String url(String input) {
        String url = "";
        if (input.equals("production")) {
            url = "https://www.onlinepayment.com.my/MOLPay/API/seamless/3.17/js/MOLPay_seamless.deco.js";
        } else if (input.equals("sandbox")) {
            url = "https://sandbox.molpay.com/MOLPay/API/seamless/3.16/js/MOLPay_seamless.deco.js";
        }
        return url;
    }

    /***********************************************************
     * Key1 : Hashstring generated on Merchant system
     * either $merchant or $domain could be one from POST
     * and one that predefined internally
     * by right both values should be identical
     * @return Hash String
     * @throws java.security.NoSuchAlgorithmException
     ************************************************************/

    public static String generateKeys(String... args) {
        return hash(String.join("", args));
    }

    public static String verificationKey(String tranID, String orderid, String status, String domain, String amount, String currency, String paydate, String appcode, String vkey) {
        String key0 = hash(tranID + orderid + status + domain + amount + currency);
        return hash(paydate + domain + key0 + appcode + vkey);
    }


    public static String getYour_process_status() {
        return your_process_status;
    }

    /**
     *
     */
    public void TrustCert() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
    }

    /**
     * Request parameters:
     *
     * @param type       Demo type
     * @param skey       This is the data integrity protection hash string
     * @param tranID     Generated by MOLPay System. Integer, 10 digits. Unique transaction ID for tracking purpose
     * @param domain     Merchant ID in MOLPay system.
     * @param status     2-digit numeric value, 00 for Successful payment; 11 for failure; 22 for pending.
     * @param amount     2 decimal points numeric value. The total amount paid or to be paid for CASH payment request.
     * @param currency   2 or 3 chars (ISO-4217) currency code. Default currency is RM (indicating MYR) for Malaysia channels
     * @param paydate    Date/Time( YYYY-MM-DD HH:mm:ss). Date/Time of the transaction
     * @param orderid    Alphanumeric, 32 characters. Invoice or order number from merchant system.
     * @param appcode    Alphanumeric, 16 chars. Bank approval code. Mandatory for Credit Card. Certain channel returns empty value
     * @param error_code Error Codes section
     * @param error_desc Error message or description
     * @param channel    Predefined string in MOLPay system. Channel references for merchant system
     * @return Sent Parameters
     * @throws Exception
     */

    public String IPN_Return(String type, String skey, String tranID, String domain, String status, String amount, String currency, String paydate, String orderid, String appcode, String error_code, String error_desc, String channel) throws Exception {
        TrustCert();
        // Check either demo type is production or sandbox
        String url = CheckDemo(type);

        // each received values from MOLPay
        String blocks[] = {
                skey, tranID, domain, status, amount, currency, paydate, orderid, appcode, error_code, error_desc, channel
        };

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("skey");
        parameter.add("tranID");
        parameter.add("domain");
        parameter.add("status");
        parameter.add("amount");
        parameter.add("currency");
        parameter.add("paydate");
        parameter.add("orderid");
        parameter.add("appcode");
        parameter.add("error_code");
        parameter.add("error_desc");
        parameter.add("channel");

        // Add request header
        String USER_AGENT = "Mozilla/5.0";
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Dont change default values
        String urlParameters = "treq=1"; // Additional parameter for IPN. Value always set to 1.
        // Generate urlString for each received values from MOLPay
        for (int i = 0; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return urlParameters;
    }

    /**
     * @param type get demo type to either sandbox or production demo
     * @return demo type
     */
    public String CheckDemo(String type) {
        // Check either demo type is production or sandbox
        String url = "";
        if (type.equals("production")) {
            url = "https://onlinepayment.com.my/MOLPay/API/chkstat/returnipn.php";
        } else if (type.equals("sandbox")) {
            url = "https://sandbox.molpay.com/MOLPay/API/chkstat/returnipn.php";
        }
        return url;
    }

    public String IPN_Notification(String type, String nbcb, String skey, String tranID, String domain, String status, String amount, String currency, String paydate, String orderid, String appcode, String error_code, String error_desc, String channel) throws Exception {
        TrustCert();
        // Check either demo type is production or sandbox
        String url = CheckDemo(type);

        // each received values from MOLPay
        String blocks[] = {
                nbcb, skey, tranID, domain, status, amount, currency, paydate, orderid, appcode, error_code, error_desc, channel
        };

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
        ArrayList<String> parameter = new ArrayList<String>();
        parameter.add("nbcb");
        parameter.add("skey");
        parameter.add("tranID");
        parameter.add("domain");
        parameter.add("status");
        parameter.add("amount");
        parameter.add("currency");
        parameter.add("paydate");
        parameter.add("orderid");
        parameter.add("appcode");
        parameter.add("error_code");
        parameter.add("error_desc");
        parameter.add("channel");

        // Add request header
        String USER_AGENT = "Mozilla/5.0";
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Dont change default values
        String urlParameters = "treq=1"; // Additional parameter for IPN. Value always set to 1.
        // Generate urlString for each received values from MOLPay
        for (int i = 0; i < parameter.size(); i++) {
            urlParameters = urlParameters.concat("&" + parameter.get(i) + "=" + blocks[i]);
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return urlParameters;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMpsmerchantid() {
        return mpsmerchantid;
    }

    public void setMpsmerchantid(String mpsmerchantid) {
        this.mpsmerchantid = mpsmerchantid;
    }

    public String getVkey() {
        return vkey;
    }

    public void setVkey(String vkey) {
        this.vkey = vkey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getMpschannel() {
        return mpschannel;
    }

    public void setMpschannel(String mpschannel) {
        this.mpschannel = mpschannel;
    }

    public String getMpsamount() {
        return mpsamount;
    }

    public void setMpsamount(String mpsamount) {
        this.mpsamount = mpsamount;
    }

    public String getMpsorderid() {
        return mpsorderid;
    }

    public void setMpsorderid(String mpsorderid) {
        this.mpsorderid = mpsorderid;
    }

    public String getMpsbill_name() {
        return mpsbill_name;
    }

    public void setMpsbill_name(String mpsbill_name) {
        this.mpsbill_name = mpsbill_name;
    }

    public String getMpsbill_email() {
        return mpsbill_email;
    }

    public void setMpsbill_email(String mpsbill_email) {
        this.mpsbill_email = mpsbill_email;
    }

    public String getMpsbill_mobile() {
        return mpsbill_mobile;
    }

    public void setMpsbill_mobile(String mpsbill_mobile) {
        this.mpsbill_mobile = mpsbill_mobile;
    }

    public String getMpsbill_desc() {
        return mpsbill_desc;
    }

    public void setMpsbill_desc(String mpsbill_desc) {
        this.mpsbill_desc = mpsbill_desc;
    }

    public String getMpscountry() {
        return mpscountry;
    }

    public void setMpscountry(String mpscountry) {
        this.mpscountry = mpscountry;
    }

    public String getMpsvcode() {
        return mpsvcode;
    }

    public void setMpsvcode(String mpsvcode) {
        this.mpsvcode = mpsvcode;
    }

    public String getMpscurrency() {
        return mpscurrency;
    }

    public void setMpscurrency(String mpscurrency) {
        this.mpscurrency = mpscurrency;
    }

    public String getMpslangcode() {
        return mpslangcode;
    }

    public void setMpslangcode(String mpslangcode) {
        this.mpslangcode = mpslangcode;
    }

    public int getMpstimer() {
        return mpstimer;
    }

    public void setMpstimer(int mpstimer) {
        this.mpstimer = mpstimer;
    }

    public String getMpstimerbox() {
        return mpstimerbox;
    }

    public void setMpstimerbox(String mpstimerbox) {
        this.mpstimerbox = mpstimerbox;
    }

    public String getMpscancelurl() {
        return mpscancelurl;
    }

    public void setMpscancelurl(String mpscancelurl) {
        this.mpscancelurl = mpscancelurl;
    }

    public String getMpsreturnurl() {
        return mpsreturnurl;
    }

    public void setMpsreturnurl(String mpsreturnurl) {
        this.mpsreturnurl = mpsreturnurl;
    }

    public String getMpsapiversion() {
        return mpsapiversion;
    }

    public void setMpsapiversion(String mpsapiversion) {
        this.mpsapiversion = mpsapiversion;
    }
}
