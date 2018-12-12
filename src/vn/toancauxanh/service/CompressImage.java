package vn.toancauxanh.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.WebApps;

import com.liferay.portal.model.Image;

import vn.greenglobal.core.CoreObject;

public class CompressImage extends CoreObject<Image> {
	@SuppressWarnings("rawtypes")
	public static Media reduceImageQuality(long sizeThreshold, Media media, String destImg, String type) throws Exception {
		float quality = 1.0f;
		IIOImage image = null;
		
		if ("jpg".equals(type)) {
			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(media.getByteData()));
			image = new IIOImage(originalImage, null, null);
		} else {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(media.getByteData()));

			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

			ImageIO.write(newBufferedImage, "jpg", new File(destImg));
			
			File file = new File(destImg);
			FileInputStream inputStream = new FileInputStream(file);
	        BufferedImage originalImage = ImageIO.read(inputStream);
	        image = new IIOImage(originalImage, null, null);
	        type = "jpg";
		}
		
		long fileSize = media.getByteData().length;
		if (fileSize <= sizeThreshold) {
			return null;
		}

		Iterator iter = ImageIO.getImageWritersByFormatName(type);
		ImageWriter writer = (ImageWriter) iter.next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

		float percent = 0.1f; // 10% of 1

		while (fileSize > sizeThreshold) {
			if (percent >= quality) {
				percent = percent * 0.1f;
			}

			quality -= percent;

			File fileOut = new File(destImg);
			if (fileOut.exists()) {
				fileOut.delete();
			}
			FileImageOutputStream output = new FileImageOutputStream(fileOut);

			writer.setOutput(output);

			iwp.setCompressionQuality(quality);

			writer.write(null, image, iwp);

			File fileOut2 = new File(destImg);
			long newFileSize = fileOut2.length();
			if (newFileSize == fileSize) {
				break;
			} else {
				fileSize = newFileSize;
			}
			System.out.println("Quanlity = " + quality + ", new file size = " + fileSize / 1024f /1024f + "MB");
			output.close();
		}

		writer.dispose();
		AImage img = new AImage(new File(destImg));
		
		File fileOut = new File(destImg);
		if (fileOut.exists()) {
			fileOut.delete();
		}
		return img;
	}
	
	public static Media reduceImge(Media media) {
		long fileSize = media.getByteData().length;
		long maxSize = fileSize * 20 / 100;
		String type = media.getName().substring(media.getName().lastIndexOf(".") + 1);
		String destPath = new vn.toancauxanh.gg.model.Image().folderStore() + (media.getName() + Calendar.getInstance().getTimeInMillis()).hashCode() + ".jpg";
		/*if (fileSize > 2097152) {
			maxSize = fileSize * 20 / 100;
		} else  if (fileSize > 1572864 && fileSize <= 2097152) {
			maxSize = fileSize * 30 / 100;
		} else if (fileSize > 1048576 && fileSize <= 1572864) {
			maxSize = fileSize * 40 / 100;
		} else if (524288 < fileSize && fileSize <= 1048576) {
			maxSize = fileSize * 50 / 100;
		} else if (fileSize >= 362144 && fileSize <= 524288) {
			maxSize = fileSize * 80 / 100;
		}*/
		
		
			try {
				return reduceImageQuality(maxSize, media, destPath, type);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return media;
			}
		
		
		
	}

}
