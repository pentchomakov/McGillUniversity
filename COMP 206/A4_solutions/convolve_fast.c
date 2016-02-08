/*
Pentcho Tchomakov
260632861
*/

#include <stdio.h>
#include <stdlib.h>
#include "fast_filter.h"

int main(int argc, char* argv[]) {

    // Obtain the width from stdin
    int width;
    width = atoi(argv[3]);

    // Length of array of weights
    float weight;
    weight = width * width;	

    // To store the floats into an array
    float *weightsArray;
    weightsArray = (float *)malloc(weight * sizeof(float));
    // Taking the floats from stdin into the weights array
	int i;
	for(i = 0; i < weight; i++) {
		weightsArray[i] = atof(argv[4 + i]);
	}

	// File pointers and other variables
	FILE *inputBMP, *outputBMP;
	long inputBMPSize;
	// Open the input picture
	inputBMP = fopen(argv[1], "rb");
	// We set the pointer at the EOF to get the size of its content with 'ftell()'
	fseek(inputBMP, 0 , SEEK_END);
	inputBMPSize = ftell(inputBMP);
	// We set the pointer to the begining of the file
	fseek(inputBMP, 0, SEEK_SET);

	// Allocating memory for data of both unaltered and altered picture
	unsigned char inputBMPContent [ sizeof(unsigned char) * (inputBMPSize)];
	unsigned char outputBMPContent [(inputBMPSize)];

	// Read the input BMP
	fread(inputBMPContent, inputBMPSize, 1, inputBMP);
	fclose(inputBMP);

	// Filter the picture with the c file provided (fast_filter)
	doFiltering(inputBMPContent, weightsArray, width, outputBMPContent);

	// Create the new BMP file of the filtered original BMP
	outputBMP = fopen(argv[2], "wb");
	fwrite(outputBMPContent, inputBMPSize, 1, outputBMP);
	fclose(outputBMP);

	return 0;
}

