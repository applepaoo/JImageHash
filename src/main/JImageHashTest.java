package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.pragone.jphash.jpHash;
import com.pragone.jphash.image.radial.RadialHash;

import hashAlgorithms.AverageHash;
import hashAlgorithms.HashingAlgorithm;
import hashAlgorithms.PerceptiveHash;
import tw.com.ruten.util.ImageUtility;

public class JImageHashTest {

	public static void main(String[] args) throws IOException, ParseException {

		try {

			int bitResolution = 64;
			int originalSum = 0;
			int pathSum = 0;
			int sum = 0;

			System.out.println("inputPath: ");
			Scanner scanner = new Scanner(System.in);
			String inputPath = scanner.nextLine();
			System.out.println("outputPath: ");
			String outputPath = scanner.next();

			File file = new File(inputPath); // 讀取測試檔
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();

			String[] arrayBF = stringBuffer.toString().split("\n");// 切割字串
			JSONParser parser = new JSONParser();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
			FileWriter fw = new FileWriter(outputPath);
			originalSum = arrayBF.length;

			for (int i = 0; i < arrayBF.length; i++) { // 拚imagePath

				Object obj = parser.parse(arrayBF[i]);
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject jsonObject1 = new JSONObject();

				if (String.valueOf(jsonObject.get("G_IMG")).contains("(null)")) {// 過濾G_IMG為(null)

				} else {

					String imagePath = ImageUtility.getImgPath(String.valueOf(jsonObject.get("G_NO")),
							String.valueOf(jsonObject.get("USER_NICK")), String.valueOf(jsonObject.get("G_STORAGE")))
							+ String.valueOf(jsonObject.get("G_IMG")).substring(0,
									jsonObject.get("G_IMG").toString().length() - 4)
							+ "_s.jpg";
					File file2 = new File("/mnt/" + imagePath);
					pathSum = pathSum + 1;

					if (imagePath.contains("null") || imagePath.contains(",") || imagePath.contains("gif")) {

					} else if (file2.exists() && !file2.isDirectory()) {

						sum = sum + 1;

						// Date current = new Date();
						//
						// // JImagaHash
						// HashingAlgorithm hasher = new AverageHash(bitResolution);
						// HashingAlgorithm hasher2 = new PerceptiveHash(bitResolution);
						// BufferedImage img = (BufferedImage) ImageIO.read(file2);
						// String jimagePHash = String.valueOf(hasher2.hash(img).toString(16));
						// String jimageAHash = String.valueOf(hasher.hash(img).toString(16));
						//
						// // jphash
						// RadialHash hash = jpHash.getImageRadialHash("/mnt/" + imagePath);
						// String jpHash = String.valueOf(hash);
						//
						// // output json
						// jsonObject1.put("G_NO", String.valueOf(jsonObject.get("G_NO")));
						// jsonObject1.put("_SOUTCE_TIME", sdf.format(current));
						// jsonObject1.put("HASH_IMG",
						// String.valueOf(jsonObject.get("G_IMG")).substring(0,
						// jsonObject.get("G_IMG").toString().length() - 4) + "_s.jpg");
						// jsonObject1.put("IMG_HASH_V1", jpHash); // jphash
						// jsonObject1.put("IMG_HASH_V2", jimagePHash); // JImageHash P_hash
						// jsonObject1.put("IMG_HASH_V3", jimageAHash); // JImageHash A_hash
						// fw.write(jsonObject1.toString() + "\r\n");
						// System.out.println(sdf.format(current));
						// System.out.println(String.valueOf(file2)); // 印出路徑
						// System.out.println(jsonObject1); // 印出json

					}

				}

			}
			// fw.close();
			System.out.println("original: " + originalSum);
			System.out.println("not null: " + pathSum);
			System.out.println("exist: " + sum);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

}
