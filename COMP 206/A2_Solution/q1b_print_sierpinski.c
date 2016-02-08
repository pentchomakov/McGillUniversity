// Pentcho Tchomakov - 260632861

#include <stdio.h>
#include <stdarg.h>
#include <stdbool.h>
#include <stdlib.h>

void printBasic(int height, char triangleArray[][1000], int x, int y) {
    int counter = 0;
    int j;
    int i;
    for (j = y; j >= (y - height + 1); j--) {
        for (i = x - counter; i <= (x + counter); i++)
            triangleArray[i][j] = '*';
        counter++;
    }
}

//This functions fills all the empty memory slots in the triangleArray with a space.
void fillEmpty(char triangleArray[][1000]) {
    
    int j;
    int i;
    for (j = 1000; j >= 0; j--) {
        for (i = 0; i < 1000; i++) {
            if (triangleArray[i][j] != '*') {
                triangleArray[i][j] = (' ');
            }
        }
    }
}

//This function is the recursive algoritm to print Sierpinski triangles depending on the fractal level.
//I called it fractilize because it sounded cool.
void sierpinskiTriangle(int height, char triangleArray[][1000], int x, int y, int fractals) {
    if (fractals == 0) {
        printBasic(height, triangleArray, x, y);
    }
    else {
        sierpinskiTriangle(height/2, triangleArray, x, y, fractals-1);
        sierpinskiTriangle(height/2, triangleArray, x-(height/2), y-(height/2), fractals-1);
        sierpinskiTriangle(height/2, triangleArray, x+(height/2), y-(height/2), fractals-1);
    }
    
}

// This function checks to see if the height is divisible by 2^fractal levels.
int isDivisible(int height, int fractals) {
    int checker = 1;
    checker = checker << fractals;
    if (height % checker == 0)
        return 1;
    else
        return 0;
}

// Check every character of the input to see if it's a number
bool isNumber(char number[])
{
    int i;
    
    //checking for negative numbers
    if (number[0] == '-')
        i = 1;
    for (i = 0; number[i] != 0; i++)
    {
        //if (number[i] > '9' || number[i] < '0')
        if (!isdigit(number[i]))
            return false;
    }
    
    return true;
}

int main(int argc, const char * argv[]) {
    // Two arguments passed:
    int  height;
    int fractals;
    // Creation of the 2D array to hold our Sierpinski Triangle
    char triangleArray[1000][1000];
    
    // We need 3 arguments passed. If less or more -> error
    if (argc != 3) {
        printf("ERROR: Please enter both a height and a fractal level. \n");
    }
    // Check if the fractal is a digit/number
    else if (!isNumber(argv[2])) {
        printf("ERROR: Fractal level must be an integer. \n");
    }
    // Check if the height is a digit/number
    else if (!isNumber(argv[1])) {
        printf("ERROR: Height must be an integer. \n");
    }
    // Check if the height is divisble by 2^fractal levels as required
    else if (isDivisible(height, fractals) == 0) {
        printf("ERROR: Height not divisible by 2^fractal levels. \n");
    }
    // Check if height is at least of 1
    else if (height < 1) {
        printf("ERROR: Height is too small. \n");
    }
    // Max height is 128
    else if (height > 128) {
        printf("ERROR: Height is too large. \n");
    }
    
    // Proceed to the recursive fractal figure "Sierpinski Triangles"
    else {
        // Start by calling the method once, which will, in return, recursively call itself
        sierpinskiTriangle(height, triangleArray, height-1, height-1, fractals);
        // We fill the rest of the empty chars with a space " ".
        fillEmpty(triangleArray);
        
        // Final step, priting the 2D array with for loops
        int j;
        int i;
        for (j = height - 1; j >= 0; j--) {
            for (i = 0; i < (2*height - 1); i++)
                printf("%c", triangleArray[i][j]);
            // New line after every y coordinate is done printing
            printf("\n");
        }
    }
    return 0;
}