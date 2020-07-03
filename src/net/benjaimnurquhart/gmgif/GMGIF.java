package net.benjaimnurquhart.gmgif;

import java.io.File;
import java.nio.file.Files;

import net.benjaminurquhart.gmparser.GMDataFile;
import net.benjaminurquhart.gmparser.resources.GIFBuilder;
import net.benjaminurquhart.gmparser.resources.SpriteResource;

public class GMGIF {

	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Usage: java -jar gmgif.jar <path/to/data> [scale]");
			return;
		}
		File file = new File(args[0]);
		if(!file.exists()) {
			System.out.println("File '" + file.getAbsolutePath() + "' does not exist");
			return;
		}
		int scale = 1;
		if(args.length > 1) {
			if(args[1].matches("\\d+")) {
				scale = Integer.parseInt(args[1]);
			}
			else {
				System.out.println("Invalid scaling '" + args[1] + ",' defaulting to " + scale);
			}
		}
		GMDataFile data = new GMDataFile(file);
		
		System.out.println(data);
		
		File folder = new File("Animations_" + data.getGameTitle());
		if(!folder.isDirectory()) {
			folder.renameTo(new File(folder.getName() + "_" + System.currentTimeMillis()));
			folder = new File("Animations_" + data.getGameTitle());
		}
		if(!folder.exists()) {
			folder.mkdirs();
		}
		File dest;
		for(SpriteResource resource : data.getSprites()) {
			if(resource.getFrames().length < 2) {
				System.out.println("Skipping sprite " + resource.getName().getString() + " as it is not animated");
				continue;
			}
			try {
				System.out.println(resource);
				dest = new File(folder, resource.getName().getString() + ".gif");
				Files.write(dest.toPath(), new GIFBuilder(resource).setScale(scale).build());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
