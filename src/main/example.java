package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.imageio.ImageIO;

import com.pragone.jphash.jpHash;
import com.pragone.jphash.image.radial.RadialHash;
import com.sun.jmx.snmp.SnmpUnknownSubSystemException;

import hashAlgorithms.AverageHash;
import hashAlgorithms.DifferenceHash;
import hashAlgorithms.HashingAlgorithm;
import hashAlgorithms.PerceptiveHash;

public class example {
	public static void main(String[] args) throws IOException {
		// Load images
		int bitResolution = 64;
		File test = new File("tmp/earth1.jpg");
		BufferedImage img1 = (BufferedImage) ImageIO.read(test);
		// String[] ins = { "tmp/earth1_caption.jpg", "tmp/earth1_cropped.jpg",
		// "tmp/earth1_resized.jpg", "tmp/earth2.jpg",
		// "tmp/groot1.jpg", "tmp/ru1.jpg", "tmp/ru2.jpg", "tmp/ru3.jpg" };

		File file = new File("D:\\workspace2\\jphash\\tmp");
		String[] filenames;

		if (file.isDirectory()) {

			filenames = file.list();
			HashingAlgorithm hasher = new AverageHash(bitResolution);
			HashingAlgorithm hasher2 = new PerceptiveHash(bitResolution);
			BigInteger A_hash1 = hasher.hash(img1);
			String A_binaryHash1 = A_hash1.toString(2);

			BigInteger P_hash1 = hasher2.hash(img1);
			String P_binaryHash1 = P_hash1.toString(2);
			// System.out.println(binaryHash1);

			for (int i = 0; i < filenames.length; i++)

			{

				File test1 = new File("tmp/" + filenames[i]);
				BufferedImage img2 = (BufferedImage) ImageIO.read(test1);

				// A_Hash
				BigInteger A_Hash2 = hasher.hash(img2);
				String A_binaryHash2 = A_Hash2.toString(2);

				// P_Hash
				BigInteger P_Hash2 = hasher2.hash(img2);
				String P_binaryHash2 = P_Hash2.toString(2);

				String new_A_binaryHash2 = A_binaryHash2
						+ String.format("%" + (P_binaryHash2.length() - A_binaryHash2.length()) + "s", "0")
								.replaceAll(" ", "0");

				System.out.println(filenames[i]);
				//System.out.println("A_Hash_fingerPrint: " + A_binaryHash2);
				System.out.println("A_Hash_fingerPrint: " + new_A_binaryHash2);
				System.out.println("P_Hash_fingerPrint: " + P_binaryHash2);

				int A_dis = hammingDistance(new BigInteger(A_binaryHash1), new BigInteger(A_binaryHash2));
				int P_dis = hammingDistance(new BigInteger(P_binaryHash1), new BigInteger(P_binaryHash2));
				System.out.println("A_Hash Diatance:" + A_dis);
				System.out.println("P_Hash Diatance:" + P_dis);
				System.out.println("-----------");

			}

		}

		// for (String in : ins) {
		// File test1 = new File(in);
		// BufferedImage img2 = (BufferedImage) ImageIO.read(test1);
		//
		// File file = new File("D:\\workspace2\\jphash\\tmp");
		// String[] filenames;
		//
		// if (file.isDirectory()) {
		//
		// filenames = file.list();
		//
		// HashingAlgorithm hasher = new AverageHash(bitResolution);
		// HashingAlgorithm hasher2 = new PerceptiveHash(bitResolution);
		//
		// String newHash1 = String
		// .format("%16s%16s", hasher.hash(img1).toString(16),
		// hasher2.hash(img1).toString(16))
		// .replaceAll(" ", "0");
		// String newHash2 = String
		// .format("%16s%16s", hasher.hash(img2).toString(16),
		// hasher2.hash(img2).toString(16))
		// .replaceAll(" ", "0");
		//
		// int dis = hammingDistance(new BigInteger(newHash1, 16), new
		// BigInteger(newHash2, 16));
		// System.out.println(in + ", dis= " + dis);
		// System.out.println("hash_Goods1: " + newHash1);
		// System.out.println("hash_Goods2: " + newHash2);
		//
		// }
		//
		// }

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
