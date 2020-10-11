package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by vin on 6/4/2015.
 */
public class CryptographyFiles {

    private String iv = "fedf343@#%yu4321";
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private String SecretKey = "&*fg126789a@bcde";

    public void makeEncryptedAllFiles(FileInputStream file,int fileName) throws Exception {

        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        FileInputStream fis = (FileInputStream) file;



        File outfile = new File("F:\\Programming\\android\\Project\\cooking recipes\\sent\\Encrypted\\"+fileName+".xml");
        int read;
        if(!outfile.exists())
            outfile.createNewFile();
        FileOutputStream fos = new FileOutputStream(outfile);
        FileInputStream encfis = new FileInputStream(outfile);
        Cipher encipher = Cipher.getInstance("AES");
        encipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

        CipherInputStream cis = new CipherInputStream(fis, encipher);
        while((read = cis.read())!=-1)
        {
            fos.write((char)read);
            fos.flush();
        }
        fos.close();

    }

    public static void main(String[] args) throws Exception {
        String unencrypted = "F:\\Programming\\android\\Project\\cooking recipes\\sent\\UnEncrypted";
        File dir = new File(unencrypted);
        File[] files = dir.listFiles();
        CryptographyFiles encryption = new CryptographyFiles();
        int i = 1;
        for (File child : files) {
            encryption.makeEncryptedAllFiles(new FileInputStream(child),i);
            i++;
        }

    }


    public StringBuilder deCipherFile(InputStream file) throws Exception{

        ivspec = new IvParameterSpec(iv.getBytes());
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        int read;
        InputStream fis =  file;
        CipherInputStream cis = new CipherInputStream(fis, decipher);
        StringBuilder sb = new StringBuilder();
        while((read = cis.read())!=-1)
        {
            sb.append((char)read);

        }
        System.out.println(sb.toString());
        cis.close();
        return sb;

    }


}
