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

public class JImageHashPro {

	public static void main(String[] args) throws IOException, ParseException {

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

		for (int i = 0; i < arrayBF.length; i++) { // 拚imagePath

			try {

				Object obj = parser.parse(arrayBF[i]);
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject jsonObject1 = new JSONObject();

				if (String.valueOf(jsonObject.get("G_IMG")).contains("(null)")) {// 過濾G_IMG為(null)

				} else {
					String[] G_IMGPath = String.valueOf(jsonObject.get("G_IMG")).split(",");// 如果有多個G_IMG取第一個
					String G_IMGPath1 = G_IMGPath[0].toString(); // 第一個G_IMG
					if (G_IMGPath[0].contains(".")) { // 篩選副檔名

						String[] G_IMGPath2 = G_IMGPath1.split("\\."); // 擷取副檔名
						try {
							String imageName = G_IMGPath2[0]; // IMG name
							String str = G_IMGPath2[1]; // 副檔名名稱
							switch (str) {
							case "jpg":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw); // (副檔名, 接json,
																									// 出json, 第一張圖, 時間,
																									// 寫出)
								break;

							case "jpeg":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw);
								break;

							case "JPG":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw);
								break;

							case "JPEG":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw);
								break;

							case "png":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw);
								break;

							case "PNG":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw);
								break;

							case "Jpg":
								getFingerPrint(str, jsonObject, jsonObject1, imageName, sdf, fw);
								break;

							}
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;
						}

					}
				}

			} catch (IIOException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (StringIndexOutOfBoundsException e) {
				continue;
			} catch (Exception e) {
				continue;
			}

		}
		fw.close();

	}

	static void getFingerPrint(String str, JSONObject jsonObject, JSONObject jsonObject1, String imageName,
			SimpleDateFormat sdf, FileWriter fw) throws IOException {

		int bitResolution = 64;

		String imagePath = ImageUtility.getImgPath(String.valueOf(jsonObject.get("G_NO")),
				String.valueOf(jsonObject.get("USER_NICK")), String.valueOf(jsonObject.get("G_STORAGE"))) + imageName
				+ "_s." + str;

		File file2 = new File("/mnt/" + imagePath);

		if (imagePath.contains("null")) {

		} else {

			Date current = new Date();
			// HashingAlgorithm hasher = new AverageHash(bitResolution);
			HashingAlgorithm hasher2 = new PerceptiveHash(bitResolution);
			BufferedImage img = (BufferedImage) ImageIO.read(file2);
			String jimagePHash = String.valueOf(hasher2.hash(img).toString(16));
			// String jimageAHash = String.valueOf(hasher.hash(img).toString(16));

			// jphash
			RadialHash hash = jpHash.getImageRadialHash("/mnt/" + imagePath);
			String jpHash = String.valueOf(hash);

			// output json
			jsonObject1.put("G_NO", String.valueOf(jsonObject.get("G_NO")));
			jsonObject1.put("_SOURCE_TIME", sdf.format(current));
			jsonObject1.put("HASH_IMG", imageName);
			jsonObject1.put("IMG_HASH_V1", jpHash); // jphash
			jsonObject1.put("IMG_HASH_V2", jimagePHash); // JImageHash P_hash
			// jsonObject1.put("IMG_HASH_V3", jimageAHash); // JImageHash A_hash
			fw.write(jsonObject1.toString() + "\r\n");
			System.out.println(sdf.format(current));
			System.out.println(String.valueOf(file2)); // 印出路徑
			System.out.println(jsonObject1); // 印出json

		}
	}
}
