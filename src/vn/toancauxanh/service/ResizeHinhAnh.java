package vn.toancauxanh.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;

import vn.toancauxanh.model.Setting;
import vn.toancauxanh.gg.model.Image;

import com.mysema.commons.lang.Pair;

public class ResizeHinhAnh {
	public static final float DEFAULT_M_SIZE = (float)235/432;
	public static final float DEFAULT_S_SIZE = (float)125/200;
	
	public static void saveMediumAndSmall2(Image image, String strFolderStore) throws IOException {
		System.out.println("saveMediumAndSmall2");
		String fileUrl = strFolderStore + image.getName();
		System.out.println("fileUrl: " + fileUrl);
		File file = new File(fileUrl);
		if (file.exists()){
			BufferedImage originalImage = ImageIO.read(file);
			List<Pair<Integer, Integer>> list_size = getHeightSmallAndMedium2();
			String extension = image.getName().substring(image.getName().lastIndexOf(".") + 1);			
			if (list_size.size() > 0) {
				String outFileStr = strFolderStore + "m_" + image.getName();
				File outFile = new File(outFileStr);				
				int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();		
				BufferedImage resizeImagePng = resizeImage(originalImage, type, list_size.get(0).getFirst(), list_size.get(0).getSecond());
				ImageIO.write(resizeImagePng, extension, outFile);
			}
			if (list_size.size() == 2) {
				String outFileStr = strFolderStore + "s_" + image.getName();
				File outFile = new File(outFileStr);				
				int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();		
				BufferedImage resizeImagePng = resizeImage(originalImage, type, list_size.get(1).getFirst(), list_size.get(1).getSecond());
				ImageIO.write(resizeImagePng, extension, outFile);
			}
		}
	}
	
	public static void saveMediumAndSmall2(Image image, String strFolderStore, float size) throws IOException {
		System.out.println("saveMediumAndSmall2");
		String fileUrl = strFolderStore + image.getName();
		System.out.println("fileUrl: " + fileUrl);
		File file = new File(fileUrl);
		if (file.exists()){
			BufferedImage originalImage = ImageIO.read(file);
			List<Pair<Integer, Integer>> list_size = getHeightSmallAndMedium2(size);
			String extension = image.getName().substring(image.getName().lastIndexOf(".") + 1);			
			if (list_size.size() > 0) {
				String outFileStr = strFolderStore + "m_" + image.getName();
				File outFile = new File(outFileStr);				
				int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();		
				BufferedImage resizeImagePng = resizeImage(originalImage, type, list_size.get(0).getFirst(), list_size.get(0).getSecond());
				ImageIO.write(resizeImagePng, extension, outFile);
			}
			if (list_size.size() == 2) {
				String outFileStr = strFolderStore + "s_" + image.getName();
				File outFile = new File(outFileStr);				
				int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();		
				BufferedImage resizeImagePng = resizeImage(originalImage, type, list_size.get(1).getFirst(), list_size.get(1).getSecond());
				ImageIO.write(resizeImagePng, extension, outFile);
			}
		}
	}
	
	public static void saveMediumAndSmall(Image image, String strFolderStore)
			throws IOException {
		System.out.println("saveMediumAndSmall");
		String fileUrl = strFolderStore + image.getName();
		System.out.println("fileUrl: " + fileUrl);
		
		File file = new File(fileUrl);
		if (file.exists()){
			BufferedImage originalImage = ImageIO.read(file);
			
			List<Pair<Integer, Integer>> list_size = getHeightSmallAndMedium(originalImage);
			String extension = image.getName().substring(image.getName().lastIndexOf(".") + 1);
			System.out.println("list_size: " + list_size);
			if (list_size.size() > 0) {
				BufferedImage resizeImageHintJpg = Scalr.resize(originalImage, 
						Scalr.Mode.FIT_EXACT, 
						list_size.get(0).getFirst(),
						list_size.get(0).getSecond(), 
						Scalr.OP_ANTIALIAS);
				
				System.out.println("resizeImageHintJpg: " + resizeImageHintJpg);
				String outFileStr = strFolderStore + "m_" + image.getName();
				File outFile = new File(outFileStr);
				System.out.println("outFileStr: " + outFileStr);
				System.out.println("outFile: " + outFile);
				ImageIO.write(resizeImageHintJpg, extension, outFile);
			}

			if (list_size.size() == 2) {
				BufferedImage resizeImageHintJpg = Scalr.resize(originalImage, 
						Scalr.Mode.FIT_EXACT, 
						list_size.get(1).getFirst(),
						list_size.get(1).getSecond(), 
						Scalr.OP_ANTIALIAS);
				ImageIO.write(resizeImageHintJpg, extension, new File(
						strFolderStore + "s_" + image.getName()));
			}
		}
		
	}

	private static List<Pair<Integer, Integer>> getHeightSmallAndMedium(
			BufferedImage originalImage) {
		int heightMedium = 0;
		int heightSmall = 0;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		List<Pair<Integer, Integer>> list = new ArrayList<>();
		Setting setting = new BasicService<>().core().getSetting();

			int widthMediumConf = setting.getWidthMedium();
			int widthSmallConf = setting.getWidthSmall();
			if (widthMediumConf != 0) {
				double tile_medium = (double) width / (double) widthMediumConf;
				heightMedium = (int) (height / tile_medium);
				Pair<Integer, Integer> pair_medium = new Pair<>(
						widthMediumConf, heightMedium);
				list.add(pair_medium);
			}
			if (widthSmallConf != 0) {
				double tile_small = (double) width / (double) widthSmallConf;
				heightSmall = (int) Math.round(height / tile_small);
				Pair<Integer, Integer> pair_small = new Pair<>(widthSmallConf,
						heightSmall);
				list.add(pair_small);
			}

		return list;
	}
	
	private static List<Pair<Integer, Integer>> getHeightSmallAndMedium2() {
		int heightMedium = 0;
		int heightSmall = 0;
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		Setting setting = new BasicService<>().core().getSetting();
		int widthMediumConf = setting.getWidthMedium();
		int widthSmallConf = setting.getWidthSmall();
		if (widthMediumConf != 0) {
			heightMedium = (int) (widthMediumConf*DEFAULT_M_SIZE);
			Pair<Integer, Integer> pair_medium = new Pair<>(widthMediumConf, heightMedium);
			list.add(pair_medium);
		}
		if (widthSmallConf != 0) {
			heightSmall = (int) (widthSmallConf*DEFAULT_S_SIZE);
			Pair<Integer, Integer> pair_small = new Pair<>(widthSmallConf,heightSmall);
			list.add(pair_small);
		}
		System.out.println("DEFAULT_M_SIZE: "+DEFAULT_M_SIZE);
		System.out.println("DEFAULT_S_SIZE: "+DEFAULT_S_SIZE);
		System.out.println("heightMedium: "+heightMedium);
		System.out.println("widthSmallConf: "+widthSmallConf);
		return list;
	}
	
	private static List<Pair<Integer, Integer>> getHeightSmallAndMedium2(float size) {
		int heightMedium = 0;
		int heightSmall = 0;
		List<Pair<Integer, Integer>> list = new ArrayList<>();
		Setting setting = new BasicService<>().core().getSetting();
		int widthMediumConf = setting.getWidthMedium();
		int widthSmallConf = setting.getWidthSmall();
		if (widthMediumConf != 0) {
			heightMedium = (int) (widthMediumConf*size);
			Pair<Integer, Integer> pair_medium = new Pair<>(widthMediumConf, heightMedium);
			list.add(pair_medium);
		}
		if (widthSmallConf != 0) {
			heightSmall = (int) (widthSmallConf*size);
			Pair<Integer, Integer> pair_small = new Pair<>(widthSmallConf,heightSmall);
			list.add(pair_small);
		}
		return list;
	}
	
	public static void resizeFitWidth(Image image, String strFolderStore) {
		String fileUrl = strFolderStore + image.getName();
		System.out.println("fileUrl: " + fileUrl);
		File file = new File(fileUrl);
		if (file.exists()){
			BufferedImage originalImage;
			String extension = image.getName().substring(image.getName().lastIndexOf(".") + 1);
			try {
				originalImage = ImageIO.read(file);
				int x = originalImage.getWidth()/originalImage.getHeight();
				BufferedImage resizeImagePng = resizeImage(originalImage, 1, 680, 680/x);
				String outFileStr = strFolderStore + "m_" + image.getName();
				File outFile = new File(outFileStr);
				ImageIO.write(resizeImagePng, extension, outFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private static BufferedImage resizeImage(BufferedImage originalImage,
			int type, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height,
				type);
		BufferedImage thumbImage = new BufferedImage(width, height,
				type);

		if((double)width/height >= (double)originalImage.getWidth()/originalImage.getHeight()){
		// Fit to width
			resizedImage = Scalr.resize(originalImage, Scalr.Mode.FIT_TO_WIDTH,
					width, height, Scalr.OP_ANTIALIAS);
			Graphics2D tGraphics2D = thumbImage.createGraphics(); //create a graphics object to paint to
			tGraphics2D.drawImage(resizedImage, 0, 0, width, height, 0, 
			(resizedImage.getHeight() - height)/2 , width, 
			(resizedImage.getHeight() - height)/2 + height, null); //draw the image scaled */
			tGraphics2D.dispose();
		// Fit to height  
		}else{
			resizedImage = Scalr.resize(originalImage, Scalr.Mode.FIT_TO_HEIGHT,
					width, height, Scalr.OP_ANTIALIAS);
			Graphics2D tGraphics2D = thumbImage.createGraphics(); //create a graphics object to paint to
			tGraphics2D.drawImage(resizedImage, 0, 0, width, height, (resizedImage.getWidth() - width)/2, 0,
			(resizedImage.getWidth() - width)/2 + width, height, null); //draw the image scaled */
			tGraphics2D.dispose();
		}
		return thumbImage;
	}
	
}