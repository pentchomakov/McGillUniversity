// Pentcho Tchomakov - 260632861

#include <stdio.h>
#include <stdbool.h>
#include <stdarg.h>
#include <stdlib.h>

// Method to insert spaces
// Once we've added all the stars we wanted in the 2D array, we add spaces to every other coordinates.
// We replace "" with " " when [x][y] doesn't contain a "*"
void fillEmpty(char triangleArray[][1000]) {
    // For some reason, we need to declear our "for loop" variables in advance.
    // It works in XCode, but not on the ssh/trottier termianls without this.
    int j;
    int i;
    
    for (j = 1000; j >= 0; j--) {
        for (i = 0; i < 1000; i++)
            if (triangleArray[i][j] != '*')
                triangleArray[i][j]=(' ');
    }
}

// When fractal = 0, we print a single triangle at the given coordinates.
// Same as Q1a except in a 2D array
void triangle(int height, char triangleArray[][1000], int x, int y) {
    int counter = 0;
    // Same thing here, instanciate the variables before the for loops
    int j;
    int i;
    
    // We add the stars at the given coordinates of the 1000/100
    for (j=y; j >= (y - height + 1); j--) {
        for (i = x - counter; i <= (x + counter); i++)
            triangleArray[i][j] = '*';
        counter++;
    }
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
    int  height;
    
    // We define a 2D array of size 1000x1000
    char triangleArray[1000][1000];
    
    // We check if we have 3 arguments that have been passed
    if (argc != 2 || argc > 3) {
        printf("ERROR: Please enter a height.\n");
    }
    
    // We check that the input only contains digits and is a number
    else if (isNumber(height) == false) {
        printf("ERROR: Height must be an integer.\n");
    }
    
    // Height has to be above 1
    else if (height < 1) {
        printf("ERROR: Height is too small.\n");
    }
    
    // Height cannot be bigger than 128
    else if (height >= 128) {
        printf("ERROR: Height is too large.\n");
    }
    
    // If all conditions pass, we can print our triangle.
    else {
        // Creates triangle
        triangle(height, triangleArray, height-1, height-1);
        // Replaces empty chars with spaces " " in the 2D array
        fillEmpty(triangleArray);
        // Define the variables for the for loops
        int j;
        int i;
        
        for (j = height - 1; j >= 0; j--) {
            for (i = 0; i < (2*height - 1); i++)
                printf("%c", triangleArray[i][j]);
            // After printing every y coordinate completely, we make a new line.
            printf("\n");
        }
    }
    
    return 0;
}