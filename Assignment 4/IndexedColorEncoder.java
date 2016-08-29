import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Josh Johnson 1539024
 *
 * IndexedColor Encoder is a helper class for ImageAnalyzer.
 * It is where the methods for palette building and image encoding will be
 * implemented.
 * 
 * Students: Implement code wherever you see "TODO".
 */
public class IndexedColorEncoder {
	public static CustomHashtables customHashtables; // Your own hash table facility.
	CustomHashtables.SeparateChainingHashtable<Block, Integer> H ;
	CustomHashtables.SeparateChainingHashtable<Block, Integer> tht1;

	public Color[][] colorImg;
	public ArrayList<Block> sortedBlocks; // to store sorted blocks(list L)
	public Color[] palette;	// stores the first U elements of list L
	public int[][] encodedPixels;	// to store the value each pixel in the image is encoded to.
	int blockSize; // Controls how much colors are grouped during palette building.
	int hashFunctionChoice; // Either 1, 2, or 3. Controls whether to use h1, h2, or h3.
	ImageAnalyzer theMainApp;
	int w, h; // width and height of current image.
	ArrayList<BlockCountPair> L; // Used when building a palette and encoding pixels.
	public double error; // To hold the per-pixel lossy encoding error.

	/* Constructor sets up a link back to the application, so that parts of
	 * the GUI and the working image can be accessed from this class.
	 */
	public IndexedColorEncoder(ImageAnalyzer theMainApp) {
		this.theMainApp = theMainApp;
		customHashtables = new CustomHashtables(); // Getting ready for your own hash table.
		hashFunctionChoice = 1; // Default hash function number is 1.
		blockSize = 4; // Default blockSize is 4x4x4

	}
	public void setHashFunctionChoice(int hc) {
		hashFunctionChoice = hc;
		System.out.println("Hash function choice is now "+hashFunctionChoice);
	}

	public void setBlockSize(int bs) {
		blockSize = bs;
		System.out.println("Block size is now "+blockSize);
	}
	class BlockCountPair {
		Block b; Integer count;
		public BlockCountPair(Block b, Integer count) {
			this.b = b; this.count = count;
		}
	}

	/**
	 * Build the palette for the image. Also collects data to be used by the info() method. 
	 * @param paletteSize The size of the palette that the user selects.
	 */
	public void buildPalette(int paletteSize) {
		// Get the image we will be processing.
		BufferedImage I = theMainApp.biWorking;
		// Convert it to a 2-D array of Color objects.
		colorImg = theMainApp.storeCurrentPixels(I);
		// cache the width and height of this image:
		w = I.getWidth(); h = I.getHeight();

		//Set up a hash table for our blocks (the keys) and their counts (the values).
        H = customHashtables.new SeparateChainingHashtable<Block, Integer>(13, 2.0);  // TODO Change tableSize and rehashingThreshold if you need to.

		// Now we develop our implementation based on the pseudocode from Part I of the Assignment.

		//We scan through all the pixels of the source image I and for each pixel p:
        int maxCollisions = 0;
        int totalPutCollisions = 0;
        int totalGetCollisions = 0;
        int insertations = 0;
		for (int row=0; row<I.getHeight(); row++) {
			for (int col=0; col<I.getWidth(); col++) {
				int tempInt = I.getRGB(col, row);
				int tempBlue = (tempInt) & 0xFF;
				int tempGreen = (tempInt >> 8 ) & 0xFF;
				int tempRed = (tempInt >> 16) & 0xFF;
				Color tempColor = new Color(tempRed, tempBlue, tempGreen);
				Block tempBlock = new Block(tempColor);
				int tempCol = 0;
				if (H.getAndMore(tempBlock).val != null) {
					int occurences = H.getAndMore(tempBlock).val; 					
					tempCol = H.putAndMore(tempBlock, occurences + 1).k;
				} else {
					tempCol = H.putAndMore(tempBlock, 1).k;
				}
				if (tempCol > maxCollisions) {
					maxCollisions = tempCol;
				}
				totalPutCollisions += tempCol;
				totalGetCollisions += H.getAndMore(tempBlock).k;
				insertations++;
			}
			System.out.print(".");
		}
		System.out.println("Average number of collisions per insertion " + totalPutCollisions/insertations);
		System.out.println("Maximum number of collisions for any one insertion " + maxCollisions);
		System.out.println("Average number of collisions per get operation " + totalGetCollisions/insertations);
		//By this point, we have a hash table full of blocks and their counts.
		//Next we make a list L of the blocks and their counts by getting all of the pairs out of the hash table.

		// THIS PART OF THE ALGORITHM IS DONE FOR YOU:
		Set<Block> keys = H.keySet();
		L = new ArrayList<BlockCountPair>();
		Iterator<Block> ib = keys.iterator();
		while (ib.hasNext()) {
			Block b = ib.next();
			if ((Integer)(H.getAndMore(b)).val != null) {
				L.add(new BlockCountPair(b, (Integer)(H.getAndMore(b)).val));
			}			
		}
		// Now sort L by the weight values, so that the w values are in nonincreasing order. 
		// The largest is first.
		
		Collections.sort(L, new Comparator<BlockCountPair> (){
			public int compare(BlockCountPair b1, BlockCountPair b2) {
				return b2.count - b1.count; // Note the reverse order.
			}
		});
		//Assuming the palette should have U entries, choose the first U elements of L, and call this LU.
		int U = paletteSize;
		Color[] T = new Color[U]; // create an empty color table T.
		int s = blockSize;
		//System.out.println("Color table T has these entries:");
		
		// Optionally print out these color objects as you go.
		for (int i = 0; i < U; i++) {
			Block tempBlock = L.get(i).b;
			int tempRed = s * tempBlock.Br + s / 2;
			int tempGreen = s * tempBlock.Bg + s / 2;
			int tempBlue = s * tempBlock.Bb + s / 2;
			Color representative = new Color(tempRed, tempGreen, tempBlue);
			T[i] = representative;
		}
		
		palette = T; // save the palette

		// Now enable encoding:
		theMainApp.encodeSSItem.setEnabled(true);
		theMainApp.encodeFItem.setEnabled(true);
		// If using a small image, optionally show the hash table, for debugging:
		//System.out.println(H);
		// This assumes you have written a toString method for your hash table that
		// makes a nice display of the contents of the table.
		info();
	}

	/**
	 * A simple slow and simple encoder. It goes through each pixel and picks the best image from the color palette.
	 */
	public void encodeSlowAndSimple() {
		ImageAnalyzer.ElapsedTime essTime = theMainApp.new ElapsedTime();
		essTime.startTiming();
		// Allocate a buffer for the new image.
		encodedPixels = new int[h][w] ;	// to store the value each pixel in the image is encoded to.
		// Scan the Color version of the original image.
		for (int i=0; i<h; i++) {
			for (int j=0; j<w; j++) {
				Color c = colorImg[i][j];
				int tempColor = 0;
				double distance = Double.MAX_VALUE;
				for (int k = 0; k < palette.length; k++) {
					double tempDistance = c.euclideanDistance(palette[k]);
					if (tempDistance < distance) {
						distance = tempDistance;
						tempColor = k;
					}
				}
				encodedPixels[i][j] = tempColor;
			}
		}
		info();
		essTime.reportTime(); 
		System.out.println("Encoding with the slow-and-simple method is complete. Try decoding to see how much information was lost.");
		theMainApp.decodeItem.setEnabled(true);
	}

	/**
	 * A slighter more complicated encoder. It goes through the pixels and replaces their index in the Hashtable.
	 */
	public void encodeFast() {  
		ImageAnalyzer.ElapsedTime efTime = theMainApp.new ElapsedTime();
		efTime.startTiming();
		// Loop through the elements of L.
		Iterator LIter = L.iterator();
		int s = blockSize;
		while (LIter.hasNext()) {
			BlockCountPair tempPair = (BlockCountPair) LIter.next();
			Block tempBlock = tempPair.b;
			Color blockColor = new Color(s * tempBlock.Br + s/2, s * tempBlock.Bg + s/2, s * tempBlock.Bb + s/2);
			int tempColor = 0;
			double distance = Double.MAX_VALUE;
			for (int k = 0; k < palette.length; k++) {
				double tempDistance = blockColor.euclideanDistance(palette[k]);
				if (tempDistance < distance) {
					distance = tempDistance;
					tempColor = k;
				}
			}						
			H.putAndMore(tempBlock, tempColor);
		}
		// Now scan the source image.
		encodedPixels = new int[h][w] ;	// to store the value each pixel in the image is encoded to.
		// Scan the Color version of the original image.
		for (int i=0; i<h; i++) {
			for (int j=0; j<w; j++) {
				Color c = colorImg[i][j];
				Block tempBlock = new Block(c);
				if (H.keySet().contains(tempBlock)){
					int idx = H.getAndMore(tempBlock).val;
					encodedPixels[i][j] = idx;
				}				
			}
		}
		info();
		efTime.reportTime(); 
		System.out.println("Encoding with the fast method is complete. Try decoding to see how much information was lost.");
		theMainApp.decodeItem.setEnabled(true);
	}
	
	/**
	 * A very simple method to print out basic information, including hash function, number of pixels, key set size, table size, 
	 * number of key-value pairs and the load factor. 
	 */
	public void info() {
		System.out.println("Hash function choice is " + hashFunctionChoice);
		System.out.println("The number of pixels in the images is " + h * w);
		System.out.println("The number of distinct color bins is " + H.keySet().size());
		System.out.println("The tablesize of the hashtable is " + H.tableSize);
		System.out.println("The number of key-value pairs in the hashtable is " + H.size);
		System.out.println("The load factor of the table is " + H.getLambda());
	}

	public class Color {
		int r, g, b;

		Color(int r, int g, int b) {
			this.r = r; this.g = g; this.b = b;    		
		}
		
		public int toInt() {
			return (((r<<8) + g) << 8)+r;
		}
		
		public String toString() {
			return "("+r+","+g+","+b+")";
		}

		double euclideanDistance(Color c2) {
			if (c2==null) { return Double.MAX_VALUE; }
			int dr = r-c2.r;
			int dg = g-c2.g;
			int db = b-c2.b;
			int sum_sq = dr*dr + dg*dg + db*db;
			return Math.sqrt(sum_sq);
		}
		int findIndexOfClosestColor(Color[] palette){
			double dist_so_far = 256.0 * 2; // something greater than 255 * cube root of 3.
			int best_index_so_far = 0;
			for (int i=0; i<palette.length; i++) {
				double this_distance = euclideanDistance(palette[i]);
				if (this_distance < dist_so_far) {
					dist_so_far = this_distance;
					best_index_so_far = i;
				}
			}
			return best_index_so_far;
		}
	}

	public class Block extends Object {
		int Br, Bg, Bb; // ADDED BY SLT.
		public Block(int Br, int Bg, int Bb) {
			this.Br = Br; this.Bg = Bg; this.Bb = Bb;
		}
		public Block(Color c) {
			Br = c.r / blockSize; Bg = c.g / blockSize; Bb = c.b / blockSize;
		}
		
		/**
		 * This is the eclipse auto-generated hashCode method. Returns a hash of all the fields.
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Bb;
			result = prime * result + Bg;
			result = prime * result + Br;
			return result;
		}
		
		/**
		 * This is the eclipse auto-generated equals method. Returns true if the two obejcsts are equal to eachother.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Block other = (Block) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Bb != other.Bb)
				return false;
			if (Bg != other.Bg)
				return false;
			if (Br != other.Br)
				return false;
			return true;
		}
		
		public String toString() {
			return "("+Br+","+Bg+","+Bb+")";
		}
		
		/**
		 * This is the eclipse auto-generated getOutType method. Used in equals and hashcode methods.
		 * @return
		 */
		private IndexedColorEncoder getOuterType() {
			return IndexedColorEncoder.this;
		}
	}

	/**
	 * This is the first hashing code as specified in part 1.
	 * 
	 * @param b The block that is being hashed
	 * @return returns an int hashcode
	 */
	public int h1(Block b) {
		return b.Br ^ b.Bg ^ b.Bb;
	}

	/**
	 * This is the second hashing code as specified in part 1.
	 * 
	 * @param b The block that is being hashed
	 * @return returns an int hashcode
	 */
	public int h2(Block b) {
		return 1024 * b.Br + 32 * b.Bg + b.Bb;
	}

	/**
	 * This is my personal implementation of a hashcode. I use bitwise AND and OR operations. 
	 * 
	 * @param b The block that is being hashed
	 * @return returns an int hashcode.
	 */
	public int h3(Block b) {
		return ((b.Br & b.Bb | b.Bg ) + "").hashCode();
	}

/*
 * The decode method is done for you.
 * Once you have either of the encoding methods working, you'll be able to run
 * this one from the menu and see how the image looks after compression.
 */
public void decode() {
	// Scan through the encodedPixels and look up each one's color, and put it into the image buffer.
	for (int i=0; i<h; i++ ) {
		for (int j=0; j<w; j++) {
			int pixelsColorIndex = encodedPixels[i][j];
			Color c = palette[pixelsColorIndex];
		    theMainApp.putPixel(theMainApp.biWorking, i, j, c);
			
		}
	}
	theMainApp.repaint();
	error = theMainApp.computeError(colorImg, theMainApp.biWorking);
	System.out.println("Average per-pixel error: "+error);

}
}
