/*
 * Liam Donohoe
 * Implementation of Toy DES
 * 
 * 
 */

public class HW1 
{
	static int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7 };
	static int IIP[] = { 4, 1, 3, 5, 7, 2, 8, 6 };
	static int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
	static int P8[] = { 6, 3, 7, 4, 8, 5, 10, 9 };
	static int P4[] = {2, 4, 3, 1};
	
	static int s0[][] = {{1, 0, 3, 2}, 
					{3, 2, 1, 0}, 
					{0, 2, 1, 3}, 
					{3, 1, 3, 2}};
	static int s1[][] = {{0, 1, 2, 3}, 
					{2, 0, 1, 3}, 
					{3, 0, 1, 0}, 
					{2, 1, 0, 3}};
	
	static int key1;
	static int key2;
	
	//Simple helper function to pop n bits from the right
	//of a binary string, and return them all in an array
	public static int[] popBits(int input, int n) 
	{
		int[] bits = new int[n];
		for (int i=0; i<n; i++) 
		{
			bits[i] = (1) & (input >> 0);
			input >>= 1;
		}
		return bits;
	}
	
	public static String binToString(int input, int len) 
	{
		String binString = "";
		int[] bitVals = popBits(input, len);

		for (int i=len-1; i>=0; i--) 
		{
			binString += bitVals[i];
		}
		return binString;
	}
	
	//Take the bits from the in value, and reorder the bits to the order 
	//defined in perms. 
	public static int permutation(int inPerm, int[] perms, int numBits) 
	{
		
		int bitLen = perms.length;
		char[] bitVals = binToString(inPerm, numBits).toCharArray();
		char[] permBits = new char[bitLen];
		
		for (int i=0; i<bitLen; i++) 
		{
			int newPos = perms[i];
			permBits[i] = bitVals[newPos-1];
		}
		
		String permString = new String(permBits);
		int outPerm = Integer.parseInt(permString, 2);
		
		return outPerm;
	}
	
	//Simple helper function to get the left 4 bits of an 8 bit string
	public static int getLeft(int pre) 
	{
		int l = pre >> 4;
		
		return l;
		
	}
	
	//Simple Helper function to get the right 4 bits as an integer
	public static int getRight(int pre) 
	{
		
		return (((1 << 4)-1) & (pre)); 
		
	}
	
	public static int P10(int input) 
	{
		int perm = permutation(input, P10, 10);
		return perm;
	}
	
	public static int P8(int input) 
	{
		int perm = permutation(input, P8, 8);
		return perm;
	}
	
	public static int P4(int input)  
	{
		int perm = permutation(input, P8, 8);
		return perm;
	}
	
	public static void genKeys(int tenBit) 
	{
		String tenString = new String(binToString(tenBit, 10));
		
		for (int i=0; i<3; i++) 
		{
		String left = tenString.substring(0, 5);

		String right = tenString.substring(5);

		char l = left.charAt(0);
		char r = right.charAt(0);
		left = left.substring(1);
		right = right.substring(1);
		left += l;
		right += r;
		
		tenString = left + right;
		if (i == 0) 
		{
			int shiftInt = Integer.parseInt(tenString, 2);
			key1 = permutation(shiftInt, P8, 10);
		}
		}
		int shiftInt = Integer.parseInt(tenString, 2);
		
		key2 = permutation(shiftInt, P8, 10);
	}
	
	public static int s(int input, int[][] sBox) 
	{
		String fourBit = binToString(input, 4);
		String column = "" + fourBit.charAt(0) + fourBit.charAt(3);
		String row = "" + fourBit.charAt(1) + fourBit.charAt(2);
		
		int rowVal = Integer.parseInt(row, 2);
		int colVal = Integer.parseInt(column, 2);
		
		
		int out = sBox[rowVal][colVal];
		
		return out;
	}
	
	public static int fiest(int input, int key) 
	{
		int inputToEight = fourToEight(input);
		
		int cross = inputToEight ^ key;
		
		int left = getLeft(cross);
		int right = getRight(cross);
		
		left = s(left, s0);
		right = s(right, s1);
		
		
		String lString = binToString(left, 2);
		String rString = binToString(right, 2);
		
		String four = lString + rString;
		int fourVal = Integer.parseInt(four, 2);
		fourVal = permutation(fourVal, P4, 4);
		
		return fourVal;
		
	}
	
	//A simple function to permute and expand the four bit
	//String into an eight bit string
	public static int fourToEight(int input) {
		//1234 => 41232341 or dabcbcda, reversed : adcbcbad
		int a, b, c, d;
		int[] bits = popBits(input, 4);
		a = bits[0]; b = bits[1]; c = bits[2]; d = bits[3];
		String bitString = new String();
		bitString += "" + a + d + c + b + c + b + a + d;
		int eightBit = Integer.parseInt(bitString, 2);
		return eightBit;
		
	}
/**
 * 
 * Performs a simplified DES algorithm to encrypt 8 bit text
 * 
 * @param plainText input text to be encrypted, (8 bits at a time)
 * @return encrypted plainText
 */
	public static int encrypt(int plainText) 
	{
		//Perform initial permutation on the plainText
		int initial = permutation(plainText, IP, 8);
		
		//Split into two halves of plaintext
		int left = getLeft(initial);
		int right = getRight(initial);
				
		//Fiestel Function with right half, and k1
		int f = fiest(right, key1);
		
		//XOR result of F with left, 
		//original right becomes left2
		int right2 = left ^ f;
		int left2 = right;
	
		//Fiestel with new Right half, and k2
		int f2 = fiest(right2, key2);
		
		//XOR result of f2 with left2
		int left3 = left2 ^ f2;
		//Convert to strings to combine easily
		String rFinal = binToString(right2, 4);
		String lFinal = binToString(left3, 4);
		
		String fin = lFinal + rFinal;
		
		//Parse binary back into an integer
		int finBin = Integer.parseInt(fin, 2);
		
		//Inverse the initial permutation
		finBin = permutation(finBin, IIP, 8);
		
		//Return the encrypted value
		return finBin;
	}
	
	/**
	 * 
	 * Decrypts ciphertext back to the original message using 
	 * simplified DES in reverse.
	 * 
	 * @param cipherText input text to be decrypted (8 bits)
	 * @return deciphered cipherText
	 */
	private static int decrypt(int cipherText) 
	{

		//Initial permutation of input
		int inverse = permutation(cipherText, IP, 8);
		
		//Split into left and right
		int left = getLeft(inverse);
		int right = getRight(inverse);
		
		//Perform fiestel function with right and k2
		int f2 = fiest(right, key2);
		
		//XOR result of Fiestel with left
		//Right becomes left2
		int right2 = f2 ^ left;
		int left2 = right;
		
		//Fiestel function with k1 and new right
		int f1 = fiest(right2, key1);
		
		//XOR result of fiestel with left2
		//right2 becomes left final
		int left3 = f1 ^ left2;
		
		//Convert to Strings to combine for simplicity
		String rFinal = binToString(right2, 4);
		String lFinal = binToString(left3, 4);
		
		
		String finVal = lFinal + rFinal;
		
		//Parse back into an integer
		int finBin = Integer.parseInt(finVal, 2);
		
		//Inverse permutation for final step
		finBin = permutation(finBin, IIP, 8); 
		
		return finBin;
		
		
	}

		
	public static void main(String[] args) 
	{
		//CHANGE BACK TO CONSOLE INPUT
		//ADD ERROR CHECKING FOR INPUT
		//OR NOT, HONESTLY NOT SURE WHAT INPUT SHOULD BE
		
		//Plaintext/ciphertext to be encrypted/decrypted
		String plain = "00111001";
		
		//Input key for encryption/decryption
		String keyText = "0100101101";
		
		//Parse input to an integer
		int bit = Integer.parseInt(plain, 2);
		int key = Integer.parseInt(keyText, 2);
		
		//Create the two keys that will be used for en/decryption
		genKeys(key);
		
		System.out.println("The bit value is : " + bit + " from : " + plain);
		System.out.println("The key value is : " + key + " from : " + keyText);
		
		//encrypt the input bits
		int cipher = encrypt(bit);
		//convert back to a string for printing
		String cipherText = binToString(cipher, 8);
		
		System.out.println("The Cipher is : " + cipherText);
		
		//Decrypt the message that was just encrypted to ensure
		//message can be decoded
		int replained = decrypt(cipher);

		String plainshouldbe = binToString(replained, 8);
		
		System.out.print("The plain is " + plainshouldbe);

		
	}

}
