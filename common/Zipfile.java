package common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.UnsupportedEncodingException;

public class Zipfile {

    public static byte[] compress(final String str) {
        if ((str == null) || (str.length() == 0)) {
            return null;
        }
        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(obj);
        } catch (IOException e){
            e.printStackTrace();
        }
        
        try {
           gzip.write(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            gzip.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        return obj.toByteArray();
    }

    public static String decompress(final byte[] compressed) {
        String outStr = "";
        if ((compressed == null) || (compressed.length == 0)) {
            return "";
        }
        if (isCompressed(compressed)) {
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
            } catch (IOException e){
                e.printStackTrace();
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    outStr += line;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            outStr = new String(compressed);
        }
        return outStr;
    }

    public static boolean isCompressed(final byte[] compressed) {
        return (compressed[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
    public static void main(String[] args){
		byte[] a=null;
		String d="";
		 a =compress("Hello world");
		 d=decompress(a);
		 System.out.println(a);
		System.out.println(d);	
		}
}
