package com.maigo.cloud.authentication;

import com.maigo.cloud.model.User;
import com.maigo.cloud.service.ServiceManager;
import com.maigo.cloud.service.UserService;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.Charset;

public class AuthenticationManager
{
    private String username = "";
    private String realm = "";
    private String nonce = "";
    private String cnonce = "";
    private String nc = "";
    private String qop = "";
    private String digest_uri = "";
    private String charset = "";
    private String response = "";

    private String userPassword;

    private User user;
    private boolean isAuthenticatited;

    public boolean authenticate(String content)
    {
        String[] splitStrings = content.split(",");
        for(String string : splitStrings)
        {
            if(string.startsWith("username"))
            {
                string.trim();
                int start = string.indexOf("\"");
                int end = string.indexOf("\"", start+1);
                username = string.substring(start+1, end);
            }
            else if(string.startsWith("realm"))
            {
                string.trim();
                int start = string.indexOf("\"");
                int end = string.indexOf("\"", start+1);
                realm = string.substring(start+1, end);
            }
            else if(string.startsWith("nonce"))
            {
                string.trim();
                int start = string.indexOf("\"");
                int end = string.indexOf("\"", start+1);
                nonce = string.substring(start+1, end);
            }
            else if(string.startsWith("cnonce"))
            {
                string.trim();
                int start = string.indexOf("\"");
                int end = string.indexOf("\"", start+1);
                cnonce = string.substring(start+1, end);
            }
            else if(string.startsWith("digest-uri"))
            {
                string.trim();
                int start = string.indexOf("\"");
                int end = string.indexOf("\"", start+1);
                digest_uri = string.substring(start+1, end);
            }
            else if(string.startsWith("nc"))
            {
                string.trim();
                nc = string.substring(string.indexOf("=")+1);
            }
            else if(string.startsWith("qop"))
            {
                string.trim();
                qop = string.substring(string.indexOf("=")+1);
            }
            else if(string.startsWith("response"))
            {
                string.trim();
                response = string.substring(string.indexOf("=")+1);
            }
            else if(string.startsWith("charset"))
            {
                string.trim();
                charset = string.substring(string.indexOf("=")+1);
            }
        }

        if(!digest_uri.equals("xmpp/" + realm))
            return false;

        if(!qop.contains("auth"))
            return false;

        if(!charset.equals("utf-8"))
            return false;

        if(username.equals("") || realm.equals("") || nonce.equals("") || cnonce.equals("")
                || response.equals("") || digest_uri.equals("") || nc.equals(""))
            return false;

        //get the user password from database
        UserService userService = (UserService)ServiceManager.getInstance().getService("userService");
        user = userService.getUserByUsername(username);
        if(user == null)
            return false;   //user with this username is not exist.

        userPassword = user.getPassword();

        //start to compute the response value (base on http://wiki.xmpp.org/web/SASLandDIGEST-MD5)
        //note that the smack client did not put the authzid in A1 (in step3)
        //step 1:
        String X = username + ":" + realm + ":" + userPassword;

        //step 2:
        Digest digest = new MD5Digest();
        byte[] YBytes = new byte[digest.getDigestSize()];
        byte[] XBytes = X.getBytes(Charset.forName(charset));
        digest.update(XBytes, 0, XBytes.length);
        digest.doFinal(YBytes, 0);

        //step 3:
        byte[] A1Part1Bytes = YBytes;
        String A1Part2 = ":" + nonce + ":" + cnonce;
        byte[] A1Part2Bytes = A1Part2.getBytes();

        int length = A1Part1Bytes.length + A1Part2Bytes.length;
        byte[] A1Bytes = new byte[length];
        System.arraycopy(A1Part1Bytes, 0, A1Bytes, 0, A1Part1Bytes.length);
        System.arraycopy(A1Part2Bytes, 0, A1Bytes, A1Part1Bytes.length, A1Part2Bytes.length);

        //step 4:
        String A2 = "AUTHENTICATE:" + digest_uri;

        //step 5:
        String HA1 = get32HexDigitMD5(A1Bytes);

        //step 6:
        String HA2 = get32HexDigitMD5(A2.getBytes());

        //step 7:
        String KD = HA1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + HA2;

        //step 8:
        String Z = get32HexDigitMD5(KD.getBytes());

        isAuthenticatited = Z.equals(response);

        return isAuthenticatited;
    }

    private String get32HexDigitMD5(byte[] bytes)
    {
        Digest digest = new MD5Digest();
        byte[] result = new byte[digest.getDigestSize()];
        digest.update(bytes, 0, bytes.length);
        digest.doFinal(result, 0);

        return Hex.toHexString(result);
    }

    /**
     * generate the rspauth value. Only difference with calculation in method authenticate() is in step 4
     * @return rspauth
     */
    public String getRspauthValue()
    {
        if(!isAuthenticatited)
            throw new IllegalStateException("getRspauthValue is only available when authentication is pass " +
                    "by the method authenticate()");

        //start to compute the response value (base on http://wiki.xmpp.org/web/SASLandDIGEST-MD5)
        //note that the smack client did not put the authzid in A1 (in step3)
        //step 1:
        String X = username + ":" + realm + ":" + userPassword;

        //step 2:
        Digest digest = new MD5Digest();
        byte[] YBytes = new byte[digest.getDigestSize()];
        byte[] XBytes = X.getBytes(Charset.forName(charset));
        digest.update(XBytes, 0, XBytes.length);
        digest.doFinal(YBytes, 0);

        //step 3:
        byte[] A1Part1Bytes = YBytes;
        String A1Part2 = ":" + nonce + ":" + cnonce;
        byte[] A1Part2Bytes = A1Part2.getBytes();

        int length = A1Part1Bytes.length + A1Part2Bytes.length;
        byte[] A1Bytes = new byte[length];
        System.arraycopy(A1Part1Bytes, 0, A1Bytes, 0, A1Part1Bytes.length);
        System.arraycopy(A1Part2Bytes, 0, A1Bytes, A1Part1Bytes.length, A1Part2Bytes.length);

        //step 4:
        //note that the smack client drop the "AUTHENTICATE" when calculate the response expected from server
        String A2 = ":" + digest_uri;

        //step 5:
        String HA1 = get32HexDigitMD5(A1Bytes);

        //step 6:
        String HA2 = get32HexDigitMD5(A2.getBytes());

        //step 7:
        String KD = HA1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + HA2;

        //step 8:
        String Z = get32HexDigitMD5(KD.getBytes());

        return Z;
    }

    /**
     * return the user that is authenticated. null will be returned when is not authenticated.
     */
    public User getAuthenticatedUser()
    {
        if(!isAuthenticatited)
            return null;

        return user;
    }
}
