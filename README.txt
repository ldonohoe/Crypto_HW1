Liam Donohoe

I decided on implementing this algorithm using Java. For execution of this program, you may compile in a command prompt using; javac HW1.java. The resulting .class file can then be run using; java HW1.class. 

I have added the option to also input an argument to this being the name of the file to encrypt. If an argument is not provided, then it will default to "test.txt" or any other filename that may be changed in the file itself. The file being input is expected to be in the current working directory of the java file, but I may later change this to require a full path to be input. Upon successfully opening and reading the file, all of the data is combined into a string, and then converted to binary.

My implementation of this algorithm makes large use of bitwise operations to manipulate the data, and facilitate encryption and decryption. Once the file has been read and converted to a byte array, and then to binary, we can break it up into eight bit chunks. Each eight bit chunk is then sent through my implementation of toyDES, and the result is converted back into a character, and added to the cipher text. Before beginning the encryption, both of the keys are generated, and stored for later use. Each eight bit chunk of data is then sent through the process of DES. For the most part, the data is handled in integer form, but for some operations such as permutations, expansions, splits, and similar operations, I convert the data to a binary string to make the operations simpler. 

As for the specifics of the functions, the permutation function makes use of this conversion to a string. I convert the string form of the binary data to a character array, and create a destination character array for the permutation. I then go through each value in the Permutation array, and replace the value in the new array. For the permutation of the 10 bit key to 8 bit keys, I had to create an additional parameter for this function. Permutation takes the integer to be permuted, the array holding the permutations, and also the number of bits in the input. This way, the function allows for a reduction in the number of bits being permuted.

Once I have dealt with the data in the form of a string, I make use of the built in Integer function Integer.parse(String, base). This allows me to easily convert a string of '1's and '0's back to an integer. I make use of this very often in this implementation. 


