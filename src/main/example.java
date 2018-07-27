package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

import com.sun.jmx.snmp.SnmpUnknownSubSystemException;

import hashAlgorithms.AverageHash;
import hashAlgorithms.DifferenceHash;
import hashAlgorithms.HashingAlgorithm;
import hashAlgorithms.PerceptiveHash;

public class example {
	public static void main(String[] args) throws IOException {
		String[] ins = { "tmp/earth1_caption.jpg", "tmp/earth1_cropped.jpg", "tmp/earth1_resized.jpg", "tmp/earth2.jpg",
				"tmp/groot1.jpg", "tmp/ru1.jpg", "tmp/ru2.jpg", "tmp/ru3.jpg" };
		// Load images
		int bitResolution = 64;

		File test = new File("tmp/earth1.jpg");
		BufferedImage img1 = (BufferedImage) ImageIO.read(test);
		for (String in : ins) {
			File test1 = new File(in);
			BufferedImage img2 = (BufferedImage) ImageIO.read(test1);

			// Pick an algorithm
			HashingAlgorithm hasher = new AverageHash(bitResolution);
			HashingAlgorithm hasher2 = new PerceptiveHash(bitResolution);

			// 印出hash

			String newHash1 = String.format("%16s%16s", hasher.hash(img1).toString(16), hasher2.hash(img1).toString(16))
					.replaceAll(" ", "0");
			String newHash2 = String.format("%16s%16s", hasher.hash(img2).toString(16), hasher2.hash(img2).toString(16))
					.replaceAll(" ", "0");

			int dis = hammingDistance(new BigInteger(newHash1, 16), new BigInteger(newHash2, 16));
			System.out.println(in + ", dis= " + dis);
			System.out.println("hash_Goods1: " + newHash1); 
			System.out.println("hash_Goods2: " + newHash2);
			
		}
	}

	
	
	// 計算兩指紋的hammingDiatance
	public static int hammingDistance(BigInteger i, BigInteger i2) {

		return i.xor(i2).bitCount();
	}
	
	public static String hexToBinary(String hexnumber) {
		BigInteger temp = new BigInteger(hexnumber, 16);
		return temp.toString(2);

	}
}
