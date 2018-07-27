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
		File test = new File("airpods/airpods1.jpg");
		BufferedImage img1 = (BufferedImage) ImageIO.read(test);
		RadialHash hash1 = jpHash.getImageRadialHash("airpods/airpods1.jpg");

		File file = new File("D:\\workspace2\\JImageHash\\airpods");

		String[] filenames;

		if (file.isDirectory()) {

			filenames = file.list();
			HashingAlgorithm hasher = new AverageHash(bitResolution);
			HashingAlgorithm hasher2 = new PerceptiveHash(bitResolution);
			// 原始圖片
			BigInteger A_hash1 = hasher.hash(img1);
			String A_binaryHash1 = A_hash1.toString(2);
			BigInteger P_hash1 = hasher2.hash(img1);
			String P_binaryHash1 = P_hash1.toString(2);

			for (int i = 0; i < filenames.length; i++)

			{

				File test1 = new File(file + "/" + filenames[i]);
				BufferedImage img2 = (BufferedImage) ImageIO.read(test1);
				RadialHash hash2 = jpHash.getImageRadialHash(file + "/" + filenames[i]);

				// A_Hash
				BigInteger A_Hash2 = hasher.hash(img2);
				String A_binaryHash2 = A_Hash2.toString(2);

				// P_Hash
				BigInteger P_Hash2 = hasher2.hash(img2);
				String P_binaryHash2 = P_Hash2.toString(2);

				System.out.println(filenames[i]);
				System.out.println("P_Hash_fingerPrint: " + P_binaryHash2);

				if (P_binaryHash2.length() != A_binaryHash2.length()) {
					String new_A_binaryHash2 = A_binaryHash2
							+ String.format("%" + (P_binaryHash2.length() - A_binaryHash2.length()) + "s", "0")
									.replaceAll(" ", "0");
					System.out.println("A_Hash_fingerPrint: " + new_A_binaryHash2);
				} else {

					System.out.println("A_Hash_fingerPrint: " + A_binaryHash2);

				}

				int A_dis = hammingDistance(new BigInteger(A_binaryHash1), new BigInteger(A_binaryHash2));
				int P_dis = hammingDistance(new BigInteger(P_binaryHash1), new BigInteger(P_binaryHash2));
				System.out.println("A_Hash Diatance:" + A_dis);
				System.out.println("P_Hash Diatance:" + P_dis);
				System.out.println("Similarity: " + jpHash.getSimilarity(hash1, hash2));
				System.out.println("-----------");

			}

		}

	}

	// 計算兩指紋的hammingDiatance
	public static int hammingDistance(BigInteger i, BigInteger i2) {

		return i.xor(i2).bitCount();
	}

	public static double getSimilarity(RadialHash hash1, RadialHash hash2) {

		int N = hash1.getCoefficients().length;

		byte[] x_coeffs = hash1.getCoefficients();
		byte[] y_coeffs = hash2.getCoefficients();

		double r[] = new double[N];
		double sumx = 0.0;
		double sumy = 0.0;
		for (int i = 0; i < N; i++) {
			sumx += x_coeffs[i] & 0xFF;
			sumy += y_coeffs[i] & 0xFF;
		}
		double meanx = sumx / N;
		double meany = sumy / N;
		double max = 0;
		for (int d = 0; d < N; d++) {
			double num = 0.0;
			double denx = 0.0;
			double deny = 0.0;
			for (int i = 0; i < N; i++) {
				num += (x_coeffs[i] - meanx) * (y_coeffs[(N + i - d) % N] - meany);
				denx += Math.pow((x_coeffs[i] - meanx), 2);
				deny += Math.pow((y_coeffs[(N + i - d) % N] - meany), 2);
			}
			r[d] = num / Math.sqrt(denx * deny);
			if (r[d] > max)
				max = r[d];
		}
		return max; // To change body of created methods use File | Settings | File Templates.
	}

}
